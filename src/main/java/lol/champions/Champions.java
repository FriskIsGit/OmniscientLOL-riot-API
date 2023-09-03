package lol.champions;

import lol.requests.Option;
import lol.requests.SimpleResponse;
import lol.utils.PathUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Champions{
    private static final HashMap<Integer, String> idToName = new HashMap<>(256);
    private static final HashMap<String, Integer> nameToId = new HashMap<>(256);

    private static final HashMap<Integer, Role[]> idToRoles = new HashMap<>(256);
    private static final String CHAMPIONS_FILE = "champions.txt";
    private static final String ROLES_FILE = "roles.txt";
    private static boolean isJar;
    public Champions(){
    }

    static{
        try{
            isJar = runFromJar();
            loadChampions();
            loadRoles();
        }catch (IOException ignored){
        }
    }

    private static void loadChampions() throws IOException{
        if(isJar){
            InputStream is = Champions.class.getResourceAsStream('/' + CHAMPIONS_FILE);
            String champs = SimpleResponse.streamToString(is);
            List<String> lines = readAllLines(champs);
            readChampionsFromList(lines);
        }else{
            URL url = Champions.class.getResource('/' + CHAMPIONS_FILE);
            if(url == null)
                return;

            String path = PathUtils.convertURLToString(url);
            readChampionsFromList(Files.readAllLines(Paths.get(path)));
        }
    }
    private static void readChampionsFromList(List<String> lines){
        for(String line : lines){
            int colon = line.indexOf(':');
            String name = line.substring(0, colon);
            String strId = line.substring(colon + 1);
            int id = Integer.parseInt(strId);
            idToName.put(id, name);
            nameToId.put(name, id);
        }
    }
    private static void loadRoles() throws IOException{
        if(isJar){
            InputStream is = Champions.class.getResourceAsStream('/' + ROLES_FILE);
            String roles = SimpleResponse.streamToString(is);
            List<String> lines = readAllLines(roles);
            readRolesFromList(lines);
        }else{
            URL url = Champions.class.getResource('/' + ROLES_FILE);
            if(url == null)
                return;

            String path = PathUtils.convertURLToString(url);
            readRolesFromList(Files.readAllLines(Paths.get(path)));
        }
    }
    private static void readRolesFromList(List<String> lines){
        for(String line : lines){
            int colon = line.indexOf(':');
            String name = line.substring(0, colon);
            String rolesStr = line.substring(colon + 1);
            String[] roleSplit = rolesStr.split(",");
            Integer id = nameToId.get(name);
            if(id == null){
                continue;
            }

            Role[] roles = new Role[roleSplit.length];
            for (int i = 0; i < roles.length; i++){
                char identifier = roleSplit[i].charAt(0);
                roles[i] = Role.from(identifier);
            }
            idToRoles.put(id, roles);
        }
    }

    private static List<String> readAllLines(String str){
        Scanner scanner = new Scanner(str);
        List<String> result = new ArrayList<>();
        while (true) {
            String line;
            try{
                line = scanner.nextLine();
            }catch (NoSuchElementException e){
                break;
            }
            result.add(line);
        }
        return result;


    }

    public static boolean playsRole(int championId, Role role){
        Role[] roles = idToRoles.get(championId);
        if(roles == null)
            return false;

        for (Role r : roles){
            if (r == role){
                return true;
            }
        }
        return false;
    }
    public static  Role[] rolesOf(int championId){
        Role[] roles = idToRoles.get(championId);
        if(roles == null){
            return new Role[0];
        }
        return roles;
    }
    public static String getChampionName(int id){
        return idToName.get(id);
    }
    public static Option<Integer> getChampionId(String name){
        Integer maybeId = nameToId.get(name);
        if(maybeId == null){
            return Option.none();
        }
        return Option.of(maybeId);
    }
    public static boolean runFromJar(){
        return Champions.class.getProtectionDomain().getCodeSource().getLocation().toString().endsWith(".jar");
    }
}
