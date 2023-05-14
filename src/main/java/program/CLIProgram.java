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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
                    fetchGame(split[1]);
                    break;
                case "now":
                    System.out.println(System.currentTimeMillis());
                    break;
                case "p":
                case "player":
                    SummonerDTO summoner = SummonerV4.fetchPlayerByName(split[1]);
                    System.out.println(summoner);
                    break;
                case "region":
                    Riot.setRegion(split[1]);
                    break;
                case "rank":
                    fetchRank(split[1]);
                    break;
                case "rotation":
                    String[] names = fetchChampionNames();
                    System.out.println(Arrays.toString(names));
                    break;
            }
        }
    }

    private void fetchRank(String summonerName){
        SummonerDTO summoner = SummonerV4.fetchPlayerByName(summonerName);
        if(summoner == null){
            return;
        }
        LeagueEntryDTO[] ranks = LeagueV4.leagueById(Riot.REGION, summoner.id);
        System.out.println(Arrays.toString(ranks));
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

    //pretty prints scoreboard
    public void fetchGame(String playerName){
        SummonerDTO summoner = SummonerV4.fetchPlayerByName(playerName);
        if(summoner == null)
            return;

        CurrentGameInfo gameInfo = SpectatorV4.queryGame(Riot.REGION, summoner);
        if(gameInfo == null){
            System.err.println("Likely the game is over by now or it's a clash game");
            return;
        }
        long now = System.currentTimeMillis();
        CurrentGameParticipant[] participants = gameInfo.participants;
        assert participants.length == 10;

        SummonerEntry[] leftTeam = getSummonerEntries(participants, 0, 5);
        SummonerEntry[] rightTeam = getSummonerEntries(participants, 5, 10);
        complementTeamsRanks(leftTeam, rightTeam);
        String scoreboard = statsToPrettyString(leftTeam, rightTeam, summoner.id);
        ElapsedTime elapsedTime = new ElapsedTime(now - gameInfo.gameStartTime);
        System.out.println(elapsedTime);
        System.out.println(scoreboard);
    }

    private void complementTeamsRanks(SummonerEntry[] leftTeam, SummonerEntry[] rightTeam){
        List<SummonerEntry> entriesToComplement = new ArrayList<>();
        for (SummonerEntry entry : leftTeam){
            if (entry.duoRank.getTier() == LeagueRank.UNRANKED){
                entriesToComplement.add(entry);
            }
        }
        for (SummonerEntry entry : rightTeam){
            if (entry.duoRank.getTier() == LeagueRank.UNRANKED){
                entriesToComplement.add(entry);
            }
        }
        retrieveInParallel(entriesToComplement);
    }

    private void retrieveInParallel(List<SummonerEntry> entries){
        if(entries.size() == 0){
            return;
        }
        int threads = entries.size();
        System.out.println("Retrieving op.gg data, threads: " + threads);
        AtomicInteger activeThreads = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        for (SummonerEntry entry : entries){
            Runnable runnableTask = () -> {
                SummonerDTO summoner = SummonerV4.fetchPlayerById(entry.summonerId);
                if(summoner == null){
                    activeThreads.decrementAndGet();
                    return;
                }
                List<RankEntry> ranks = OPGG.retrievePastSeasonRank(Riot.REGION, summoner.name);
                if (ranks.size() > 0){
                    entry.duoRank = ranks.get(0).rank;
                }
                activeThreads.decrementAndGet();
            };
            activeThreads.incrementAndGet();
            executor.submit(runnableTask);
        }
        long st = System.currentTimeMillis();
        final int timeOutMs = 5000;
        //block until all threads are finished
        int hits = 0;
        while(activeThreads.get() > 0){
            hits++;
            if(System.currentTimeMillis() - st > timeOutMs){
                System.err.println("Timed out at rank complementing");
                break;
            }
        }
        //System.out.println(hits);
    }

    private String statsToPrettyString(SummonerEntry[] leftTeam, SummonerEntry[] rightTeam, String queriedPlayerId){
        StringBuilder str = new StringBuilder();
        final int CHAMP_LEN = 13, DUO_LEN = 4, FLEX_LEN = 5, PIPE_LEN = 2;
        int totalWidth = (CHAMP_LEN + DUO_LEN + FLEX_LEN + PIPE_LEN) * 2;
        str.append(line(totalWidth, '_'));
        str.append('\n');
        str.append(padToLength("CHAMPION", CHAMP_LEN));
        str.append(padToLength("DUO", DUO_LEN));
        str.append(padToLength("FLEX", FLEX_LEN));
        str.append(padToLength("│", PIPE_LEN));
        str.append(padToLength("FLEX", FLEX_LEN));
        str.append(padToLength("DUO", DUO_LEN));
        str.append(padToLength("CHAMPION", CHAMP_LEN));
        str.append('\n');
        str.append(line(totalWidth, '‾'));
        str.append('\n');
        boolean searching = true;
        for (int t = 0; t < 5; t++){
            SummonerEntry playerL = leftTeam[t];
            SummonerEntry playerR = rightTeam[t];
            String leftChampion = playerL.championName, rightChampion = playerR.championName;
            if(searching && playerL.summonerId.equals(queriedPlayerId)){
                leftChampion = "*" + playerL.championName + "*";
                searching = false;
            }else if(searching && playerR.summonerId.equals(queriedPlayerId)){
                rightChampion = "*" + playerR.championName + "*";
                searching = false;
            }
            str.append(padToLength(leftChampion, CHAMP_LEN));
            str.append(padToLength(playerL.duoRank.toSimpleRank(), DUO_LEN));
            str.append(padToLength(playerL.flexRank.toSimpleRank(), FLEX_LEN));
            str.append(padToLength("│", PIPE_LEN));
            str.append(padToLength(playerR.flexRank.toSimpleRank(), FLEX_LEN));
            str.append(padToLength(playerR.duoRank.toSimpleRank(), DUO_LEN));
            str.append(padToLength(rightChampion, CHAMP_LEN));
            str.append('\n');
        }
        str.append(line(totalWidth, '='));
        str.append('\n');
        LeagueRank leftDuoAvg = averageDuoRank(leftTeam);
        LeagueRank leftFlexAvg = averageFlexRank(leftTeam);
        LeagueRank rightFlexAvg = averageFlexRank(rightTeam);
        LeagueRank rightDuoAvg = averageDuoRank(rightTeam);
        str.append(padToLength("AVERAGE", CHAMP_LEN));
        str.append(padToLength(leftDuoAvg.toSimpleRank(), DUO_LEN));
        str.append(padToLength(leftFlexAvg.toSimpleRank(), FLEX_LEN));
        str.append(padToLength("│", PIPE_LEN));
        str.append(padToLength(rightFlexAvg.toSimpleRank(), FLEX_LEN));
        str.append(padToLength(rightDuoAvg.toSimpleRank(), DUO_LEN));
        str.append(padToLength("AVERAGE", CHAMP_LEN));
        return str.toString();
    }

    private static StringBuilder padToLength(String str, int length){
        StringBuilder padded = new StringBuilder(str);
        int diff = length - str.length();
        for (int i = 0; i < diff; i++){
            padded.append(' ');
        }
        return padded;
    }
    private static StringBuilder line(int length, char chr){
        StringBuilder padded = new StringBuilder(length);
        for (int i = 0; i < length; i++){
            padded.append(chr);
        }
        return padded;
    }

    public static LeagueRank averageDuoRank(SummonerEntry[] summoners){
        int total = 0;
        int entries = summoners.length;
        for (SummonerEntry summoner : summoners){
            total += summoner.duoRank.divisionScore();
        }
        return LeagueRank.fromDivisionScore(total/entries);
    }
    public static LeagueRank averageFlexRank(SummonerEntry[] summoners){
        int total = 0;
        int entries = summoners.length;
        for (SummonerEntry summoner : summoners){
            total += summoner.flexRank.divisionScore();
        }
        return LeagueRank.fromDivisionScore(total/entries);
    }

    //retrieves ranks and basic information about game participants
    private static SummonerEntry[] getSummonerEntries(CurrentGameParticipant[] participants, int from, int to){
        SummonerEntry[] team = new SummonerEntry[to - from];
        for (int i = from, e = 0; i < to; i++, e++){
            CurrentGameParticipant participant = participants[i];
            LeagueEntryDTO[] ranks = LeagueV4.leagueById(Riot.REGION, participant.summonerId);
            String champName = Champions.getChampionName(participant.championId);
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
            SummonerEntry summonerEntry = new SummonerEntry(participant.summonerId, champName, duoRank, flexRank);
            team[e] = summonerEntry;
        }
        return team;
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
        String[] commands = {"game", "player", "region", "rank", "rotation"};
        System.out.println(Arrays.toString(commands));
    }
}
