package jomoku;

/**
 * Models a black or white stone of this game.
 *
 * @see http://en.wikipedia.org/wiki/Five_in_a_Row_%28game%29
 * @author Johannes Bechberger
 * @version 1.0
 */
public class Stone {

    /**
     * Player owning this stone.
     */
    private Player player;
    /**
     * Position of this stone on the board.
     */
    private Position position;
    /**
     * The board, this stone is set on.
     */
    private Board board;

    /**
     * Constructs a stone object.
     *
     * @param player Player owning this stone
     * @param position Position of this stone on the board
     */
    public Stone(Player player, Position position) {
        this.player = player;
        this.position = position;
        this.board = player.getBoard();
    }

    @Override
    public String toString() {
        return position.toString();
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @return the board
     */
    public Board getBoard() {
        return board;
    }
    
    /**
     * Simple position container.
     */
    public static class Position {

        /**
         * Column number, 0 is the first column.
         */
        private int column;
        /**
         * Row number, 0 is the first row.
         */
        private int row;

        /**
         * Constructs a position.
         *
         * @param column Column number, 0 is the first column
         * @param row Row number, 0 is the first row
         */
        public Position(int column, int row) {
            this.column = column;
            this.row = row;
        }

        /**
         * Does this point lays in the given boundaries?
         *
         * @param numberOfColumns Number of columns of the boundary
         * @param numberOfRows Number of rows of the boundary
         * @return Does this point lays in the given boundaries?
         */
        public boolean isInBounds(int numberOfColumns, int numberOfRows) {
            return column < numberOfColumns && row < numberOfRows;
        }

        @Override
        public String toString() {
            return column + "x" + row;
        }

        /**
         * @return the column
         */
        public int getColumn() {
            return column;
        }

        /**
         * @return the row
         */
        public int getRow() {
            return row;
        }
        
        /**
         * 
         * @return the column
         */
        public int getX(){
            return column;
        }
        
        /**
         * @return the row
         */
        public int getY() {
            return row;
        }
    }
}
