package program;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CLIProgramSplitTest{
    @Test
    public void test0Spaces(){
        String input = "command";
        String[] split = CLIProgram.twoSplit(input);
        assertEquals(2, split.length);
        assertEquals("command", split[0]);
    }
    @Test
    public void test1Space(){
        String input = "command arg";
        String[] split = CLIProgram.twoSplit(input);
        assertEquals(2, split.length);
        assertEquals("command", split[0]);
        assertEquals("arg", split[1]);
    }
    @Test
    public void testManyArgs(){
        String input = "command arg1 arg2 arg3";
        String[] split = CLIProgram.twoSplit(input);
        assertEquals(2, split.length);
        assertEquals("command", split[0]);
        assertEquals("arg1 arg2 arg3", split[1]);
    }
}
