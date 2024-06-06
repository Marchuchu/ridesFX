package eus.ehu.ridesfx.utils;

import java.util.ResourceBundle;

public class StringUtils {


    public static String translate(String txt) {
        return ResourceBundle.getBundle("Etiquetas").getString(txt);
    }
}
