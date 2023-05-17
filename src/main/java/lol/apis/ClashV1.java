package lol.apis;

public class ClashV1{
    public static final String byPuuid = "/lol/clash/v1/players/by-puuid/{encryptedPUUID}";
    public static final String bySummoner = "/lol/clash/v1/players/by-summoner/{summonerId}";
    public static final String byTeamId = "/lol/clash/v1/teams/{teamId}";
    public static final String tournaments = "/lol/clash/v1/tournaments";
    public static final String tournamentsByTeam = "/lol/clash/v1/tournaments/by-team/{teamId}";
    public static final String tournamentsById = "/lol/clash/v1/tournaments/{tournamentId}";
}
