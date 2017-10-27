package com.deepTear.nettylearning;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
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

import java.net.InetSocketAddress;

public class NettyServer_01 {

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
					/*pipeLine.addLast(new StringDecoder(Charset.forName("utf-8")));
					pipeLine.addLast(new StringEncoder(Charset.forName("utf-8")));*/
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

		private final byte[] dataSize = new byte[4];

		private byte[] data;

		private int size;

		private String readLine = "";

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("---------------有新客户端连入服务器---------------");
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			ByteBuf buf = (ByteBuf)msg;
			while(buf.isReadable()){
				buf.readBytes(dataSize);
				size = ParseUtils.byteToInt(dataSize);
				System.out.println("客户端消息长度---------->" + size + "  字节");
				if(size > 0){
					data = new byte[size];
					buf.readBytes(data);
					readLine = new String(data,"UTF-8");
				}
				System.out.println(readLine);
			}

			/*readLine = (String)msg;
			System.out.println(readLine);*/
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}

	}
}
