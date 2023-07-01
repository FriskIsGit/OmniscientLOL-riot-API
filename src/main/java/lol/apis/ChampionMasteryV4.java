package lol.apis;

import com.alibaba.fastjson.JSONArray;
import lol.Riot;
import lol.dtos.ChampionMasteryDTO;
import lol.requests.SimpleResponse;
import lol.requests.URIPath;
import org.apache.http.client.fluent.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChampionMasteryV4{
    public static final String bySummoner = "/lol/champion-mastery/v4/champion-masteries/by-summoner/{encryptedSummonerId}";
    public static final String bySummonerByChampion = "/lol/champion-mastery/v4/champion-masteries/by-summoner/{encryptedSummonerId}/by-champion/{championId}";
    public static final String bySummonerTop = "/lol/champion-mastery/v4/champion-masteries/by-summoner/{encryptedSummonerId}/top";
    public static final String scoresBySummoner = "/lol/champion-mastery/v4/scores/by-summoner/{encryptedSummonerId}";

    public static List<ChampionMasteryDTO> queryMostMastery(String puuid){
        String endpoint = URIPath.of(bySummonerTop).args(puuid);
        Request request = Riot.newRequest(endpoint);
        SimpleResponse response = SimpleResponse.performRequest(request).expect("No response");
        if(response.code != 200){
            System.out.println(response);
            return Collections.emptyList();
        }
        JSONArray array = JSONArray.parseArray(response.body);
        List<ChampionMasteryDTO> champMasteries = new ArrayList<>(array.size());
        for(int i = 0; i < array.size(); i++){
            ChampionMasteryDTO masteryDTO = ChampionMasteryDTO.fromJson(array.getJSONObject(i));
            champMasteries.add(masteryDTO);
        }
        return champMasteries;
    }
}
