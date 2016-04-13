package com.newman.netty4;

import com.newman.netty4.netty4.protobuf.PROTOCOL;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class DoubleClientHandlerInitialize extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		// pipeline.addLast(new ProtobufVarint32FrameDecoder());
		// pipeline.addLast(new
		// ProtobufDecoder(ProtocolMessage.getDefaultInstance()));
		// pipeline.addLast(new ByteToProtobufDecoder());
		// pipeline.addLast(new ProtobufToByteEncoder());
		// pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
		// pipeline.addLast(new ProtobufEncoder());

		pipeline.addLast(new StringDecoder());
		pipeline.addLast(new StringEncoder());

		pipeline.addLast(new DoubleClientHandler());
	}

}
