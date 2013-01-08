package jomoku;

import java.awt.Color;

/**
 * Models the game managing the two players and the board with stones,
 * encapsulates it.
 *
 * @see http://en.wikipedia.org/wiki/Five_in_a_Row_%28game%29
 * @author Johannes Bechberger
 * @version 1.0
 */
public class Game {

    /**
     * Default minimum number of stones a player has to place in a row to win.
     *
     * @see http://en.wikipedia.org/wiki/Five_in_a_Row_%28game%29
     */
    public static final int DEFAULT_NUMBER_OF_STONES_IN_A_ROW_TO_WIN = 5;
    /**
     * Minimum number of stones a player has to place in a row to win.
     *
     * @see http://en.wikipedia.org/wiki/Five_in_a_Row_%28game%29
     */
    private int numberOfStonesInARowToWin;
    /**
     * Number of columns of the board being played on.
     */
    private int numberOfColumns;
    /**
     * Number of rows of the board being played on.
     */
    private int numberOfRows;
    /**
     * White player.
     */
    private Player whitePlayer;
    /**
     * Black player.
     */
    private Player blackPlayer;
    /**
     * Board of the white player.
     */
    private Board whitePlayerBoard;
    /**
     * Board of the black player.
     */
    private Board blackPlayerBoard;
    /**
     * Player being able to set stones for the white and the black player.
     */
    private Player joker;

    /**
     * Constructs a game.
     *
     * @param numberOfColums Number of columns of the board being played on.
     * @param numberOfRows Number of rows of the board being played on.
     * @param numberOfStonesInARowToWin Minimum number of stones a player. has
     * to place in a row to win
     */
    public Game(int numberOfColums, int numberOfRows, int numberOfStonesInARowToWin) {
        this.numberOfColumns = numberOfColums;
        this.numberOfRows = numberOfRows;
        this.numberOfStonesInARowToWin = numberOfStonesInARowToWin;
        this.whitePlayer = new Player(this, Player.PlayerType.WHITE);
        this.blackPlayer = new Player(this, Player.PlayerType.BLACK);
        this.whitePlayerBoard = whitePlayer.getBoard();
        this.blackPlayerBoard = blackPlayer.getBoard();
        this.joker = new Player(this, Player.PlayerType.BOTH);
    }

    /**
     * Constructs a game.
     *
     * @param NUMBER_OF_COLUMNS Number of columns of the board being played on.
     * @param NUMBER_OF_ROWS Number of rows of the board being played on.
     */
    public Game(int NUMBER_OF_COLUMNS, int NUMBER_OF_ROWS) {
        this.numberOfColumns = NUMBER_OF_COLUMNS;
        this.numberOfRows = NUMBER_OF_ROWS;
        this.numberOfStonesInARowToWin = DEFAULT_NUMBER_OF_STONES_IN_A_ROW_TO_WIN;
        this.whitePlayer = new Player(this, Player.PlayerType.WHITE);
        this.blackPlayer = new Player(this, Player.PlayerType.BLACK);
        this.whitePlayerBoard = whitePlayer.getBoard();
        this.blackPlayerBoard = blackPlayer.getBoard();
        this.joker = new Player(this, Player.PlayerType.BOTH);
    }

    /**
     * Blocks the given position for players to set their stone on.
     *
     * @param position Position of the stone
     */
    public void blockStonePosition(Stone.Position position) {
        Board.blockStonePosition(position);
    }

    /**
     * Blocks the given positions for players to set their stone on.
     *
     * @param positions Positions of the stones
     */
    public void blockStonePositions(Stone.Position[] positions) {
        for (Stone.Position position : positions) {
            Board.blockStonePosition(position);
        }
    }

