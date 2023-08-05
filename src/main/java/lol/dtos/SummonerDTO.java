package lol.dtos;

import com.alibaba.fastjson.JSONObject;
import lol.Riot;

import java.util.ArrayList;
import java.util.HashMap;

public class SummonerDTO{
    private static final ArrayList<RegionalSummoner> summonerNameCache = new ArrayList<>();
    private static final HashMap<String, SummonerDTO> summonerPUUIDCache = new HashMap<>();
    private static final HashMap<String, SummonerDTO> summonerIDCache = new HashMap<>();

    public String accountId, name, id, puuid;
    public int profileIconId, revisionDate, summonerLevel;

    public static void add(SummonerDTO summoner){
        if(summoner == null)
            return;
        summonerNameCache.add(new RegionalSummoner(summoner, Riot.REGION));
        summonerPUUIDCache.put(summoner.puuid, summoner);
        summonerIDCache.put(summoner.id, summoner);
    }
    //returns null if not found
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
    public static SummonerDTO getByName(String name){
        if(name == null)
            return null;
        for(RegionalSummoner regSummoner : summonerNameCache){
            if(regSummoner.region.equals(Riot.REGION) && regSummoner.summoner.name.equals(name)){
                return regSummoner.summoner;
            }
        }
        return null;
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
class RegionalSummoner{
    public SummonerDTO summoner;
    public String region;

    public RegionalSummoner(SummonerDTO summoner, String region){
        this.region = region;
        this.summoner = summoner;
    }

    @Override
    public String toString(){
        return "RegionSummoner{" +
                "summoner=" + summoner +
                ", region='" + region + '\'' +
                '}';
    }
}
