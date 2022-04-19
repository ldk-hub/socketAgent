package com.xpm.config;

import org.springframework.stereotype.Component;

import com.xpm.handler.TestDecoder;
import com.xpm.handler.TestHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SocketChannelInitializer extends ChannelInitializer<SocketChannel>{
	private final TestHandler testHandler;
	
	
	//클라이언트 소켓 채널이 생성될 때 호출
	@Override
	protected void initChannel(SocketChannel  ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//decoder는  @Sharable이 안됨. Bean객체 주입이 안되고 매번 새로운 객체 생성해야함.
		 TestDecoder testDecoder = new TestDecoder();
	
		pipeline.addLast(testDecoder);
		pipeline.addLast(testHandler);
		
	}


	
	
	
}
