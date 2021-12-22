import java.awt.Point;
/**
 * MazeWalker is the object that is responsible for staking out a path down some
 * maze. Given a 2D array of maze cells and a starting location, it calculates
 * the next "legal" move such that the walker can eventually cover every square
 * in the maze that is reachable from that starting location.
 */

public class MazeWalker {

    /**
     * The possible states of the current "walk" through a maze.
     */
    public enum WalkerState {

        /**
         * Indicates that the maze walker has reached its assigned destination.
         */
        THERE_ALREADY,

        /**
         * Indicates that the maze walker has concluded that it is impossible to
         * reach its destination.
         */
        IMPOSSIBLE_TO_GET_THERE,

        /**
         * Indicates that the maze walker would like to move left.
         */
        MOVE_LEFT,

        /**
         * Indicates that the maze walker would like to move up.
         */
        MOVE_UP,

        /**
         * Indicates that the maze walker would like to move right.
         */
        MOVE_RIGHT,

        /**
         * Indicates that the maze walker would like to move down.
         */
        MOVE_DOWN
    }

    /**
     * The data structure for maintaining the current path.
     */
    private WalkerState[] path;

    /**
     * The index for the current node in the path.
     */
    private int pathIndex;

    /**
     * The data structure for keeping track of "passed" squares.
     */
    private boolean[][] beenThere;

    /**
     * The maze that is being walked.
     */
    private Maze maze;

    /**
     * The column of the walker's destination.
     */
    private int destinationColumn;

    /**
     * The row of the walker's destination.
     */
    private int destinationRow;

    private int backtracking = 0;

    /**
     * Initializes the MazeWalker, providing it with the maze to use and the
     * walker's destination.
     */
    public MazeWalker(Maze maze, int destinationRow, int destinationColumn) {
        this.maze = maze;
        this.destinationRow = destinationRow;
        this.destinationColumn = destinationColumn;

        // The path stack starts out empty.
        path = new WalkerState[this.maze.getMazeWidth() * this.maze.getMazeHeight()];
        pathIndex = -1;

        // The "been-there" array starts off completely clear.
        beenThere = new boolean[this.maze.getMazeHeight()][this.maze.getMazeWidth()];
        for (int row = 0; row < beenThere.length; row++) {
            for (int column = 0; column < beenThere[row].length; column++) {
                beenThere[row][column] = false;
            }
        }
    }

    /**
     * Takes a step toward reaching the given destination from the given current
     * location, and returns either the direction of the next step, whether or
     * not that destination has been reached, or whether that destination is
     * impossible to reach. Directions are in this oder: left, up, right, down.
     * As an optional extension, pick a direction with a customizable approach.
     */
    public WalkerState areWeThereYet(int currentRow, int currentColumn) {
        pathIndex++;
        beenThere[currentRow][currentColumn] = true;

        if (destinationColumn == currentColumn && destinationRow == currentRow) {
            return WalkerState.THERE_ALREADY;
        }
        Maze.Location mouse = maze.getLocation(currentRow, currentColumn);

        if (mouse.getLeft().isOpen() && !beenThere[mouse.getLeft().getRow()][mouse.getLeft().getColumn()]) {
            backtracking = 0;
            path[pathIndex] = WalkerState.MOVE_LEFT;
            return WalkerState.MOVE_LEFT;
        }
        if (mouse.getAbove().isOpen() && !beenThere[mouse.getAbove().getRow()][mouse.getAbove().getColumn()]) {
            backtracking = 0;
            path[pathIndex] = WalkerState.MOVE_UP;
            return WalkerState.MOVE_UP;
        }
        if (mouse.getRight().isOpen() && !beenThere[mouse.getRight().getRow()][mouse.getRight().getColumn()]) {
            backtracking = 0;
            path[pathIndex] = WalkerState.MOVE_RIGHT;
            return WalkerState.MOVE_RIGHT;
        }
        if (mouse.getBelow().isOpen() && !beenThere[mouse.getBelow().getRow()][mouse.getBelow().getColumn()]) {
            backtracking = 0;
            path[pathIndex] = WalkerState.MOVE_DOWN;
            return WalkerState.MOVE_DOWN;
        }
        if (pathIndex > 1 && path[pathIndex - 1] != null) {
            backtracking++;
            switch (path[pathIndex - backtracking]) {
                case MOVE_LEFT:
                    if (mouse.getRight().isOpen()) {
                        path[pathIndex] = WalkerState.MOVE_RIGHT;
                        return WalkerState.MOVE_RIGHT;
                    }
                    break;
                case MOVE_UP:
                    if (mouse.getBelow().isOpen()) {
                        path[pathIndex] = WalkerState.MOVE_DOWN;
                        return WalkerState.MOVE_DOWN;
                    }
                    break;
                case MOVE_RIGHT:
                    if (mouse.getLeft().isOpen()) {
                        path[pathIndex] = WalkerState.MOVE_LEFT;
                        return WalkerState.MOVE_LEFT;
                    }
                    break;
                case MOVE_DOWN:
                    if (mouse.getAbove().isOpen()) {
                        path[pathIndex] = WalkerState.MOVE_UP;
                        return WalkerState.MOVE_UP;
                    }
                    break;
                default:
                    break;
            }
        }
        
        return WalkerState.IMPOSSIBLE_TO_GET_THERE;
    }

    /**
     * Returns a representation of the locations which the walker has visited.
     * The 2D array's dimensions should correspond to those of the walker's
     * assigned maze.
     */
    public boolean[][] getBeenThere() {
        return beenThere;
    }

    /**
     * Returns the current path taken by the walker.
     */
    public WalkerState[] getCurrentPath() {
        WalkerState[] currentPath = new WalkerState[pathIndex + 1];
        for (int i = 0; i < pathIndex; i++) {
            currentPath[i] = path[i];
        }
        return currentPath;
    }
}
