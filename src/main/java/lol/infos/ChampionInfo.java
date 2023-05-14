package lol.infos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;

public class ChampionInfo{
    public int maxNewPlayerLevel;
    public int[] freeChampionIdsForNewPlayers;

    @Override
    public String toString(){
        return "ChampionInfo{" +
                "maxNewPlayerLevel=" + maxNewPlayerLevel +
                ", freeChampionIdsForNewPlayers=" + Arrays.toString(freeChampionIdsForNewPlayers) +
                '}';
    }

    public static ChampionInfo fromJson(String json){
        if(json == null)
            return null;

        JSONObject jsonInfo = JSONObject.parseObject(json);
        ChampionInfo info = new ChampionInfo();
        info.freeChampionIdsForNewPlayers = toIntArr(jsonInfo.getJSONArray("freeChampionIdsForNewPlayers"));
        info.maxNewPlayerLevel = jsonInfo.getInteger("maxNewPlayerLevel");
        return info;
    }

    private static int[] toIntArr(JSONArray arr){
        int len = arr.size();
        int[] ints = new int[len];
        for (int i = 0; i < len; i++){
            ints[i] = arr.getInteger(i);
        }
        return ints;
    }

}
