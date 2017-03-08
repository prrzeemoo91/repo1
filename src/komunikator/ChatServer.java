package komunikator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.*;

public class ChatServer implements Runnable {
	
	private ArrayList<Socket> connected = new ArrayList<Socket>();
	private ArrayList<String> users = new ArrayList<String>();
	private HashMap<Socket,String> data = new HashMap<Socket,String>();
	
	static final int SERVER_PORT = 17000;
	private String host;
	private ServerSocket server;
	private Timer timer;
	
	
	public ChatServer()
	{
		Thread t = new Thread(this);
		t.start();
		timer = new Timer(users);
		Thread timeThread = new Thread(timer);
		timeThread.start();		
	}
	
	
	@Override
	public void run() {
		
		Socket s;
		ChatClientThread client;
		
		try
		{
		server = new ServerSocket(SERVER_PORT);
		host = InetAddress.getLocalHost().getHostName();
		
		}
		catch(IOException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(null, "Unable to create the server socket."); 
		   	System.exit(0);
		}
		System.out.println("Server has been started at host: " + host);
		
		while(true)
		{
		try {
			s = server.accept();
			connected.add(s);
			timer.reset();

			System.out.println("Client connected from: "+ s.getLocalAddress().getHostName());

			addUser(s);

			client = new ChatClientThread(s,connected,users,data,timer);
			Thread clientT = new Thread(client);
			clientT.start();

		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Server error - unable to connect to client.");
		}
		}
		
		
	}
	
	
	
	public void addUser(Socket s) throws IOException, ClassNotFoundException
	{
		
		Scanner input = new Scanner(s.getInputStream());
		
		String name = input.nextLine();
		users.add(name);
		data.put(s,name);

		for(int i=0;i<connected.size();i++)
		{
			Socket tmps = connected.get(i);
			PrintWriter output = new PrintWriter(tmps.getOutputStream(),true);
			output.println("!%#&" + users);
	        output.flush();
		}	
	}
	
	 public static void main(String[] args) throws IOException
	 {
		 new ChatServer(); 
	 }

}

