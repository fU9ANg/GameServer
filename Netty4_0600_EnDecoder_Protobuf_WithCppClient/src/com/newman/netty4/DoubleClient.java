package com.newman.netty4;

import com.newman.netty4.netty4.protobuf.*;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class DoubleClient {

	private final String host;
	private final int port;

	public DoubleClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void run() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.handler(new DoubleClientHandlerInitialize());

			Channel channel = bootstrap.connect(host, port).sync().channel();

			// login
			PROTOCOL.ProtocolMessage.Builder pmbuilder = PROTOCOL.ProtocolMessage.newBuilder();
			pmbuilder.setType(PROTOCOL.CommMessageType.CMT_C_LOGIN);
			PROTOCOL.ReqLogin.Builder loginbuilder = PROTOCOL.ReqLogin.newBuilder();
			loginbuilder.setId(67);
			loginbuilder.setToken("JAVA skdJFKSJDFKJSKDJFfff");
			loginbuilder.setAppVersion("v1.3");
			loginbuilder.setPlatform("android v1.0 versi的");
			pmbuilder.setReqLogin(loginbuilder.build());
			channel.write(pmbuilder.build());

			// pptlist
			PROTOCOL.ProtocolMessage.Builder pmbuilder2 = PROTOCOL.ProtocolMessage.newBuilder();
			pmbuilder2.setType(PROTOCOL.CommMessageType.CMT_C_PPTLIST);
			PROTOCOL.ReqPPTList.Builder pptlistbuider = PROTOCOL.ReqPPTList.newBuilder();
			pptlistbuider.setUserid(10);
			pptlistbuider.setVspid(20);
			pmbuilder2.setReqPptList(pptlistbuider.build());
			channel.write(pmbuilder2.build());

			channel.flush();

			channel.closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new DoubleClient("localhost", 8765).run();
	}
}
