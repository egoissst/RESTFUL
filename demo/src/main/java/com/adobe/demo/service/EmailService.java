package com.adobe.demo.service;

public class EmailService {
	private String host;
	private int port;
	
	public EmailService(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void sendEmail(String msg) {
		System.out.println(msg + " sent to " + host + ": " + port );
	}
}
