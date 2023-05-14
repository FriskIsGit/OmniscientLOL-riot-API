package lol.apis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lol.Riot;
import lol.dtos.LeagueEntryDTO;
import lol.requests.SimpleResponse;
import lol.requests.URIPath;
import org.apache.http.client.fluent.Request;

public class LeagueV4{
    //riot API shows a return value of this endpoint as Set[LeagueEntryDTO]  (why not the usual 'List' type)
    //the returned array length is in range [0-2]
    public static final String byId = "/lol/league/v4/entries/by-summoner/{encryptedSummonerId}";
    public static final String byLeagueId = "/lol/league/v4/leagues/{leagueId}";

    public static LeagueEntryDTO[] leagueById(String region, String id){
        String endpoint = URIPath.of(byId).args(id);
        Request request = Riot.newRequest(region, endpoint);
        SimpleResponse response = SimpleResponse.performRequest(request).expect("Active game - no response");
        if(response.code != 200){
            System.out.println(response);
            return new LeagueEntryDTO[0];
        }
        JSONArray arr = JSONObject.parseArray(response.body);
        int len = arr.size();
        LeagueEntryDTO[] ranks = new LeagueEntryDTO[len];
        for (int i = 0; i < len; i++){
            ranks[i] = LeagueEntryDTO.fromJson(arr.getJSONObject(i));
        }
        return ranks;
    }
    public static void byLeagueId(String leagueId){
        String endpoint = URIPath.of(LeagueV4.byLeagueId).args(leagueId);
        Request request = Riot.newRequest(endpoint);
        SimpleResponse response = SimpleResponse.performRequest(request).expect("No response");
        System.out.println(response);
    }
}
