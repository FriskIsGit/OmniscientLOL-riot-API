package program;

import com.alibaba.fastjson.JSONArray;
import lol.Riot;
import lol.apis.*;
import lol.champions.Champions;
import lol.dtos.ChampionMasteryDTO;
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
import program.structs.TableFormat;

import java.util.*;

public class CLIProgram{
    private final Scanner scanner = new Scanner(System.in, "UTF-8");
    private final TableFormat playerInfo = new TableFormat(true);

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
                    fetchPlayerInfo(commands[1].trim());
                    playerInfo.clear();
                    break;
                case "regions":
                    System.out.println(Arrays.toString(Riot.REGIONS));
                    break;
                case "matches":
                case "games":
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
                case "portal":
                    System.out.println("https://developer.riotgames.com/");
                    break;
                /*case "replay":
                    Replays.retrieveGameById(commands[1]);
                    break;*/
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
        //loads static initializers (api key and champions to not mess with the flow later )
        try{
            Class.forName("lol.Riot");
            Class.forName("lol.champions.Champions");
        }catch (ClassNotFoundException | ExceptionInInitializerError e){
            e.printStackTrace();
        }
    }

    private void fetchPlayerInfo(String summonerName){
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
        playerInfo.addColumnDefinition("Level");
        playerInfo.addColumnDefinition("DUO");
        playerInfo.addColumnDefinition("FLEX");

        List<ChampionMasteryDTO> masteries = ChampionMasteryV4.queryMostMastery(summoner.id);
        for(ChampionMasteryDTO mastery : masteries){
            playerInfo.addColumnDefinition(Champions.getChampionName(mastery.championId), 8);
        }

        List<Object> values = new ArrayList<>();
        values.add(summoner.summonerLevel);
        values.add(duoRank.toSimpleRank());
        values.add(flexRank.toSimpleRank());

        for(ChampionMasteryDTO mastery : masteries){
            values.add(mastery.championPoints);
        }

        playerInfo.writeToRow(values);
        System.out.println(playerInfo);
        System.out.println("Other ranks: " + pastRanks);
        System.out.println("Summoner id: " + summoner.id);
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
        String[] commands = {"game", "player", "find", "match", "matches", "region", "regions"};
        System.out.println(Arrays.toString(commands));
    }
}