    /**
     * Places a joker stone (a stone counting for the black and the white
     * player) at this position.
     *
     * @param position Position of the stone
     * @return false if the stone can't be set a this position, true otherwise.
     */
    public boolean placeJokerStone(Stone.Position position) {
        if (blackPlayerBoard.canStoneBePlacedAtPosition(position)
                && whitePlayerBoard.canStoneBePlacedAtPosition(position)) {
            whitePlayerBoard.placeStone(position);
            blackPlayerBoard.placeStone(position);
            return true;
        }
        return false;
    }

    /**
     * Places joker stones (a joker stone is a stone counting for the black and
     * the white player) at the given positions.
     *
     * @param positions Positions of the stones
     */
    public void placeJokerStones(Stone.Position[] positions) {
        for (Stone.Position position : positions) {
            placeJokerStone(position);
        }
    }

    /**
     * Places a joker stone (a stone counting for the black and the white
     * player) at this position.
     *
     * @param player the player placing the stone
     * @param position Position of the stone
     * @return false if the stone can't be set a this position, true otherwise.
     */
    public boolean placeStone(Player player, Stone.Position position) {
        Board board = player.isWhite() ? whitePlayerBoard : blackPlayerBoard;
        if (canStoneBePlacedAtPosition(position)) {
            return board.placeStone(position);
        }
        return false;
    }

    /**
     * Can a stone be placed at the given position?
     *
     * @param position Given position
     * @return Can a stone be placed at the given position?
     */
    public boolean canStoneBePlacedAtPosition(Stone.Position position) {
        return whitePlayerBoard.canStoneBePlacedAtPosition(position)
                && blackPlayerBoard.canStoneBePlacedAtPosition(position);
    }

    /**
     * Checks with player wins.
     *
     * @return the winning player or null if no player wins currently
     */
    public Player winner() {
        if (whitePlayerBoard.doesPlayerWin()) {
            return whitePlayer;
        } else if (blackPlayerBoard.doesPlayerWin()) {
            return blackPlayer;
        } else {
            return null;
        }
    }

    /**
     * Checks whether the game has ended with a drawn (no player wins).
     *
     * @return Has the game ended with a drawn?
     */
    public boolean drawn() {
        return Board.getFreeFields() <= 0;
    }

    /**
     * Returns the type of the field at the given position.
     *
     * @param position Position of the field on the combined board.
     * @return The type of the field at the given position.
     */
    public FieldType getFieldType(Stone.Position position) {
        if (Board.isStonePositionBlocked(position)) {
            return FieldType.BLOCKED;
        } else {
            Stone black_stone = blackPlayer.getBoard().getStone(position);
            Stone white_stone = whitePlayer.getBoard().getStone(position);
            if (black_stone != null) {
                if (white_stone != null) {
                    return FieldType.JOKER;
                } else {
                    return FieldType.BLACK;
                }
            } else if (white_stone != null) {
                return FieldType.WHITE;
            } else {
                return FieldType.FREE;
            }
        }
    }

    /**
     * Returns a two dimensional array representing the game board. When the
     * field is blocked or the opponent has already placed a stone there, then
     * there's a false otherwise a true at the fields position in the array.
     *
     * @param self given player
     * @return two dimensional array
     */
    public boolean[][] getCanStoneBePlacedBooleanWhithoutSelf(Player self) {
        boolean[][] arr = new boolean[numberOfColumns][numberOfRows];
        boolean[][] ownBoardArr = self.getBoard().getSimpleStoneArray();
        boolean[][] opponentBoardArr = getOpponent(self).getBoard().getSimpleStoneArray();
        boolean[][] blockedStonesArr = Board.getIsBlockedStoneArray(numberOfColumns, numberOfRows);
        for (int i = 0; i < numberOfColumns; i++) {
            boolean[] ownRowArr = ownBoardArr[i];
            boolean[] opponentRowArr = opponentBoardArr[i];
            boolean[] blockedRowArr = blockedStonesArr[i];
            for (int j = 0; j < numberOfRows; j++) {
                arr[i][j] = !(opponentRowArr[j] && !ownRowArr[j]) && !blockedRowArr[j];
            }
        }
        return arr;
    }

