package com.deepTear.nettylearning;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.Scanner;


/**
 *
 * @author Administrator
 *
 */
public class NettyClient_03 {

	private static int port = 9999;

	private static EventLoopGroup workerGroup;

	public static void main(String[] args) throws Exception{
		try {
			workerGroup = new NioEventLoopGroup();
			InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1",port);
			Bootstrap bootStrap = new Bootstrap();
			bootStrap.group(workerGroup);
			bootStrap.channel(NioSocketChannel.class);
			bootStrap.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline pipeLine = ch.pipeline();
					pipeLine.addLast(new ChannelInitializer<Channel>(){
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ChannelPipeline pipeLine = ch.pipeline();
							pipeLine.addLast(new ObjectEncoder());
							pipeLine.addLast(new ClientHandler());
						}
					});
				}

			});
			bootStrap.option(ChannelOption.SO_KEEPALIVE,true);
			ChannelFuture future = bootStrap.connect(socketAddress).sync();

			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

	private static class ClientHandler extends ChannelInboundHandlerAdapter{

		@Override
		public void channelActive(final ChannelHandlerContext ctx) throws Exception {
			System.out.println("连接到服务器了");
			new Thread(new Runnable() {
				public void run() {
					String readLine = null;
					Scanner print = new Scanner(System.in);
					User user = null;
					int i = 0;
					while(true){
						readLine = print.next();
						if(readLine.equals("close")){
							ctx.close();
							break;
						}
						i++;
						System.out.println("用户输入---------->" + readLine);
						try {
							user = new User();
							user.setAge(20 + i);
							user.setProfile("我只总共国人" + new Random(100).nextInt());
							user.setUserName(readLine);
							ctx.writeAndFlush(user);
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

	}
}
