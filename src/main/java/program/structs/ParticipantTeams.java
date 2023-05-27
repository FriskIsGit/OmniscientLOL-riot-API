package program.structs;

import lol.dtos.ParticipantDTO;

//almost identical to GameTeams
public class ParticipantTeams{
    public ParticipantDTO[] leftTeam;
    public ParticipantDTO[] rightTeam;

    public static ParticipantTeams split(ParticipantDTO[] participants){
        ParticipantTeams teams = new ParticipantTeams();
        int leftCount = 0, rightCount = 0;
        long leftId = participants[0].teamId;

        for (ParticipantDTO participant : participants){
            if (participant.teamId == leftId){
                leftCount++;
            }else{
                rightCount++;
            }
        }

        teams.leftTeam = new ParticipantDTO[leftCount];
        teams.rightTeam = new ParticipantDTO[rightCount];
        System.arraycopy(participants, 0, teams.leftTeam, 0, leftCount);
        System.arraycopy(participants, leftCount, teams.rightTeam, 0, rightCount);
        return teams;
    }
}
