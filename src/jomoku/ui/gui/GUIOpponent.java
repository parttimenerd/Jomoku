package jomoku.ui.gui;

import jomoku.Game;
import jomoku.Player;
import jomoku.Stone.Position;
import jomoku.opponent.Opponent;
import jomoku.ui.AbstractUI;

/**
 * Models an opponent GUI player.
 * 
 * @author Johannes Bechberger
 * @version 0.4
 */
public class GUIOpponent extends Opponent {

    /**
     * Contructs a GUI opponent.
     *
     * @param player the Jomoku player this opponent builds the UI for and plays
     * with
     * @param ui UI this player belongs to
     */
    public GUIOpponent(Player player, AbstractUI ui) {
        super(player, ui);
    }

    @Override
    public Position getNextPosition() {
        String text = (isWhite() ? "White" : "Black") + " player: ";
        if (getUi().getUI().getOption("showAutoDebugMatrix") != null) {
            System.out.println(text);
        }
        Position pos = super.getNextPosition();
        if (getUi().getUI().getOption("showAutoDebugMatrix") != null) {
            System.out.println("=> " + pos);
        } else {
            ((GUI) getUi()).getFrame().setTitle(text + pos);
        }
        return pos;
    }

    @Override
    public void handleError(String msg) {
        System.err.println(msg);
    }

    @Override
    public void otherPlayerAction(Position position) {
    }

    @Override
    public void replay(Game game) {
        super.replay(game);
    }
}
