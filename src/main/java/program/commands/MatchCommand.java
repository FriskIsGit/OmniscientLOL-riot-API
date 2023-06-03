package program.commands;

import com.alibaba.fastjson.JSONArray;
import lol.Riot;
import lol.apis.MatchV5;
import lol.apis.SummonerV4;
import lol.dtos.*;
import lol.requests.SimpleResponse;
import lol.requests.URIPath;
import org.apache.http.client.fluent.Request;
import program.structs.*;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class MatchCommand{
    private final TableFormat participantsTable = new TableFormat(true);
    private final TableFormat teamsTable = new TableFormat(true);
    private TeamStats leftStats, rightStats;
    private MatchDTO match;
    private BothTeamsStats gameStats;
    private boolean isARAM = false;
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
        System.out.println(response.body);
        if(response.code != 200){
            return;
        }
        try{
            //avoid 20 requests/second limit
            Thread.sleep(1000);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        JSONArray stringArr = JSONArray.parseArray(response.body);
        String[] ids  = new String[stringArr.size()];
        for (int i = 0; i < stringArr.size(); i++){
            ids[i] = stringArr.getString(i);
        }

        MatchDTO[] matches = retrieveMatchesByIds(ids);
        if(matches.length == 0){
            System.out.println("No matches found.");
            return;
        }
        SimpleMatch[] simpleMatches = new SimpleMatch[matches.length];
        for (int i = 0; i < simpleMatches.length; i++){
            MatchDTO aMatch = matches[i];
            SimpleMatch simpleMatch = new SimpleMatch(aMatch.metadata.matchId);
            ParticipantDTO[] participants = aMatch.info.participants;
            int index = 0;
            for (int j = 0; j < participants.length; j++){
                ParticipantDTO participant = participants[j];
                if (participant.puuid.equals(summoner.puuid)){
                    simpleMatch.champ = participant.championName;
                    simpleMatch.kills = participant.kills;
                    simpleMatch.deaths = participant.deaths;
                    simpleMatch.assists = participant.assists;
                    index = j;
                    break;
                }
            }
            ParticipantTeams teams = ParticipantTeams.split(aMatch.info.participants);
            TeamDTO[] teamDTOs = aMatch.info.teams;
            simpleMatch.win = (index < teams.leftTeam.length && teamDTOs[0].win)
                    || (index >= teams.leftTeam.length && teamDTOs[1].win);
            simpleMatch.length = TimeElapsed.fromSeconds(aMatch.info.gameDuration);
            simpleMatch.type = aMatch.info.matchType();
            if(simpleMatch.type == null)
                System.out.println(aMatch.info.queueId);
            simpleMatch.queueId = aMatch.info.queueId;
            simpleMatches[i] = simpleMatch;
        }
        TableFormat matchesTable = new TableFormat(true);
        matchesTable.addColumnDefinition("MATCH-ID", 15);
        matchesTable.addColumnDefinition("CHAMPION", 13);
        matchesTable.addColumnDefinition("K/D/A", 13);
        matchesTable.addColumnDefinition("LENGTH", 9);
        matchesTable.addColumnDefinition("WIN");
        matchesTable.addColumnDefinition("TYPE", 12);

        for(SimpleMatch simpleMatch : simpleMatches){
            String win = simpleMatch.win ? "yes" : "no";
            matchesTable.writeToRow(Arrays.asList(simpleMatch.id, simpleMatch.champ, simpleMatch.kda(),
                    simpleMatch.length, win, simpleMatch.type));
        }
        int sumK=0, sumD=0, sumA=0;
        int allSeconds = 0;
        for(SimpleMatch simpleMatch : simpleMatches){
            allSeconds += simpleMatch.length.toAllSeconds();
            sumK += simpleMatch.kills;
            sumD += simpleMatch.deaths;
            sumA += simpleMatch.assists;
        }
        int entries = matches.length;
        String averageKDA = new SimpleMatch(sumK / entries, sumD / entries, sumA / entries).kda();
        matchesTable.writeSeparatorRow('-');
        matchesTable.writeToRow(Arrays.asList('-', '-', averageKDA, TimeElapsed.fromSeconds(allSeconds/entries), '-', '-'));
        System.out.println(matchesTable);
    }

    private MatchDTO[] retrieveMatchesByIds(String[] ids){
        if(ids.length == 0){
            return new MatchDTO[0];
        }

        int threads = ids.length;
        MatchDTO[] matches = new MatchDTO[threads];
        System.out.println("Retrieving matches, threads: " + threads);
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        Future<?>[] futures = new Future[threads];
        for (int i = 0; i < threads; i++){
            String matchId = ids[i];
            int finalIndex = i;
            Runnable runnableTask = () -> {
                String endpoint = URIPath.of(MatchV5.byMatchId).args(matchId);
                String routingRegion = Riot.toRoutingRegion(Riot.REGION);
                Request request = Riot.newRequest(routingRegion, endpoint);
                SimpleResponse response = SimpleResponse.performRequest(request).expect("NO RESPONSE");
                if(response.code != 200){
                    System.out.println(response.body);
                    return;
                }
                MatchDTO currMatch = MatchDTO.fromJson(response.body);
                if (currMatch == null){
                    return;
                }
                matches[finalIndex] = currMatch;
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
                    System.err.println("Timed out at match retrieving");
                    break;
                }
                future.get(timeOutMs - timeSpent, TimeUnit.MILLISECONDS);
            }catch (InterruptedException | ExecutionException | TimeoutException e){
                System.err.println("Timed out at match retrieving");
                break;
            }
        }
        return matches;
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
        if(response.code != 200){
            System.out.println(response.body);
            return;
        }
        //System.out.println(response.body);
        match = MatchDTO.fromJson(response.body);
        String simpleGameInfo = infoToString(match.info);
        System.out.println(simpleGameInfo);
        gameStats = new BothTeamsStats(match.info.participants);
        isARAM = match.info.gameMode.equals("ARAM");
        ParticipantTeams teams = ParticipantTeams.split(match.info.participants);
        leftStats = TeamStats.fromParticipants(teams.leftTeam);
        rightStats = TeamStats.fromParticipants(teams.rightTeam);
        participantsToTable(teams);
        System.out.println(participantsTable);
        participantsToTeamTable();
        System.out.println(teamsTable);
    }

    private static String infoToString(InfoDTO info){
        StringBuilder str = new StringBuilder();
        str.append(TimeElapsed.fromSeconds(info.gameDuration)).append(' ');
        str.append(info.matchType()).append(' ');
        Instant start = Instant.ofEpochMilli(info.gameStartTimestamp);
        ZonedDateTime zoneDate = ZonedDateTime.ofInstant(start, ZoneId.systemDefault());
        str.append(zoneDate.getDayOfMonth()).append(' ')
                .append(zoneDate.getMonth()).append(' ')
                .append(zoneDate.getYear());
        return str.toString();
    }

    private void participantsToTeamTable(){
        TeamDTO[] teamsData = match.info.teams;
        assert teamsData.length == 2;
        String leftResult = teamsData[0].win ? "VICTORY" : (leftStats.surrendered ? "SURRENDER" : "DEFEAT");
        String rightResult = teamsData[1].win ? "VICTORY" : (rightStats.surrendered ? "SURRENDER" : "DEFEAT");
        teamsTable.addColumnDefinition("TEAMS", 8);
        teamsTable.addColumnDefinition("RESULT", 9);
        teamsTable.addColumnDefinition("KILLS");
        if(isARAM){
            teamsTable.addColumnDefinition("CC-kills-with-ally");
        }else{
            teamsTable.addColumnDefinition("HERALDS");
            teamsTable.addColumnDefinition("DRAGONS");
            teamsTable.addColumnDefinition("BARONS");
            teamsTable.addColumnDefinition("STOLEN");
            teamsTable.addColumnDefinition("INVADE-KILLS");
        }
        teamsTable.addColumnDefinition("TURRET-KILLS");
        if(isARAM){
            teamsTable.writeToRow(Arrays.asList("UPPER", leftResult, leftStats.kills, leftStats.ccKills, leftStats.turretsDestroyed));
            teamsTable.writeToRow(Arrays.asList("LOWER", rightResult, rightStats.kills, rightStats.ccKills, rightStats.turretsDestroyed));
        }else{
            teamsTable.writeToRow(Arrays.asList("UPPER", leftResult, leftStats.kills, leftStats.heralds,
                    leftStats.dragons, leftStats.barons, leftStats.stolen, leftStats.invadeKills, leftStats.turretsDestroyed));
            teamsTable.writeToRow(Arrays.asList("LOWER", rightResult, rightStats.kills, rightStats.heralds,
                    rightStats.dragons, rightStats.barons, rightStats.stolen, rightStats.invadeKills, rightStats.turretsDestroyed));
        }
    }

    private void participantsToTable(ParticipantTeams teams){
        participantsTable.addColumnDefinition("CHAMP", 13);
        participantsTable.addColumnDefinition("K/D/A", 8);
        participantsTable.addColumnDefinition("DMG_DEALT%", true);
        /*participantsTable.addColumnDefinition("DMG_DEALT_CHAMPS", true);
        participantsTable.addColumnDefinition("DMG_TAKEN", true);
        participantsTable.addColumnDefinition("DMG_MITIGATED", true);*/
        participantsTable.addColumnDefinition("DMG_ABSORBED%", true);
        participantsTable.addColumnDefinition("KP%"); // in team
        participantsTable.addColumnDefinition("CC%", 6, true);
        participantsTable.addColumnDefinition("CS_SCORE", true);
        participantsTable.addColumnDefinition("GOLD", 8, true);
        participantsTable.addColumnDefinition("PINGS", true);
        participantsTable.addColumnDefinition("TURRET_DMG%", true);
        if(isARAM){

        }else{
            participantsTable.addColumnDefinition("PINKS", true);
            participantsTable.addColumnDefinition("VISION", true);
        }
        participantsTable.addColumnDefinition("SKILLSHOTS_DODGED", true);
        participantsTable.addColumnDefinition("PLAYER_NAME", 16);

        appendParticipantEntries(teams.leftTeam, leftStats);
        participantsTable.writeSeparatorRow('-');
        participantsTable.writeSummaryRow(0);
        participantsTable.writeSeparatorRow('/');
        participantsTable.writeSeparatorRow('\\');
        participantsTable.writeEmptyRow();
        appendParticipantEntries(teams.rightTeam, rightStats);
        participantsTable.writeSeparatorRow('-');
        participantsTable.writeSummaryRow(teams.leftTeam.length + 1);
    }

    private void appendParticipantEntries(ParticipantDTO[] team, TeamStats teamStats){
        List<Object> row = new ArrayList<>();
        for (ParticipantDTO participant : team){
            row.add(participant.championName);
            row.add(participant.kills + "/" + participant.deaths + "/" + participant.assists);
            row.add(percentage(participant.totalDamageDealtToChampions, gameStats.totalDamageDealtToChampions));
            /*row.add(participant.totalDamageDealtToChampions);
            row.add(participant.totalDamageTaken);
            row.add(participant.damageSelfMitigated);*/
            row.add(percentage(participant.totalDamageTaken + participant.damageSelfMitigated, gameStats.allTotalDamageTaken));
            row.add((int)percentage(participant.kills + participant.assists, teamStats.kills));
            row.add(percentage(participant.timeCCingOthers, gameStats.timeCCingOthers));
            row.add(csPerMinute(
                    participant.totalMinionsKilled + participant.neutralMinionsKilled,
                    TimeElapsed.fromSeconds(match.info.gameDuration)));
            row.add(participant.goldEarned);
            int allPings = participant.enemyMissingPings + participant.commandPings + participant.dangerPings + participant.enemyVisionPings + participant.getBackPings + participant.allInPings;
            row.add(allPings);
            row.add(percentage(participant.damageDealtToTurrets, gameStats.damageDealtToTurrets));
            if(isARAM){
            }
            else{
                row.add(participant.visionWardsBoughtInGame); //control wards (pink wards)
                row.add(participant.visionScore);
            }
            row.add(participant.challenges == null ? '-' : participant.challenges.skillshotsDodged);
            row.add(participant.summonerName);
            participantsTable.writeToRow(row);
            row.clear();
        }
    }

    public double percentage(int part, int total){
        double divided = part / (double) total;
        return divided * 100;
    }

    private static double csPerMinute(int minions, TimeElapsed elapsed){
        double seconds = elapsed.seconds + elapsed.minutes * 60;
        return minions * 60 / seconds;
    }
}

