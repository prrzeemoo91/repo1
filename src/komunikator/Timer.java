package komunikator;

import java.util.ArrayList;

import javax.swing.JOptionPane;

class Timer implements Runnable{

	private int time=0;
	private ArrayList<String> users;
	private int shutdown;
	
	public Timer(ArrayList<String> users)
	{
		this.users = users;
		try{
		shutdown=Integer.parseInt(JOptionPane.showInputDialog("Enter auto-shutdown time (sec):"));
		}
		catch(NumberFormatException e){
		JOptionPane.showMessageDialog(null, "Wrong format, auto-shutdown set to 10sec of inactivity.");	
		shutdown=10;
		}
	}
	
	@Override
	public void run() {

		while(true)
		{
			try {
				Thread.sleep(1000);
				time++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if(users.size()==0 && time>shutdown)
				{
				JOptionPane.showMessageDialog(null,"Server auto-shuttdown.");
				System.exit(0);
				}
			
		}
	}
	
	public int getTime()
	{
		return time;
	}
	public void reset()
	{
		time=0;
	}
	
}