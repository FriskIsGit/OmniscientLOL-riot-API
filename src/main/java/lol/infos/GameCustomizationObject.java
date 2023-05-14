package lol.infos;

import com.alibaba.fastjson.JSONObject;

public class GameCustomizationObject{
    public String category, content;

    public static GameCustomizationObject fromJson(JSONObject json){
        GameCustomizationObject object = new GameCustomizationObject();
        object.category = json.getString("category");
        object.content = json.getString("content");
        return object;
    }
}
