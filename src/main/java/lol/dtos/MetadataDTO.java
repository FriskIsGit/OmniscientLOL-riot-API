package lol.dtos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;

public class MetadataDTO{
    public String dataVersion, matchId;
    public String[] participantsPuuids;

    public static MetadataDTO fromJson(String json){
        if(json == null || json.isEmpty())
            return null;

        return fromJson(JSONObject.parseObject(json));
    }

    public static MetadataDTO fromJson(JSONObject json){
        if(json == null)
            return null;
        MetadataDTO metadata = new MetadataDTO();
        metadata.dataVersion = json.getString("dataVersion");
        metadata.matchId = json.getString("matchId");
        metadata.participantsPuuids = toStringArr(json.getJSONArray("participants"));
        return metadata;
    }

    private static String[] toStringArr(JSONArray arr){
        int len = arr.size();
        String[] strings = new String[len];
        for (int i = 0; i < len; i++){
            strings[i] = arr.getString(i);
        }
        return strings;
    }

    @Override
    public String toString(){
        return "MetadataDTO{" +
                "dataVersion='" + dataVersion + '\'' +
                ", matchId='" + matchId + '\'' +
                ", participantsPuuids=" + Arrays.toString(participantsPuuids) +
                '}';
    }
}
