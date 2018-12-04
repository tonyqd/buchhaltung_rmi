package base;

import java.io.Serializable;

public class BuchungType  implements Serializable{
	private static final long serialVersionUID = 1L;
	private int index;
	private String buchungname;


	public BuchungType (int index, String buchungname)
	{
		this.index = index;
		this.buchungname = buchungname; 
	}

	public String getBuchungname() {
		return buchungname;
	}

	public void setBuchungname(String buchungname) {
		this.buchungname = buchungname;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex()
	{
		return index;
	}


}
