package com.xpm.socketsrv;


import java.net.InetSocketAddress;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



//��Ƽ ������ �����ϴ� Ŭ�����̴�. ApplicationStartupTask Ŭ�������� ��������Ʈ 
//���� �����Ҷ� �� Ŭ������ start() �޼ҵ带 �����ϵ��ϼ�������.
@Slf4j
@RequiredArgsConstructor
@Component
public class ServerSocket {
	
	private final ServerBootstrap serverBootstrap;
	private final InetSocketAddress tcpPort;
	private Channel serverChannel;
	
	
	public void start() {
		//������ host, port�� ������ ���ε��ϰ� incoming connection�� �޵��� ���Ĺ���
		try {
			ChannelFuture serverChannelFuture = serverBootstrap.bind(tcpPort).sync();
		
			serverChannel = serverChannelFuture.channel().closeFuture().sync().channel();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//bean�� �����ϱ� ���� �ؾ��� �۾��� ������ ����
	@PreDestroy
	public void stop() {
		if(serverChannel != null) {
			serverChannel.close();
			serverChannel.parent().closeFuture();
		}
	}
}
