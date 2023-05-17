package lol.dtos;

import com.alibaba.fastjson.JSONObject;

public class BanDTO{
    public int championId, pickTurn;

    public static BanDTO fromJson(JSONObject json){
        if(json == null)
            return null;

        BanDTO ban = new BanDTO();
        ban.championId = json.getInteger("championId");
        ban.pickTurn = json.getInteger("pickTurn");
        return ban;
    }

    public static BanDTO fromJson(String json){
        if(json == null)
            return null;
        return fromJson(JSONObject.parseObject(json));
    }

    @Override
    public String toString(){
        return "BanDTO{" +
                "championId=" + championId +
                ", pickTurn=" + pickTurn +
                '}';
    }
}
