package stephane_karraz_2953046_jade_kadri_295333_go.Logic;

public class GameLogic {
    int                 height, width;
    int                 currentPlayer;   // teamIds => 1 or 2, 0 for not played
    Team[]              teams;

    Stone[][]           stones;

    public void         init(int nbX, int nbY) {
        width = nbX;
        height = nbY;

        teams = new Team[2];
        teams[0] = new Team(1);
        teams[1] = new Team(2);

        stones = new Stone[nbX][nbY];

        for (int i = 0; i < nbX; i++) {
            for (int j = 0; j < nbY; j++) {
                stones[i][j] = new Stone(0);
            }
        }
    }

    public boolean      isGameEnded() {
        return false;
    }

    public int          getTeamScore(int teamId) {
        return teams[teamId-1].getScore();
    }

    public void         resetGame(int nbX, int nbY) {
        init(nbX, nbY);
    }

    public int          getStoneTeamIdAt(int x, int y) {
        if (isInBounds(x, y))
            return stones[x][y].teamId;
        return (0);
    }

    public boolean      isPosCanBePlayed(int x, int y) {
        if (!(isInBounds(x, y)))
            return false;
        if (stones[x][y].teamId != 0)
            return false;

        return true;
    }

    public boolean      playPos(int x, int y) {
        return false;
    }

    // public methods
    // =====================
    // private methods

    private boolean     isInBounds(int x, int y) {
        return (!(x < 0 || x >= width || y < 0 ||  y >= height));
    }

    private void        transferStones(StonesGroup newgroup, int teamId, int X, int Y) {
        if (isInBounds(X, Y) && stones[X][Y].teamId == teamId) {
            for (StonesGroup group: teams[teamId - 1].groups) {
                if (group.group.contains(stones[X][Y])) {
                    newgroup.group.addAll(group.group);
                    teams[teamId - 1].groups.remove(group);
                    break ;
                }
            }
        }
    }

    private void        updateLiberties(int teamId, int x, int y) {
        StonesGroup newgroup = new StonesGroup();
        transferStones(newgroup, teamId, x-1, y);
        transferStones(newgroup, teamId, x+1, y);
        transferStones(newgroup, teamId, x, y-1);
        transferStones(newgroup, teamId, x, y+1);
        teams[teamId - 1].groups.add(newgroup);
    }

    private int         countStoneLiberties(int teamId, int x, int y) {
        int             liberties = 4;
        if (!(isInBounds(x-1, y)) || stones[x-1][y].teamId == ((teamId == 1) ? 2 : 1))
            liberties -= 1;
        if (!(isInBounds(x+1, y)) || stones[x+1][y].teamId == ((teamId == 1) ? 2 : 1))
            liberties -= 1;
        if (!(isInBounds(x, y-1)) || stones[x][y-1].teamId == ((teamId == 1) ? 2 : 1))
            liberties -= 1;
        if (!(isInBounds(x, y+1)) || stones[x][y+1].teamId == ((teamId == 1) ? 2 : 1))
            liberties -= 1;
        return liberties;
    }

    private boolean     canTakeStone(int teamId, int x, int y) {
        if (isInBounds(x-1, y) && stones[x-1][y].teamId > 0 && stones[x-1][y].teamId != teamId) {
            for (StonesGroup group: teams[(teamId == 2) ? 1 : 0].groups) {
                if (group.group.contains(stones[x-1][y]) && group.liberties == 1) { return true; }
            }
        }
        if (isInBounds(x+1, y) && stones[x+1][y].teamId > 0 && stones[x+1][y].teamId != teamId) {
            for (StonesGroup group: teams[(teamId == 2) ? 1 : 0].groups) {
                if (group.group.contains(stones[x+1][y]) && group.liberties == 1) { return true; }
            }
        }
        if (isInBounds(x, y-1) && stones[x][y-1].teamId > 0 && stones[x][y-1].teamId != teamId) {
            for (StonesGroup group: teams[(teamId == 2) ? 1 : 0].groups) {
                if (group.group.contains(stones[x][y-1]) && group.liberties == 1) { return true; }
            }
        }
        if (isInBounds(x, y+1) && stones[x][y+1].teamId > 0 && stones[x][y+1].teamId != teamId) {
            for (StonesGroup group: teams[(teamId == 2) ? 1 : 0].groups) {
                if (group.group.contains(stones[x][y+1]) && group.liberties == 1) { return true; }
            }
        }
        return false;
    }

    // testing main
    public static void  main(String args[]) {

    }
}