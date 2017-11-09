package com.deepTear.nettylearning;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


public class NettyHeartBeatServerDemo {

	private static int port = 9999;

	private static EventLoopGroup bossGroup;

	private static EventLoopGroup workerGroup;

	public static void main(String[] args) throws Exception {
		try {
			bossGroup = new NioEventLoopGroup();
			workerGroup = new NioEventLoopGroup();
			InetSocketAddress socketAddress = new InetSocketAddress(port);
			ServerBootstrap bootStrap = new ServerBootstrap();
			bootStrap.group(bossGroup, workerGroup);
			bootStrap.channel(NioServerSocketChannel.class);
			bootStrap.childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline pipeLine = ch.pipeline();
					/**
					 * 	1）readerIdleTime：为读超时时间（即测试端一定时间内未接受到被测试端消息）
					 *  2）writerIdleTime：为写超时时间（即测试端一定时间内向被测试端发送消息）
					 *  3）allIdleTime：所有类型的超时时间
					 */
					pipeLine.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
					pipeLine.addLast(new StringDecoder());
					pipeLine.addLast(new ServerHandler());
				}

			});
			bootStrap.option(ChannelOption.SO_BACKLOG, 128);
			bootStrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture future = bootStrap.bind(socketAddress).sync();
			//future.addListener(ChannelFutureListener.CLOSE);
			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}


	private static class ServerHandler extends ChannelInboundHandlerAdapter{

		private int timeOutCount;

		@Override
		public void channelActive(final ChannelHandlerContext ctx) throws Exception {
			System.out.println("---------------有新客户端连入服务器---------------");
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			String message = (String)msg;
			if(message != null){
				if(message.equals("$$_heart_beat_")){
					System.out.println("-----------------------------------客户端发送心跳数据包");
				}else{
					System.out.println("-----------------------------------客户端发送业务数据包" + "-------------------->" + message);
				}
			}
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
				if (timeOutEvent.state() == IdleState.READER_IDLE) {
					timeOutCount ++;
	                System.out.println("10 秒没有接收到客户端的信息了");
	                if (timeOutCount > 2) {
	                    System.out.println("关闭这个不活跃的channel");
	                    ctx.channel().close();
	                }
	            }
	        } else {
	            super.userEventTriggered(ctx, evt);
	        }
		}

	}

}
