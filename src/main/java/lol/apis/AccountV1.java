package lol.apis;

public class AccountV1{
    public static final String byPuuid = "/riot/account/v1/accounts/by-puuid/{puuid}";
    public static final String byRiotIdByTagline = "/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}";
    public static final String me = "/riot/account/v1/accounts/me";
    public static final String byGameByPuuid = "/riot/account/v1/active-shards/by-game/{game}/by-puuid/{puuid}";
}
