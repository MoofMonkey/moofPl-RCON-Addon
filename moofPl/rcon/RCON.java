package moofPl.rcon;

import java.net.ServerSocket;
import java.util.ArrayList;

public class RCON extends Thread {
	public static String[] blacklist = new String[] { "pex ", "op " };
	protected static ArrayList<RCONUser> users = new ArrayList<RCONUser>();
	private int port = 10032;
	ServerSocket ss;

	public RCON(ArrayList<RCONUser> _users) {
		users = _users;
		start();
	}
	
	public RCON(ArrayList<RCONUser> _users, int _port) {
		users = _users;
		port = _port;
		start();
	}
	
	public RCON(ArrayList<RCONUser> _users, String[] _blacklist) {
		users = _users;
		blacklist = _blacklist;
		start();
	}
	
	public RCON(ArrayList<RCONUser> _users, int _port, String[] _blacklist) {
		users = _users;
		port = _port;
		blacklist = _blacklist;
		start();
	}

	public void onClose() throws Throwable {
		ss.close();
	}

	@Override
	public void run() {
		try {
			ss = new ServerSocket(port);
			while (!ss.isClosed())
				new RCONImpl(ss.accept()).start();
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
