package moofPl.rcon;

public class RCONUser {
	public String userName, password;
	public boolean isSuperUser;
	
	public RCONUser(String _userName, String _password, boolean _isSuperUser) {
		userName = _userName;
		password = _password;
		isSuperUser = _isSuperUser;
	}
}
