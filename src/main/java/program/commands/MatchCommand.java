package program.commands;

import lol.Riot;
import lol.apis.MatchV5;
import lol.apis.SummonerV4;
import lol.dtos.InfoDTO;
import lol.dtos.MatchDTO;
import lol.dtos.ParticipantDTO;
import lol.dtos.SummonerDTO;
import lol.requests.SimpleResponse;
import lol.requests.URIPath;
import org.apache.http.client.fluent.Request;
import program.structs.ParticipantTeams;
import program.structs.TableFormat;
import program.structs.TimeElapsed;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class MatchCommand{
    private final TableFormat participantsTable = new TableFormat(true);
    private final TableFormat teamsTable = new TableFormat(true);
    private MatchDTO match;
    private GameStats gameStats;
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
        if(response.code != 200){
            System.out.println(response.body);
            return;
        }
        match = MatchDTO.fromJson(response.body);
        String simpleGameInfo = infoToString(match.info);
        System.out.println(simpleGameInfo);
        gameStats = new GameStats(match.info.participants);
        isARAM = match.info.gameMode.equals("ARAM");
        ParticipantTeams teams = ParticipantTeams.split(match.info.participants);
        TableFormat table = participantsToTable(teams);
        System.out.println(table);
    }

    private static String infoToString(InfoDTO info){
        StringBuilder str = new StringBuilder();
        str.append(TimeElapsed.fromSeconds(info.gameDuration)).append(' ');
        str.append(info.gameMode).append(' ');
        Instant start = Instant.ofEpochMilli(info.gameStartTimestamp);
        ZonedDateTime zoneDate = ZonedDateTime.ofInstant(start, ZoneId.systemDefault());
        str.append(zoneDate.getDayOfMonth()).append(' ')
                .append(zoneDate.getMonth()).append(' ')
                .append(zoneDate.getYear());
        return str.toString();
    }

    private TableFormat participantsToTable(ParticipantTeams teams){
        participantsTable.addColumnDefinition("CHAMP", 13);
        participantsTable.addColumnDefinition("DMG_DEALT%", true);
        participantsTable.addColumnDefinition("DMG_ABSORBED%", true);
        participantsTable.addColumnDefinition("CC%", 6, true);
        participantsTable.addColumnDefinition("CS_SCORE", true);
        participantsTable.addColumnDefinition("GOLD", 8, true);
        participantsTable.addColumnDefinition("PINGS", true);
        participantsTable.addColumnDefinition("TURRET_DMG%", true);
        if(isARAM){

        }else{
            participantsTable.addColumnDefinition("MAX_LVL_LEAD",false);
            participantsTable.addColumnDefinition("VISION", true);
        }

        appendParticipantEntries(teams.leftTeam);
        participantsTable.writeSeparatorRow('‾');
        participantsTable.writeSummaryRow(0);
        participantsTable.writeSeparatorRow('/');
        participantsTable.writeSeparatorRow('\\');
        participantsTable.writeEmptyRow();
        appendParticipantEntries(teams.rightTeam);
        participantsTable.writeSeparatorRow('‾');
        participantsTable.writeSummaryRow(teams.leftTeam.length + 1);
        return participantsTable;
    }

    private void appendParticipantEntries(ParticipantDTO[] team){
        List<Object> row = new ArrayList<>();
        for (ParticipantDTO participant : team){
            row.add(participant.championName);
            row.add(percentage(participant.totalDamageDealtToChampions, gameStats.totalDamageDealtToChampions));
            row.add(percentage(participant.totalDamageTaken + participant.damageSelfMitigated, gameStats.allTotalDamageTaken));
            row.add(percentage(participant.timeCCingOthers, gameStats.timeCCingOthers));
            row.add(csPerMinute(
                    participant.totalMinionsKilled + participant.neutralMinionsKilled,
                    TimeElapsed.fromSeconds(match.info.gameDuration)));
            row.add(participant.goldEarned);
            row.add(participant.enemyMissingPings + participant.dangerPings + participant.getBackPings + participant.allInPings);
            row.add(percentage(participant.damageDealtToTurrets, gameStats.damageDealtToTurrets));
            if(isARAM){
            }
            else{
                row.add(participant.challenges.maxLevelLeadLaneOpponent);
                row.add(participant.visionScore);
            }
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

class GameStats{
    //sum of all participants in a game
    int totalDamageDealtToChampions, allTotalDamageTaken, timeCCingOthers, damageDealtToTurrets;

    public GameStats(ParticipantDTO[] entries){
        sumUpEntries(entries);
    }

    private void sumUpEntries(ParticipantDTO[] entries){
        for(ParticipantDTO participant : entries){
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


