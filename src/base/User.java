package base;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String vorname;
	private String nachname;
	private int userid;
	
	public User(int userid, String username, String vorname, String nachname)
	{
		this.userid = userid;
		this.username = username;
		this.vorname = vorname;
		this.nachname = nachname;
	}
	
	
	public String getUsername()
	{
		return username;
	}
	
	public String getVorname()
	{
		return vorname;
	}
	
	public int getUserid()
	{
		return userid;
	}
	
	public String getNachname()
	{
		return nachname;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public void setVorname(String vorname)
	{
		this.vorname = vorname;
	}
	
	public void setUserid(int userid)
	{
		this.userid = userid;
	}
	
	public void setNachname(String nachname)
	{
		this.nachname = nachname;
	}
}
