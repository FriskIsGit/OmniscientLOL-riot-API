package lol.infos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CurrentGameInfo{
    //gameLength is delayed by more than 3 minutes, use (now - gameStartTime) instead
    public long gameId, gameStartTime, mapId, gameLength, gameQueueConfigId;
    public String gameType, platformId, gameMode, encryptionKey;
    public BannedChampion[] bannedChampions;
    public CurrentGameParticipant[] participants;

    public static CurrentGameInfo fromJson(String json){
        if(json == null)
            return null;
        return fromJson(JSONObject.parseObject(json));
    }
    public static CurrentGameInfo fromJson(JSONObject json){
        CurrentGameInfo info = new CurrentGameInfo();
        info.gameId = json.getLong("gameId");
        info.gameStartTime = json.getLong("gameStartTime");
        info.mapId = json.getLong("mapId");
        info.gameLength = json.getLong("gameLength");
        info.gameQueueConfigId = json.getLong("gameQueueConfigId");

        info.gameType = json.getString("gameType");
        info.platformId = json.getString("platformId");
        info.gameMode = json.getString("gameMode");
        JSONObject observer =  json.getJSONObject("observers");
        if(observer != null){
            info.encryptionKey = json.getString("encryptionKey");
        }

        info.bannedChampions = toBannedChampionsArr(json.getJSONArray("bannedChampions"));
        info.participants = toParticipantsArr(json.getJSONArray("participants"));
        return info;
    }
    private static BannedChampion[] toBannedChampionsArr(JSONArray arr){
        int len = arr.size();
        BannedChampion[] banned = new BannedChampion[len];
        for (int i = 0; i < len; i++){
            banned[i] = BannedChampion.fromJson(arr.getJSONObject(i));
        }
        return banned;
    }
    private static CurrentGameParticipant[] toParticipantsArr(JSONArray arr){
        int len = arr.size();
        CurrentGameParticipant[] banned = new CurrentGameParticipant[len];
        for (int i = 0; i < len; i++){
            banned[i] = CurrentGameParticipant.fromJson(arr.getJSONObject(i));
        }
        return banned;
    }
}
