package program.commands;

import lol.Riot;
import lol.apis.LeagueV4;
import lol.apis.SpectatorV4;
import lol.apis.SummonerV4;
import lol.champions.Champions;
import lol.champions.Role;
import lol.dtos.LeagueEntryDTO;
import lol.dtos.SummonerDTO;
import lol.infos.CurrentGameInfo;
import lol.infos.CurrentGameParticipant;
import lol.opgg.OPGG;
import lol.opgg.RankEntry;
import lol.ranks.LeagueRank;
import lol.ranks.Queue;
import lol.spells.Spell;
import program.structs.TimeElapsed;
import program.structs.SummonerEntry;
import program.structs.GameTeams;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameCommand{
    private CurrentGameInfo gameInfo;
    private SummonerEntry[] leftTeam;
    private SummonerEntry[] rightTeam;

    protected GameCommand(){
    }

    public static void fetchGame(String playerName){
        new GameCommand().fetchGameImpl(playerName);
    }

    //pretty prints scoreboard
    public void fetchGameImpl(String playerName){
        SummonerDTO summoner = SummonerV4.fetchPlayerByName(playerName);
        if(summoner == null)
            return;

        gameInfo = SpectatorV4.queryGame(Riot.REGION, summoner);
        if(gameInfo == null){
            System.err.println("Likely the game is over by now or it's a clash game");
            return;
        }
        GameTeams gameTeams = GameTeams.split(gameInfo.participants);
        leftTeam = getSummonerEntries(gameTeams.leftTeam);
        rightTeam = getSummonerEntries(gameTeams.rightTeam);
        sortTeams();
        complementTeamsRanks();
        StringBuilder simpleGameInfo = new StringBuilder();
        if(gameInfo.gameStartTime != 0){
            TimeElapsed timeElapsed = new TimeElapsed(System.currentTimeMillis() - gameInfo.gameStartTime);
            simpleGameInfo.append(timeElapsed).append(' ');
        }
        simpleGameInfo.append(gameInfo.gameMode);
        System.out.println(simpleGameInfo);
        String scoreboard = statsToPrettyString(summoner.id);
        System.out.println(scoreboard);
    }

    //attempts to sort teams by roles
    private void sortTeams(){
        if(gameInfo.gameMode.equals("ARAM")){
            //don't sort since ARAM has no roles
            return;
        }
        sortTeam(leftTeam);
        sortTeam(rightTeam);
    }
    private void sortTeam(SummonerEntry[] summoners){
        int len = summoners.length;
        if(len != 5){
            return;
        }

        Player[] players = new Player[5];
        //convert champion names to ids
        for (int i = 0; i < len; i++){
            int champId = Champions.getChampionId(summoners[i].championName).expect("Unknown champion name");
            players[i] = new Player(champId);
        }
        RoleState state = new RoleState();

        for (int i = 0; i < len; i++){
            SummonerEntry summoner = summoners[i];
            if (summoner.spell1 == Spell.SMITE || summoner.spell2 == Spell.SMITE){
                players[i].assignedRole = Role.JUNGLE;
                state.setAssigned(Role.JUNGLE);
                break;
            }
        }

        Role[] roles = {Role.TOP, Role.JUNGLE, Role.MID, Role.ADC, Role.SUPPORT};
        //assign single-role champions first, for more probability
        for (int i = 0; i < 5; i++){
            Role targetRole = roles[i];
            if(state.isAssigned(targetRole)){
                continue;
            }

            //iterate over players
            for (int j = 0; j < 5; j++){
                if(players[j].hasRole()){
                    continue;
                }
                Role[] champRoles = Champions.rolesOf(players[j].championId);
                if(champRoles.length != 1){
                    continue;
                }
                if (champRoles[0] == targetRole){
                    players[j].assignedRole = targetRole;
                    state.setAssigned(targetRole);
                    break;
                }
            }
        }

        List<Role[]> rolePool = new ArrayList<>();
        //assign many-role champions
        for (int j = 0; j < 5; j++){
            if(players[j].hasRole()){
                continue;
            }
            Role[] champRoles = Champions.rolesOf(players[j].championId);
            //store roles to filter the role pool for unique elements to avoid conflicts
            rolePool.add(champRoles);
        }

        List<Role> unique = uniqueRoles(rolePool);
        for(Role role : unique){
            if(state.isAssigned(role)){
                continue;
            }
            //iterate over players
            for (int j = 0; j < 5; j++){
                if(players[j].hasRole()){
                    continue;
                }
                if (Champions.playsRole(players[j].championId, role)){
                    players[j].assignedRole = role;
                    state.setAssigned(role);
                    break;
                }
            }
        }

        SummonerEntry[] summonersCopy = new SummonerEntry[len];
        System.arraycopy(summoners, 0, summonersCopy, 0, len);

        Iterator<Role> unassignedRoles = state.unassignedRoles().iterator();
        for (int i = 0; i < 5; i++){
            Player player = players[i];
            if(player.hasRole()){
                int index = RoleState.roleToIndex(player.assignedRole);
                summoners[index] = summonersCopy[i];
            }else{
                // fill the gaps
                Role unassigned = unassignedRoles.next();
                int index = RoleState.roleToIndex(unassigned);
                summoners[index] = summonersCopy[i];
            }
        }
    }

    public List<Role> uniqueRoles(List<Role[]> roleArrays){
        int arrays = roleArrays.size();
        if(arrays == 0){
            return Collections.emptyList();
        }
        if(arrays == 1){
            Role[] roles = roleArrays.get(0);
            List<Role> set = new ArrayList<>(roles.length);
            set.addAll(Arrays.asList(roles));
            return set;
        }

        List<Role> unique = new ArrayList<>(Arrays.asList(roleArrays.get(0)));
        List<Role> repeatedRoles = new ArrayList<>();
        for (int i = 1; i < arrays; i++){
            Role[] roles = roleArrays.get(i);
            for (Role role : roles){
                if (unique.contains(role)){
                    unique.remove(role);
                    repeatedRoles.add(role);
                }else if(!repeatedRoles.contains(role)){
                    //not contained in unique
                    unique.add(role);
                }
            }
        }
        return unique;
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
        final int CHAMP_LEN = 14, DUO_LEN = 4, FLEX_LEN = 5, PIPE_LEN = 2;
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
            Spell spell1 = Spell.from(participant.spell1Id);
            Spell spell2 = Spell.from(participant.spell2Id);
            SummonerEntry summonerEntry = new SummonerEntry(participant.summonerId, champName, duoRank, flexRank, spell1, spell2);
            team[i] = summonerEntry;
        }
        return team;
    }
}


class Player{
    public int championId;
    public Role assignedRole;

    public Player(int championId){
        this.championId = championId;
    }

    public boolean hasRole(){
        return assignedRole != null;
    }

    @Override
    public String toString(){
        return "[" + championId + ", " + assignedRole + "]";
    }
}

class RoleState{
    private static final Role[] ROLES = {Role.TOP, Role.JUNGLE, Role.MID, Role.ADC, Role.SUPPORT};
    public boolean[] assignedRoles = new boolean[5];

    public boolean isAssigned(Role role){
        int index = roleToIndex(role);
        return assignedRoles[index];
    }
    public void setAssigned(Role role){
        int index = roleToIndex(role);
        assignedRoles[index] = true;
    }
    public List<Role> unassignedRoles(){
        List<Role> unassigned = new ArrayList<>(5);
        for (int i = 0; i < 5; i++){
            if(assignedRoles[i]){
               continue;
            }
            unassigned.add(ROLES[i]);
        }
        return unassigned;
    }
    public static int roleToIndex(Role role){
        switch (role){
            case TOP:
                return 0;
            case JUNGLE:
                return 1;
            case MID:
                return 2;
            case ADC:
                return 3;
            case SUPPORT:
            default:
                return 4;
        }
    }
}
