package lol.champions;

import lol.requests.Option;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Champions{
    private static final HashMap<Integer, String> idToName = new HashMap<>(256);
    private static final HashMap<String, Integer> nameToId = new HashMap<>(256);

    static{
        try{
            loadChampions();
        }catch (IOException ignored){
        }
    }

    private static void loadChampions() throws IOException{
        URL url = Champions.class.getResource("/champions.txt");
        if(url == null)
            return;

        URI uri;
        try{
            uri = url.toURI();
        }catch (URISyntaxException e){
            e.printStackTrace();
            return;
        }

        List<String> lines = Files.readAllLines(Paths.get(uri.getPath().substring(1)));
        for(String line : lines){
            int colon = line.indexOf(':');
            String name = line.substring(0, colon);
            String strId = line.substring(colon + 1);
            int id = Integer.parseInt(strId);
            idToName.put(id, name);
            nameToId.put(name, id);
        }
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
}
