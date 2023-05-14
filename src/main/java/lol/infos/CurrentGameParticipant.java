package lol.infos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CurrentGameParticipant{
    public int championId;
    public long profileIconId, teamId, spell1Id, spell2Id;
    public boolean isBot;
    public String summonerId;
    public GameCustomizationObject[] gameCustomizationObjects;

    public static CurrentGameParticipant fromJson(JSONObject json){
        CurrentGameParticipant participant = new CurrentGameParticipant();
        participant.championId = json.getInteger("championId");

        participant.profileIconId = json.getLong("profileIconId");
        participant.teamId = json.getLong("teamId");
        participant.spell1Id = json.getLong("spell1Id");
        participant.spell2Id = json.getLong("spell2Id");

        participant.isBot = json.getBoolean("bot");
        participant.summonerId = json.getString("summonerId");
        participant.gameCustomizationObjects = toGameCustomizationObjectsArr(json.getJSONArray("gameCustomizationObjects"));
        return participant;
    }
    public static CurrentGameParticipant fromJson(String json){
        if(json == null)
            return null;
        return fromJson(JSONObject.parseObject(json));
    }
    public static GameCustomizationObject[] toGameCustomizationObjectsArr(JSONArray arr){
        int len = arr.size();
        GameCustomizationObject[] participants = new GameCustomizationObject[len];
        for (int i = 0; i < len; i++){
            participants[i] = GameCustomizationObject.fromJson(arr.getJSONObject(i));
        }
        return participants;

    }
}
