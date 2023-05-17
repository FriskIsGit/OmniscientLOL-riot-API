package lol.dtos;

import com.alibaba.fastjson.JSONObject;

public class PlayerDTO{
    public String summonerId, teamId;
    public String position; // (Legal values: UNSELECTED, FILL, TOP, JUNGLE, MIDDLE, BOTTOM, UTILITY)
    public String role; // (Legal values: CAPTAIN, MEMBER)

    public static PlayerDTO fromJson(String json){
        if(json == null)
            return null;
        return fromJson(JSONObject.parseObject(json));
    }

    public static PlayerDTO fromJson(JSONObject json){
        if(json == null)
            return null;
        PlayerDTO player = new PlayerDTO();
        player.summonerId = json.getString("summonerId");
        player.teamId = json.getString("teamId");
        player.position = json.getString("position");
        player.role = json.getString("role");
        return player;
    }
}
