package com.chat.inchatin.server;

import java.net.InetAddress;

public class ServerClient {
	public String name;
	public InetAddress address;
	public int port;
	public int ID;
	public int attempt = 0;
	
	public ServerClient(String name, InetAddress address, int port, final int ID) {
		this.name = name;
		this.address = address;
		this.port = port;
		this.ID = ID;
	}
	
	public int getId() {
		return ID;
	}
}
