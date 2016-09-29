package moofPl.rcon;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class RCONImpl extends Thread {
	Socket s;
	DataInputStream in;
	DataOutputStream out;
	public String userName;
	public RCONUser user;
	
	public RCONImpl(Socket _s) {
		s = _s;
	}
	
	@Override
	public void run() {
		try {
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
			
			String line;
			while(!s.isClosed()) {
				line = in.readUTF();
				
				String[] split = line.split(" ");
				String name = split[0];
				String[] args = new String[] {  };
				
				if(split.length >= 2)
					System.arraycopy(split, 1, args, 0, split.length - 2);
				
				if(user == null && !name.equals("PWD"))
					if(user == null && name.equals("USER"))
						onCommand("USER", args);
					else {
						onCommand(RCONCommands.HACKINGATTEMPT, null);
						break;
					}
				
				if(user != null && line.startsWith("PWD"))
					onCommand(RCONCommands.ALREADYAUTHED, null);
				
				onCommand(name, args);
			}
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	protected void onCommand(String name, String[] args) throws Throwable {
		onCommand(RCONCommands.getCommandByname(name), args);
	}
	
	protected void onCommand(RCONCommands command, String[] args) throws Throwable {
		command.executor.execute(this, args, out);
		if(!command.isCustom)
			out.flush();
	}
	
	public void onClose() throws Throwable {
		onCommand(RCONCommands.ONCLOSE, null);
		
		in.close();
		out.close();
		s.close();
	}
}
