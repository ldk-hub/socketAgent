package com.xpm.config;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class XpmConfiguration{
	
	@Value("${server.host}")
    private String host;
    @Value("${server.port}")
    private int port;
    @Value("${server.netty.boss-count}")
    private int bossCount;
    @Value("${server.netty.worker-count}")
    private int workerCount;
    @Value("${server.netty.keep-alive}")
    private boolean keepAlive;
    @Value("${server.netty.backlog}")
    private int backlog;
    
    @PostConstruct
    public void init() {
    	System.out.println(workerCount);
    }
	@Bean
	public ServerBootstrap serverBootstrap(SocketChannelInitializer socketChannelInitializer) {
		//ServerBootstrap >>서버 설정을 도와주는 class
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGruop(), workerGroup())
		// NioServerSocketChannel: incoming connections를 수락하기 위해 새로운 Channel을 객체화할 때 사용
		.channel(NioServerSocketChannel.class)
		.handler(new LoggingHandler(LogLevel.DEBUG))
		// ChannelInitializer: 새로운 Channel을 구성할 때 사용되는 특별한 handler. 주로 ChannelPipeline으로 구성
		.childHandler(socketChannelInitializer);
		
		//ServerBootstrap에 대한 Option 추가기능
		//SO_BACKLOG:동시에 수용가능한 최대 incoming connection 개수
		//이 외에도 SO_KEEPALIVE, TCP_NODELEY등 옵션이있음.		
		b.option(ChannelOption.SO_BACKLOG, backlog);
		return b;
	}

	//boss > incoming connection을 수락하고, 수락한 connection을 worker에게 등록(레지스터)
	@Bean(destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup bossGruop() {
		return new NioEventLoopGroup(workerCount);
	}
	
	
	//worker : boss 가 수락한 연결의 트래픽 관리
		@Bean(destroyMethod = "shutdownGracefully")
		public NioEventLoopGroup workerGroup() {
			return new NioEventLoopGroup(bossCount);
		}

	//IP 소켓 주소(IP주소, Port 번호) 구현
	//도메인 이름으로 객체 생성가능
	@Bean
	public InetSocketAddress inetSocketAddress() {
		return new InetSocketAddress(host, port);
	}
	
}
