package program.structs;

import lol.ranks.LeagueRank;

public class SummonerEntry{
    public String summonerId;
    public String championName;
    public LeagueRank duoRank;
    public LeagueRank flexRank;

    public SummonerEntry(String summonerId, String championName, LeagueRank duoRank, LeagueRank flexRank){
        this.summonerId = summonerId;
        this.championName = championName;
        this.duoRank = duoRank;
        this.flexRank = flexRank;
    }

}
