package jomoku.opponent;

import jomoku.Game;
import jomoku.Player;
import jomoku.Stone.Position;
import jomoku.ui.AbstractPlayer;
import jomoku.ui.AbstractUI;

/**
 * Models an opponent player
 *
 * @author Johannes Bechberger
 * @version 0.5
 */
public abstract class Opponent extends AbstractPlayer {

    private OpponentEngine engine;

    /**
     *
     * @param player the Jomoku player this opponent builds the UI for and plays
     * with
     * @param ui UI this player belongs to
     */
    public Opponent(Player player, AbstractUI ui) {
        super(player, ui);
        engine = new OpponentEngine(player);
    }

    @Override
    public void init() {
        engine.init();
    }

    @Override
    public Position getNextPosition() {
        if (getUi().getUI().getOption("showAutoDebugMatrix") != null) {
            engine.printScoreMatrix();
        }
        Position nextPosition = engine.examineBestPosition();
        engine.setStoneType(nextPosition, getPlayer().getFieldType());
        return nextPosition;
    }

    @Override
    public void otherPlayerAction(Position position) {
        engine.setStoneType(position, getPlayer().getOpponent().getFieldType());
    }

    @Override
    public void replay(Game game) {
        super.replay(game);
        engine = new OpponentEngine(getPlayer());
        engine.init();
    }
}
