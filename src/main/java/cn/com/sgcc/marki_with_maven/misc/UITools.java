package cn.com.sgcc.marki_with_maven.misc;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class UITools {

	private static ResourceBundle bundle = null;

	static public String getString(String key) {
		String value = "nada";
		if (bundle == null) {
			bundle = ResourceBundle.getBundle("resources.strings");
		}
		try {
		    value = bundle.getString(key);
		} catch (MissingResourceException e) {
		    System.out.println("java.util.MissingResourceException: Couldn't find value for: " + key);
		}
		return value;
	}
}
