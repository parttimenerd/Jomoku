package jomoku.ui;

import jomoku.Game;
import jomoku.Stone.Position;

/**
 * Models an abstract UI with the needed functionality for a game.
 *
 * @author Johannes Bechberger
 * @version 1.0
 */
public abstract class AbstractUI {

    private UI ui;
    private Game game;
    private AbstractPlayer whitePlayer;
    private AbstractPlayer blackPlayer;

    /**
     *
     * @param ui main UI this UI belongs to
     * @param game game this object is the UI for
     */
    public AbstractUI(UI ui, Game game) {
        this.game = game;
        this.ui = ui;
    }

    /**
     *
     * @return the UI of the black player
     */
    public AbstractPlayer getBlackPlayer() {
        return blackPlayer;
    }

    /**
     *
     * @return the UI of the white player
     */
    public AbstractPlayer getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * Sets the UI of the black player.
     *
     * @param blackPlayer new UI of the black player
     */
    protected void setBlackPlayer(AbstractPlayer blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    /**
     * Sets the UI of the white player.
     *
     * @param whitePlayer new UI of the white player
     */
    protected void setWhitePlayer(AbstractPlayer whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    /**
     *
     * @return game this object is the UI for
     */
    public Game getGame() {
        return game;
    }

    /**
     *
     * @return main UI this UI belongs to
     */
    public UI getUI() {
        return ui;
    }

    /**
     * Initializes this UI
     */
    public void init() {
        whitePlayer.init();
        blackPlayer.init();
    }

    /**
     * Initializes this UI
     *
     * @param whiteAuto Is the white player played by the UI itself?
     * @param blackAuto Is the black player played by the UI itself?
     */
    public abstract void init(boolean whiteAuto, boolean blackAuto);

    /**
     * Handles the event when one of the two players placed a stone.
     *
     * @param player player having placed the stone
     * @param nextPosition the position of the placed stone
     */
    public abstract void handlePlayerActionOccured(AbstractPlayer player, Position nextPosition);

    /**
     * Handles the end of the game when one player wins.
     *
     * @param winner Play who won this game
     * @return Do the players want to replay?
     */
    public abstract boolean handleWin(AbstractPlayer winner);

    /**
     * Handles the end of the game when no player wins.
     * @return Do the players want to replay?
     */
    public abstract boolean handleDrawn();

    /**
     * Configures the current UI with the given game for replay.
     *
     * @param game given game
     */
    public void replay(Game game) {
        this.game = game;
        init();
    }
}
