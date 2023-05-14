package lol.dtos;

import com.alibaba.fastjson.JSONObject;

public class TeamDTO{
    public BanDTO bans;
    public ObjectivesDTO objectives;
    public int teamId;
    public boolean win;

    public static TeamDTO fromJson(JSONObject json){
        TeamDTO team = new TeamDTO();
        team.teamId = json.getInteger("teamId");
        team.win = json.getBoolean("win");
        team.bans = BanDTO.fromJson(json.getJSONObject("bans"));
        return team;
    }

    public static TeamDTO fromJson(String json){
        if(json == null)
            return null;
        return fromJson(JSONObject.parseObject(json));
    }
}
