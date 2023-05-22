package program;

import com.alibaba.fastjson.JSONArray;
import lol.Riot;
import lol.apis.*;
import lol.champions.Champions;
import lol.dtos.LeagueEntryDTO;
import lol.dtos.SummonerDTO;
import lol.infos.ChampionInfo;
import lol.opgg.OPGG;
import lol.opgg.RankEntry;
import lol.ranks.LeagueRank;
import lol.ranks.Queue;
import program.commands.FindPlayerCommand;
import program.commands.GameCommand;
import program.commands.MatchCommand;

import java.util.*;

public class CLIProgram{
    private final Scanner scanner = new Scanner(System.in);

    public void execute(){
        String input = "";
        while(!input.equals("exit")){
            input = scanner.nextLine();
            String[] commands = twoSplit(input);
            System.out.println(Arrays.toString(commands));
            switch (commands[0].toLowerCase()){
                case "g":
                case "game":
                    if(commands[1].isEmpty()){
                        System.err.println("Missing name argument");
                        continue;
                    }
                    GameCommand.fetchGame(commands[1]);
                    break;
                case "now":
                    System.out.println(System.currentTimeMillis());
                    break;
                case "region":
                case "r":
                    Riot.setRegion(commands[1]);
                    break;
                case "player":
                case "p":
                    if(commands[1].isEmpty()){
                        System.err.println("Missing name argument");
                        continue;
                    }
                    fetchPlayerRank(commands[1]);
                    break;
                case "regions":
                    System.out.println(Arrays.toString(Riot.REGIONS));
                    break;
                case "matches":
                    if(commands[1].isEmpty()){
                        System.err.println("Missing name argument");
                        continue;
                    }
                    MatchCommand.fetchMatches(commands[1]);
                    break;
                case "match":
                case "m":
                    if(commands[1].isEmpty()){
                        System.err.println("Missing matchId argument");
                        continue;
                    }
                    MatchCommand.fetchMatch(commands[1]);
                    break;
                case "find":
                    List<String> servers = FindPlayerCommand.findPlayer(commands[1]);
                    System.out.println(servers);
                    break;
                default:
            }
        }
    }

    public static String[] twoSplit(String input){
        int firstSpace = -1;
        char[] arr = input.toCharArray();
            for (int i = 0; i < arr.length; i++){
                if(arr[i] == ' '){
                    firstSpace = i;
                    break;
                }
            }
            if(firstSpace == -1){
            return new String[]{input, " "};
        }
        String[] output = new String[2];
        output[0] = input.substring(0, firstSpace);
        if(firstSpace + 1 == arr.length){
            output[1] = " ";
            return output;
        }
        output[1] = input.substring(firstSpace + 1);
        return output;
    }

    public void earlyInitialize(){
        //loads static initializers (api key to not mess with the flow later)
        try{
            Class.forName("lol.Riot");
        }catch (ClassNotFoundException ignored){}
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
        String[] commands = {"game", "player", "find", "match", "region", "matches"};
        System.out.println(Arrays.toString(commands));
    }
}
