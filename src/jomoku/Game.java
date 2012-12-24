package jomoku;

/**
 * Models the game managing the two players and the board with stones,
 * encapsulates it.
 *
 * @see http://en.wikipedia.org/wiki/Five_in_a_Row_%28game%29
 * @author Johannes Bechberger
 */
public class Game {

    /**
     * Default minimum number of stones a player has to place in a row to win
     *
     * @see http://en.wikipedia.org/wiki/Five_in_a_Row_%28game%29
     */
    public static final int DEFAULT_NUMBER_OF_STONES_IN_A_ROW_TO_WIN = 5;
    
    /**
     * Minimum number of stones a player has to place in a row to win
     *
     * @see http://en.wikipedia.org/wiki/Five_in_a_Row_%28game%29
     */
    public final int NUMBER_OF_STONES_IN_A_ROW_TO_WIN;
    /**
     * Number of columns of the board being played on.
     */
    public final int NUMBER_OF_COLUMNS;
    /**
     * Number of rows of the board being played on.
     */
    public final int NUMBER_OF_ROWS;
    /**
     * White player
     */
    public final Player WHITE_PLAYER;
    /**
     * Black player
     */
    public final Player BLACK_PLAYER;
    /**
     * Board of the white player
     */
    public final Board WHITE_PLAYER_BOARD;
    /**
     * Board of the black player
     */
    public final Board BLACK_PLAYER_BOARD;
    /**
     * Player being able to set stones for the white and the black player.
     */
    public final Player JOKER;

    /**
     * Constructs a game
     *
     * @param NUMBER_OF_COLUMNS Number of columns of the board being played on.
     * @param NUMBER_OF_ROWS Number of rows of the board being played on.
     * @param NUMBER_OF_STONES_IN_A_ROW_TO_WIN Minimum number of stones a player.
     * has to place in a row to win
     */
    public Game(int NUMBER_OF_COLUMNS, int NUMBER_OF_ROWS, int NUMBER_OF_STONES_IN_A_ROW_TO_WIN) {
        this.NUMBER_OF_COLUMNS = NUMBER_OF_COLUMNS;
        this.NUMBER_OF_ROWS = NUMBER_OF_ROWS;
        this.NUMBER_OF_STONES_IN_A_ROW_TO_WIN = NUMBER_OF_STONES_IN_A_ROW_TO_WIN;
        this.WHITE_PLAYER = new Player(this, Player.PlayerType.WHITE);
        this.BLACK_PLAYER = new Player(this, Player.PlayerType.BLACK);
        this.WHITE_PLAYER_BOARD = WHITE_PLAYER.getBoard();
        this.BLACK_PLAYER_BOARD = BLACK_PLAYER.getBoard();
        this.JOKER = new Player(this, Player.PlayerType.BOTH);
    }
    
    /**
     * Constructs a game
     *
     * @param NUMBER_OF_COLUMNS Number of columns of the board being played on.
     * @param NUMBER_OF_ROWS Number of rows of the board being played on.
     */
    public Game(int NUMBER_OF_COLUMNS, int NUMBER_OF_ROWS) {
        this.NUMBER_OF_COLUMNS = NUMBER_OF_COLUMNS;
        this.NUMBER_OF_ROWS = NUMBER_OF_ROWS;
        this.NUMBER_OF_STONES_IN_A_ROW_TO_WIN = DEFAULT_NUMBER_OF_STONES_IN_A_ROW_TO_WIN;
        this.WHITE_PLAYER = new Player(this, Player.PlayerType.WHITE);
        this.BLACK_PLAYER = new Player(this, Player.PlayerType.BLACK);
        this.WHITE_PLAYER_BOARD = WHITE_PLAYER.getBoard();
        this.BLACK_PLAYER_BOARD = BLACK_PLAYER.getBoard();
        this.JOKER = new Player(this, Player.PlayerType.BOTH);
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
        if (BLACK_PLAYER_BOARD.canStoneBePlacedAtPosition(position) && WHITE_PLAYER_BOARD.canStoneBePlacedAtPosition(position)) {
            WHITE_PLAYER_BOARD.placeStone(position);
            BLACK_PLAYER_BOARD.placeStone(position);
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
     * @param position Position of the stone
     * @param is_white_stone Whether the stone is placed by the white player
     * (true) or the black player (false).
     * @return false if the stone can't be set a this position, true otherwise.
     */
    public boolean placeStone(Stone.Position position, boolean is_white_stone) {
        Board board = is_white_stone ? WHITE_PLAYER_BOARD : BLACK_PLAYER_BOARD;
        if (canStoneBePlacedAtPosition(position)) {
            return board.placeStone(position);
        }
        return false;
    }

    public boolean canStoneBePlacedAtPosition(Stone.Position position) {
        return WHITE_PLAYER_BOARD.canStoneBePlacedAtPosition(position)
                && BLACK_PLAYER_BOARD.canStoneBePlacedAtPosition(position);
    }

    /**
     * Checks with player wins
     *
     * @return the winning player or null if no player wins currently
     */
    public Player winner() {
        if (WHITE_PLAYER_BOARD.doesPlayerWin()) {
            return WHITE_PLAYER;
        } else if (BLACK_PLAYER_BOARD.doesPlayerWin()) {
            return BLACK_PLAYER;
        } else {
            return null;
        }
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
            Stone black_stone = BLACK_PLAYER_BOARD.getStone(position);
            Stone white_stone = WHITE_PLAYER_BOARD.getStone(position);
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
     * Returns the types of the fields of the combined board as a two
     * dimensional array.
     *
     * @return The field type of the combined board.
     */
    public FieldType[][] getBoardFieldTypes() {
        FieldType[][] arr = new FieldType[NUMBER_OF_COLUMNS][NUMBER_OF_ROWS];
        for (int i = 0; i < arr.length; i++) {
            FieldType[] row = arr[i];
            for (int j = 0; j < row.length; j++) {
                arr[i][j] = getFieldType(new Stone.Position(i, j));
            }
        }
        return arr;
    }

    /**
     * Type of a field on the board
     */
    public static enum FieldType {

        /**
         * There's a black stone placed on it.
         */
        BLACK,
        /**
         * There's a white stone placed on it.
         */
        WHITE,
        /**
         * There's a joker stone placed on it.
         */
        JOKER,
        /**
         * This field is blocked.
         */
        BLOCKED,
        /**
         * This field is free of stones.
         */
        FREE
    }
}
