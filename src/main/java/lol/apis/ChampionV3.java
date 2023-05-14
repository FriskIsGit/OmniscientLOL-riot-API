package lol.apis;

import lol.Riot;
import lol.infos.ChampionInfo;
import lol.requests.SimpleResponse;
import org.apache.http.client.fluent.Request;

public class ChampionV3{
    public static final String rotation = "/lol/platform/v3/champion-rotations";

    //the region shouldn't matter for rotations (mainly timezone dependent)
    public static ChampionInfo queryChampionRotations(){
        Request request = Riot.newRequest("eun1", rotation);
        SimpleResponse response = SimpleResponse.performRequest(request).expect("Champion rotations - no response");
        if(response.code != 200){
            System.out.println(response);
            return null;
        }
        return ChampionInfo.fromJson(response.body);
    }
}
