package jomoku.ui.console;

import jomoku.Player;
import jomoku.Stone.Position;
import jomoku.opponent.Opponent;
import jomoku.ui.AbstractUI;

/**
 * Models an opponent console player.
 * 
 * @author Johannes Bechberger
 * @version 1.0
 */
public class ConsoleOpponent extends Opponent {

    /**
     * Contructs a console opponent.
     * 
     * @param player the Jomoku player this opponent builds the UI for and plays with
     * @param ui UI this player belongs to
     */
    public ConsoleOpponent(Player player, AbstractUI ui) {
        super(player, ui);
    }

    @Override
    public Position getNextPosition() {
        System.out.println((isWhite() ? "White" : "Black") + " player: ");
        Position pos = super.getNextPosition();
        if (getUi().getUI().getOption("showAutoDebugMatrix") != null) {
            System.out.println("=> " + pos);
        } else {
            System.out.println(pos);
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
}
