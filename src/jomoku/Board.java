package jomoku;

import java.util.ArrayList;

/**
 * Models the playing board of the game, each of the both players has one.
 *
 * @see http://en.wikipedia.org/wiki/Five_in_a_Row_%28game%29
 * @author Johannes Bechberger
 * @version 1.0
 */
public class Board {

    /**
     * List of stone positions on the board, on which setting a stone is not
     * allowed.
     */
    private static ArrayList<Stone.Position> blockedStonePositions = new ArrayList<>();
    private static int numberOfFreeFields;
    private static boolean numberOfFreeFieldsInitialized = false;
    /**
     * Array the stones are stored in.
     */
    private Stone[][] stones;
    /**
     * Player owning this game.
     */
    private Player player;
    /**
     * Game the board lays in.
     */
    private Game game;

    /**
     * Constructs a board with the size specified in the game.
     *
     * @param game Game
     * @param player Player owning this board.
     */
    public Board(Game game, Player player) {
        this.stones = new Stone[game.getNumberOfColumns()][game.getNumberOfRows()];
        if (!numberOfFreeFieldsInitialized) {
            numberOfFreeFields += game.getNumberOfColumns() * game.getNumberOfRows();
            numberOfFreeFieldsInitialized = true;
        }
        this.game = game;
        this.player = player;
    }

    /**
     * Places a stone at the given position.
     *
     * @param position Given position
     * @return Could the stone be placed at the given position?
     */
    public boolean placeStone(Stone.Position position) {
        if (canStoneBePlacedAtPosition(position)) {
            stones[position.getColumn()][position.getRow()] = new Stone(player, position);
            numberOfFreeFields -= 1;
            return true;
        }
        return false;
    }

    /**
     * Is a stone already set at the given position?
     *
     * @param position Given position
     * @return Is a stone already set at the given position?
     */
    public boolean isStoneSetAtPosition(Stone.Position position) {
        return position.isInBounds(game.getNumberOfColumns(), game.getNumberOfRows())
                && stones[position.getColumn()][position.getRow()] != null;
    }

    /**
     * Can a stone be placed at the given position?
     *
     * @param position Given position
     * @return Can a stone be placed at the given position?
     */
    public boolean canStoneBePlacedAtPosition(Stone.Position position) {
        return position.isInBounds(game.getNumberOfColumns(), game.getNumberOfRows())
                && !isStonePositionBlocked(position) && !isStoneSetAtPosition(position);
    }

    /**
     * Blocks the given position for players to set their stone on.
     *
     * @param position Position being blocked.
     */
    public static void blockStonePosition(Stone.Position position) {
        blockedStonePositions.add(position);
        numberOfFreeFields -= 1;
    }

    /**
     * Is the given position blocked for players to place their stone on?
     *
     * @param position Position in question.
     * @return Is the given position blocked for players to place their stone
     * on?
     */
    public static boolean isStonePositionBlocked(Stone.Position position) {
        return blockedStonePositions.contains(position);
    }

    /**
     * Returns the number of fields on the board on which a stone can be placed
     * on and on which no stone is being placed.
     *
     * @return number of free fields
     */
    public static int getFreeFields() {
        return numberOfFreeFields;
    }

    /**
     * Returns the stone at the given position.
     *
     * @param position Given position
     * @return Stone on this position or null if there's no stone
     */
    public Stone getStone(Stone.Position position) {
        if (position.isInBounds(game.getNumberOfColumns(), game.getNumberOfRows())) {
            return stones[position.getColumn()][position.getRow()];
        }
        return null;
    }

    /**
     * Does the player owning this board win?
     *
     * @return Does the player owning this board win?
     */
    public boolean doesPlayerWin() {
        if (checkForWinningRow(stones) || checkForWinningDiagonal(stones)) {
            return true;
        }
        Stone[][] horizontal_row = new Stone[game.getNumberOfRows()][game.getNumberOfColumns()];
        for (int i = 0; i < stones.length; i++) {
            Stone[] row = stones[i];
            for (int j = 0; j < row.length; j++) {
                horizontal_row[j][i] = row[j];
            }
        }
        return checkForWinningRow(horizontal_row);
    }

    /**
     * Checks the row whether or not it inherit an unbroken row of stones being
     * at minimum as long as needed (as specified in the Game class) for the
     * player owning this board to win the game.
     *
     * @param row row to be checked
     * @return Whether or not this row inherits a winning row of stones.
     */
    private boolean checkForWinningRow(Stone[] row) {
        int max_stone_row_length = 0;
        int stone_row_length = 0;
        for (int i = 0; i < row.length; i++) {
            if (row[i] != null) {
                stone_row_length += 1;
            } else {
                if (stone_row_length > max_stone_row_length) {
                    max_stone_row_length = stone_row_length;
                }
                stone_row_length = 0;
            }
        }
        return max_stone_row_length >= game.getNumberOfStonesInARowToWin();
    }

