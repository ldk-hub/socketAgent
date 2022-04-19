package com.xpm.handler;

import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Sharable
@RequiredArgsConstructor
public class TestHandler extends ChannelInboundHandlerAdapter {
	private int DATA_LENGTH = 1024;
	private ByteBuf buff;
	
	
	//�ڵ鷯 ȣ��޼ҵ�
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		buff = ctx.alloc().buffer(DATA_LENGTH);
	}
	
	// �ڵ鷯�� ���ŵ� �� ȣ��Ǵ� �޼ҵ�
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        buff = null;
    }

    // Ŭ���̾�Ʈ�� ����Ǿ� Ʈ������ ������ �غ� �Ǿ��� �� ȣ��Ǵ� �޼ҵ�
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.info("Remote Address: " + remoteAddress);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf mBuf = (ByteBuf) msg;
        buff.writeBytes(mBuf);  // Ŭ���̾�Ʈ���� ������ �����Ͱ� ������
        mBuf.release();

        final ChannelFuture f = ctx.writeAndFlush(buff);
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        ctx.close();
        cause.printStackTrace();
    }
	
	
	
}
