package lol.ranks;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class LeagueRankToTierTest{
    @Test
    public void test1(){
        LeagueRank rank = LeagueRank.fromDivisionScore(12);
        assertSame(LeagueRank.SILVER, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void test2(){
        LeagueRank rank = LeagueRank.fromDivisionScore(30);
        assertSame(LeagueRank.CHALLENGER, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void test3(){
        LeagueRank rank = LeagueRank.fromDivisionScore(24);
        assertSame(LeagueRank.DIAMOND, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void betweenDiamondAndMasterTest(){
        LeagueRank rank = LeagueRank.fromDivisionScore(25);
        assertSame(LeagueRank.DIAMOND, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void test4(){
        LeagueRank rank = LeagueRank.fromDivisionScore(20);
        assertSame(LeagueRank.PLATINUM, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void grandmasterTest1(){
        LeagueRank rank = LeagueRank.fromDivisionScore(28);
        assertSame(LeagueRank.GRANDMASTER, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void grandmasterTest2(){
        LeagueRank rank = LeagueRank.fromDivisionScore(29);
        assertSame(LeagueRank.GRANDMASTER, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void gold2Test(){
        LeagueRank rank = LeagueRank.fromDivisionScore(15);
        assertSame(LeagueRank.GOLD, rank.getTier());
        assertSame(2, rank.getDivision());
    }
    @Test
    public void bronze2Test(){
        LeagueRank rank = LeagueRank.fromDivisionScore(7);
        assertSame(LeagueRank.BRONZE, rank.getTier());
        assertSame(2, rank.getDivision());
    }
}
