package program.structs;

public class SimpleMatch{
    //from a player's perspective
    public String id, champ, type;
    public TimeElapsed length;
    public int kills, deaths, assists;
    public boolean win;
    public long queueId;

    public SimpleMatch(String id){
        this.id = id;
    }
    public SimpleMatch(int k, int d, int a){
        this.kills = k;
        this.deaths = d;
        this.assists = a;
    }
    public String kda(){
        return "" + this.kills + '/' + this.deaths + '/' + this.assists;
    }

    @Override
    public String toString(){
        return id + " " + champ + " " + length + " " + kda();
    }
}
