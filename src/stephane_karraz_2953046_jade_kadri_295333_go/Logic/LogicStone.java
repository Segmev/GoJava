package stephane_karraz_2953046_jade_kadri_295333_go.Logic;

public class LogicStone {
    int     teamId;
    int     x, y;

    public LogicStone(int teamId, int x, int y) {
        this.teamId = teamId;
        this.x = x;
        this.y = y;
    }

    int     getTeam() { return teamId; }

    int     getX() { return x; }

    int     getY() { return y; }
}

