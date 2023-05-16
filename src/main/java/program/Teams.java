package program;

import lol.infos.CurrentGameParticipant;

public class Teams{
    public CurrentGameParticipant[] leftTeam;
    public CurrentGameParticipant[] rightTeam;

    public static Teams split(CurrentGameParticipant[] participants){
        Teams teams = new Teams();
        int leftCount = 0, rightCount = 0;
        long leftId = participants[0].teamId;

        for (CurrentGameParticipant participant : participants){
            if (participant.teamId == leftId){
                leftCount++;
            }else{
                rightCount++;
            }
        }

        teams.leftTeam = new CurrentGameParticipant[leftCount];
        teams.rightTeam = new CurrentGameParticipant[rightCount];
        System.arraycopy(participants, 0, teams.leftTeam, 0, leftCount);
        System.arraycopy(participants, leftCount, teams.rightTeam, 0, rightCount);
        return teams;
    }
}
