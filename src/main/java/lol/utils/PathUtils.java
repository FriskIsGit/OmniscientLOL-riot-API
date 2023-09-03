package lol.utils;

import java.net.URL;

public class PathUtils{
    public static final String OS = System.getProperty("os.name");

    public static String convertURLToString(URL url){
        String str = url.getPath();
        if(OS.startsWith("Win") && str.charAt(0) == '/'){
            str = str.substring(1);
        }
        return str.replace("%20"," ");
    }
}
