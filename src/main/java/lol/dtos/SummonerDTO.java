package lol.dtos;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

public class SummonerDTO{
    private static final HashMap<String, SummonerDTO> summonerNameCache = new HashMap<>();
    private static final HashMap<String, SummonerDTO> summonerPUUIDCache = new HashMap<>();
    private static final HashMap<String, SummonerDTO> summonerIDCache = new HashMap<>();

    public String accountId, name, id, puuid;
    public int profileIconId, revisionDate, summonerLevel;

    public static void add(SummonerDTO summoner){
        if(summoner == null)
            return;
        summonerNameCache.put(summoner.name, summoner);
        summonerPUUIDCache.put(summoner.puuid, summoner);
        summonerIDCache.put(summoner.id, summoner);
    }
    //returns null if not found
    public static SummonerDTO getByName(String name){
        if(name == null)
            return null;
        return summonerNameCache.get(name);
    }
    public static SummonerDTO getByPuuid(String puuid){
        if(puuid == null)
            return null;
        return summonerPUUIDCache.get(puuid);
    }
    public static SummonerDTO getById(String id){
        if(id == null)
            return null;
        return summonerIDCache.get(id);
    }

    public static SummonerDTO fromJson(String json){
        if(json == null)
            return null;
        SummonerDTO leagueEntry = new SummonerDTO();
        JSONObject jsonSummoner = JSONObject.parseObject(json);
        leagueEntry.id = jsonSummoner.getString("id");
        leagueEntry.accountId = jsonSummoner.getString("accountId");
        leagueEntry.puuid = jsonSummoner.getString("puuid");
        leagueEntry.name = jsonSummoner.getString("name");
        leagueEntry.profileIconId = jsonSummoner.getInteger("profileIconId");
        leagueEntry.revisionDate = jsonSummoner.getInteger("revisionDate");
        leagueEntry.summonerLevel = jsonSummoner.getInteger("summonerLevel");
        return leagueEntry;
    }

    @Override
    public String toString(){
        return "SummonerDTO{" +
                "accountId='" + accountId + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", puuid='" + puuid + '\'' +
                ", profileIconId=" + profileIconId +
                ", revisionDate=" + revisionDate +
                ", summonerLevel=" + summonerLevel +
                '}';
    }
}
