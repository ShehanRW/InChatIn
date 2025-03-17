package com.chat.inchatin;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private String name, address;
	private int port;
	private JTextField textMessage;
	private JTextArea textHistory;
	
	private DatagramSocket socket;
	private InetAddress ip;
	
	private Thread send;
	
	public Client(String name, String address, int port) {
		setTitle("InChaIn");
		this.name = name;
		this.address = address;
		this.port = port;
		
		boolean connect = openConnection(address);
		if(!connect) {
			console("Error cannot connect to the host");
		}
		
		makeWindow();
		
		console("Attempting a connection to " + address + ":" + port + " user: " + name);
		String connection = "/c/"+name;
		send(connection.getBytes());
	}
	
	public boolean openConnection(String address) {
		try {
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void send(final byte[] data) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	public String recieve() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String message = new String(packet.getData());
		return message;
	}
	
	private void makeWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900,550);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{16,827,30,7};
		gbl_contentPane.rowHeights = new int[]{35,475,40};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		textHistory = new JTextArea();
		JScrollPane scroll = new JScrollPane(textHistory);
		GridBagConstraints gbc_textHistory = new GridBagConstraints();
		textHistory.setEditable(false);
		gbc_textHistory.insets = new Insets(0, 0, 5, 5);
		gbc_textHistory.fill = GridBagConstraints.BOTH;
		gbc_textHistory.gridx = 0;
		gbc_textHistory.gridy = 0;
		gbc_textHistory.gridwidth = 3;
		gbc_textHistory.gridheight = 2;
		gbc_textHistory.insets = new Insets(0,5,0,0);
		contentPane.add(scroll, gbc_textHistory);
		
		textMessage = new JTextField();
		textMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(textMessage.getText());
				}
			}
		});
		GridBagConstraints gbc_textMessage = new GridBagConstraints();
		gbc_textMessage.insets = new Insets(0, 0, 0, 5);
		gbc_textMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_textMessage.gridx = 1;
		gbc_textMessage.gridy = 2;
		contentPane.add(textMessage, gbc_textMessage);
		textMessage.setColumns(10);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(textMessage.getText());
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 2;
		contentPane.add(btnNewButton, gbc_btnNewButton);
		
		setVisible(true);
		textMessage.requestFocusInWindow();
	}
	
	public void console(String message) {
		textHistory.append(message + "\n\r");
		textHistory.setCaretPosition(textHistory.getDocument().getLength());
	}
	
	public void send(String message) {
		if(message.equals(""))return;
		message = name + ": " + message;
		console(message);
		send(message.getBytes());
		textMessage.setText("");
	}
}