    /**
     * Returns the types of the fields of the combined board as a two
     * dimensional array.
     *
     * @return The field type of the combined board.
     */
    public FieldType[][] getBoardFieldTypes() {
        FieldType[][] arr = new FieldType[numberOfColumns][numberOfRows];
        for (int i = 0; i < arr.length; i++) {
            FieldType[] row = arr[i];
            for (int j = 0; j < row.length; j++) {
                arr[i][j] = getFieldType(new Stone.Position(i, j));
            }
        }
        return arr;
    }

    private static String boardStringRep(Game.FieldType[][] array) {
        int c_str_length = (array.length - 1 + "").length();
        int r_str_length = array.length >= 1 ? (array[0].length - 1 + "").length() : 0;
        String retstr = "";
        //Print column indezes
        for (int i = 0; i < c_str_length; i++) {
            retstr += whiteSpace(r_str_length);
            for (int j = 0; j < array.length; j++) {
                String str = " ";
                String j_str = j + "";
                if (c_str_length - i <= j_str.length()) {
                    str = j_str.charAt(i - (c_str_length - j_str.length())) + "";
                }
                retstr += " | " + str;
            }
            retstr += "\n";
        }
        //Print rest of table
        for (int j = 0; j < array[0].length; j++) {
            String r_str = j + "";
            retstr += whiteSpace(r_str_length - r_str.length());
            retstr += r_str;
            for (int i = 0; i < array.length; i++) {
                retstr += " | " + array[i][j].toString();
            }
            retstr += "\n";
        }
        return retstr;
    }

    /**
     * Returns the string representation (or text based visualization) of the
     * summarized board.
     *
     * @return String representation of the summarized board.
     */
    public String boardStringRep() {
        return boardStringRep(getBoardFieldTypes());
    }

    private static String whiteSpace(int length) {
        String retString = "";
        for (int i = 0; i < length; i++) {
            retString += " ";
        }
        return retString;
    }

    /**
     * Returns the opponent of the given player.
     *
     * @param self given player
     * @return opponent
     */
    public Player getOpponent(Player self) {
        if (self.isWhite()) {
            return blackPlayer;
        } else {
            return whitePlayer;
        }
    }

    /**
     * @return the numberOfStonesInARowToWin
     */
    public int getNumberOfStonesInARowToWin() {
        return numberOfStonesInARowToWin;
    }

    /**
     * @return the numberOfColumns
     */
    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    /**
     * @return the numberOfRows
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * @return the whitePlayer
     */
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * @return the blackPlayer
     */
    public Player getBlackPlayer() {
        return blackPlayer;
    }

    /**
     * @return the whitePlayerBoard
     */
    public Board getWhitePlayerBoard() {
        return whitePlayerBoard;
    }

    /**
     * @return the blackPlayerBoard
     */
    public Board getBlackPlayerBoard() {
        return blackPlayerBoard;
    }

    /**
     * @return the joker
     */
    public Player getJoker() {
        return joker;
    }

    /**
     * Type of a field on the board.
     */
    public static enum FieldType {

        /**
         * There's a black stone placed on it.
         */
        BLACK("X", Color.black),
        /**
         * There's a white stone placed on it.
         */
        WHITE("O", Color.white),
        /**
         * There's a joker stone placed on it.
         */
        JOKER("+", Color.blue),
        /**
         * This field is blocked.
         */
        BLOCKED("#", Color.red),
        /**
         * This field is free of stones.
         */
        FREE(" ", Color.lightGray);
        private String stringRep;
        private Color color;

        private FieldType(String stringRep, Color color) {
            this.stringRep = stringRep;
            this.color = color;
        }

        /**
         * Returns of the field when being painted.
         *
         * @return the color of the field when being painted
         */
        public Color getColor() {
            return color;
        }

        @Override
        public String toString() {
            return stringRep;
        }
    }
}