    /**
     * Checks the row array whether or not own of its containing rows it inherit
     * an unbroken row of stones being at minimum as long as needed (as
     * specified in the Game class) for the player owning this board to win the
     * game.
     *
     * @param row row array to be checked
     * @return Whether or not this row inherits a winning row of stones.
     */
    private boolean checkForWinningRow(Stone[][] rows) {
        for (int i = 0; i < rows.length; i++) {
            Stone[] row = rows[i];
            int max_stone_row_length = 0;
            int stone_row_length = 0;
            for (int j = 0; j < row.length; j++) {
                if (row[j] != null) {
                    stone_row_length += 1;
                } else {
                    if (stone_row_length > max_stone_row_length) {
                        max_stone_row_length = stone_row_length;
                    }
                    stone_row_length = 0;
                }
            }
            if (max_stone_row_length >= game.getNumberOfStonesInARowToWin()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForWinningDiagonal(Stone[][] rows) {
        for (int i = 0; i < rows.length; i++) {
            if (checkForWinningDiagonal(rows, i, 0)) {
                return true;
            }
        }
        for (int i = 0; i < rows[0].length; i++) {
            if (checkForWinningDiagonal(rows, 0, i)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForWinningDiagonal(Stone[][] array, int start_column, int start_row) {
        int columns = array.length;
        int rows = array[0].length;
        int max_stone_row_length = 0;
        int stone_row_length = 0;
        int max_stone_row_length2 = 0;
        int stone_row_length2 = 0;
        for (int i = 0; i + start_column < columns && i + start_row < rows; i++) {
            if (array[start_column + i][start_row + i] != null) {
                stone_row_length += 1;
            } else {
                if (stone_row_length > max_stone_row_length) {
                    max_stone_row_length = stone_row_length;
                }
                stone_row_length = 0;
            }
            if (array[rows - start_column - i - 1][start_row + i] != null) {
                stone_row_length2 += 1;
            } else {
                if (stone_row_length2 > max_stone_row_length2) {
                    max_stone_row_length2 = stone_row_length2;
                }
                stone_row_length2 = 0;
            }
        }
        return Math.max(Math.max(max_stone_row_length, stone_row_length),
                Math.max(max_stone_row_length2, stone_row_length2)) >= game.getNumberOfStonesInARowToWin();
    }

    /**
     * Returns a two dimensional array representing the current board. True in a
     * field means that there's a stone.
     *
     * @return two dimensional array
     */
    public boolean[][] getSimpleStoneArray() {
        boolean[][] arr = new boolean[stones.length][stones[0].length];
        for (int i = 0; i < stones.length; i++) {
            Stone[] row = stones[i];
            for (int j = 0; j < row.length; j++) {
                arr[i][j] = row[j] != null;
            }
        }
        return arr;
    }

    /**
     * Returns a two dimensional array representing the current board. 1 means
     * that there's a stone, 0 the opposite and -2^31 that there can no stone be
     * placed.
     *
     * @return two dimensional array
     */
    public int[][] getSimpleStoneIntArray() {
        int[][] arr = new int[stones.length][stones[0].length];
        for (int i = 0; i < stones.length; i++) {
            Stone[] row = stones[i];
            for (int j = 0; j < row.length; j++) {
                if (row[j] != null) {
                    arr[i][j] = 1;
                } else if (isStonePositionBlocked(new Stone.Position(i, j))) {
                    arr[i][j] = Integer.MIN_VALUE;
                } else {
                    arr[i][j] = 0;
                }
            }
        }
        return arr;
    }

    /**
     *
     * @return number of columns of this board
     */
    public int getNumberOfColumns() {
        return game.getNumberOfColumns();
    }

    /**
     *
     * @return number of row of this board
     */
    public int getNumberOfRows() {
        return game.getNumberOfRows();
    }

    /**
     *
     * @return the game this board belongs to
     */
    public Game getGame() {
        return game;
    }

    /**
     * Resets the number of free fields counter.
     *
     * @param game the game the boards are belonging to
     */
    public static void resetNumberOfFreeFields(Game game) {
        numberOfFreeFields += game.getNumberOfColumns() * game.getNumberOfRows();
    }

    /**
     * Returns a two dimensional array representing a board part with the
     * specified size. There's the value true in this matrix where the field is
     * blocked and false where it isn't.
     *
     * @param columns number of columns of the board part
     * @param rows number of rows of the board part
     * @return two dimensional array
     */
    public static boolean[][] getIsBlockedStoneArray(int columns, int rows) {
        boolean[][] arr = new boolean[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                arr[i][j] = false;
            }
        }
        for (int i = 0; i < blockedStonePositions.size(); i++) {
            Stone.Position position = blockedStonePositions.get(i);
            if (position.isInBounds(columns, rows)) {
                arr[position.getColumn()][position.getRow()] = true;
            }
        }
        return arr;
    }
}
