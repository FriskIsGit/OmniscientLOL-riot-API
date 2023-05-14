package lol.opgg;

import lol.ranks.LeagueRank;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ParsingTest{
    @Test
    public void test1(){
        String body = "<ul class=\"tier-list\"><li><div style=\"position: relative;\" class=\"\"><b>S5</b> silver  1</div></li></ul>";
        List<RankEntry> entryList = OPGG.parseLiElements(body, 0);
        RankEntry entry = entryList.get(0);
        assertEquals(1, entryList.size());
        assertEquals(5, entry.season);
        assertEquals(1, entry.rank.getDivision());
        assertEquals(LeagueRank.SILVER, entry.rank.getTier());
    }
    @Test
    public void test2(){
        String body = "<ul class=\"tier-list\"><li><div class=\"\" style=\"position: relative;\"><b>S2022</b> silver" +
                "  4</div></li><li><div class=\"\" style=\"position: relative;\"><b>S7</b> platinum  5</div></li><li>" +
                "<div class=\"\" style=\"position: relative;\"><b>S6</b> platinum  5</div></li></ul>";
        List<RankEntry> entryList = OPGG.parseLiElements(body, 0);
        System.out.println(entryList);
    }
}
