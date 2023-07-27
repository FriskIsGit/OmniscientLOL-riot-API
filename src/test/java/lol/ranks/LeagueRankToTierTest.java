package lol.ranks;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class LeagueRankToTierTest{
    @Test
    public void test1(){
        LeagueRank rank = LeagueRank.fromDivisionScore(LeagueRank.SILVER);
        assertSame(LeagueRank.SILVER, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void test2(){
        LeagueRank rank = LeagueRank.fromDivisionScore(LeagueRank.CHALLENGER);
        assertSame(LeagueRank.CHALLENGER, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void test3(){
        LeagueRank rank = LeagueRank.fromDivisionScore(LeagueRank.DIAMOND);
        assertSame(LeagueRank.DIAMOND, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void betweenDiamondAndMasterTestLow(){
        LeagueRank rank = LeagueRank.fromDivisionScore(LeagueRank.DIAMOND + 1);
        assertSame(LeagueRank.DIAMOND, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void betweenDiamondAndMasterTestHigh(){
        LeagueRank rank = LeagueRank.fromDivisionScore(LeagueRank.MASTER-1);
        assertSame(LeagueRank.MASTER, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void test4(){
        LeagueRank rank = LeagueRank.fromDivisionScore(LeagueRank.PLATINUM);
        assertSame(LeagueRank.PLATINUM, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void emerald(){
        LeagueRank rank = LeagueRank.fromDivisionScore(LeagueRank.EMERALD);
        assertSame(LeagueRank.EMERALD, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void grandmasterTest1(){
        LeagueRank rank = LeagueRank.fromDivisionScore(LeagueRank.GRANDMASTER);
        assertSame(LeagueRank.GRANDMASTER, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void grandmasterTest2(){
        LeagueRank rank = LeagueRank.fromDivisionScore(LeagueRank.GRANDMASTER);
        assertSame(LeagueRank.GRANDMASTER, rank.getTier());
        assertSame(1, rank.getDivision());
    }
    @Test
    public void gold2Test(){
        LeagueRank rank = LeagueRank.fromDivisionScore(LeagueRank.GOLD-1);
        assertSame(LeagueRank.GOLD, rank.getTier());
        assertSame(2, rank.getDivision());
    }
    @Test
    public void bronze2Test(){
        LeagueRank rank = LeagueRank.fromDivisionScore(LeagueRank.BRONZE-1);
        assertSame(LeagueRank.BRONZE, rank.getTier());
        assertSame(2, rank.getDivision());
    }
}
