package lol.opgg;

import lol.ranks.LeagueRank;

public class RankEntry{
    public int season; //3 - 13, where season 10 is year 2020
    public LeagueRank rank;

    public RankEntry(int seasonOrYear, LeagueRank rank){
        this.season = toSeason(seasonOrYear);
        this.rank = rank;
    }
    public RankEntry(String season, LeagueRank rank){
        this(Integer.parseInt(season), rank);
    }

    //returns -1 for invalid years
    public static int toSeason(int number){
        if(number > 0 && number < 10){
            return number;
        }
        if(number < 2011 || number > 2099){
            System.err.println("Year is out of bounds");
            return -1;
        }
        return (number % 2000) - 10;
    }

    @Override
    public String toString(){
        return "{" + "s=" + season + ", rank=" + rank + '}';
    }
}
