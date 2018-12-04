package base;

import java.io.Serializable;

public class Konto implements Serializable{

	private static final long serialVersionUID = 1L;
	private int index;
	private String Kontoname;
	
	public Konto(int index, String kontoname) {
		this.index = index;
		Kontoname = kontoname;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getKontoname() {
		return Kontoname;
	}
	public void setKontoname(String kontoname) {
		Kontoname = kontoname;
	}
	
}
