package program.commands;

import lol.champions.Role;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GameCommandTest{
    @Test
    public void test1(){
        GameCommand gameCommand = new GameCommand();
        Role[] roles1 = {Role.TOP, Role.MID};
        Role[] roles2 = {Role.MID, Role.SUPPORT};
        Role[] roles3 = {Role.JUNGLE, Role.MID};
        List<Role> roles = gameCommand.uniqueRoles(Arrays.asList(roles1, roles2, roles3));
        System.out.println(roles);
        assertEquals(3, roles.size());
    }
    @Test
    public void test2(){
        GameCommand gameCommand = new GameCommand();
        Role[] roles1 = {Role.TOP, Role.MID, Role.ADC};
        Role[] roles2 = {Role.MID, Role.SUPPORT};
        Role[] roles3 = {Role.JUNGLE, Role.MID, Role.ADC};
        List<Role> roles = gameCommand.uniqueRoles(Arrays.asList(roles1, roles2, roles3));
        System.out.println(roles);
        assertEquals(3, roles.size());
    }
    @Test
    public void test3(){
        GameCommand gameCommand = new GameCommand();
        Role[] roles1 = {Role.TOP, Role.MID, Role.ADC};
        Role[] roles2 = {Role.MID, Role.SUPPORT};
        Role[] roles3 = {Role.JUNGLE, Role.MID, Role.ADC, Role.TOP};
        List<Role> roles = gameCommand.uniqueRoles(Arrays.asList(roles1, roles2, roles3));
        System.out.println(roles);
        assertEquals(2, roles.size());
    }
}
