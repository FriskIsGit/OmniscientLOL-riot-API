package lol.apis;

public class ChampionMasteryV4{
    public static final String bySummoner = "/lol/champion-mastery/v4/champion-masteries/by-summoner/{encryptedSummonerId}";
    public static final String bySummonerByChampion = "/lol/champion-mastery/v4/champion-masteries/by-summoner/{encryptedSummonerId}/by-champion/{championId}";
    public static final String bySummonerTop = "/lol/champion-mastery/v4/champion-masteries/by-summoner/{encryptedSummonerId}/top";
    public static final String scoresBySummoner = "/lol/champion-mastery/v4/scores/by-summoner/{encryptedSummonerId}";
}
