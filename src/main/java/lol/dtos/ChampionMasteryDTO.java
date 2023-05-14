package lol.dtos;

import com.alibaba.fastjson.JSONObject;

public class ChampionMasteryDTO{

    public int championId, championLevel, tokensEarned, championPoints;
    public long lastPlayTime, championPointsUntilNextLevel, championPointsSinceLastLevel;
    public String summonerId;
    public boolean chestGranted;

    public static ChampionMasteryDTO fromJson(String json){
        if(json == null)
            return null;
        return fromJson(JSONObject.parseObject(json));
    }

    private static ChampionMasteryDTO fromJson(JSONObject json){
        ChampionMasteryDTO championMastery = new ChampionMasteryDTO();
        championMastery.championId = json.getInteger("championId");
        championMastery.championLevel = json.getInteger("championLevel");
        championMastery.tokensEarned = json.getInteger("tokensEarned");
        championMastery.championPoints = json.getInteger("championPoints");

        championMastery.lastPlayTime = json.getLong("lastPlayTime");
        championMastery.championPointsUntilNextLevel = json.getLong("championPointsUntilNextLevel");
        championMastery.championPointsSinceLastLevel = json.getLong("championPointsSinceLastLevel");
        championMastery.summonerId = json.getString("summonerId");
        championMastery.chestGranted = json.getBoolean("chestGranted");
        return championMastery;
    }

    @Override
    public String toString(){
        return "ChampionMasteryDTO{" +
                "championId=" + championId +
                ", championLevel=" + championLevel +
                ", tokensEarned=" + tokensEarned +
                ", championPoints=" + championPoints +
                ", lastPlayTime=" + lastPlayTime +
                ", championPointsUntilNextLevel=" + championPointsUntilNextLevel +
                ", championPointsSinceLastLevel=" + championPointsSinceLastLevel +
                ", summonerId='" + summonerId + '\'' +
                ", chestGranted=" + chestGranted +
                '}';
    }
}
