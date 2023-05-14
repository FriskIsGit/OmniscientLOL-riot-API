package lol.apis;

import lol.Riot;
import lol.dtos.SummonerDTO;
import lol.infos.CurrentGameInfo;
import lol.requests.SimpleResponse;
import lol.requests.URIPath;
import org.apache.http.client.fluent.Request;

public class SpectatorV4{
    public static final String bySummoner = "/lol/spectator/v4/active-games/by-summoner/{encryptedSummonerId}";

    public static CurrentGameInfo queryGame(String region, SummonerDTO summoner){
        String endpoint = URIPath.of(bySummoner).args(summoner.id);
        Request request = Riot.newRequest(region, endpoint);
        SimpleResponse response = SimpleResponse.performRequest(request).expect("Active game - no response");
        if(response.code != 200){
            System.out.println(response);
            return null;
        }
        return CurrentGameInfo.fromJson(response.body);
    }
}
