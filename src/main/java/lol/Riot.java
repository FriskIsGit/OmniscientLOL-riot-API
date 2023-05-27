package lol;

import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Riot{
    public static final String[] REGIONS = {
            "na1", "eun1", "euw1", "jp1", "kr", "la1", "la2",
            "br1", "oc1", "ph2", "ru", "sg2", "th2", "tr1", "tw2", "vn2"};
    private static String RIOT_API_KEY = "RGAPI-991801c7-2e18-4546-8f24-71bda387deb6";
    public static String REGION = "eun1";

    static{
        loadAPIKey();
    }
    public static void setRegion(String region){
        boolean found = false;
        for(String reg : REGIONS){
            if(reg.equals(region)){
                found = true;
                break;
            }
        }
        if(found){
            REGION = region;
        }else{
            System.err.println("Region '" + region + "' not found");
        }
    }

    public static Request newRequest(String region, String endpoint){
        StringBuilder uri = new StringBuilder("https://");
        uri.append(region);
        uri.append(".api.riotgames.com");
        if(!endpoint.startsWith("/")){
            uri.append('/');
        }
        uri.append(endpoint);
        Request request = Request.Get(uri.toString());
        request.addHeader("X-Riot-Token", RIOT_API_KEY)
                .connectTimeout(2000)
                .socketTimeout(8000);
        return request;
    }
    public static Request newRequest(String endpoint){
        return newRequest(REGION, endpoint);
    }
    public static String toRoutingRegion(String region){
        switch (region){
            case "na1":
            case "br":
            case "la1":
            case "la2":
                return "AMERICAS";
            case "kr":
            case "jp1":
                return "ASIA";
            case "eun1":
            case "euw1":
            case "tr1":
            case "ru":
                return "EUROPE";
            case "oc1":
            case "ph2":
            case "sg2":
            case "th2":
            case "tw2":
            case "vn2":
                return "SEA";
            default:
                System.err.println("Region: " + region + " doesn't have a routing region");
                return null;
        }
    }
    private static void loadAPIKey(){
        URL url = Riot.class.getProtectionDomain().getCodeSource().getLocation();
        String executingPath = convertURLToString(url);
        int lastSlash = executingPath.lastIndexOf('/');
        executingPath = executingPath.substring(0, lastSlash);
        Path keyPath = Paths.get(executingPath + "/api_key.txt");
        if(!Files.exists(keyPath)){
            System.err.println("api_key.txt not found");
            return;
        }
        byte[] pathBytes;
        try{
            pathBytes = Files.readAllBytes(keyPath);
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        RIOT_API_KEY = new String(pathBytes);
    }
    private static String convertURLToString(URL url){
        String str = url.getPath();
        if(str.charAt(0) == '/'){
            str = str.substring(1);
        }
        return str.replace("%20"," ");
    }
}
