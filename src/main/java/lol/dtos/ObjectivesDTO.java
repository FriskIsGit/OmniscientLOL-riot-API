package lol.dtos;

import com.alibaba.fastjson.JSONObject;

public class ObjectivesDTO{
    public boolean first;
    public int kills;

    public static ObjectivesDTO fromJson(JSONObject json){
        ObjectivesDTO objectives = new ObjectivesDTO();
        objectives.first = json.getBoolean("first");
        objectives.kills = json.getInteger("kills");
        return objectives;
    }

    public static ObjectivesDTO fromJson(String json){
        if(json == null)
            return null;
        return fromJson(JSONObject.parseObject(json));
    }

    @Override
    public String toString(){
        return "ObjectivesDto{" +
                "first=" + first +
                ", kills=" + kills +
                '}';
    }
}
