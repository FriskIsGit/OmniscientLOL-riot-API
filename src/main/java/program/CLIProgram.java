package program;

import com.alibaba.fastjson.JSONArray;
import lol.Riot;
import lol.apis.*;
import lol.champions.Champions;
import lol.dtos.LeagueEntryDTO;
import lol.dtos.SummonerDTO;
import lol.infos.ChampionInfo;
import lol.infos.CurrentGameInfo;
import lol.infos.CurrentGameParticipant;
import lol.opgg.OPGG;
import lol.opgg.RankEntry;
import lol.ranks.LeagueRank;
import lol.ranks.Queue;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CLIProgram{
    private final Scanner scanner = new Scanner(System.in);

    public void execute(){
        String input = "";
        while(!input.equals("exit")){
            input = scanner.nextLine();
            String[] split = input.split(" ");
            split = new String[]{split[0], merge(split, 1)};
            System.out.println(Arrays.toString(split));
            switch (split[0]){
                case "g":
                case "game":
                    if(split[1].isEmpty()){
                        System.err.println("Missing name argument");
                        continue;
                    }
                    GameCommand.fetchGame(split[1]);
                    break;
                case "now":
                    System.out.println(System.currentTimeMillis());
                    break;
                case "region":
                    Riot.setRegion(split[1]);
                    break;
                case "rank":
                case "player":
                case "p":
                    if(split[1].isEmpty()){
                        System.err.println("Missing name argument");
                        continue;
                    }
                    fetchPlayerRank(split[1]);
                    break;
                case "rotation":
                    String[] names = fetchChampionNames();
                    System.out.println(Arrays.toString(names));
                    break;
                default:
            }
        }
    }

    private void fetchPlayerRank(String summonerName){
        SummonerDTO summoner = SummonerV4.fetchPlayerByName(summonerName);
        if(summoner == null){
            return;
        }
        LeagueEntryDTO[] ranks = LeagueV4.leagueById(Riot.REGION, summoner.id);
        LeagueRank duoRank = new LeagueRank(LeagueRank.UNRANKED);
        LeagueRank flexRank = new LeagueRank(LeagueRank.UNRANKED);
        for (LeagueEntryDTO rank : ranks){
            Queue queueType = Queue.fromString(rank.queueType);
            if (queueType == Queue.RANKED_SOLO_5x5){
                duoRank = new LeagueRank(
                        LeagueRank.tier(rank.tier),
                        LeagueRank.division(rank.rank),
                        Queue.RANKED_SOLO_5x5
                );
            }else if(queueType == Queue.RANKED_FLEX_SR){
                flexRank = new LeagueRank(
                        LeagueRank.tier(rank.tier),
                        LeagueRank.division(rank.rank),
                        Queue.RANKED_FLEX_SR
                );
            }
        }
        List<RankEntry> pastRanks = OPGG.retrievePastSeasonRank(Riot.REGION, summoner.name);
        String info = "Level: " + summoner.summonerLevel + '\n' +
                "DUO rank: " + duoRank.toSimpleRank() + '\n' +
                "FLEX rank: " + flexRank.toSimpleRank() + '\n' +
                "Other: " + pastRanks;
        System.out.println(info);
    }

    private String[] fetchChampionNames(){
        ChampionInfo info = ChampionV3.queryChampionRotations();
        assert info != null;
        int[] championIds = info.freeChampionIdsForNewPlayers;
        String[] names = new String[championIds.length];
        for (int i = 0; i < names.length; i++){
            String name = Champions.getChampionName(championIds[i]);
            names[i] = name;
        }
        return names;
    }

    public static String merge(String[] arr, int from){
        StringBuilder str = new StringBuilder();
        int len = arr.length;
        for (int i = from; i < len; i++){
            str.append(arr[i]);
            if(i != len-1){
                str.append(' ');
            }
        }
        return str.toString();
    }
    private static String[] toStringArr(JSONArray arr){
        int len = arr.size();
        String[] strings = new String[len];
        for (int i = 0; i < len; i++){
            strings[i] = arr.getString(i);
        }
        return strings;
    }

    public void printCommands(){
        System.out.print("Available commands: ");
        String[] commands = {"game", "player", "region", "rotation"};
        System.out.println(Arrays.toString(commands));
    }
}
