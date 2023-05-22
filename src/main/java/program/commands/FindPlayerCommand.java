package program.commands;

import lol.Riot;
import lol.apis.SummonerV4;
import lol.requests.SimpleResponse;
import lol.requests.URIPath;
import org.apache.http.client.fluent.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class FindPlayerCommand{
    //returns server names
    public static List<String> findPlayer(String playerName){
        List<String> servers = Collections.synchronizedList(new ArrayList<>());
        int regions = Riot.REGIONS.length;
        ExecutorService executor = Executors.newFixedThreadPool(regions);
        Future<?>[] futures = new Future[regions];
        for (int i = 0; i < regions; i++){
            final String region = Riot.REGIONS[i];
            Runnable runnableTask = () -> {
                String endpoint = URIPath.of(SummonerV4.byName).args(playerName);
                Request request = Riot.newRequest(region, endpoint);
                SimpleResponse response = SimpleResponse.performRequest(request).expect("There was no response(SummonerV4)");
                if(response.code != 200){
                    if(response.code == 429)
                        System.err.println("Rate limit!");
                    return;
                }
                synchronized (servers){
                    servers.add(region);
                }
            };
            futures[i] = executor.submit(runnableTask);
        }
        long st = System.currentTimeMillis();
        final int timeOutMs = 15000;
        //block until all threads are finished
        for(Future<?> future : futures){
            try{
                long timeSpent = System.currentTimeMillis() - st;
                if(timeSpent > timeOutMs){
                    System.err.println("Timed out at rank complementing");
                    break;
                }
                future.get(timeOutMs - timeSpent, TimeUnit.MILLISECONDS);
            }catch (InterruptedException | ExecutionException | TimeoutException e){
                System.err.println("Timed out at rank complementing");
                break;
            }
        }
        System.out.println("Done");
        return servers;
    }
}
