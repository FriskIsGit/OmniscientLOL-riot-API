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
            turretsLost, visionScore, visionWardsBoughtInGame, wardsKilled, wardsPlaced, controlWardsPlaced;
    public int allInPings, commandPings, dangerPings, enemyMissingPings, enemyVisionPings, getBackPings, holdPings;
    public boolean firstBloodAssist, firstBloodKill, firstTowerAssist, firstTowerKill,
            gameEndedInEarlySurrender, gameEndedInSurrender, teamEarlySurrendered, win;
    public String championName, individualPosition, lane, puuid, riotIdName, riotIdTagline, role,
            summonerId, summonerName, teamPosition;
    public Challenges challenges;

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
        participant.totalMinionsKilled = json.getInteger("totalMinionsKilled");
        participant.totalTimeCCDealt = json.getInteger("totalTimeCCDealt");
        participant.wardsKilled = json.getInteger("wardsKilled");
        participant.wardsPlaced = json.getInteger("wardsPlaced");
        participant.trueDamageTaken = json.getInteger("trueDamageTaken");
        participant.trueDamageDealt = json.getInteger("trueDamageDealt");
        participant.trueDamageDealtToChampions = json.getInteger("trueDamageDealtToChampions");

        participant.pentaKills = json.getInteger("pentaKills");
        participant.quadraKills = json.getInteger("quadraKills");
        participant.tripleKills = json.getInteger("tripleKills");
        participant.doubleKills = json.getInteger("doubleKills");
        participant.unrealKills = json.getInteger("unrealKills");

        participant.allInPings = json.getInteger("allInPings");
        participant.commandPings = json.getInteger("commandPings");
        participant.dangerPings = json.getInteger("dangerPings");
        participant.enemyMissingPings = json.getInteger("enemyMissingPings");
        participant.enemyVisionPings = json.getInteger("enemyVisionPings");
        participant.getBackPings = json.getInteger("getBackPings");
        participant.holdPings = json.getInteger("holdPings");

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

        participant.challenges = Challenges.from(json.getJSONObject("challenges"));
        return participant;
    }
    public static ParticipantDTO fromJson(String json){
        if(json == null)
            return null;
        return fromJson(JSONObject.parseObject(json));
    }
}

class Challenges{
    public int abilityUses, acesBefore15Minutes, alliedJungleMonsterKills, baronTakedowns, blastConeOppositeOpponentCount,
            bountyGold, buffsStolen, completeSupportQuestInTime, dancedWithRiftHerald,
            deathsByEnemyChamps, dodgeSkillShotsSmallWindow, doubleAces, dragonTakedowns,
            earlyLaningPhaseGoldExpAdvantage, elderDragonKillsWithOpposingSoul, elderDragonMultikills,
            enemyChampionImmobilizations, enemyJungleMonsterKills, epicMonsterKillsNearEnemyJungler,
            epicMonsterKillsWithin30SecondsOfSpawn, epicMonsterSteals, epicMonsterStolenWithoutSmite, firstTurretKilled,
            flawlessAces, fullTeamTakedown, getTakedownsInAllLanesEarlyJungleAsLaner, hadOpenNexus,
            immobilizeAndKillWithAlly, initialBuffCount, initialCrabCount,
            jungleCsBefore10Minutes, junglerTakedownsNearDamagedEpicMonster, kTurretsDestroyedBeforePlatesFall,
            killAfterHiddenWithAlly, killedChampTookFullTeamDamageSurvived, killingSprees,
            killsNearEnemyTurret, killsOnOtherLanesEarlyJungleAsLaner, killsOnRecentlyHealedByAramPack,
            killsUnderOwnTurret, killsWithHelpFromEpicMonster, knockEnemyIntoTeamAndKill, landSkillShotsEarlyGame,
            laneMinionsFirst10Minutes, laningPhaseGoldExpAdvantage, legendaryCount, lostAnInhibitor,
            maxCsAdvantageOnLaneOpponent, maxKillDeficit, maxLevelLeadLaneOpponent, mejaisFullStackInTime,
            moreEnemyJungleThanOpponent, multiKillOneSpell, multiTurretRiftHeraldCount, multikills,
            multikillsAfterAggressiveFlash, mythicItemUsed, outerTurretExecutesBefore10Minutes, outnumberedKills,
            outnumberedNexusKill, perfectDragonSoulsTaken, perfectGame, pickKillWithAlly, poroExplosions,
            quickCleanse, quickFirstTurret, quickSoloKills, riftHeraldTakedowns, saveAllyFromDeath, scuttleCrabKills,
            skillshotsDodged, skillshotsHit, snowballsHit, soloBaronKills, soloKills, stealthWardsPlaced,
            survivedSingleDigitHpCount, survivedThreeImmobilizesInFight, takedownOnFirstTurret, takedowns,
            takedownsAfterGainingLevelAdvantage, takedownsBeforeJungleMinionSpawn, takedownsFirstXMinutes,
            takedownsInAlcove, takedownsInEnemyFountain, teamBaronKills, teamElderDragonKills, teamRiftHeraldKills,
            threeWardsOneSweeperCount, tookLargeDamageSurvived, turretPlatesTaken, turretTakedowns,
            turretsTakenWithRiftHerald, twentyMinionsIn3SecondsCount,
            unseenRecalls, wardTakedowns, wardTakedownsBefore20M, wardsGuarded;
    public double controlWardTimeCoverageInRiverOrEnemyHalf, damagePerMinute, damageTakenOnTeamPercentage,
            effectiveHealAndShielding, gameLength, goldPerMinute, kda, killParticipation,
            teamDamagePercentage, visionScoreAdvantageLaneOpponent, visionScorePerMinute;

