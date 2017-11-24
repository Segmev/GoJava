package stephane_karraz_2953046_jade_kadri_295333_go.Logic;

public class GameLogic {

    public void         init(int nbX, int nbY) {
        width = nbX;
        height = nbY;

        currentPlayer = 1;
        teams = new Team[2];
        teams[0] = new Team(1);
        teams[1] = new Team(2);

        stones = new Stone[nbX][nbY];
        for (int i = 0; i < nbX; i++) {
            for (int j = 0; j < nbY; j++) {
                stones[i][j] = new Stone(0, i, j);
            }
        }
    }

    public void         resetGame(int nbX, int nbY) {
        init(nbX, nbY);
    }

    public boolean      isGameEnded() {
        return false;
    }

    public int          getTeamScore(int teamId) {
        return teams[teamId-1].getScore();
    }

    public int          getStoneTeamIdAt(int x, int y) {
        if (isInBounds(x, y))
            return stones[x][y].teamId;
        return (-1);
    }

    public boolean      isPosCanBePlayed(int x, int y) {
        if (!(isInBounds(x, y)) || stones[x][y].teamId > 0)
            return false;
        if (getNbLibertiesAtEmptyPose(currentPlayer, x, y) <= 0) {
            if (!canTakeStone(currentPlayer, x, y)) {
                return false;
            }
        }
        return true;
    }

    public boolean      playPos(int x, int y) {
        if (!(isPosCanBePlayed(x, y)))
            return false;

        stones[x][y].teamId = currentPlayer;
        updateOrCreateGroupWithStone(currentPlayer, x, y);

        currentPlayer = (currentPlayer == 1 ? 2 : 1);
        return true;
    }

    public int          getCurrentPlayer() {
        return currentPlayer;
    }

    // public methods
    // =====================
    // private methods

    private int         getLibertiesAtPos(int teamId, int x, int y) {
        if (isInBounds(x, y) && stones[x][y].teamId == 0) {
            return 1;
        } else if (isInBounds(x, y) && stones[x][y].teamId > 0 && stones[x][y].teamId != teamId) {
            for (StonesGroup group: teams[(teamId == 2) ? 1 : 0].groups) {
                if (group.group.contains(stones[x][y]) && group.liberties.size() > 1) {
                    return group.liberties.size() - 1;
                }
            }
        }
        return 0;
    }

    private int         getNbLibertiesAtEmptyPose(int teamId, int x, int y) {
        int liberties = 0;
        liberties += getLibertiesAtPos(teamId, x-1, y);
        liberties += getLibertiesAtPos(teamId, x+1, y);
        liberties += getLibertiesAtPos(teamId, x, y-1);
        liberties += getLibertiesAtPos(teamId, x, y+1);
        return liberties;
    }

    private boolean     isInBounds(int x, int y) {
        return (!(x < 0 || x >= width || y < 0 ||  y >= height));
    }

    private void        updateGroupLiberties(StonesGroup group, int teamId) {
        group.liberties.clear();
        for(Stone stone: group.group) {
            if (isInBounds(stone.x-1, stone.y) && stones[stone.x-1][stone.y].teamId == 0) {
                if (!(group.liberties.contains(stones[stone.x-1][stone.y])))
                    group.liberties.add(stones[stone.x-1][stone.y]);
            }
            if (isInBounds(stone.x+1, stone.y) && stones[stone.x+1][stone.y].teamId == 0) {
                if (!(group.liberties.contains(stones[stone.x+1][stone.y])))
                    group.liberties.add(stones[stone.x+1][stone.y]);
            }
            if (isInBounds(stone.x, stone.y-1) && stones[stone.x][stone.y-1].teamId == 0) {
                if (!(group.liberties.contains(stones[stone.x][stone.y-1])))
                    group.liberties.add(stones[stone.x][stone.y-1]);
            }
            if (isInBounds(stone.x, stone.y+1) && stones[stone.x][stone.y+1].teamId == 0) {
                if (!(group.liberties.contains(stones[stone.x][stone.y+1])))
                    group.liberties.add(stones[stone.x][stone.y+1]);
            }
        }
    }

    private void        transferStones(StonesGroup newGroup, int teamId, int X, int Y) {
        if (isInBounds(X, Y) && stones[X][Y].teamId == teamId) {
            for (StonesGroup group: teams[teamId - 1].groups) {
                if (group.group.contains(stones[X][Y])) {
                    newGroup.group.addAll(group.group);
                    teams[teamId - 1].groups.remove(group);
                    break ;
                }
            }
        }
    }

    private void        updateOrCreateGroupWithStone(int teamId, int x, int y) {
        StonesGroup newGroup = new StonesGroup();
        transferStones(newGroup, teamId, x-1, y);
        transferStones(newGroup, teamId, x+1, y);
        transferStones(newGroup, teamId, x, y-1);
        transferStones(newGroup, teamId, x, y+1);
        newGroup.group.add(stones[x][y]);
        updateGroupLiberties(newGroup, teamId);
        teams[teamId - 1].groups.add(newGroup);
    }

    StonesGroup         canGroupsBeTaken(int teamId, int x, int y){
        if (isInBounds(x, y) && stones[x][y].teamId > 0 && stones[x][y].teamId != teamId) {
            for (StonesGroup group: teams[(teamId == 1) ? 1 : 0].groups) {
                if (group.group.contains(stones[x][y]) && group.liberties.size() <= 1) {
                    return group;
                }
            }
        }
        return null;
    }

    private boolean     canTakeStone(int teamId, int x, int y) {
        return   ( canGroupsBeTaken(teamId, x-1, y) != null
                || canGroupsBeTaken(teamId, x+1, y) != null
                || canGroupsBeTaken(teamId, x, y-1) != null
                || canGroupsBeTaken(teamId, x, y+1) != null
        );
    }

    private void        takeStonesAround(int teamId, int x, int y) {
        StonesGroup[]   takenGroups = new StonesGroup[4];

        takenGroups[0] = canGroupsBeTaken(teamId, x-1, y);
        takenGroups[1] = canGroupsBeTaken(teamId, x+1, y);
        takenGroups[2] = canGroupsBeTaken(teamId, x, y-1);
        takenGroups[3] = canGroupsBeTaken(teamId, x, y+1);

        for (StonesGroup tgroup : takenGroups) {
            if (tgroup != null) {
                teams[teamId - 1].stonesTaken += takenGroups[0].group.size();
                for (Stone stone : tgroup.group) {
                    stone.teamId = 0;
                }
                teams[teamId].groups.remove(tgroup);
            }
        }
    }

    int                 height, width;
    private int         currentPlayer;   // teamIds => 1 or 2, 0 for not played
    Team[]              teams;

    Stone[][]           stones;

    // testing main
    public static void  main(String args[]) {
        GameLogic game = new GameLogic();
        game.init(9,9);
        System.out.println(game.playPos(5,6));
        System.out.println(game.playPos(4,5));
        System.out.println(game.playPos(4,4));
        System.out.println(game.playPos(5,5));
        System.out.println(game.playPos(6,5));
        System.out.println(game.teams[0].groups.get(0).liberties.size());
    }
}