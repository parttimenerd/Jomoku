package jomoku;

/**
 * Models a black or white stone of this game
 *
 * @see http://en.wikipedia.org/wiki/Five_in_a_Row_%28game%29
 * @author Johannes Bechberger
 */
public class Stone {

    /**
     * Player owning this stone
     */
    public final Player PLAYER;
    /**
     * Position of this stone on the board
     */
    public final Position POSITION;
    /**
     * The board, this stone is set on
     */
    public final Board BOARD;

    /**
     * Constructs a stone object
     *
     * @param player Player owning this stone
     * @param position Position of this stone on the board
     * @param board The board, this stone is set on
     */
    public Stone(Player player, Position position) {
        this.PLAYER = player;
        this.POSITION = position;
        this.BOARD = player.getBoard();
    }

    @Override
    public String toString() {
        return POSITION.toString();
    }
    
    /**
     * Simple position container
     */
    public static class Position {

        /**
         * Column number, 0 is the first column
         */
        public final int COLUMN;
        /**
         * Row number, 0 is the first row
         */
        public final int ROW;

        /**
         * Constructs a position
         *
         * @param column Column number, 0 is the first column
         * @param row Row number, 0 is the first row
         */
        public Position(int column, int row) {
            this.COLUMN = column;
            this.ROW = row;
        }

        /**
         * Does this point lays in the given boundaries?
         *
         * @param number_of_columns
         * @param number_of_rows
         * @return Does this point lays in the given boundaries?
         */
        public boolean isInBounds(int number_of_columns, int number_of_rows) {
            return COLUMN < number_of_columns && ROW < number_of_rows;
        }

        @Override
        public String toString() {
            return COLUMN + "x" + ROW;
        }
    }
}
