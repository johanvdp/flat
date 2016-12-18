package nl.jvdploeg.flat.start;

import java.util.Locale;
import java.util.ResourceBundle;

public class Start {

    public static void main(final String[] args) {
        final Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle("nl.jvdploeg.flat.start.Start", locale);
        String key = "key";
		String value = bundle.getString(key );
        System.out.println(key + "=" + value);
    }

}
