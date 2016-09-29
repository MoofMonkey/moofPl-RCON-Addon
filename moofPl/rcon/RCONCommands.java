package moofPl.rcon;

import java.io.DataOutputStream;
import java.io.File;

import moofPl.Main;

public enum RCONCommands {
	AUTH_USER (
			"USER",
			new RCONCommandExecutor() {
				@Override
				public void execute(RCONImpl caller, String[] args, DataOutputStream out) throws Throwable {
					String user = "";
					for(String str : args)
						user += str + " ";
					user = user.substring(0, user.length() - 2);
					
					caller.userName = user;
				}
			},
			true
	),
	AUTH_PASSWORD (
			"PWD",
			new RCONCommandExecutor() {
				@Override
				public void execute(RCONImpl caller, String[] args, DataOutputStream out) throws Throwable {
					RCONUser user = RCONUtils.getUserByName(caller.userName);
					if(user == null) {
						caller.onCommand(HACKINGATTEMPT, null);
						return;
					}
					
					String pwd = "";
					for(String str : args)
						pwd += str + " ";
					pwd = pwd.substring(0, pwd.length() - 2);
					boolean isCorrect = user.password.equals(pwd);
					
					if(!isCorrect)
						caller.onCommand(HACKINGATTEMPT, null);
					else
						out.writeUTF("Password accepted.");
				}
			},
			false
	),
	ALREADYAUTHED (
			"PWD",
			new RCONCommandExecutor() {
				@Override
				public void execute(RCONImpl caller, String[] args, DataOutputStream out) throws Throwable {
					out.writeUTF("Already authed!");
				}
			},
			false
	),
	CMD (
			new String[] { "CMD", "BUKKITCMD" },
			new RCONCommandExecutor() {
				@Override
				public void execute(RCONImpl caller, String[] args, DataOutputStream out) throws Throwable {
					String cmd = "";
					for(String str : args)
						cmd += str + " ";
					cmd = cmd.substring(0, cmd.length() - 2);
					
					if(RCONUtils.checkIsInBlackList(cmd))
						if(!caller.user.isSuperUser) {
							caller.onCommand(HACKINGATTEMPT, null);
							return;
						}
					
					if (Main.exec(cmd))
						out.writeUTF("Executed " + cmd);
					else
						out.writeUTF("Can't execute " + cmd);
				}
			},
			false
	),
	GETINFO (
			"GETINFO",
			new RCONCommandExecutor() {
				@Override
				public void execute(RCONImpl caller, String[] args, DataOutputStream out) throws Throwable {
					out.writeUTF("OS: " + System.getProperty("os.name"));
					out.writeUTF("UserName: " + System.getProperty("user.name"));
					out.writeUTF("UserHome: " + System.getProperty("user.home"));
					out.writeUTF("UserWork: " + System.getProperty("user.dir"));
					out.writeUTF("Architecture: " + System.getProperty("os.arch"));
					out.writeUTF("JRE " + System.getProperty("java.version"));
					out.writeUTF("Processors (cores): " + Runtime.getRuntime().availableProcessors());
					out.writeUTF("Free Memory: " + Runtime.getRuntime().freeMemory());
					out.writeUTF("All Memory: " + Runtime.getRuntime().maxMemory());
					out.writeUTF("JRE's Memory: " + Runtime.getRuntime().totalMemory());
					File[] roots = File.listRoots();
					for (File root : roots) {
						out.writeUTF(root.getName() + " File system root: " + root.getAbsolutePath());
						out.writeUTF(root.getName() + " Total space (bytes): " + root.getTotalSpace());
						out.writeUTF(root.getName() + " Free space (bytes): " + root.getFreeSpace());
						out.writeUTF(root.getName() + " Usable space (bytes): " + root.getUsableSpace());
					}
				}
			},
			false
	),
	GC (
			"GC",
			new RCONCommandExecutor() {
				@Override
				public void execute(RCONImpl caller, String[] args, DataOutputStream out) throws Throwable {
					out.writeUTF("Executing GC....");
					out.flush();
					System.gc();
				}
			},
			true
	),
	PINGPONG (
			"PING",
			new RCONCommandExecutor() {
				@Override
				public void execute(RCONImpl caller, String[] args, DataOutputStream out) throws Throwable {
					out.writeUTF("PONG");
				}
			},
			false
	),
	BYE (
			"BYE",
			new RCONCommandExecutor() {
				@Override
				public void execute(RCONImpl caller, String[] args, DataOutputStream out) throws Throwable {
					out.writeUTF("BYE =3");
					out.flush();
					caller.s.close();
				}
			},
			true
	),
	HACKINGATTEMPT (
			new String[] {  },
			new RCONCommandExecutor() {
				@Override
				public void execute(RCONImpl caller, String[] args, DataOutputStream out) throws Throwable {
					out.writeUTF("Hacking attempt!");
					out.flush();
					caller.s.close();
				}
			},
			true
	),
	ONCLOSE (
			new String[] {  },
			new RCONCommandExecutor() {
				@Override
				public void execute(RCONImpl caller, String[] args, DataOutputStream out) throws Throwable {
					out.writeUTF("Server shutting down. BYE =3");
				}
			},
			false
	),
	UNKNOWN (
			new String[] {  },
			new RCONCommandExecutor() {
				@Override
				public void execute(RCONImpl caller, String[] args, DataOutputStream out) throws Throwable {
					out.writeUTF("Unknown command");
				}
			},
			false
	),
	;
	
	String[] names;
	RCONCommandExecutor executor;
	boolean isCustom;
	
	private RCONCommands(String _name, RCONCommandExecutor _executor, boolean _isCustom) {
		names = new String[] { _name };
		executor = _executor;
		isCustom = _isCustom;
	}
	
	private RCONCommands(String[] _names, RCONCommandExecutor _executor, boolean _isCustom) {
		names = _names;
		executor = _executor;
		isCustom = _isCustom;
	}
	
	public static RCONCommands getCommandByname(String name) {
		RCONCommands command = UNKNOWN;
		
		for(RCONCommands cmd : values())
			for(String str : cmd.names)
				if(str.equals(name))
					command = cmd;
		
		return command;
	}
}
