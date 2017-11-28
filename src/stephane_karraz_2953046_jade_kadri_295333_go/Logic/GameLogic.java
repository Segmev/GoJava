package stephane_karraz_2953046_jade_kadri_295333_go.Logic;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class GameLogic {

    public void         init(int nbX, int nbY) {
        width = nbX;
        height = nbY;
        end = false;
        territoryPhase = false;

        currentPlayer = 1;
        teams = new Team[2];
        teams[0] = new Team(1);
        teams[1] = new Team(2);

        stones = new LogicStone[nbX][nbY];
        board = new int[nbX][nbY];
        territoryBoard = new int[nbX][nbY];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                stones[i][j] = new LogicStone(0, i, j);
                board[i][j] = 0;
            }
        }
        snapshots = new LinkedList<>();
    }

    public int[][]      territoryRemoveOrAdd(int x, int y) {
        if (!(isInBounds(x, y)) || stones[x][y].teamId == 0)
            return territoryBoard;
        for (LogicStonesGroup group : teams[stones[x][y].teamId - 1].groups) {
            if (group.group.contains(stones[x][y])) {
                System.out.println("GOT THERE");
                for (LogicStone stone: group.group) {
                    territoryBoard[stone.x][stone.y] =
                            (territoryBoard[stone.x][stone.y] == stone.teamId ?
                                    territoryBoard[stone.x][stone.y] + 4
                                    :
                                    stone.teamId
                            );
                    removeTerritoryDirection(stone.x, stone.y, -1, 0, stone.teamId + 2);
                    removeTerritoryDirection(stone.x, stone.y, 1, 0, stone.teamId + 2);
                    removeTerritoryDirection(stone.x, stone.y, 0, -1, stone.teamId + 2);
                    removeTerritoryDirection(stone.x, stone.y, 0, 1, stone.teamId + 2);
                }
                break;
            }
        }
        return getUpdatedTerritory();
    }

    // 0: vide, 1 ou 2: team id: 3 ou 4: territoire vide, 5 ou 6: territoire avec groupe retiré
    public int[][]      getTerritoryBoard() {
        return territoryBoard;
    }

    public void         continueGame() {
        territoryPhase = false;
    }

    public void         resetGame(int nbX, int nbY) {
        init(nbX, nbY);
    }

    public boolean      isGameEnded() {
        return end;
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

    public boolean      posCanBePlayed(int teamId, int x, int y) {
        if (!(isInBounds(x, y)) || stones[x][y].teamId > 0)
            return false;
        if (getNbLibertiesAtEmptyPose(teamId, x, y) <= 0) {
            if (!canTakeStone(teamId, x, y)) {
                return false;
            }
        }
        return true;
    }

    public boolean      playPos(int x, int y) {
        if (end || territoryPhase || !(posCanBePlayed(currentPlayer, x, y)))
            return false;

        takeStonesAround(currentPlayer, x, y);
        stones[x][y].teamId = currentPlayer;

        if (isKo()) {
            restoreLastSnapshot();
            return false;
        }

        updateOrCreateGroupWithStone(currentPlayer, x, y);
        switchPlayers();
        teams[currentPlayer - 1].passedTurn = false;
        checkAnyMovesAvailable(currentPlayer);
        addSnapShot();
        return true;
    }

    public void         passTurn() { // give 0 for current player
        if (teams[(currentPlayer == 1 ? 1 : 0)].passedTurn) {
            territoryPhase = true;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    territoryBoard[i][j] = stones[i][j].teamId;
                }
            }
        }
        teams[currentPlayer - 1].passedTurn = true;
        currentPlayer = (currentPlayer == 1 ? 2 : 1);
    }

    public void         rollbackTurn() {
        if (snapshots.isEmpty())
            return ;
        snapshots.remove(0);
        if (!snapshots.isEmpty())
            restoreLastSnapshot();
        else
            resetGame(width, height);
        switchPlayers();
    }

    public int          getCurrentPlayer() {
        return currentPlayer;
    }

    // public methods
    // =====================
    // private methods

    private boolean     checkAnyMovesAvailable(int teamId) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (posCanBePlayed(teamId, i, j)){
                    return true;
                }
            }
        }
        passTurn();
        return false;
    }

    private int         howManyLibertiesAtDir(int teamId, int x, int y) {
        if (!(isInBounds(x, y)) || (stones[x][y].teamId > 0 && stones[x][y].teamId != teamId))
            return 0;
        if (stones[x][y].teamId == 0)
            return 1;
        for (LogicStonesGroup group: teams[teamId-1].groups) {
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

    private void        updateGroupLiberties(LogicStonesGroup group) {
        group.liberties.clear();
        for(LogicStone stone: group.group) {
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

    private void        transferStones(LogicStonesGroup newGroup, int teamId, int X, int Y) {
        if (isInBounds(X, Y) && stones[X][Y].teamId == teamId) {
            for (LogicStonesGroup group: teams[teamId - 1].groups) {
                if (group.group.contains(stones[X][Y])) {
                    newGroup.group.addAll(group.group);
                    teams[teamId - 1].groups.remove(group);
                    break ;
                }
            }
        }
    }

    private void        updateOrCreateGroupWithStone(int teamId, int x, int y) {
        LogicStonesGroup newGroup = new LogicStonesGroup();
        transferStones(newGroup, teamId, x-1, y);
        transferStones(newGroup, teamId, x+1, y);
        transferStones(newGroup, teamId, x, y-1);
        transferStones(newGroup, teamId, x, y+1);
        newGroup.group.add(stones[x][y]);
        updateGroupLiberties(newGroup);
        teams[teamId - 1].groups.add(newGroup);
    }

    LogicStonesGroup canGroupsBeTaken(int teamId, int x, int y){
        if (isInBounds(x, y) && stones[x][y].teamId > 0 && stones[x][y].teamId != teamId) {
            for (LogicStonesGroup group: teams[(teamId == 1) ? 1 : 0].groups) {
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
        LogicStonesGroup[]   takenGroups = new LogicStonesGroup[4];

        takenGroups[0] = canGroupsBeTaken(teamId, x-1, y);
        takenGroups[1] = canGroupsBeTaken(teamId, x+1, y);
        takenGroups[2] = canGroupsBeTaken(teamId, x, y-1);
        takenGroups[3] = canGroupsBeTaken(teamId, x, y+1);

        for (LogicStonesGroup tgroup : takenGroups) {
            if (tgroup != null) {
                teams[teamId - 1].stonesTaken += tgroup.group.size();
                for (LogicStone stone : tgroup.group) {
                    stone.teamId = 0;
                }
                teams[(teamId == 1 ? 1 : 0)].groups.remove(tgroup);
            }
        }
    }

    private void        addSnapShot() {
        int[][]         snapBoard = new int[width][height];
        int[]           scores = new int[2];

        scores[0] = teams[0].stonesTaken;
        scores[1] = teams[1].stonesTaken;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                snapBoard[i][j] = stones[i][j].teamId;
            }
        }
        snapshots.push(new Pair<>(snapBoard, scores));
    }

    private void        restoreLastSnapshot() {
        if (snapshots.isEmpty())
            return ;
        Pair<int[][], int[]> snap = snapshots.get(0);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                stones[i][j].teamId = snap.getKey()[i][j];
            }
        }
        teams[0].groups.clear();
        teams[1].groups.clear();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (stones[i][j].teamId > 0)
                    updateOrCreateGroupWithStone(stones[i][j].teamId, i, j);
            }
        }
        teams[0].stonesTaken = snap.getValue()[0];
        teams[1].stonesTaken = snap.getValue()[1];
    }

    private boolean     isKo() {
        if (snapshots.size() < 2)
            return false;

        Pair<int[][], int[]> snap = snapshots.get(1);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (stones[i][j].teamId != snap.getKey()[i][j])
                    return false;
            }
        }
        return true;
    }

    private void        switchPlayers() { currentPlayer = (currentPlayer == 1 ? 2 : 1); }

    private boolean     isWallOrTeamStoneAtTheEnd(int x, int y, int direX, int direY, int teamId) {
        while (isInBounds(x + direX, y + direY)) {
            if (territoryBoard[x+direX][y+direY] == teamId)
                return true;
            else if (territoryBoard[x+direX][y+direY] > 0 && territoryBoard[x+direX][y+direY] <= 2) {
                return false;
            }
            direX = (direX == 0 ? 0 : direX + (direX < 0 ? -1 : 1));
            direY = (direY == 0 ? 0 : direY + (direY < 0 ? -1 : 1));
        }
        return true;
    }

    private void        removeTerritoryDirection(int x, int y, int direX, int direY, int territoryId) {
        while (isInBounds(x+direX, y+direY)) {
            if (territoryBoard[x+direX][y+direY] == territoryId) {
                territoryBoard[x+direX][y+direY] = 0;
            } else if (territoryBoard[x+direX][y+direY] <= 2) {
                break;
            }

            direX = (direX == 0 ? 0 : direX + (direX < 0 ? -1 : 1));
            direY = (direY == 0 ? 0 : direY + (direY < 0 ? -1 : 1));
        }
    }

    private void        updateDirection(int x, int y, int direX, int direY, int teamId) {
        while (isInBounds(x+direX, y+direY)) {
            if (territoryBoard[x+direX][y+direY] == 0) {
                territoryBoard[x+direX][y+direY] = teamId + 2;
            } else if (territoryBoard[x+direX][y+direY] <= 2) {
                break;
            }
            direX = (direX == 0 ? 0 : direX + (direX < 0 ? -1 : 1));
            direY = (direY == 0 ? 0 : direY + (direY < 0 ? -1 : 1));
        }
    }

    private int[][]     getUpdatedTerritory() {
        for (int i = 0; i < 2; i++) {
            for (LogicStonesGroup group : teams[i].groups) {
                for (LogicStone stone : group.group) {
                    if (territoryBoard[stone.x][stone.y] <= 2) {
                        if (isWallOrTeamStoneAtTheEnd(stone.x, stone.y, -1, 0, stone.teamId))
                            updateDirection(stone.x, stone.y, -1, 0, stone.teamId);
                        if (isWallOrTeamStoneAtTheEnd(stone.x, stone.y, 1, 0, stone.teamId))
                            updateDirection(stone.x, stone.y, 1, 0, stone.teamId);
                        if (isWallOrTeamStoneAtTheEnd(stone.x, stone.y, 0, -1, stone.teamId))
                            updateDirection(stone.x, stone.y, 0, -1, stone.teamId);
                        if (isWallOrTeamStoneAtTheEnd(stone.x, stone.y, 0, 1, stone.teamId))
                            updateDirection(stone.x, stone.y, 0, 1, stone.teamId);
                    }
                }
            }
        }
        return territoryBoard;
    }

    int                 height, width;
    private int         currentPlayer;   // teamIds => 1 or 2, 0 for not played
    Team[]              teams;

    LogicStone[][]      stones;
    int[][]             board;
    int[][]             territoryBoard;

    //        <board,takenStones>
    LinkedList<Pair<int[][], int[]>> snapshots;

    boolean             end, territoryPhase;


    // testing main
    public static void  main(String args[]) {
        GameLogic game = new GameLogic();
        game.init(5,5);

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
                System.out.println("Game end: " + game.isGameEnded());
                System.out.println("Territory phase: " + game.territoryPhase);
                System.out.println("Player turn: " + game.getCurrentPlayer());
                System.out.print("x: ");
                x = Integer.parseInt(br.readLine());
                if (x == -1) {
                    game.passTurn();
                    game.printBoard();
                    continue;
                }
                if (x == -2) {
                    game.rollbackTurn();
                    game.printBoard();
                    continue;
                }
                System.out.print("y: ");
                y = Integer.parseInt(br.readLine());
                if (!game.territoryPhase) {
                    if (!(game.playPos(x, y)))
                        System.out.println("Bad move.");
                } else {
                    game.territoryRemoveOrAdd(x, y);
                }
                game.printBoard();
                System.out.println("score: player_1  " + game.getTeamScore(1) + " --- player_2  " + game.getTeamScore(2));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
    private void        printBoard() {
        int[][] boar;
        if (territoryPhase)
            boar = getUpdatedTerritory();
        else
            boar = board;

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
                System.out.print(" " + boar[j][i]);
            }
            System.out.println();
        }
        System.out.println();
    }
}