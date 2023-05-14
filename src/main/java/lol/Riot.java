package lol;

import org.apache.http.client.fluent.Request;

public class Riot{
    public static final String[] REGIONS = {
            "na1", "eun1", "euw1", "jp1", "kr", "la1", "la2",
            "br1", "oc1", "ph2", "ru", "sg2", "th2", "tr1", "tw2", "vn2"};
    private static final String RIOT_API_KEY = "RGAPI-56f62acd-6df4-4005-9b9c-01506a99c586";
    public static String REGION = "eun1";

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
                .socketTimeout(4000);
        return request;
    }
    public static Request newRequest(String endpoint){
        return newRequest(REGION, endpoint);
    }
    public static void setRoutingRegion(){
        switch (REGION){
            case "na1":
            case "br":
            case "la1":
            case "la2":
                REGION = "AMERICAS";
                break;
            case "kr":
            case "jp1":
                REGION = "ASIA";
                break;
            case "eun1":
            case "euw1":
            case "tr1":
            case "ru":
                REGION = "EUROPE";
                break;
            case "oc1":
            case "ph2":
            case "sg2":
            case "th2":
            case "tw2":
            case "vn2":
                REGION = "SEA";
                break;
            default:
                System.err.println("Region: " + REGION + " not found");
        }
    }
}
