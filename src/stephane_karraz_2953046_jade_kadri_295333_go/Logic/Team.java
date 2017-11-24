package stephane_karraz_2953046_jade_kadri_295333_go.Logic;

import java.util.ArrayList;
import java.util.List;

class Team {
    private int         team;
    List<StonesGroup>   groups;

    int                 stonesTaken;
    boolean             passedTurn;

    Team(int team) {
        this.team = team;
        groups = new ArrayList<>();
        passedTurn = false;
    }

    public int          getTeam() {
        return team;
    }

    public int          getScore() {
        int stonesOnBoard = 0;

        for (StonesGroup group : groups) {
            stonesOnBoard += group.group.size();
        }
        return stonesOnBoard + stonesTaken;
    }
}
