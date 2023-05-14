package lol.infos;

import com.alibaba.fastjson.JSONObject;

public class BannedChampion{
    public int pickTurn, championId;
    public long teamId;

    public static BannedChampion fromJson(String json){
        if(json == null || json.isEmpty())
            return null;

        return fromJson(JSONObject.parseObject(json));
    }

    public static BannedChampion fromJson(JSONObject json){
        BannedChampion bannedChampion = new BannedChampion();
        bannedChampion.pickTurn = json.getInteger("pickTurn");
        bannedChampion.championId = json.getInteger("championId");
        bannedChampion.teamId = json.getLong("teamId");
        return bannedChampion;
    }

}
