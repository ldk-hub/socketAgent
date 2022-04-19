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
		//ServerBootstrap >>���� ������ �����ִ� class
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGruop(), workerGroup())
		// NioServerSocketChannel: incoming connections�� �����ϱ� ���� ���ο� Channel�� ��üȭ�� �� ���
		.channel(NioServerSocketChannel.class)
		.handler(new LoggingHandler(LogLevel.DEBUG))
		// ChannelInitializer: ���ο� Channel�� ������ �� ���Ǵ� Ư���� handler. �ַ� ChannelPipeline���� ����
		.childHandler(socketChannelInitializer);
		
		//ServerBootstrap�� ���� Option �߰����
		//SO_BACKLOG:���ÿ� ���밡���� �ִ� incoming connection ����
		//�� �ܿ��� SO_KEEPALIVE, TCP_NODELEY�� �ɼ�������.		
		b.option(ChannelOption.SO_BACKLOG, backlog);
		return b;
	}

	//boss > incoming connection�� �����ϰ�, ������ connection�� worker���� ���(��������)
	@Bean(destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup bossGruop() {
		return new NioEventLoopGroup(workerCount);
	}
	
	
	//worker : boss �� ������ ������ Ʈ���� ����
		@Bean(destroyMethod = "shutdownGracefully")
		public NioEventLoopGroup workerGroup() {
			return new NioEventLoopGroup(bossCount);
		}

	//IP ���� �ּ�(IP�ּ�, Port ��ȣ) ����
	//������ �̸����� ��ü ��������
	@Bean
	public InetSocketAddress inetSocketAddress() {
		return new InetSocketAddress(host, port);
	}
	
}