class BothTeamsStats{
    //sum of all participants in a game
    int totalDamageDealt, totalDamageDealtToChampions, allTotalDamageTaken, timeCCingOthers, damageDealtToTurrets;

    public BothTeamsStats(ParticipantDTO[] entries){
        sumUpEntries(entries);
    }

    private void sumUpEntries(ParticipantDTO[] entries){
        for(ParticipantDTO participant : entries){
            totalDamageDealt += participant.totalDamageDealt;
            totalDamageDealtToChampions += participant.totalDamageDealtToChampions;
            allTotalDamageTaken += participant.totalDamageTaken + participant.damageSelfMitigated;
            timeCCingOthers += participant.timeCCingOthers;
            damageDealtToTurrets += participant.damageDealtToTurrets;
        }
    }

    @Override
    public String toString(){
        return "Stats{}";
    }
}

class TeamStats{
    public int barons = 0;
    public int heralds = 0;
    public int dragons = 0;
    public int elders = 0;
    public int stolen = 0;
    public int kills = 0;
    public int invadeKills = 0;
    public int turretsDestroyed = 0;
    public int ccKills = 0;
    public boolean surrendered = false;
    public static TeamStats fromParticipants(ParticipantDTO[] team){
        TeamStats teamStats = new TeamStats();
        for(ParticipantDTO participant : team){
            teamStats.dragons += participant.dragonKills;
            teamStats.barons += participant.baronKills;
            teamStats.stolen += participant.objectivesStolen;
            teamStats.kills += participant.kills;
            teamStats.turretsDestroyed += participant.turretKills;
            if(!teamStats.surrendered)
                teamStats.surrendered = participant.gameEndedInSurrender;
            if(participant.challenges != null)
                teamStats.heralds = participant.challenges.teamRiftHeraldKills;
            if(participant.challenges != null)
                teamStats.ccKills = participant.challenges.immobilizeAndKillWithAlly + participant.challenges.knockEnemyIntoTeamAndKill;
            if(participant.challenges != null)
                teamStats.elders = participant.challenges.teamElderDragonKills;
            if(participant.challenges != null)
                teamStats.invadeKills = participant.challenges.takedownsBeforeJungleMinionSpawn;

        }
        return teamStats;
    }
}

