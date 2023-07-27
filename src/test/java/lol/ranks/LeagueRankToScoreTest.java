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
        assertEquals(LeagueRank.IRON-3, rank.divisionScore());
    }
    @Test
    public void iron3Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.IRON, 3);
        assertEquals(LeagueRank.IRON-2, rank.divisionScore());
    }
    @Test
    public void iron1Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.IRON, 1);
        assertEquals(LeagueRank.IRON, rank.divisionScore());
    }
    @Test
    public void bronze3Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.BRONZE, 3);
        assertEquals(LeagueRank.BRONZE-2, rank.divisionScore());
    }
    @Test
    public void silver3Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.SILVER, 3);
        assertEquals(LeagueRank.SILVER-2, rank.divisionScore());
    }
    @Test
    public void silver2Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.SILVER, 2);
        assertEquals(LeagueRank.SILVER-1, rank.divisionScore());
    }
    @Test
    public void gold1Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.GOLD, 1);
        assertEquals(LeagueRank.GOLD, rank.divisionScore());
    }
    @Test
    public void plat1Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.PLATINUM, 1);
        assertEquals(LeagueRank.PLATINUM, rank.divisionScore());
    }
    @Test
    public void plat2Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.PLATINUM, 2);
        assertEquals(LeagueRank.PLATINUM-1, rank.divisionScore());
    }

    @Test
    public void emerald4Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.EMERALD, 4);
        assertEquals(LeagueRank.EMERALD-3, rank.divisionScore());
    }
    @Test
    public void diamond3Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.DIAMOND, 3);
        assertEquals(LeagueRank.DIAMOND-2, rank.divisionScore());
    }
    @Test
    public void diamond1Test(){
        LeagueRank rank = new LeagueRank(LeagueRank.DIAMOND, 1);
        assertEquals(LeagueRank.DIAMOND, rank.divisionScore());
    }
    @Test
    public void masterTest(){
        //highest diamond + 4
        LeagueRank rank = new LeagueRank(LeagueRank.MASTER);
        assertEquals(LeagueRank.MASTER, rank.divisionScore());
    }
    @Test
    public void grandmasterTest(){
        //highest diamond + 4
        LeagueRank rank = new LeagueRank(LeagueRank.GRANDMASTER);
        assertEquals(LeagueRank.GRANDMASTER, rank.divisionScore());
    }
    @Test
    public void challengerTest(){
        //highest diamond + 4
        LeagueRank rank = new LeagueRank(LeagueRank.CHALLENGER);
        assertEquals(LeagueRank.CHALLENGER, rank.divisionScore());
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
        }catch (IllegalArgumentException ignored){
        }
    }
    @Test
    public void d5Test(){
        // 2013-2018
        LeagueRank rank = new LeagueRank(LeagueRank.DIAMOND, 5);
        LeagueRank equivalent = new LeagueRank(LeagueRank.EMERALD, 1);
        int diff = LeagueRank.DIAMOND - LeagueRank.EMERALD;
        assertEquals(equivalent.divisionScore() + diff-4, rank.divisionScore());
    }
}
