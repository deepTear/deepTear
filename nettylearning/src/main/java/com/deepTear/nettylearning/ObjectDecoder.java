package com.deepTear.nettylearning;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class ObjectDecoder extends MessageToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {

		System.out.println(msg);

		out.add(msg);

	}



}