    public static Challenges from(JSONObject json){
        if(json == null)
            return null;
        Challenges challenges = new Challenges();
        challenges.abilityUses = json.getInteger("abilityUses");
        challenges.acesBefore15Minutes = json.getInteger("acesBefore15Minutes");
        challenges.alliedJungleMonsterKills = json.getInteger("alliedJungleMonsterKills");

        challenges.epicMonsterSteals = json.getInteger("epicMonsterSteals");
        challenges.dragonTakedowns = json.getInteger("dragonTakedowns");
        challenges.laningPhaseGoldExpAdvantage = json.getInteger("laningPhaseGoldExpAdvantage");
        challenges.immobilizeAndKillWithAlly = json.getInteger("immobilizeAndKillWithAlly");
        challenges.knockEnemyIntoTeamAndKill = json.getInteger("knockEnemyIntoTeamAndKill");
        challenges.jungleCsBefore10Minutes = json.getInteger("jungleCsBefore10Minutes");
        challenges.turretTakedowns = json.getInteger("turretTakedowns");
        challenges.maxCsAdvantageOnLaneOpponent = json.getInteger("maxCsAdvantageOnLaneOpponent");
        challenges.saveAllyFromDeath = json.getInteger("saveAllyFromDeath");
        challenges.mejaisFullStackInTime = json.getInteger("mejaisFullStackInTime");
        challenges.perfectGame = json.getInteger("perfectGame");
        challenges.soloKills = json.getInteger("soloKills");
        challenges.skillshotsHit = json.getInteger("skillshotsHit");
        challenges.skillshotsDodged = json.getInteger("skillshotsDodged");
        challenges.turretPlatesTaken = json.getInteger("turretPlatesTaken");
        challenges.unseenRecalls = json.getInteger("unseenRecalls");
        challenges.wardTakedowns = json.getInteger("wardTakedowns");
        challenges.wardsGuarded = json.getInteger("wardsGuarded");

        challenges.damagePerMinute = json.getDouble("damagePerMinute");
        challenges.damageTakenOnTeamPercentage = json.getDouble("damageTakenOnTeamPercentage");
        challenges.effectiveHealAndShielding = json.getDouble("effectiveHealAndShielding");
        challenges.gameLength = json.getDouble("gameLength");
        challenges.goldPerMinute = json.getDouble("goldPerMinute");
        challenges.kda = json.getDouble("kda");
        challenges.killParticipation = json.getDouble("killParticipation");
        challenges.teamDamagePercentage = json.getDouble("teamDamagePercentage");
        challenges.visionScoreAdvantageLaneOpponent = json.getDouble("visionScoreAdvantageLaneOpponent");
        challenges.visionScorePerMinute = json.getDouble("visionScorePerMinute");
        return challenges;
    }
}