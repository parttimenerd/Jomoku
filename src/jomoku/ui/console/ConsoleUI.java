package jomoku.ui.console;

import jomoku.Game;
import jomoku.Player;
import jomoku.Stone.Position;
import jomoku.ui.AbstractPlayer;
import jomoku.ui.AbstractUI;
import jomoku.ui.UI;

/**
 * A console based UI for this game.
 *
 * @author Johannes Bechberger
 * @version 1.0
 */
public class ConsoleUI extends AbstractUI {

    private final String WINNER_TEXT = "{{Player}}. You've won!!! (Overall actions: {{actions}})";
    private final String REMMI_TEXT = "Remmi. No Player has won after {{actions}}.";
    private int actions = 0;

    /**
     *
     * @param ui main UI this UI belongs to
     * @param game game this object is the UI for
     */
    public ConsoleUI(UI ui, Game game) {
        super(ui, game);
    }

    /**
     *
     * @return whether it's actually printed or not according to the options
     */
    private boolean printBoard() {
        if (getUI().getOption("printNoBoards") != null) {
            return false;
        }
        System.out.println(getGame().boardStringRep());
        return true;
    }

    @Override
    public void init(boolean whiteAuto, boolean blackAuto) {
        if (whiteAuto) {
            setWhitePlayer(new ConsoleOpponent(new Player(getGame(), Player.PlayerType.WHITE), this));
        } else {
            setWhitePlayer(new ConsolePlayer(new Player(getGame(), Player.PlayerType.WHITE), this));
        }
        if (blackAuto) {
            setBlackPlayer(new ConsoleOpponent(new Player(getGame(), Player.PlayerType.BLACK), this));
        } else {
            setBlackPlayer(new ConsolePlayer(new Player(getGame(), Player.PlayerType.BLACK), this));
        }
        init();
        printBoard();
    }

    @Override
    public void handlePlayerActionOccured(AbstractPlayer player, Position nextPosition) {
        printBoard();
        actions += 1;
    }

    @Override
    public boolean handleWin(AbstractPlayer winner) {
        String player_rep = winner.isWhite() ? "white" : "black";
        System.out.println(WINNER_TEXT.replaceAll("\\{\\{Player\\}\\}", player_rep.toUpperCase())
                .replaceAll("\\{\\{player\\}\\}", player_rep).replaceAll("\\{\\{actions\\}\\}", actions + ""));
        return false;
    }

    @Override
    public boolean handleDrawn() {
        System.out.println(REMMI_TEXT.replaceAll("\\{\\{actions\\}\\}", actions + ""));
        return false;
    }
}
