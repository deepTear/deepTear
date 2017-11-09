package com.deepTear.nettylearning;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ObjectEncoder extends MessageToMessageEncoder<User> {

	@Override
	protected void encode(ChannelHandlerContext ctx, User msg, List<Object> out) throws Exception {
		ByteBuf buf = Unpooled.directBuffer(512);
		buf.writeInt(msg.getAge());
		buf.writeBytes(msg.getAttachment());
		buf.writeBytes(msg.getProfile().getBytes());
		buf.writeBytes(msg.getUserName().getBytes());
		//ctx.writeAndFlush(buf);
		out.add(buf);
		System.out.println(msg);
		//out.add(msg);
	}



}
