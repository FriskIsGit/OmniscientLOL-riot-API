package lol.opgg;

import lol.ranks.LeagueRank;
import lol.ranks.Queue;
import lol.requests.Option;
import lol.requests.SimpleResponse;
import lol.requests.URIPath;
import org.apache.http.client.fluent.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// serves as a web-scraper for op.gg website
// it's used to retrieve only the ranks of summoners in previous seasons as they're normally inaccessible in riot's API
// this information can be used to find a more accurate rank average of players who are currently unranked
public class OPGG{
    private static final String endpoint = "https://www.op.gg/summoners/{region}/{name}";
    private static final String[] REGIONS = {
            "na", "euw", "eune", "oce",
            "kr", "jp", "br", "las", "lan",
            "ru", "tr", "sg", "ph", "tw", "vn", "th"
    };
    //internal caching
    private static final HashMap<String, List<RankEntry>> summonersToDuoRanks = new HashMap<>();

    public static List<RankEntry> retrievePastSeasonRank(String region, String name){
        region = toOPGGRegion(region);
        List<RankEntry> entries = summonersToDuoRanks.get(region + name);
        if(entries != null){
            return entries;
        }
        String url = URIPath.of(endpoint).args(region, name);
        Request request = Request.Get(url).addHeader("Accept", "text/html")
                .connectTimeout(2000)
                .socketTimeout(4000);
        Option<SimpleResponse> responseOption = SimpleResponse.performRequest(request);
        if(!responseOption.isSome()){
            System.err.println("There was no response");
            return Collections.emptyList();
        }
        SimpleResponse response = responseOption.unwrap();
        String body = response.body;
        if(!body.startsWith("<!DOCTYPE html>") && !body.startsWith("<html")){
            System.err.println("HTML body missing.");
            //return Collections.emptyList();
        }
        int tiers = body.indexOf("<ul class=\"tier-list\"");
        int moreTiers = response.body.indexOf("<ul class=\"more-tier-list\"", tiers); // javascript click needed
        if(moreTiers != -1){
            System.out.println("More tiers are present in the html.");
        }
        if(tiers == -1){
            System.err.println("There's no tier-list for user: " + name);
            return Collections.emptyList();
        }
        List<RankEntry> rankEntries;
        try{
            rankEntries = parseLiElements(body, tiers + 21);
        }catch (StringIndexOutOfBoundsException e){
            System.err.println("Parsing failed, out of bounds substring");
            return Collections.emptyList();
        }
        summonersToDuoRanks.put(region + name, rankEntries);
        return rankEntries;
    }

    public static List<RankEntry> parseLiElements(String html, int from){
        int len = html.length();
        List<RankEntry> entries = new ArrayList<>();
        for (int i = from; i < len; i++){
            if(html.charAt(i) != '<'){
                continue;
            }
            String tag = html.substring(i, i+4);
            switch (tag){
                case "<li>":
                    int boldSt = html.indexOf("<b>", i + 4);
                    int bolEnd = html.indexOf("</b>", boldSt + 3);
                    String mixedSeason = html.substring(boldSt + 3, bolEnd);

                    int divEnd = html.indexOf("</div", bolEnd + 4);
                    String mixedRank = html.substring(bolEnd + 4, divEnd);
                    entries.add(toRankEntry(mixedSeason, mixedRank));
                    i = divEnd;
                    break;
                // </ul> tier-list end
                case "</ul":
                    return entries;
                default:
                    break;
            }
        }
        return entries;
    }

    private static RankEntry toRankEntry(String mixedSeason, String mixedRank){
        int numSt = -1;
        for (int i = 0; i < mixedSeason.length(); i++){
            char chr = mixedSeason.charAt(i);
            if(Character.isDigit(chr)){
                if(numSt == -1){
                    numSt = i;
                }
            }
        }
        int yearOrSeason = Integer.parseInt(mixedSeason.substring(numSt));

        int tierSt = -1;
        int from = 0;
        int rankTier = -1, division = 1;
        for (int i = 0; i < mixedRank.length(); i++){
            char chr = mixedRank.charAt(i);
            if(Character.isLetter(chr)){
                if(tierSt == -1){
                    tierSt = i;
                }
            }else if(tierSt != -1){
                String tier = mixedRank.substring(tierSt, i).toUpperCase();
                rankTier = LeagueRank.tier(tier);
                from = i;
                break;
            }
        }

        for (int i = from; i < mixedRank.length(); i++){
            char chr = mixedRank.charAt(i);
            if(Character.isDigit(chr)){
                division = chr - 48;
                break;
            }
        }
        LeagueRank rank = new LeagueRank(rankTier, division, Queue.RANKED_SOLO_5x5);
        return new RankEntry(yearOrSeason, rank);
    }

    private static String toOPGGRegion(String riotRegion){
        switch (riotRegion){
            case "na1":
                return "na";
            case "eun1":
                return "eune";
            case "euw1":
                return "euw";
            case "jp1":
                return "jp";
            case "kr":
                return "kr";
            case "ru":
                return "ru";
            case "la1":
                return "lan";
            case "la2":
                return "las";
            case "oc1":
                return "oce";
            case "tr1":
                return "tr";
            case "ph2":
                return "ph";
            case "sg2":
                return "sg";
            case "th2":
                return "th";
            case "tw2":
                return "tw";
            case "vn2":
                return "vn";
            default:
                return riotRegion;
        }
    }
}
