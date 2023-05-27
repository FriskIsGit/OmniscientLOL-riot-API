package lol.dtos;

import com.alibaba.fastjson.JSONObject;

public class Challenges{
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
            wardTakedowns, wardTakedownsBefore20M, wardsGuarded;
    public int unseenRecalls; // always 0
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
        try{
            challenges.laningPhaseGoldExpAdvantage = json.getInteger("laningPhaseGoldExpAdvantage");
            challenges.maxCsAdvantageOnLaneOpponent = json.getInteger("maxCsAdvantageOnLaneOpponent");
            challenges.maxLevelLeadLaneOpponent = json.getInteger("maxLevelLeadLaneOpponent");
            challenges.killParticipation = json.getDouble("killParticipation");
            challenges.teamDamagePercentage = json.getDouble("teamDamagePercentage");
            challenges.visionScoreAdvantageLaneOpponent = json.getDouble("visionScoreAdvantageLaneOpponent");
        }catch (NullPointerException ignored){
        }

        challenges.immobilizeAndKillWithAlly = json.getInteger("immobilizeAndKillWithAlly");
        challenges.knockEnemyIntoTeamAndKill = json.getInteger("knockEnemyIntoTeamAndKill");
        challenges.jungleCsBefore10Minutes = json.getInteger("jungleCsBefore10Minutes");
        challenges.turretTakedowns = json.getInteger("turretTakedowns");
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
        challenges.visionScorePerMinute = json.getDouble("visionScorePerMinute");
        return challenges;
    }
}