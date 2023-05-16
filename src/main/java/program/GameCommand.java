package program;

import lol.Riot;
import lol.apis.LeagueV4;
import lol.apis.SpectatorV4;
import lol.apis.SummonerV4;
import lol.champions.Champions;
import lol.dtos.LeagueEntryDTO;
import lol.dtos.SummonerDTO;
import lol.infos.CurrentGameInfo;
import lol.infos.CurrentGameParticipant;
import lol.opgg.OPGG;
import lol.opgg.RankEntry;
import lol.ranks.LeagueRank;
import lol.ranks.Queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameCommand{
    private SummonerEntry[] leftTeam;
    private SummonerEntry[] rightTeam;

    private GameCommand(){
    }

    //pretty prints scoreboard
    public void fetchGameImpl(String playerName){
        SummonerDTO summoner = SummonerV4.fetchPlayerByName(playerName);
        if(summoner == null)
            return;

        CurrentGameInfo gameInfo = SpectatorV4.queryGame(Riot.REGION, summoner);
        if(gameInfo == null){
            System.err.println("Likely the game is over by now or it's a clash game");
            return;
        }
        Teams teams = Teams.split(gameInfo.participants);
        leftTeam = getSummonerEntries(teams.leftTeam);
        rightTeam = getSummonerEntries(teams.rightTeam);
        complementTeamsRanks();
        String scoreboard = statsToPrettyString(summoner.id);
        ElapsedTime elapsedTime = new ElapsedTime(System.currentTimeMillis() - gameInfo.gameStartTime);
        System.out.println(elapsedTime);
        System.out.println(scoreboard);
    }

    public static void fetchGame(String playerName){
        new GameCommand().fetchGameImpl(playerName);
    }

    private void complementTeamsRanks(){
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

    private static void retrieveInParallel(List<SummonerEntry> entries){
        if(entries.size() == 0){
            return;
        }
        int threads = entries.size();
        System.out.println("Retrieving op.gg data, threads: " + threads);
        AtomicInteger finished = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        Future<?>[] futures = new Future[threads];
        for (int i = 0; i < threads; i++){
            SummonerEntry entry = entries.get(i);
            Runnable runnableTask = () -> {
                SummonerDTO summoner = SummonerV4.fetchPlayerById(entry.summonerId);
                if (summoner == null){
                    return;
                }
                List<RankEntry> ranks = OPGG.retrievePastSeasonRank(Riot.REGION, summoner.name);
                if (ranks.size() > 0){
                    entry.duoRank = ranks.get(0).rank;
                }
                finished.incrementAndGet();
            };
            futures[i] = executor.submit(runnableTask);
        }
        long st = System.currentTimeMillis();
        final int timeOutMs = 5000;
        //block until all threads are finished
        for(Future<?> future : futures){
            try{
                long timeSpent = System.currentTimeMillis() - st;
                if(timeSpent > timeOutMs){
                    System.err.println("Timed out at rank complementing");
                    break;
                }
                future.get(timeOutMs - timeSpent, TimeUnit.MILLISECONDS);
            }catch (InterruptedException | ExecutionException | TimeoutException e){
                System.err.println("Timed out at rank complementing");
                break;
            }
        }
    }

    private String statsToPrettyString(String queriedPlayerId){
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
        int biggerTeam = Math.max(leftTeam.length, rightTeam.length);
        for (int t = 0; t < biggerTeam; t++){
            SummonerEntry playerL = null, playerR = null;
            String leftChampion = "", rightChampion = "";
            if(t < leftTeam.length){
                playerL = leftTeam[t];
                if(searching && playerL.summonerId.equals(queriedPlayerId)){
                    leftChampion = "*" + playerL.championName + "*";
                    searching = false;
                }else{
                    leftChampion = playerL.championName;
                }
            }
            if(t < rightTeam.length){
                playerR = rightTeam[t];
                if(searching && playerR.summonerId.equals(queriedPlayerId)){
                    rightChampion = "*" + playerR.championName + "*";
                    searching = false;
                }else{
                    rightChampion = playerR.championName;
                }
            }

            str.append(padToLength(leftChampion, CHAMP_LEN));
            str.append(padToLength(playerL == null ? "" : playerL.duoRank.toSimpleRank(), DUO_LEN));
            str.append(padToLength(playerL == null ? "" : playerL.flexRank.toSimpleRank(), FLEX_LEN));
            str.append(padToLength("│", PIPE_LEN));
            str.append(padToLength(playerR == null ? "" : playerR.flexRank.toSimpleRank(), FLEX_LEN));
            str.append(padToLength(playerR == null ? "" : playerR.duoRank.toSimpleRank(), DUO_LEN));
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
        int entries = summoners.length;
        if(entries == 0){
            return new LeagueRank(LeagueRank.UNRANKED);
        }
        int total = 0;
        for (SummonerEntry summoner : summoners){
            total += summoner.duoRank.divisionScore();
        }
        return LeagueRank.fromDivisionScore(total/entries);
    }
    public static LeagueRank averageFlexRank(SummonerEntry[] summoners){
        int entries = summoners.length;
        if(entries == 0){
            return new LeagueRank(LeagueRank.UNRANKED);
        }
        int total = 0;
        for (SummonerEntry summoner : summoners){
            total += summoner.flexRank.divisionScore();
        }
        return LeagueRank.fromDivisionScore(total/entries);
    }

    //retrieves ranks and basic information about game participants
    private static SummonerEntry[] getSummonerEntries(CurrentGameParticipant[] participants){
        int len = participants.length;
        SummonerEntry[] team = new SummonerEntry[len];
        for (int i = 0; i < len; i++){
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
            team[i] = summonerEntry;
        }
        return team;
    }
}
