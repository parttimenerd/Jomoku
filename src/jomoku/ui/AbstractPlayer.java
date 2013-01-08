package jomoku.ui;

import jomoku.Game;
import jomoku.Player;
import jomoku.Stone;

/**
 * Builds the UI of a Jomoku player.
 *
 * @author Johannes Bechberger
 * @version 1.0
 */
public abstract class AbstractPlayer {

    private Player player;
    private Player opponent;
    private AbstractUI ui;

    /**
     * Constructs an AbstractPlayer.
     *
     * @param player the Jomoku player this object builds the UI for
     * @param ui UI this player belongs to
     */
    public AbstractPlayer(Player player, AbstractUI ui) {
        this.player = player;
        this.opponent = player.getGame().getOpponent(player);
        this.ui = ui;
    }

    /**
     * Initializes the player
     */
    public abstract void init();

    /**
     * Returns the position of the next stone the player wants to place.
     *
     * @return the position of the next stone
     */
    public abstract Stone.Position getNextPosition();

    /**
     * Handles an error (i.e. "You can't place a stone there").
     *
     * @param msg error message
     */
    public abstract void handleError(String msg);

    /**
     * Handles the event when the other player of the game has placed a stone.
     *
     * @param position position of the stone placed by the other player
     */
    public abstract void otherPlayerAction(Stone.Position position);

    /**
     *
     * @return Whether this player is the white player
     */
    public boolean isWhite() {
        return player.isWhite();
    }

    /**
     *
     * @return the Jomoku player this object builds the UI for
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @return the opponent of the Jomoku player this object builds the UI for
     */
    public Player getOpponent() {
        return opponent;
    }

    /**
     *
     * @return the game this player belongs to
     */
    public Game getGame() {
        return player.getGame();
    }

    /**
     *
     * @return the AbstractUI this player belongs to
     */
    public AbstractUI getUi() {
        return ui;
    }

    @Override
    public String toString() {
        return isWhite() ? "white player" : "black player";
    }

    /**
     * Resets the current player with the given game, making it possible to
     * replay.
     *
     * @param game given game
     */
    public void replay(Game game) {
        player = isWhite() ? game.getWhitePlayer() : game.getBlackPlayer();
        init();
    }
}
