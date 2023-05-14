package lol.ranks;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class LeagueRankAverageTest{
    @Test
    public void unranked(){
        LeagueRank u1 = new LeagueRank(LeagueRank.UNRANKED);
        LeagueRank u2 = new LeagueRank(LeagueRank.UNRANKED);
        LeagueRank u3 = new LeagueRank(LeagueRank.UNRANKED);
        LeagueRank u4 = new LeagueRank(LeagueRank.UNRANKED);
        LeagueRank u5 = new LeagueRank(LeagueRank.UNRANKED);
        int total = u1.divisionScore() + u2.divisionScore()
                + u3.divisionScore() + u4.divisionScore() + u5.divisionScore();
        int average = total / 5;
        LeagueRank actual = LeagueRank.fromDivisionScore(average);
        assertSame(LeagueRank.UNRANKED, actual.getTier());
        assertEquals(1, actual.getDivision());
    }
    @Test
    public void twoRanks1(){
        LeagueRank gold3 = new LeagueRank(LeagueRank.GOLD, 3);
        LeagueRank diamond3 = new LeagueRank(LeagueRank.DIAMOND, 3);
        int total = gold3.divisionScore() + diamond3.divisionScore();
        int average = total / 2;
        LeagueRank actual = LeagueRank.fromDivisionScore(average);
        assertSame(LeagueRank.PLATINUM, actual.getTier());
        assertEquals(3, actual.getDivision());
    }
    @Test
    public void twoRanks2(){
        LeagueRank d1 = new LeagueRank(LeagueRank.DIAMOND, 1);
        LeagueRank d3 = new LeagueRank(LeagueRank.DIAMOND, 3);
        int total = d1.divisionScore() + d3.divisionScore();
        int average = total / 2;
        LeagueRank actual = LeagueRank.fromDivisionScore(average);
        assertSame(LeagueRank.DIAMOND, actual.getTier());
        assertEquals(2, actual.getDivision());
    }
    @Test
    public void twoRanks3(){
        LeagueRank s1 = new LeagueRank(LeagueRank.SILVER, 4);
        LeagueRank g3 = new LeagueRank(LeagueRank.GOLD, 4);
        int total = s1.divisionScore() + g3.divisionScore();
        int average = total / 2;
        LeagueRank actual = LeagueRank.fromDivisionScore(average);
        assertSame(LeagueRank.SILVER, actual.getTier());
        assertEquals(2, actual.getDivision());
    }
    @Test
    public void twoRanks4(){
        LeagueRank b4 = new LeagueRank(LeagueRank.BRONZE, 4);
        LeagueRank d4 = new LeagueRank(LeagueRank.DIAMOND, 4);
        int total = b4.divisionScore() + d4.divisionScore();
        int average = total / 2;
        LeagueRank actual = LeagueRank.fromDivisionScore(average);
        assertSame(LeagueRank.GOLD, actual.getTier());
        assertEquals(4, actual.getDivision());
    }
    @Test
    public void threeRanks1(){
        LeagueRank b3 = new LeagueRank(LeagueRank.BRONZE, 3);
        LeagueRank g3 = new LeagueRank(LeagueRank.GOLD, 3);
        LeagueRank d3 = new LeagueRank(LeagueRank.DIAMOND, 3);
        int total = b3.divisionScore() + g3.divisionScore() + d3.divisionScore();
        int average = total / 3;
        LeagueRank actual = LeagueRank.fromDivisionScore(average);
        assertSame(LeagueRank.GOLD, actual.getTier());
        assertEquals(3, actual.getDivision());
    }
    @Test
    public void fiveRanks(){
        LeagueRank r1 = new LeagueRank(LeagueRank.UNRANKED);
        LeagueRank r2 = new LeagueRank(LeagueRank.SILVER, 1);
        LeagueRank r3 = new LeagueRank(LeagueRank.UNRANKED);
        LeagueRank r4 = new LeagueRank(LeagueRank.SILVER, 3);
        LeagueRank r5 = new LeagueRank(LeagueRank.BRONZE, 2);
        int total = r1.divisionScore() + r2.divisionScore() + r3.divisionScore() + r4.divisionScore() + r5.divisionScore();
        int average = total / 5;
        System.out.println("AVERAGE: " + average);
        LeagueRank actual = LeagueRank.fromDivisionScore(average);
        System.out.println(actual);
        assertSame(LeagueRank.BRONZE, actual.getTier());
        assertEquals(4, actual.getDivision());
    }
    @Test
    public void maverickTest(){
        LeagueRank r1 = new LeagueRank(LeagueRank.BRONZE, 3);
        LeagueRank r2 = new LeagueRank(LeagueRank.BRONZE, 3);
        LeagueRank r3 = new LeagueRank(LeagueRank.BRONZE, 3);
        LeagueRank r4 = new LeagueRank(LeagueRank.BRONZE, 3);
        LeagueRank r5 = new LeagueRank(LeagueRank.MASTER);
        int total = r1.divisionScore() + r2.divisionScore() + r3.divisionScore() + r4.divisionScore() + r5.divisionScore();
        int average = total / 5;
        System.out.println("AVERAGE: " + average);
        LeagueRank leftActual = LeagueRank.fromDivisionScore(average);
        System.out.println(leftActual);
    }
}
