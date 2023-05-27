package program.structs;

import lol.infos.CurrentGameParticipant;

public class GameTeams{
    public CurrentGameParticipant[] leftTeam;
    public CurrentGameParticipant[] rightTeam;

    public static GameTeams split(CurrentGameParticipant[] participants){
        GameTeams gameTeams = new GameTeams();
        int leftCount = 0, rightCount = 0;
        long leftId = participants[0].teamId;

        for (CurrentGameParticipant participant : participants){
            if (participant.teamId == leftId){
                leftCount++;
            }else{
                rightCount++;
            }
        }

        gameTeams.leftTeam = new CurrentGameParticipant[leftCount];
        gameTeams.rightTeam = new CurrentGameParticipant[rightCount];
        System.arraycopy(participants, 0, gameTeams.leftTeam, 0, leftCount);
        System.arraycopy(participants, leftCount, gameTeams.rightTeam, 0, rightCount);
        return gameTeams;
    }
}
