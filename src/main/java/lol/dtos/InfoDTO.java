package lol.dtos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class InfoDTO{
    public long gameCreation, gameDuration, gameStartTimestamp, gameEndTimestamp, gameId;
    public String gameMode, gameName, gameType, gameVersion, platformId, tournamentCode;
    public int mapId, queueId;
    public TeamDTO[] teams;
    public ParticipantDTO[] participants;

    public static InfoDTO fromJson(String json){
        if(json == null)
            return null;
        return fromJson(JSONObject.parseObject(json));
    }

    public static InfoDTO fromJson(JSONObject json){
        if(json == null)
            return null;
        InfoDTO info = new InfoDTO();
        info.gameCreation = json.getLong("gameCreation");
        info.gameDuration = json.getLong("gameDuration");
        info.gameStartTimestamp = json.getLong("gameStartTimestamp");
        info.gameEndTimestamp = json.getLong("gameEndTimestamp");
        info.gameId = json.getLong("gameId");

        info.gameMode = json.getString("gameMode");
        info.gameName = json.getString("gameName");
        info.gameType = json.getString("gameType");
        info.gameVersion = json.getString("gameVersion");
        info.platformId = json.getString("platformId");
        info.tournamentCode = json.getString("tournamentCode");

        info.mapId = json.getInteger("mapId");
        info.queueId = json.getInteger("queueId");

        info.participants = toParticipantsArr(json.getJSONArray("participants"));
        info.teams = toTeamsArr(json.getJSONArray("teams"));
        return info;
    }

    private static ParticipantDTO[] toParticipantsArr(JSONArray arr){
        int len = arr.size();
        ParticipantDTO[] players = new ParticipantDTO[len];
        for (int i = 0; i < len; i++){
            players[i] = ParticipantDTO.fromJson(arr.getJSONObject(i));
        }
        return players;
    }
    private static TeamDTO[] toTeamsArr(JSONArray arr){
        int len = arr.size();
        TeamDTO[] teams = new TeamDTO[len];
        for (int i = 0; i < len; i++){
            teams[i] = TeamDTO.fromJson(arr.getJSONObject(i));
        }
        return teams;
    }


}
