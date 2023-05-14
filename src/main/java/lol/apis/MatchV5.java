package lol.apis;

public class MatchV5{
    public static String idsByPuuid = "/lol/match/v5/matches/by-puuid/{puuid}/ids"; //returns List[string]
    public static String byMatchId = "/lol/match/v5/matches/{matchId}"; //returns MatchDTO
    public static String byMatchIdTimeline = "/lol/match/v5/matches/{matchId}/timeline"; //returns MatchTimelineDto
}
