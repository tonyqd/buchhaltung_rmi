package base;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DatenbankManager  extends Remote{
	public static final String SERVICE_NAME = "Datenbank";

	void printMsg() throws RemoteException;

	int userLogin(String username, String passwort) throws RemoteException;
	
	int checkUserAvailable(String username) throws RemoteException;
	
	int createUser(String username, String passwort, String vorname, String nachname, int Geschlecht) throws RemoteException;
	
	User getUser(int userid) throws RemoteException;
	
	List<BuchungType> getBuchungTypen() throws RemoteException;
	
	List<Konto> getKontonamen() throws RemoteException;
	
	int insertBuchung(Buchung buchung) throws RemoteException;
	
	List<Buchung> getBuchungen(int userid, String von, String bis) throws RemoteException;
}
