package lol.apis;

import lol.Riot;
import lol.dtos.SummonerDTO;
import lol.requests.SimpleResponse;
import lol.requests.URIPath;
import org.apache.http.client.fluent.Request;

public class SummonerV4{
    //all endpoints return SummonerDTO
    public static final String byName = "/lol/summoner/v4/summoners/by-name/{summonerName}";
    public static final String byId = "/lol/summoner/v4/summoners/{encryptedSummonerId}";
    public static final String byPuuid = "/fulfillment/v1/summoners/by-puuid/{rsoPUUID}";
    public static final String me = "/lol/summoner/v4/summoners/me";

    public static SummonerDTO fetchPlayerByName(String playerName){
        SummonerDTO summoner = SummonerDTO.getByName(playerName);
        if(summoner == null){
            String endpoint = URIPath.of(SummonerV4.byName).args(playerName);
            Request request = Riot.newRequest(endpoint);
            SimpleResponse response = SimpleResponse.performRequest(request).expect("There was no response(SummonerV4)");
            if(response.code != 200){
                System.out.println(response.body);
                return null;
            }
            summoner = SummonerDTO.fromJson(response.body);
            SummonerDTO.add(summoner);
        }
        return summoner;
    }

    public static SummonerDTO fetchPlayerByPuuid(String puuid){
        SummonerDTO summoner = SummonerDTO.getByPuuid(puuid);
        if(summoner == null){
            String endpoint = URIPath.of(SummonerV4.byPuuid).args(puuid);
            Request request = Riot.newRequest(endpoint);
            SimpleResponse response = SimpleResponse.performRequest(request).expect("There was no response(SummonerV4)");
            if(response.code != 200){
                System.out.println(response.body);
                return null;
            }
            summoner = SummonerDTO.fromJson(response.body);
            SummonerDTO.add(summoner);
        }
        return summoner;
    }
    public static SummonerDTO fetchPlayerById(String summonerId){
        SummonerDTO summoner = SummonerDTO.getById(summonerId);
        if(summoner == null){
            String endpoint = URIPath.of(SummonerV4.byId).args(summonerId);
            Request request = Riot.newRequest(endpoint);
            SimpleResponse response = SimpleResponse.performRequest(request).expect("There was no response(SummonerV4)");
            if(response.code != 200){
                System.out.println(response.body);
                return null;
            }
            summoner = SummonerDTO.fromJson(response.body);
            SummonerDTO.add(summoner);
        }
        return summoner;
    }
}
