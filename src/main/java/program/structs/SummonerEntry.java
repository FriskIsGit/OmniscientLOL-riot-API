package program.structs;

import lol.ranks.LeagueRank;
import lol.spells.Spell;

public class SummonerEntry{
    public String summonerId;
    public String championName;
    public LeagueRank duoRank;
    public LeagueRank flexRank;
    public Spell spell1;
    public Spell spell2;

    public SummonerEntry(String summonerId, String championName, LeagueRank duoRank, LeagueRank flexRank,
                         Spell spell1, Spell spell2){
        this.summonerId = summonerId;
        this.championName = championName;
        this.duoRank = duoRank;
        this.flexRank = flexRank;
        this.spell1 = spell1;
        this.spell2 = spell2;
    }

}
