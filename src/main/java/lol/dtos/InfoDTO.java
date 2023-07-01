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
        info.gameEndTimestamp = json.containsKey("gameEndTimestamp") ? json.getLong("gameEndTimestamp") : -1;
        info.gameId = json.getLong("gameId");

        info.gameMode = json.getString("gameMode");
        info.gameName = json.getString("gameName");
        info.gameVersion = json.getString("gameVersion");
        info.platformId = json.getString("platformId");
        info.tournamentCode = json.getString("tournamentCode");

        info.mapId = json.getInteger("mapId");
        info.queueId = json.getInteger("queueId");

        info.gameDuration = json.getInteger("gameDuration");
        //determine season - gameDuration is provided in seconds but used to be millis
        int season = gameVersionToSeason(info.gameVersion);
        if(season <= 11){
            info.gameDuration /= 1000;
        }

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

    public static int gameVersionToSeason(String gameVersion){
        if(gameVersion == null){
            throw new NullPointerException("Unknown game version");
        }
        int firstDot = gameVersion.indexOf('.');
        String toParse = gameVersion.substring(0, firstDot);
        return Integer.parseInt(toParse);
    }

    public static String matchType(int queueId){
        switch (queueId){
            case 400:
                return "DRAFT";
            case 420:
                return "RANKED";
            case 430:
                return "BLIND";
            case 440:
                return "FLEX";
            case 450:
                return "ARAM";
            case 460:
                return "TWISTED_TREELINE";
            case 700:
                return "CLASH";
            case 830:
                return "INTRO";
            case 840:
                return "BEGINNER";
            case 850:
                return "INTERMEDIATE";
            case 900:
                return "URF";
            default:
                System.err.println("Unknown queue id: " + queueId);
                return null;
        }
    }
    public String matchType(){
        return matchType(this.queueId);
    }
}
