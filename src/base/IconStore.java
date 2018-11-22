package base;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public class IconStore {

	private static ImageIcon okButton = null;
	private static Locale de_DE = new Locale("de", "DE");
	private static ResourceBundle bundle = ResourceBundle.getBundle("client.bundles.buchhaltung", de_DE);
	private static String imagedir = "/client/panel/icons/";
	
	
	public static ImageIcon getokButton()
	{
		if (okButton == null)
		{
			String dir = imagedir + bundle.getString("okButtonName");
			java.net.URL imgURL = IconStore.class.getClass().getResource(dir);
	        if (imgURL != null) {
	            return new ImageIcon(imgURL);
	        } else {
	            System.err.println("Couldn't find file: " + imagedir);
	            return null;
	        }
		}
		
		return okButton;
	}
	
	public static ImageIcon getabrechenButton()
	{
		if (okButton == null)
		{
			String dir = imagedir + bundle.getString("abrechenButtonName");
			java.net.URL imgURL = IconStore.class.getClass().getResource(dir);
	        if (imgURL != null) {
	            return new ImageIcon(imgURL);
	        } else {
	            System.err.println("Couldn't find file: " + imagedir);
	            return null;
	        }
		}
		
		return okButton;
	}
}
