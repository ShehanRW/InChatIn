package com.chat.inchatin;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textName;
	private JTextField textAddress;
	private JLabel lblAddress;
	private JLabel lblPort;
	private JTextField textPort;
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300,380);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textName = new JTextField();
		textName.setBounds(64, 48, 156, 20);
		contentPane.add(textName);
		textName.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Name");
		lblNewLabel.setBounds(64, 28, 46, 14);
		contentPane.add(lblNewLabel);
		
		textAddress = new JTextField();
		textAddress.setBounds(64, 106, 156, 20);
		contentPane.add(textAddress);
		textAddress.setColumns(10);
		
		lblAddress = new JLabel("IP Address");
		lblAddress.setBounds(64, 89, 66, 14);
		contentPane.add(lblAddress);
		
		lblPort = new JLabel("Port");
		lblPort.setBounds(64, 151, 66, 14);
		contentPane.add(lblPort);
		
		textPort = new JTextField();
		textPort.setColumns(10);
		textPort.setBounds(64, 169, 156, 20);
		contentPane.add(textPort);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textName.getText();
				String address = textAddress.getText();
				int port = Integer.parseInt(textPort.getText());
				loginFunc(name, address, port);
			}
		});
		btnNewButton.setBounds(97, 278, 89, 23);
		contentPane.add(btnNewButton);
	}
	
	private void loginFunc(String name, String address, int port) {
		dispose();
		new Client(name, address, port);
	}
}
