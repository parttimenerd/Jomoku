package jomoku;

/**
 * Models a player of this game.
 *
 * @see http://en.wikipedia.org/wiki/Five_in_a_Row_%28game%29
 * @author Johannes Bechberger
 * @version 1.0
 */
public class Player {

    /**
     * Type of the player: black, white of both.
     */
    private PlayerType type;
    /**
     * Board the player sets its stone on, each player has its own.
     */
    private Board board;
    /**
     * Game the player plays in.
     */
    private Game game;

    /**
     * Constructs a player with an default board.
     *
     * @param game Game the player plays in
     * @param type Type of the player: black, white of both
     */
    public Player(Game game, PlayerType type) {
        this.game = game;
        this.type = type;
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
     *
     * @return Whether or not the current player is the white player?
     */
    public boolean isWhite() {
        return type == PlayerType.WHITE;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    /**
     * @return the type
     */
    public PlayerType getType() {
        return type;
    }
    
    /**
     * 
     * @return the field type of this player
     */
    public Game.FieldType getFieldType(){
        return isWhite() ? Game.FieldType.WHITE : Game.FieldType.BLACK;
    }
    
    /**
     * 
     * @return the opponent of this player
     */
    public Player getOpponent(){
        return game.getOpponent(this);
    }

    /**
     * Models the type of a player.
     */
    public enum PlayerType {

        /**
         * White player.
         */
        WHITE,
        /**
         * Black player.
         */
        BLACK,
        /**
         * A player of this type can set stone counting for the white an the
         * black player.
         */
        BOTH;

        @Override
        public String toString() {
            return this.name();
        }
    }
}
