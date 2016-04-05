package com.newman.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class DoubleServer {

	static final int PORT = 8765;

	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.option(ChannelOption.SO_BACKLOG, 10);
			b.childOption(ChannelOption.SO_KEEPALIVE, true);
			b.channel(NioServerSocketChannel.class);
			b.handler(new LoggingHandler(LogLevel.INFO));
			b.childHandler(new DoubleServerHandlerInitialize());

			// Start the server.
			ChannelFuture f = b.bind(PORT).sync();

			System.out.println("Server Start, Listen: " + PORT);
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}
}
