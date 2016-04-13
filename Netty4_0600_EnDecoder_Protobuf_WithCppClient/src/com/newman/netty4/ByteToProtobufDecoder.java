package com.newman.netty4;

import java.util.List;

import com.newman.netty4.netty4.protobuf.PROTOCOL.ProtocolMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ByteToProtobufDecoder extends ByteToMessageDecoder {

	// @Override
	// protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object>
	// out) throws Exception {
	// // TODO Auto-generated method stub
	// System.out.println(this.getClass().getName());
	//
	// if (in.readableBytes() < 4) {
	// return;
	// }
	// int length = in.readInt();
	// System.out.println("length: " + length);
	// if (in.readableBytes() < length) {
	// return;
	// }
	// ByteBuf frame = Unpooled.buffer(length);
	// in.readBytes(frame);
	//
	// try {
	// byte[] inByte = frame.array();
	//
	// // 解密消息体
	// // ThreeDES des = ctx.channel().attr(AppAttrKeys.ENCRYPT).get();
	// // byte[] bareByte = des.decrypt(inByte);
	//
	// // 字节转成对象
	// ProtocolMessage msg = ProtocolMessage.parseFrom(inByte);
	// // System.out.println("[APP-SERVER][RECV][remoteAddress:" +
	// // ctx.channel().remoteAddress() + "][total length:" + length
	// // + "][bare length:" + msg.getSerializedSize() + "]:\r\n" +
	// // msg.toString());
	//
	// if (msg != null) {
	// // 获取业务消息头
	// out.add(msg);
	// }
	// } catch (Exception e) {
	// System.out.println(ctx.channel().remoteAddress() + ",decode failed." +
	// e);
	// }
	// }

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println(this.getClass().getName());

		in.markReaderIndex();

		if (in.readableBytes() < 4) {
			return;
		}
		int length = 0;
		if (length == 0) {
			length = in.readInt();
		}
		if (length < 0) {// 非法数据，关闭连接
			ctx.close();
		}
		System.out.println("length: " + length);
		if (in.readableBytes() < length) {
			in.resetReaderIndex();
			return;
		}
		ByteBuf frame = Unpooled.buffer(length);
		in.readBytes(frame);

		try {
			byte[] inByte = frame.array();

			// 解密消息体
			// ThreeDES des = ctx.channel().attr(AppAttrKeys.ENCRYPT).get();
			// byte[] bareByte = des.decrypt(inByte);

			// 字节转成对象
			ProtocolMessage msg = ProtocolMessage.parseFrom(inByte);
			// System.out.println("[APP-SERVER][RECV][remoteAddress:" +
			// ctx.channel().remoteAddress() + "][total length:" + length
			// + "][bare length:" + msg.getSerializedSize() + "]:\r\n" +
			// msg.toString());

			if (msg != null) {
				// 获取业务消息头
				out.add(msg);
			}
		} catch (Exception e) {
			System.out.println(ctx.channel().remoteAddress() + ",decode failed." + e);
		}
	}
}
