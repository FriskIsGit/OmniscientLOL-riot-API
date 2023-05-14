package lol.dtos;

import com.alibaba.fastjson.JSONObject;

public class MiniSeriesDTO{
    public String progress;
    public int losses, target, wins;

    public static MiniSeriesDTO fromJson(JSONObject json){
        if(json == null){
            return null;
        }
        MiniSeriesDTO miniSeries = new MiniSeriesDTO();
        miniSeries.progress = json.getString("progress");
        miniSeries.losses = json.getInteger("losses");
        miniSeries.target = json.getInteger("target");
        miniSeries.wins = json.getInteger("wins");
        return miniSeries;
    }

    public static MiniSeriesDTO fromJson(String json){
        if(json == null)
            return null;
        return fromJson(JSONObject.parseObject(json));
    }

    @Override
    public String toString(){
        return "MiniSeriesDTO{" +
                "progress='" + progress + '\'' +
                ", losses=" + losses +
                ", target=" + target +
                ", wins=" + wins +
                '}';
    }
}
