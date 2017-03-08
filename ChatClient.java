import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

public class ChatClient extends JFrame implements Runnable,WindowListener {

	private static final long serialVersionUID = -2955747500308735363L;
	
	static final int SERVER_PORT = 17000;
	private String host;
	private Socket s;
	private String name;
	private Scanner input;
	private PrintWriter output;
	
	private JList<String> activelist = new JList<String>();
	private JLabel active = new JLabel("OnUsers:");
	private JTextArea history = new JTextArea(15,30);
	private JLabel msglab = new JLabel("Message:");
	private JTextField message = new JTextField(30);
	private JScrollPane scrollactive = new JScrollPane();
	
	private int selectedIndex;
	
	public ChatClient(String n)
	{
		super(n+"'s Client");
		name = n;
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(this);
		
		setSize(545,345);
		
		JPanel panel = new JPanel();
		
		panel.add(active);
		panel.add(activelist);
		
		 scrollactive.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	     scrollactive.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	     scrollactive.setViewportView(activelist);
	     panel.add(scrollactive);
		
		activelist.addListSelectionListener(e ->{
			
			selectedIndex = activelist.getSelectedIndex();
			System.out.println(selectedIndex);

		});
		
		panel.add(history);
		
		JScrollPane scroll_bars = new JScrollPane(history,
		  		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.add(scroll_bars);
		panel.add(msglab);
		panel.add(message);

		
		history.setEditable(false);
		
		DefaultCaret caret = (DefaultCaret)history.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		message.addActionListener(e ->
		{
			try {
				output = new PrintWriter(s.getOutputStream(),true);
			} catch (Exception e1) {

				e1.printStackTrace();
			}
			
			String tmp = message.getText();
			if(!tmp.equals(""))
			{
				if(!activelist.isSelectionEmpty())
				{
				 output.println("@#$PW@#$");
				 output.println(selectedIndex);
				 output.println(name + ": " + tmp);
				 history.append( "(PW) " + name + ": " + tmp + "\n");
				}
				else			
				{output.println(name + ": " + tmp);}
			}
			
			message.setText("");
		});
		
		String [] test = {"Anonymous","Anonymous"};
		activelist.setListData(test);
		
		this.setContentPane(panel);
		
		Thread t = new Thread(this);
		t.start();
		
		setVisible(true);
	}
	
	
	 public static void main(String[] args) throws IOException
	 {
		 String nickname =  JOptionPane.showInputDialog(null, "Enter your nickname:", "Log In", JOptionPane.PLAIN_MESSAGE);
		
		 if(nickname.equals(""))
		 new ChatClient("Anonymous");
		 else
	     new ChatClient(nickname);
	 }


	@Override
	public void run() {
		try{
		host = InetAddress.getLocalHost().getHostName();
		s = new Socket(host, SERVER_PORT);
		output = new PrintWriter(s.getOutputStream(),true);
		input = new Scanner(s.getInputStream());
		output.println(name);	
		
		while(!s.isClosed())
		{	
			
			if(input.hasNext())
	        {
	            String msg = input.nextLine();
	            
	            if(msg.contains("!%#&"))
	            {
	                String tmp = msg.substring(4);
	                tmp = tmp.replace("[", "");
	                tmp = tmp.replace("]", "");
	                
	                String[] users = tmp.split(", ");
	                
	                activelist.setListData(users);
	            }
	            else
	            {
	                history.append(msg+"\n");
	            }
	        }
		}
		}
		catch(IOException e)
		{
			System.out.println(e);
			JOptionPane.showMessageDialog(null, "Unable to connect to the server.");
			System.exit(0);
		}
		
	}


	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}


	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}


	@Override
	public void windowClosing(WindowEvent arg0) {		
		
		output.println("@#$CLOSE$#@");
		output.flush();
		
		try {
			s.close();
			System.out.println("Closing " + name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}


	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}


	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}


	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}
}


