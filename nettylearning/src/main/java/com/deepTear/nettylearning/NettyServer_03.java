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
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

public class NettyServer_03 {

	private static int port = 9999;

	public static void main(String[] args) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		InetSocketAddress socketAddress = new InetSocketAddress(port);
		ServerBootstrap bootStrap = new ServerBootstrap();
		bootStrap.group(bossGroup, workerGroup);
		bootStrap.channel(NioServerSocketChannel.class);
		bootStrap.childHandler(new ChannelInitializer(){
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeLine = ch.pipeline();
				pipeLine.addLast(new ObjectDecoder());
				pipeLine.addLast(new NettyServerHandler());
			}
		});
		bootStrap.option(ChannelOption.SO_BACKLOG, 128);
		bootStrap.childOption(ChannelOption.SO_KEEPALIVE, true);

		ChannelFuture channelFuture = bootStrap.bind(socketAddress).sync();
		channelFuture.channel().closeFuture().sync();

	}

	private static class NettyServerHandler extends ChannelInboundHandlerAdapter{

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("---------------有新客户端连入服务器---------------");
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			User user = (User)msg;
			System.out.println(user);
			ReferenceCountUtil.release(msg);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.channel().close();
		}

	}
}
