package jomoku;

/**
 * Models a player of this game
 *
 * @see http://en.wikipedia.org/wiki/Five_in_a_Row_%28game%29
 * @author Johannes Bechberger
 */
public class Player {
    /**
     * Type of the player: black, white of both
     */
    public final PlayerType TYPE;
    /**
     * Board the player sets its stone on, each player has its own
     */
    private Board board;
    /**
     * Game the player plays in
     */
    private Game game;

    /**
     * Constructs a player with an default board
     *
     * @param game Game the player plays in
     * @param type Type of the player: black, white of both
     */
    public Player(Game game, PlayerType type) {
        this.game = game;
        this.TYPE = type;
        this.board = new Board(game, this);
    }

    /**
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Models the type of a player
     */
    public enum PlayerType {

        WHITE, BLACK,
        /**
         * A player of this type can set stone counting for the white an the
         * black player
         */
        BOTH
    }
}
