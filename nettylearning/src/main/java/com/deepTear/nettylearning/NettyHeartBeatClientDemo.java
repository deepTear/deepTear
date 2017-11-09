package com.deepTear.nettylearning;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class NettyHeartBeatClientDemo {

	private static int port = 9999;

	private static EventLoopGroup workerGroup;

	private static Bootstrap bootStrap;

	private static InetSocketAddress socketAddress;

	//通过ChannelFuture实现读写操作
	private static ChannelFuture future;

	private static ConnectionWatchdog watchdog;

	public static void main(String[] args) throws Exception{
		try {
			workerGroup = new NioEventLoopGroup();
			socketAddress = new InetSocketAddress("127.0.0.1",port);
			bootStrap = new Bootstrap();
			bootStrap.group(workerGroup);
			bootStrap.channel(NioSocketChannel.class);
			bootStrap.option(ChannelOption.SO_KEEPALIVE,true);
			bootStrap.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline pipeLine = ch.pipeline();
					watchdog = new ConnectionWatchdog(bootStrap, new HashedWheelTimer(), port,true);
					pipeLine.addLast(watchdog);
					pipeLine.addLast(new ChannelHandler[]{new IdleStateHandler(0, 8, 0, TimeUnit.SECONDS),
							new StringEncoder(),
							new ClientHandler()});
				}
			});
			future = bootStrap.connect(socketAddress).sync();
			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

	private static class ClientHandler extends ChannelInboundHandlerAdapter{

		private int attempts;//重连尝试次数

		@Override
		public void channelActive(final ChannelHandlerContext ctx) throws Exception {
			System.out.println("连接到服务器了");
			new Thread(new Runnable() {
				public void run() {
					String readLine = null;
					Scanner print = new Scanner(System.in);
					while(true){
						readLine = print.next();
						if(readLine.equals("close")){
							ctx.close();
							break;
						}
						System.out.println("用户输入---------->" + readLine);
						try {
							if(ctx.channel().isActive()){
								ctx.writeAndFlush(readLine);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if(evt instanceof IdleStateEvent){
				IdleStateEvent timeOutEvent = (IdleStateEvent)evt;
				if (timeOutEvent.state() == IdleState.WRITER_IDLE) {
	                ctx.channel().writeAndFlush(Unpooled.copiedBuffer("$$_heart_beat_".getBytes()));
	                System.out.println("$$_heart_beat_");
	            }
	        } else {
	            super.userEventTriggered(ctx, evt);
	        }
		}
	}

	/**
	 *
	 * 客户端的ChannelHandler集合，由子类实现，这样做的好处：
	 * 继承这个接口的所有子类可以很方便地获取ChannelPipeline中的Handlers
	 * 获取到handlers之后方便ChannelPipeline中的handler的初始化和在重连的时候也能很方便
	 * 地获取所有的handlers
	 */
	public interface ChannelHandlerHolder {

		ChannelHandler[] handlers();
	}

	@Sharable
	private static class ConnectionWatchdog extends ChannelInboundHandlerAdapter implements TimerTask{

		private final Bootstrap bootstrap;
	    private final Timer timer;
	    private final int port;
	    private volatile boolean reconnect = true;
	    private int attempts;

	    public ConnectionWatchdog(Bootstrap bootstrap, Timer timer, int port, boolean reconnect) {
	        this.bootstrap = bootstrap;
	        this.timer = timer;
	        this.port = port;
	        this.reconnect = reconnect;
	    }

		/**
	     * channel链路每次active的时候，将其连接的次数重新☞ 0
	     */
	    @Override
	    public void channelActive(ChannelHandlerContext ctx) throws Exception {

	        System.out.println("当前链路已经激活了，重连尝试次数重新置为0");

	        attempts = 0;
	        ctx.fireChannelActive();
	    }

	    @Override
	    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	        System.out.println("链接关闭");
	        if(reconnect){
	            System.out.println("链接关闭，将进行重连");
	            if (attempts < 12) {
	                attempts++;
	                //重连的间隔时间会越来越长
	                long timeout = 2 << attempts;
	                timer.newTimeout(this, 10, TimeUnit.SECONDS);

	            }
	        }
	        ctx.fireChannelInactive();
	    }



		//public abstract ChannelHandler[] handlers();

		public void run(Timeout timeout) throws Exception {
	        ChannelFuture future;
	        //bootstrap已经初始化好了，只需要将handler填入就可以了
	        synchronized (bootstrap) {
	            bootstrap.handler(new ChannelInitializer<Channel>() {
	                @Override
	                protected void initChannel(Channel ch) throws Exception {
	                	ChannelPipeline pipeLine = ch.pipeline();
	                	watchdog = new ConnectionWatchdog(bootStrap, new HashedWheelTimer(), port,true);
						pipeLine.addLast(watchdog);
						pipeLine.addLast(new ChannelHandler[]{new IdleStateHandler(0, 8, 0, TimeUnit.SECONDS),
								new StringEncoder(),
								new ClientHandler()});
	                }
	            });
	            future = bootstrap.connect("127.0.0.1",port);
	        }
	        //future对象
	        future.addListener(new ChannelFutureListener() {
	            public void operationComplete(ChannelFuture f) throws Exception {
	                boolean succeed = f.isSuccess();
	                //如果重连失败，则调用ChannelInactive方法，再次出发重连事件，一直尝试12次，如果失败则不再重连
	                if (!succeed) {
	                    System.out.println("重连失败");
	                    f.channel().pipeline().fireChannelInactive();
	                }else{
	                    System.out.println("重连成功");
	                }
	            }
	        });
	    }



	}
}
