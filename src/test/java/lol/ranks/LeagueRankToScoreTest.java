package lol.ranks;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LeagueRankToScoreTest{
    @Test
    public void unrankedTest(){
        LeagueRank rank = new LeagueRank(LeagueRank.UNRANKED);
        assertEquals(0, rank.divisionScore());
    }
    @Test
    public void iron4Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.IRON, 4);
        assertEquals(1, rank.divisionScore());
    }
    @Test
    public void iron3Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.IRON, 3);
        assertEquals(2, rank.divisionScore());
    }
    @Test
    public void bronze3Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.BRONZE, 3);
        assertEquals(6, rank.divisionScore());
    }
    @Test
    public void silver3Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.SILVER, 3);
        assertEquals(10, rank.divisionScore());
    }
    @Test
    public void silver2Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.SILVER, 2);
        assertEquals(11, rank.divisionScore());
    }
    @Test
    public void gold1Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.GOLD, 1);
        assertEquals(16, rank.divisionScore());
    }
    @Test
    public void plat2Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.PLATINUM, 2);
        assertEquals(19, rank.divisionScore());
    }
    @Test
    public void diamond3Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.DIAMOND, 3);
        assertEquals(22, rank.divisionScore());
    }
    @Test
    public void diamond1Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.DIAMOND, 1);
        assertEquals(24, rank.divisionScore());
    }
    @Test
    public void masterTest(){
        //highest diamond + 4
        LeagueRank rank = new LeagueRank(LeagueRank.MASTER);
        assertEquals(26, rank.divisionScore());
    }
    @Test
    public void grandmasterTest(){
        //highest diamond + 4
        LeagueRank rank = new LeagueRank(LeagueRank.GRANDMASTER);
        assertEquals(28, rank.divisionScore());
    }
    @Test
    public void challengerTest(){
        //highest diamond + 4
        LeagueRank rank = new LeagueRank(LeagueRank.CHALLENGER);
        assertEquals(30, rank.divisionScore());
    }
    @Test
    public void plat5Test(){
        // 2013-2018
        LeagueRank rank = new LeagueRank(LeagueRank.PLATINUM, 5);
        LeagueRank equivalent = new LeagueRank(LeagueRank.GOLD, 1);
        assertEquals(equivalent.divisionScore(), rank.divisionScore());
    }
    @Test
    public void iron5Exception(){
        // 2013-2018
        try{
            new LeagueRank(LeagueRank.IRON, 5);
            Assert.fail();
        }catch (IllegalArgumentException e){
        }
    }
    @Test
    public void d5Test(){
        // 2013-2018
        LeagueRank rank = new LeagueRank(LeagueRank.DIAMOND, 5);
        LeagueRank equivalent = new LeagueRank(LeagueRank.PLATINUM, 1);
        assertEquals(equivalent.divisionScore(), rank.divisionScore());
    }
}
