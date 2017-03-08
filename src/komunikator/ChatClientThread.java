package komunikator;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ChatClientThread implements Runnable {

	private Socket s;
	private Scanner input;
	private PrintWriter output;
	private String msg;
	
	private ArrayList<Socket> connected;
	private ArrayList<String> users;
	private HashMap<Socket,String> data;
	private Timer timer;
	
	
	public ChatClientThread(Socket s, ArrayList<Socket> con, ArrayList<String> us, HashMap<Socket,String> data,Timer t)
	{
		this.s=s;
		this.connected=con;
		users = us;
		this.data = data;
		timer = t;		
	}
	
	public void conCheck()
	{
		if(!s.isConnected())
		{
			for(int i=0;i<connected.size();i++)
			{
				if(connected.get(i)==s)
					connected.remove(i);
				
				try{
				Socket tmps = connected.get(i);
				PrintWriter output = new PrintWriter(tmps.getOutputStream());
				output.println(s.getLocalAddress().getHostName() + " disconnected!");
				output.flush();
				}
				catch(IOException e){System.out.println(e);}
				
			}	
		}
	}

	
	@Override
	public void run() {

		try {
		try {
			input = new Scanner(s.getInputStream());
			output = new PrintWriter(s.getOutputStream());
			
			while(!s.isClosed())
			{
				conCheck();
				
				if(input.hasNext())
				{	
					msg=input.nextLine();
					
					if(msg.equals("@#$CLOSE$#@"))
					{
						String name;
						
						System.out.println("Removing");
						for(int i=0;i<connected.size();i++)
						{
							if(connected.get(i)==s)
								{
								connected.remove(i);
								name = data.get(s);
								users.remove(data.get(s));
								data.remove(s);
								input.reset();
								
								
								for(int j=0;j<connected.size();j++)
								{
									Socket tmps = connected.get(j);
									PrintWriter output = new PrintWriter(tmps.getOutputStream(),true);
									System.out.println("Sending info about users online");
									output.println("!%#&" + users);
									output.println(name + " has disconnected!");
							        							        
								}
								
								s.close();
								System.out.println(users);
								}
							
						}
					}
					else if(msg.equals("@#$PW@#$"))
					{
						int selected = Integer.valueOf(input.nextLine());
						msg = input.nextLine();
						
						Socket tmps = connected.get(selected);
						if(s!=tmps)
						{
						PrintWriter output = new PrintWriter(tmps.getOutputStream(),true);
						output.println("(PW) " + msg);
						}
					}
					
					else
					{
					System.out.println("Client said: " + msg);
					
					for(int i=0;i<connected.size();i++)
					{
						Socket tmps = connected.get(i);
						PrintWriter tmpout = new PrintWriter(tmps.getOutputStream());
						tmpout.println("(GLOBAL) "+ msg);
						tmpout.flush();
						System.out.println("Sent to: "+ tmps.getLocalAddress().getHostName());
					}
					}
				}
			}
		
		}
		finally{s.close();}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
        
		
		
	}

}
