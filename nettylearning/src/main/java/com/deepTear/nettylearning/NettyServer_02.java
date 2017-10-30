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
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

public class NettyServer_02 {

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
				pipeLine.addLast(new LengthFieldBasedFrameDecoder(
						ByteOrder.BIG_ENDIAN,
						1024,// maxFrameLength
						0,//lengthFieldOffset
						4,//lengthFieldLength
						0,//lengthAdjustment  如果客户端发送消息时，指定消息长度包含消息头 的4字节（保存消息长度）,那么该字段就为 -4，不包含就为0
						4,//initialBytesToStrip
						true));//failFast
				/**
				 * maxFrameLength：解码的帧的最大长度

				lengthFieldOffset ：长度属性的起始位（偏移位），包中存放有整个大数据包长度的字节，这段字节的其实位置

				lengthFieldLength：长度属性的长度，即存放整个大数据包长度的字节所占的长度

				lengthAdjustmen：长度调节值，在总长被定义为包含包头长度时，修正信息长度。initialBytesToStrip：跳过的字节数，根据需要我们跳过lengthFieldLength个字节，以便接收端直接接受到不含“长度属性”的内容

				failFast ：为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异常
				 */
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
			ByteBuf buf = (ByteBuf)msg;
			if(buf.readableBytes() > 0){
				System.out.println("客户端消息长度为----------------->" + buf.readableBytes());
				byte[] b = new byte[buf.readableBytes()];
				buf.readBytes(b);
				System.out.println("客户端消息内容为----------------->" + new String(b,"utf-8"));
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.channel().close();
		}

	}
}
