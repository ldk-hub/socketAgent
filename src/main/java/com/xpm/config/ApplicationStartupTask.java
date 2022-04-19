package com.xpm.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.xpm.socketsrv.ServerSocket;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationStartupTask implements ApplicationListener<ApplicationReadyEvent> {

	private final ServerSocket serverSocket;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		serverSocket.start();
	}
}