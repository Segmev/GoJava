package stephane_karraz_2953046_jade_kadri_295333_go.Logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GameLogic {

    public void         init(int nbX, int nbY) {
        width = nbX;
        height = nbY;

        currentPlayer = 1;
        teams = new Team[2];
        teams[0] = new Team(1);
        teams[1] = new Team(2);

        stones = new Stone[nbX][nbY];
        board = new int[nbX][nbY];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                stones[i][j] = new Stone(0, i, j);
                board[i][j] = 0;
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

    public int[][]      getBoard() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board[i][j] = stones[i][j].teamId;
            }
        }
        return board;
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

        takeStonesAround(currentPlayer, x, y);
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

    private int         howManyLibertiesAtDir(int teamId, int x, int y) {
        if (!(isInBounds(x, y)) || (stones[x][y].teamId > 0 && stones[x][y].teamId != teamId))
            return 0;
        if (stones[x][y].teamId == 0)
            return 1;
        for (StonesGroup group: teams[teamId-1].groups) {
            if (group.group.contains(stones[x][y])) {
                updateGroupLiberties(group);
                return group.liberties.size() - 1;
            }
        }
        return (0); // shouldn't happens
    }

    private int         getNbLibertiesAtEmptyPose(int teamId, int x, int y) {
        int liberties = 0;
        liberties += howManyLibertiesAtDir(teamId, x-1, y);
        liberties += howManyLibertiesAtDir(teamId, x+1, y);
        liberties += howManyLibertiesAtDir(teamId, x, y-1);
        liberties += howManyLibertiesAtDir(teamId, x, y+1);
        return liberties;
    }

    private boolean     isInBounds(int x, int y) {
        return (!(x < 0 || x >= width || y < 0 ||  y >= height));
    }

    private void        updateGroupLiberties(StonesGroup group) {
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
        updateGroupLiberties(newGroup);
        teams[teamId - 1].groups.add(newGroup);
    }

    StonesGroup         canGroupsBeTaken(int teamId, int x, int y){
        if (isInBounds(x, y) && stones[x][y].teamId > 0 && stones[x][y].teamId != teamId) {
            for (StonesGroup group: teams[(teamId == 1) ? 1 : 0].groups) {
                if (group.group.contains(stones[x][y])) {
                    updateGroupLiberties(group);
                    if (group.liberties.size() <= 1) {
                        return group;
                    }
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
                teams[teamId - 1].stonesTaken += tgroup.group.size();
                for (Stone stone : tgroup.group) {
                    stone.teamId = 0;
                }
                teams[(teamId == 1 ? 1 : 0)].groups.remove(tgroup);
            }
        }
    }

    int                 height, width;
    private int         currentPlayer;   // teamIds => 1 or 2, 0 for not played
    Team[]              teams;

    Stone[][]           stones;
    int[][]             board;

    void        printBoard() {
        this.getBoard();
        System.out.print("  x    ");
        for (int i = 0; i < height; i++) {
            System.out.print(i + " ");
        }
        System.out.print("\ny\n      ");

        for (int i = 0; i < height; i++) {
            System.out.print(" =");
        }
        System.out.println();

        for (int i = 0; i < height; i++) {
            System.out.print(i + "    |");
            for (int j = 0; j < width; j++) {
                System.out.print(" " + board[j][i]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // testing main
    public static void  main(String args[]) {
        GameLogic game = new GameLogic();
        game.init(7,7);

        /*
        game.playPos(1,0);
        game.playPos(2,0);
        game.playPos(0,1);
        game.playPos(0,2);
        game.playPos(4,5);
        game.playPos(1,1);
        game.playPos(4,6);
        */

        game.printBoard();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int x, y;

        for ( ; ; ) {
            try {
                System.out.print("x: ");
                x = Integer.parseInt(br.readLine());
                System.out.print("y: ");
                y = Integer.parseInt(br.readLine());
                if (!(game.playPos(x, y)))
                    System.out.println("Bad move.");
                game.printBoard();
                System.out.println("score: player_1  " + game.getTeamScore(1) + " --- player_2  " + game.getTeamScore(2));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}