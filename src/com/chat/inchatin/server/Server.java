package com.chat.inchatin.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {

    private int port;
    private DatagramSocket socket;
    private Thread run, manage, receive;
    private volatile boolean running = false;
    
    private List<ServerClient> clients = new ArrayList<ServerClient>();

    public Server(int port) {
        this.port = port;
        try {
            socket = new DatagramSocket(port); 
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        run = new Thread(this, "Server");
        run.start();
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Server started on port " + port);
        manageClients();
        receive();
    }

    private void manageClients() {
        manage = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Manage");
        manage.start();
    }

    private void receive() {
        receive = new Thread(() -> {
            while (running) {
                byte[] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                try {
                    socket.receive(packet);
//                    String message = new String(packet.getData(), 0, packet.getLength()); 
//                    System.out.println(message);
                } catch (IOException e) {
                    if (!running) break; 
                    e.printStackTrace();
                }
                process(packet);
                
//                System.out.println(clients.get(0).address.toString() + " " + clients.get(0).port);
            }
        }, "Receive");
        receive.start();
    }
    
    private void process(DatagramPacket packet) {
    	String data = new String(packet.getData());
    	if(data.startsWith("/c/")) {
    		int id = UniqueIdentifier.getIdentifier();
    		System.out.println("Identifier: "+id);
    		clients.add(new ServerClient(data.substring(3, data.length()), packet.getAddress(), packet.getPort(), id));
    		System.out.println(data.substring(3, data.length()));
    	}
    	else {
    		System.out.println(data);
    	}
    }

    public void stop() {
        running = false;
        socket.close(); 
        System.out.println("Server stopped.");
    }
}
