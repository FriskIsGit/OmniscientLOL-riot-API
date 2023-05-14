package lol.dtos;

import com.alibaba.fastjson.JSONObject;

//Data Transfer Object, DamageDTO, KillsDTO,
public class ParticipantDTO{
    public int kills, deaths, assists, baronKills, bountyLevel, champExperience, champLevel, championId,
            championTransform, consumablesPurchased,
            damageDealtToBuildings, damageDealtToObjectives, damageDealtToTurrets, damageSelfMitigated,
            magicDamageDealt, magicDamageDealtToChampions, magicDamageTaken, physicalDamageDealt,
            physicalDamageDealtToChampions, physicalDamageTaken, profileIcon, neutralMinionsKilled,
            objectivesStolen, objectivesStolenAssists, participantId,
            detectorWardsPlaced, dragonKills,
            goldEarned, goldSpent, inhibitorKills, inhibitorTakedowns, inhibitorsLost,
            item0, item1, item2, item3, item4, item5, item6, itemsPurchased, killingSprees,
            largestCriticalStrike, largestKillingSpree, largestMultiKill, longestTimeSpentLiving,
            pentaKills, quadraKills, tripleKills, doubleKills, unrealKills,
            sightWardsBoughtInGame, spell1Casts, spell2Casts, spell3Casts, spell4Casts, summoner1Casts, summoner1Id,
            summoner2Casts, summoner2Id, summonerLevel, teamId,
            timeCCingOthers, timePlayed, totalDamageDealt, totalDamageDealtToChampions, totalDamageShieldedOnTeammates,
            totalDamageTaken, totalHeal, totalHealsOnTeammates, totalMinionsKilled, totalTimeCCDealt, totalTimeSpentDead,
            totalUnitsHealed, trueDamageDealt, trueDamageDealtToChampions, trueDamageTaken, turretKills, turretTakedowns,
            turretsLost, visionScore, visionWardsBoughtInGame, wardsKilled, wardsPlaced;

    public boolean firstBloodAssist, firstBloodKill, firstTowerAssist, firstTowerKill,
            gameEndedInEarlySurrender, gameEndedInSurrender, teamEarlySurrendered, win;
    public String championName, individualPosition, lane, puuid, riotIdName, riotIdTagline, role,
            summonerId, summonerName, teamPosition;

    public static ParticipantDTO fromJson(JSONObject json){
        ParticipantDTO participant = new ParticipantDTO();
        participant.kills = json.getInteger("kills");
        participant.deaths = json.getInteger("deaths");
        participant.assists = json.getInteger("assists");

        participant.baronKills = json.getInteger("baronKills");
        participant.bountyLevel = json.getInteger("bountyLevel");
        participant.champExperience = json.getInteger("champExperience");
        participant.champLevel = json.getInteger("champLevel");
        participant.championId = json.getInteger("championId");
        participant.championTransform = json.getInteger("championTransform");
        participant.consumablesPurchased = json.getInteger("consumablesPurchased");

        participant.damageDealtToBuildings = json.getInteger("damageDealtToBuildings");
        participant.damageDealtToObjectives = json.getInteger("damageDealtToObjectives");
        participant.damageDealtToTurrets = json.getInteger("damageDealtToTurrets");
        participant.damageSelfMitigated = json.getInteger("damageSelfMitigated");
        participant.magicDamageDealt = json.getInteger("magicDamageDealt");
        participant.magicDamageDealtToChampions = json.getInteger("magicDamageDealtToChampions");
        participant.magicDamageTaken = json.getInteger("magicDamageTaken");
        participant.physicalDamageDealt = json.getInteger("physicalDamageDealt");
        participant.physicalDamageDealtToChampions = json.getInteger("physicalDamageDealtToChampions");
        participant.physicalDamageTaken = json.getInteger("physicalDamageTaken");
        participant.profileIcon = json.getInteger("profileIcon");
        participant.neutralMinionsKilled = json.getInteger("neutralMinionsKilled");
        participant.objectivesStolen = json.getInteger("objectivesStolen");
        participant.objectivesStolenAssists = json.getInteger("objectivesStolenAssists");
        participant.participantId = json.getInteger("participantId");
        participant.timeCCingOthers = json.getInteger("timeCCingOthers");
        participant.longestTimeSpentLiving = json.getInteger("longestTimeSpentLiving");
        participant.visionScore = json.getInteger("visionScore");
        participant.goldEarned = json.getInteger("goldEarned");
        participant.goldSpent = json.getInteger("goldSpent");
        participant.totalDamageDealt = json.getInteger("totalDamageDealt");
        participant.dragonKills = json.getInteger("dragonKills");
        participant.detectorWardsPlaced = json.getInteger("detectorWardsPlaced");
        participant.totalHeal = json.getInteger("totalHeal");
        participant.summonerLevel = json.getInteger("summonerLevel");

        participant.pentaKills = json.getInteger("pentaKills");
        participant.quadraKills = json.getInteger("quadraKills");
        participant.tripleKills = json.getInteger("tripleKills");
        participant.doubleKills = json.getInteger("doubleKills");
        participant.unrealKills = json.getInteger("unrealKills");

        participant.championName = json.getString("championName");
        participant.individualPosition = json.getString("individualPosition");
        participant.lane = json.getString("lane");
        participant.puuid = json.getString("puuid");
        participant.riotIdName = json.getString("riotIdName");
        participant.riotIdTagline = json.getString("riotIdTagline");
        participant.role = json.getString("role");
        participant.summonerId = json.getString("summonerId");
        participant.summonerName = json.getString("summonerName");
        participant.teamPosition = json.getString("teamPosition");

        participant.firstBloodAssist = json.getBoolean("firstBloodAssist");
        participant.firstBloodKill = json.getBoolean("firstBloodKill");
        participant.firstTowerAssist = json.getBoolean("firstTowerAssist");
        participant.firstTowerKill = json.getBoolean("firstTowerKill");
        participant.gameEndedInEarlySurrender = json.getBoolean("gameEndedInEarlySurrender");
        participant.gameEndedInSurrender = json.getBoolean("gameEndedInSurrender");
        participant.teamEarlySurrendered = json.getBoolean("teamEarlySurrendered");
        participant.win = json.getBoolean("win");
        return participant;
    }
    public static ParticipantDTO fromJson(String json){
        if(json == null)
            return null;
        return fromJson(JSONObject.parseObject(json));
    }
}
