package com.newman.netty4;

import com.newman.netty4.netty4.protobuf.PROTOCOL;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DoubleClientHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
		System.out.println("Client get msg is: " + obj);

		if (obj instanceof PROTOCOL.ProtocolMessage) {
			PROTOCOL.ProtocolMessage message = (PROTOCOL.ProtocolMessage) obj;

			System.out.println("===========CLIENT============");
			System.out.println(message.getType().name());

			switch (message.getType().getNumber()) {
			case PROTOCOL.CommMessageType.CMT_S_LOGIN_VALUE:
				System.out.println("===== DEBUG FOR `CMT_S_LOGIN_VALUE` =====");
				System.out.println("rlt_code: " + message.getRespLogin().getRlt().getCode());
				System.out.println("rlt_msg: " + message.getRespLogin().getRlt().getMsg());
				break;

			case PROTOCOL.CommMessageType.CMT_S_PPTLIST_VALUE:
				System.out.println("===== DEBUG FOR `CMT_S_PPTLIST_VALUE` =====");
				System.out.println("rlt_code: " + message.getRespPptList().getRlt().getCode());
				System.out.println("rlt_msg: " + message.getRespPptList().getRlt().getMsg());
				System.out.println("tag: " + message.getRespPptList().getTag());
				System.out.println("desc: " + message.getRespPptList().getDesc());
				System.out.println("node: ");
				for (int i = 0; i < message.getRespPptList().getNodeCount(); i++) {
					System.out.println("\tnode" + i + " - id: " + message.getRespPptList().getNode(i).getId());
					System.out.println("\tnode" + i + " - image: " + message.getRespPptList().getNode(i).getImage());
					System.out.println("\tnode" + i + " - order: " + message.getRespPptList().getNode(i).getOrder());
				}
				break;

			default:
				break;
			}

		}
	}
}
