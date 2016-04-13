package com.newman.netty4;

import com.newman.netty4.netty4.protobuf.PROTOCOL;
import com.newman.netty4.netty4.protobuf.PROTOCOL.ProtocolMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtobufToByteEncoder extends MessageToByteEncoder<PROTOCOL.ProtocolMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ProtocolMessage msg, ByteBuf out)
			throws Exception {
		out.writeInt(msg.toByteArray().length);
		out.writeBytes(msg.toByteArray());
	}

}
