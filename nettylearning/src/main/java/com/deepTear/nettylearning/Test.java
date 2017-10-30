package com.deepTear.nettylearning;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Test {

	public static void main(String[] args) {
		ByteBuf buf = Unpooled.buffer();
		int readIndex = buf.readerIndex();
		int writeIndex = buf.writerIndex();

		System.out.println(buf.capacity() + "--------------初始容量");//默认256
		System.out.println(readIndex + "------------------初始读位置");//0
		System.out.println(writeIndex + "------------------初始写位置");//0

		String str = "中国人";
		byte[] b = str.getBytes();
		System.out.println(b.length);//9

		buf.writeBytes(b);

		System.out.println(buf.writableBytes());//247
		System.out.println(buf.readableBytes());//9

		readIndex = buf.readerIndex();
		writeIndex = buf.writerIndex();
		System.out.println(readIndex + "------------------写入数据后读位置");//0
		System.out.println(writeIndex + "------------------写入数据后写位置");//9

		byte[] bb = new byte[3];
		buf.readBytes(bb);

		System.out.println(buf.writableBytes());//247
		System.out.println(buf.readableBytes());//6

		readIndex = buf.readerIndex();
		writeIndex = buf.writerIndex();
		System.out.println(readIndex + "------------------读3字节后读位置");//3
		System.out.println(writeIndex + "------------------读3字节后写位置");//9

		buf.readBytes(bb);

		System.out.println(buf.writableBytes());//247
		System.out.println(buf.readableBytes());//3

		buf.readBytes(bb);

		System.out.println(buf.writableBytes());//247
		System.out.println(buf.readableBytes());//0

		readIndex = buf.readerIndex();
		writeIndex = buf.writerIndex();
		System.out.println(readIndex);//9
		System.out.println(writeIndex);//9

		buf.clear();

		System.out.println(buf.writableBytes());//256
		System.out.println(buf.readableBytes());//0

		readIndex = buf.readerIndex();
		writeIndex = buf.writerIndex();
		System.out.println(readIndex);//0
		System.out.println(writeIndex);//0
	}
}
