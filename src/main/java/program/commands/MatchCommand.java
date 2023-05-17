package program.commands;

import lol.Riot;
import lol.apis.MatchV5;
import lol.apis.SummonerV4;
import lol.dtos.MatchDTO;
import lol.dtos.SummonerDTO;
import lol.requests.SimpleResponse;
import lol.requests.URIPath;
import org.apache.http.client.fluent.Request;

public class MatchCommand{
    private MatchCommand(){
    }

    public static void fetchMatch(String matchId){
        new MatchCommand().fetchMatchImpl(matchId);
    }
    public static void fetchMatches(String playerName){
        new MatchCommand().fetchMatchesImpl(playerName);
    }

    private void fetchMatchesImpl(String playerName){
        SummonerDTO summoner = SummonerV4.fetchPlayerByName(playerName);
        if(summoner == null){
            return;
        }
        String endpoint = URIPath.of(MatchV5.idsByPuuid).args(summoner.puuid);
        String routingRegion = Riot.toRoutingRegion(Riot.REGION);
        Request request = Riot.newRequest(routingRegion, endpoint);
        SimpleResponse response = SimpleResponse.performRequest(request).expect("NO RESPONSE");
        System.out.println(response);
    }

    private void fetchMatchImpl(String matchId){
        if(matchId.length() < 2){
            return;
        }
        //prefix with uppercase region and underscore (REGION_gameId)
        if(Character.isDigit(matchId.charAt(0))){
            matchId = Riot.REGION.toUpperCase() + '_' + matchId;
        }
        String endpoint = URIPath.of(MatchV5.byMatchId).args(matchId);
        String routingRegion = Riot.toRoutingRegion(Riot.REGION);
        Request request = Riot.newRequest(routingRegion, endpoint);
        SimpleResponse response = SimpleResponse.performRequest(request).expect("NO RESPONSE");
        MatchDTO.fromJson(response.body);
        System.out.println(response);
    }

}
