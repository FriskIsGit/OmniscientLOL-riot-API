package lol.dtos;


import com.alibaba.fastjson.JSONObject;

public class LeagueEntryDTO{
    public String leagueId, summonerId, summonerName, queueType, tier, rank;
    public int leaguePoints, wins, losses;
    public boolean hotStreak, veteran, freshBlood, inactive;
    public MiniSeriesDTO miniseries;

    public static LeagueEntryDTO fromJson(JSONObject json){
        LeagueEntryDTO leagueEntry = new LeagueEntryDTO();
        leagueEntry.leagueId = json.getString("leagueId");
        leagueEntry.summonerId = json.getString("summonerId");
        leagueEntry.summonerName = json.getString("summonerName");
        leagueEntry.queueType = json.getString("queueType");
        leagueEntry.tier = json.getString("tier");
        leagueEntry.rank = json.getString("rank");

        leagueEntry.leaguePoints = json.getInteger("leaguePoints");
        leagueEntry.wins = json.getInteger("wins");
        leagueEntry.losses = json.getInteger("losses");

        leagueEntry.hotStreak = json.getBoolean("hotStreak");
        leagueEntry.veteran = json.getBoolean("veteran");
        leagueEntry.freshBlood = json.getBoolean("freshBlood");
        leagueEntry.inactive = json.getBoolean("inactive");
        leagueEntry.miniseries = MiniSeriesDTO.fromJson(json.getJSONObject("miniseries"));
        return leagueEntry;
    }

    public static LeagueEntryDTO fromJson(String json){
        if(json == null)
            return null;
        return fromJson(JSONObject.parseObject(json));
    }

    @Override
    public String toString(){
        return "LeagueEntryDTO{" +
                "leagueId='" + leagueId + '\'' +
                ", summonerId='" + summonerId + '\'' +
                ", summerName='" + summonerName + '\'' +
                ", queueType='" + queueType + '\'' +
                ", tier='" + tier + '\'' +
                ", rank='" + rank + '\'' +
                ", leaguePoints=" + leaguePoints +
                ", wins=" + wins +
                ", losses=" + losses +
                ", hotStreak=" + hotStreak +
                ", veteran=" + veteran +
                ", freshBlood=" + freshBlood +
                ", inactive=" + inactive +
                ", miniseries=" + miniseries +
                '}';
    }
}
