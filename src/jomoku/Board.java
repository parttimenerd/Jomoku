package jomoku;

import java.util.ArrayList;

/**
 * Models the playing board of the game, each of the both players has one
 *
 * @see http://en.wikipedia.org/wiki/Five_in_a_Row_%28game%29
 * @author Johannes Bechberger
 */
public class Board {

    /**
     * Array the stones are stored in
     */
    private Stone[][] stones;
    /**
     * Player owning this game
     */
    private Player player;
    /**
     * Game the board lays in
     */
    private Game game;
    /**
     * List of stone positions on the board, on which setting a stone is not
     * allowed.
     */
    private static ArrayList<Stone.Position> blocked_stone_positions = new ArrayList<>();

    /**
     * Constructs a board with the size specified in the game
     *
     * @param game Game
     */
    public Board(Game game, Player player) {
        this.stones = new Stone[game.NUMBER_OF_COLUMNS][game.NUMBER_OF_ROWS];
        this.game = game;
        this.player = player;
    }

    public boolean placeStone(Stone.Position position) {
        if (canStoneBePlacedAtPosition(position)) {
            stones[position.COLUMN][position.ROW] = new Stone(player, position);
            return true;
        }
        return false;
    }

    public boolean isStoneSetAtPosition(Stone.Position position) {
        return position.isInBounds(game.NUMBER_OF_COLUMNS, game.NUMBER_OF_ROWS)
                && stones[position.COLUMN][position.ROW] != null;
    }

    public boolean canStoneBePlacedAtPosition(Stone.Position position) {
        return position.isInBounds(game.NUMBER_OF_COLUMNS, game.NUMBER_OF_ROWS)
                && !isStonePositionBlocked(position) && !isStoneSetAtPosition(position);
    }

    /**
     * Blocks the given position for players to set their stone on.
     *
     * @param position Position being blocked.
     */
    public static void blockStonePosition(Stone.Position position) {
        blocked_stone_positions.add(position);
    }

    /**
     * Is the given position blocked for players to place their stone on?
     *
     * @param position Position in question.
     * @return Is the given position blocked for players to place their stone
     * on?
     */
    public static boolean isStonePositionBlocked(Stone.Position position) {
        return blocked_stone_positions.contains(position);
    }

    /**
     * Returns the stone at the given position.
     *
     * @param position Given position
     * @return Stone on this position or null if there's no stone
     */
    public Stone getStone(Stone.Position position) {
        if (position.isInBounds(game.NUMBER_OF_COLUMNS, game.NUMBER_OF_ROWS)) {
            return stones[position.COLUMN][position.ROW];
        }
        return null;
    }

    /**
     * Does the player owning this board win?
     *
     * @return Does the player owning this board win?
     */
    public boolean doesPlayerWin() {
        if (checkForWinningRow(stones) || checkForWinningRow(getDiagonals(stones))) {
            return true;
        }
        Stone[][] horizontal_row = new Stone[game.NUMBER_OF_ROWS][game.NUMBER_OF_COLUMNS];
        for (int i = 0; i < stones.length; i++) {
            Stone[] row = stones[i];
            for (int j = 0; j < row.length; j++) {
                horizontal_row[j][i] = row[j];
            }
        }
        if (checkForWinningRow(horizontal_row) || checkForWinningRow(getDiagonals(horizontal_row))) {
            return true;
        }
        return false;
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
                if (stone_row_length > max_stone_row_length){
                    max_stone_row_length = stone_row_length;
                }
                stone_row_length = 0;
            }
        }
        return max_stone_row_length >= game.NUMBER_OF_STONES_IN_A_ROW_TO_WIN;
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
            if (checkForWinningRow(rows[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an array of diagonals from top left to bottom right.
     *
     * @param stone_arr Stone array
     * @return array of diagonals
     */
    private Stone[][] getDiagonals(Stone[][] stone_arr) {
        Stone[][] diagonal_arr = new Stone[game.NUMBER_OF_ROWS][game.NUMBER_OF_COLUMNS];
        for (int c_i = 0; c_i < stone_arr.length; c_i++) {
            Stone[] row = stone_arr[c_i];
            for (int r_i = 0; r_i < row.length; r_i++) {
                diagonal_arr[r_i][(c_i + r_i) % game.NUMBER_OF_COLUMNS] = row[r_i];
            }
        }
        return diagonal_arr;
    }
}
