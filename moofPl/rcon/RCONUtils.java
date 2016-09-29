package moofPl.rcon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RCONUtils {
	protected static boolean checkIsInBlackList(String cmd) {
		for(String str : RCON.blacklist)
			if(cmd.startsWith(str))
				return true;
		
		return false;
	}
	
	protected static RCONUser getUserByName(String userName) {
		if(userName == null || RCON.users == null)
			return null;
		
		for(RCONUser user : RCON.users)
			if(user.userName.equals(userName))
				return user;
		
		return null;
	}
	
	public static ArrayList<RCONUser> parseUsers(ArrayList<String> ar) throws Throwable {
		ArrayList<RCONUser> arNew = new ArrayList<RCONUser>();
		
		try {
			for(String str : ar) {
				String[] split = str.split("\t");
				
				String user = split[0];
				String password = split[1];
				boolean isSuperUser = Boolean.parseBoolean(split[2]);
				
				arNew.add(new RCONUser(user, password, isSuperUser));
			}
		} catch(Throwable t) {
			throw new UnsupportedOperationException("Bad array of users!");
		}
		
		return arNew;
	}
	
	private static ArrayList<String> parseFile(File f) throws Throwable {
		ArrayList<String> strs = new ArrayList<String>();
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
			while(br.ready())
				strs.add(br.readLine());
		} catch(Throwable t) {
			throw new UnsupportedOperationException("I/O Exception", t);
		}
		
		return strs;
	}
	
	public static ArrayList<RCONUser> parseUsers(File f) throws Throwable {
		return parseUsers(parseFile(f));
	}
}
