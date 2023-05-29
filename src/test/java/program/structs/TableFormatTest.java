package program.structs;

import org.junit.Test;

import java.util.Arrays;

public class TableFormatTest{
    @Test
    public void test1(){
        TableFormat table = new TableFormat(true);
        table.addColumnDefinition("CHAMP", 6);
        table.addColumnDefinition("ROLE", 6);
        table.addColumnDefinition("ID", 3);
        table.addColumnDefinition("CS_SCORE", 8, true);
        table.addColumnDefinition("KDA", 6, true);
        table.writeToRow(Arrays.asList(null, null, 888, 1.2, 1.9999));
        table.writeToRow(Arrays.asList("Viego", "JUNGLE", 234, 7.53f, 1.21654));
        table.writeToRow(Arrays.asList("WHAT", "CARRY", 888, 21.2, 1.9999));
        table.writeSeparatorRow('=');
        table.writeSummaryRow(0);
        System.out.println(table);
    }
    @Test
    public void test2(){
        TableFormat table = new TableFormat(true);
        table.addColumnDefinition("CHAR", 6);
        table.addColumnDefinition("Text", 6);
        table.writeToRow(Arrays.asList('=', "woah"));
    }
}


