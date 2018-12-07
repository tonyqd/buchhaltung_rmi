package base;

import java.io.Serializable;

public class Buchung implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userid;
	private int konto;
	private int buchungsType;
	private String betrag;
	private String datum;
	private String uhrzeit;
	private int buchungnumber;
	
	public int getBuchungnumber() {
		return buchungnumber;
	}

	public void setBuchungnumber(int buchungnumber) {
		this.buchungnumber = buchungnumber;
	}

	public Buchung(int buchungnumber, int userid, int konto, int buchungsType, String betrag, String datum, String uhrzeit) {
		this.userid = userid;
		this.konto = konto;
		this.buchungsType = buchungsType;
		this.betrag = betrag;
		this.datum = datum;
		this.uhrzeit = uhrzeit;
		this.buchungnumber = buchungnumber;
	}
	
	public Buchung(int userid, int konto, int buchungsType, String betrag, String datum, String uhrzeit) {
		this.userid = userid;
		this.konto = konto;
		this.buchungsType = buchungsType;
		this.betrag = betrag;
		this.datum = datum;
		this.uhrzeit = uhrzeit;
		this.buchungnumber = -1;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getKonto() {
		return konto;
	}

	public void setKonto(int konto) {
		this.konto = konto;
	}

	public int getBuchungsType() {
		return buchungsType;
	}

	public void setBuchungsType(int buchungsType) {
		this.buchungsType = buchungsType;
	}

	public String getBetrag() {
		return betrag;
	}

	public void setBetrag(String betrag) {
		this.betrag = betrag;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getUhrzeit() {
		return uhrzeit;
	}

	public void setUhrzeit(String uhrzeit) {
		this.uhrzeit = uhrzeit;
	}
	
}
