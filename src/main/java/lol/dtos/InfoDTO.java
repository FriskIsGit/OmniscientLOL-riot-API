package lol.dtos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class InfoDTO{
    //gameType - always returns MATCHED_GAME, so there's no point in storing it
    //most game-type related data is expressed as a value of queueId [Game Constants documentation xD]
    public long gameCreation, gameStartTimestamp, gameEndTimestamp, gameId;
    public String gameMode, gameName, gameVersion, platformId, tournamentCode;
    public int mapId, queueId, gameDuration;
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
        info.gameStartTimestamp = json.getLong("gameStartTimestamp");
        info.gameEndTimestamp = json.getLong("gameEndTimestamp");
        info.gameId = json.getLong("gameId");

        info.gameMode = json.getString("gameMode");
        info.gameName = json.getString("gameName");
        info.gameVersion = json.getString("gameVersion");
        info.platformId = json.getString("platformId");
        info.tournamentCode = json.getString("tournamentCode");

        info.mapId = json.getInteger("mapId");
        info.queueId = json.getInteger("queueId");
        info.gameDuration = json.getInteger("gameDuration"); //in seconds

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

    public static String matchType(int queueId){
        switch (queueId){
            case 400:
                return "NORMAL";
            case 420:
                return "RANKED";
            case 440:
                return "FLEX";
            case 450:
                return "ARAM";
            case 830:
                return "INTRO";
            case 840:
                return "BEGINNER";
            case 850:
                return "INTERMEDIATE";
            default:
                System.err.println("Unknown queue id: " + queueId);
                return null;
        }
    }
    public String matchType(){
        return matchType(this.queueId);
    }
}
