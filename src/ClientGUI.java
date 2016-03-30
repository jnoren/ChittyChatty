import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.File;
import java.net.URL;



public class ClientGUI<startingScreen> extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	// will first hold "Username:", later on "Enter message"
	private JLabel label;
	
	
	
	
	// to hold the Username and later on the messages
	private JTextField tf;
	// to hold the server address an the port number
	private JTextField tfServer, tfPort;
	// to Logout and get the list of the users
	private JButton  login, logout, whoIsIn, sendFile;
	//boolean sendFile;
	
	
	
	
	// for the chat room
	private JTextArea ta;
	// if it is for connection
	private boolean connected;
	// the Client object
	private Client client;
	// the default port number
	private int defaultPort;
	private String defaultHost;


	
	

	// Constructor connection receiving a socket number
	ClientGUI(String host, int port) {
		
		super("ChittyChatty");

	
			JDialog welcomeScreen = new JDialog();
			JPanel welcomePanel = new JPanel();
			JLabel welcome = new JLabel("ChittyChatty", SwingConstants.CENTER);
			welcomeScreen.setTitle("ChittyChatty");
			welcomePanel.setLayout(new BorderLayout());
			welcomePanel.setBackground(Color.BLUE);
			welcome.setFont(new Font("", Font.BOLD, 50));
			welcomePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
			welcome.setForeground(Color.WHITE);
			welcomePanel.add(welcome, BorderLayout.CENTER);
			welcomeScreen.add(welcomePanel);
			welcomeScreen.setSize(new Dimension(500, 700));
			welcomeScreen.setLocation(500,500);
			welcomeScreen.setVisible(true);
			welcomeScreen.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			welcomeScreen.dispose();
		
		
	
		
		defaultPort = port;
		defaultHost = host;
		
		// The NorthPanel wit
		JPanel northPanel = new JPanel(new GridLayout(3,1));
		// the server name anmd the port number
		JPanel serverAndPort = new JPanel(new GridLayout(1,5, 2, 3));
		serverAndPort.setBackground(Color.lightGray);
		
		//serverAndPort.setBackground(Color.darkGray);
		// the two JTextField with default value for server address and port number
		tfServer = new JTextField(host);
		tfPort = new JTextField("" + port);
		tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

		serverAndPort.add(new JLabel("Address  "));
		serverAndPort.add(tfServer);
		serverAndPort.add(new JLabel("Port "));
		serverAndPort.add(tfPort);
		serverAndPort.add(new JLabel(""));
		// adds the Server an port field to the GUI
		northPanel.add(serverAndPort);
		

		// the Label and the TextField
		label = new JLabel("Write Anything You Like", SwingConstants.CENTER);
		northPanel.add(label);
		tf = new JTextField("Type Away");
		tf.setBackground(Color.LIGHT_GRAY);
		northPanel.add(tf);
		add(northPanel, BorderLayout.NORTH);
		//northPanel.setBackground(Color.BITMASK); 
		

		
		
		// The CenterPanel which is the chat room
		ta = new JTextArea("Welcome ........\n", 30, 30);
		
		JPanel centerPanel = new JPanel(new GridLayout(1,1));
		centerPanel.add(new JScrollPane(ta));
		ta.setEditable(false);
		add(centerPanel, BorderLayout.CENTER);
		setLocation(500, 200); 
		

		// the 3 buttons
		//login = new JButton("login");
		
		
		ImageIcon icon = new ImageIcon("mybutton.png");
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(100, 30,  java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		login = new JButton(icon); 
		
		 
		login.addActionListener(this);
		ImageIcon icon2 = new ImageIcon("mybutton2.png");
		Image img2 = icon2.getImage();
		Image newimg2 = img2.getScaledInstance(100, 30,  java.awt.Image.SCALE_SMOOTH);
		icon2 = new ImageIcon(newimg2);
		
		logout = new JButton(icon2);
		logout.addActionListener(this);
		logout.setEnabled(false);	
		ImageIcon icon3 = new ImageIcon("mybutton4.png");
		Image img3 = icon3.getImage();
		Image newimg3 = img3.getScaledInstance(100, 30,  java.awt.Image.SCALE_SMOOTH);
		icon3 = new ImageIcon(newimg3);
		sendFile = new JButton(icon3);
		sendFile.addActionListener(this);
		
		ImageIcon icon4 = new ImageIcon("mybutton3.png");
		Image img4 = icon4.getImage();
		Image newimg4 = img4.getScaledInstance(100, 30,  java.awt.Image.SCALE_SMOOTH);
		icon4 = new ImageIcon(newimg4);
		// you have to login before being able to logout
		whoIsIn = new JButton(icon4);
		whoIsIn.addActionListener(this);
		//whoIsIn.setEnabled(false);		// you have t before being able to Who is in

		JPanel southPanel = new JPanel();
		
		southPanel.add(login);
		southPanel.add(logout);
		southPanel.add(whoIsIn);
		southPanel.add(sendFile);
		
		
		add(southPanel, BorderLayout.SOUTH);
		southPanel.setBackground(Color.lightGray);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(550,500 );
		setVisible(true);
		tf.requestFocus();
		
		

	}
	  
	// called by the Client to append text in the TextArea 
	void append(String str) {
		ta.append(str);
		ta.setCaretPosition(ta.getText().length() - 1);
		

  }
		
		
    



	// called by the GUI is the connection failed
	// we reset our buttons, label, textfield
	void connectionFailed() {
		login.setEnabled(true);
		logout.setEnabled(false);
		whoIsIn.setEnabled(false);
		label.setText("Enter your username below");
		//tf.setText("Anonymous");
				// reset port number and host name as a construction time
		tfPort.setText("" + defaultPort);
		tfServer.setText(defaultHost);
		// let the user change them
		tfServer.setEditable(false);
		tfPort.setEditable(false);
		// don't react to a <CR> after the username
		tf.removeActionListener(this);
		connected = false;
	}
		
	/*
	* Button or JTextField clicked
	*/

	//@SuppressWarnings("unused")
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		// if it is the Logout button
		if(o == logout) {
			client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
			return;
		}
		// if it the who is in button
		if(o == whoIsIn) {
			client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));				
			return;
	
		}
		if(e.getSource() == sendFile)
		{
			new SendImage();
		}

		// ok it is coming from the JTextField
		if(connected) {
			// just have to send the message
			client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, tf.getText()));				
			tf.setText("");
			
			return;
		}
		

		if(o == login) {
			// ok it is a connection request
			String username = tf.getText().trim();
			// empty username ignore it
			if(username.length() == 0)
				return;
			// empty serverAddress ignore it
			String server = tfServer.getText().trim();
			if(server.length() == 0)
				return;
			// empty or invalid port numer, ignore it
			String portNumber = tfPort.getText().trim();
			if(portNumber.length() == 0)
				return;
			int port = 0;
			try {
				port = Integer.parseInt(portNumber);
			}
			catch(Exception en) {
				return;   // nothing I can do if port number is not valid
			}
			

			// try creating a new Client with GUI
			client = new Client(server, port, username, this);
			// test if we can start the Client
			if(!client.start()) 
				return;
			tf.setText("");
			label.setText("Enter your message below");
			connected = true;
			
			// disable login button
			login.setEnabled(false);
			// enable the 2 buttons
			logout.setEnabled(true);
			whoIsIn.setEnabled(true);
			// disable the Server and Port JTextField
			tfServer.setEditable(false);
			tfPort.setEditable(false);
			// Action listener for when the user enter a message
			tf.addActionListener(this);
			
			
		}
		

	}
	

	 

	// to start the whole thing the server
	public static void main(String[] args) {
		new ClientGUI("localhost", 55555);
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

	

}