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
	
	
	//Ŭ���̾�Ʈ ���� ä���� ������ �� ȣ��
	@Override
	protected void initChannel(SocketChannel  ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//decoder��  @Sharable�� �ȵ�. Bean��ü ������ �ȵǰ� �Ź� ���ο� ��ü �����ؾ���.
		 TestDecoder testDecoder = new TestDecoder();
	
		pipeline.addLast(testDecoder);
		pipeline.addLast(testHandler);
		
	}


	
	
	
}
