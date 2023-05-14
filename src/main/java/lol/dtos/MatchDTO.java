package lol.dtos;

import com.alibaba.fastjson.JSONObject;

public class MatchDTO{
    public MetadataDTO metadata;
    public InfoDTO info;

    public static MatchDTO fromJson(String json){
        if(json == null)
            return null;
        JSONObject matchJson = JSONObject.parseObject(json);
        MatchDTO matchDTO = new MatchDTO();
        matchDTO.info = InfoDTO.fromJson(matchJson.getJSONObject("info"));
        matchDTO.metadata = MetadataDTO.fromJson(matchJson.getJSONObject("metadata"));
        return matchDTO;
    }

    @Override
    public String toString(){
        return "MatchDTO{" +
                "metadata=" + metadata +
                ", info=" + info +
                '}';
    }
}
