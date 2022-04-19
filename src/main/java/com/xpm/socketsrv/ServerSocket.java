package com.xpm.socketsrv;


import java.net.InetSocketAddress;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



//네티 서버를 실행하는 클래스이다. ApplicationStartupTask 클래스에서 스프링부트 
//서비스 시작할때 본 클래스의 start() 메소드를 실행하도록설정했음.
@Slf4j
@RequiredArgsConstructor
@Component
public class ServerSocket {
	
	private final ServerBootstrap serverBootstrap;
	private final InetSocketAddress tcpPort;
	private Channel serverChannel;
	
	
	public void start() {
		//지정한 host, port로 소켓을 바인딩하고 incoming connection을 받도록 스탠바이
		try {
			ChannelFuture serverChannelFuture = serverBootstrap.bind(tcpPort).sync();
		
			serverChannel = serverChannelFuture.channel().closeFuture().sync().channel();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//bean을 제거하기 전에 해야할 작업이 있을때 설정
	@PreDestroy
	public void stop() {
		if(serverChannel != null) {
			serverChannel.close();
			serverChannel.parent().closeFuture();
		}
	}
}
