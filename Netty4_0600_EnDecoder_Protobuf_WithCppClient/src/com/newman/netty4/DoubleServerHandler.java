package com.newman.netty4;

import com.newman.netty4.netty4.protobuf.PROTOCOL;
import com.newman.netty4.netty4.protobuf.PROTOCOL.CommMessageType;
import com.newman.netty4.netty4.protobuf.PROTOCOL.CommRlt;
import com.newman.netty4.netty4.protobuf.PROTOCOL.PPTNode;
import com.newman.netty4.netty4.protobuf.PROTOCOL.ProtocolMessage;
import com.newman.netty4.netty4.protobuf.PROTOCOL.RespLogin;
import com.newman.netty4.netty4.protobuf.PROTOCOL.RespPPTList;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DoubleServerHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("client enter.");
		// ctx.channel().writeAndFlush("abcdef");

		// send login result
		// ProtocolMessage.Builder pmLoginRltBuilder =
		// ProtocolMessage.newBuilder();
		// pmLoginRltBuilder.setType(CommMessageType.CMT_S_LOGIN);
		// RespLogin.Builder respLoginBuilder = RespLogin.newBuilder();
		//
		// CommRlt.Builder rltLoginBuilder = CommRlt.newBuilder();
		// rltLoginBuilder.setCode(10);
		// rltLoginBuilder.setMsg("Login Success!");
		//
		// respLoginBuilder.setRlt(rltLoginBuilder.build());
		// pmLoginRltBuilder.setRespLogin(respLoginBuilder.build());
		//
		// ctx.channel().writeAndFlush(pmLoginRltBuilder.build());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
		System.out.println("Server get msg is: " + obj);

		if (obj instanceof PROTOCOL.ProtocolMessage) {
			PROTOCOL.ProtocolMessage message = (PROTOCOL.ProtocolMessage) obj;

			System.out.println("============SERVER============");
			System.out.println(message.getType().name());

			switch (message.getType().getNumber()) {
			case PROTOCOL.CommMessageType.CMT_C_LOGIN_VALUE:
				System.out.println(message.getReqLogin().getId());
				System.out.println(message.getReqLogin().getToken());
				String str = new String(message.getReqLogin().getPlatform().getBytes("UTF-8"));
				System.out.println(str);
				System.out.println(message.getReqLogin().getAppVersion());

				// send login result
				ProtocolMessage.Builder pmLoginRltBuilder = ProtocolMessage.newBuilder();
				pmLoginRltBuilder.setType(CommMessageType.CMT_S_LOGIN);
				RespLogin.Builder respLoginBuilder = RespLogin.newBuilder();

				CommRlt.Builder rltLoginBuilder = CommRlt.newBuilder();
				rltLoginBuilder.setCode(10);
				rltLoginBuilder.setMsg("Login Success!");

				respLoginBuilder.setRlt(rltLoginBuilder.build());
				pmLoginRltBuilder.setRespLogin(respLoginBuilder.build());

				ctx.channel().writeAndFlush(pmLoginRltBuilder.build());
				break;

			case PROTOCOL.CommMessageType.CMT_C_PPTLIST_VALUE:
				System.out.println(message.getReqPptList().getVspid());
				System.out.println(message.getReqPptList().getUserid());

				// send pptlist result
				ProtocolMessage.Builder pmPPTListBuilder = ProtocolMessage.newBuilder();
				pmPPTListBuilder.setType(CommMessageType.CMT_S_PPTLIST);
				RespPPTList.Builder respPPTListBuilder = RespPPTList.newBuilder();
				for (int i = 0; i < 3; i++) {
					PPTNode.Builder pptNodeBuilder = PPTNode.newBuilder();
					pptNodeBuilder.setId(188 + i);
					pptNodeBuilder.setImage("http://server:port/image" + i + ".png");
					pptNodeBuilder.setOrder(288 + i);
					respPPTListBuilder.addNode(pptNodeBuilder.build());
				}
				respPPTListBuilder.setDesc("It's PPTList!");
				respPPTListBuilder.setTag(66666);
				CommRlt.Builder respPPTListRltBuilder = CommRlt.newBuilder();
				respPPTListRltBuilder.setCode(30);
				respPPTListRltBuilder.setMsg("PPTList Success!");
				respPPTListBuilder.setRlt(respPPTListRltBuilder.build());
				pmPPTListBuilder.setRespPptList(respPPTListBuilder.build());
				ctx.channel().writeAndFlush(pmPPTListBuilder.build());
				break;

			default:
				break;
			}

		}
	}

}
