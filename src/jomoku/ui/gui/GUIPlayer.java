package jomoku.ui.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import jomoku.Player;
import jomoku.Stone;
import jomoku.ui.AbstractPlayer;
import jomoku.ui.AbstractUI;

/**
 * Builds the UI of a Jomoku player.
 *
 * @author Johannes Bechberger
 * @version 1.0
 */
public class GUIPlayer extends AbstractPlayer {

    private Frame frame;
    private Stone.Position lastPosition;
    private boolean init = true;

    /**
     * Constructs an GUI player.
     *
     * @param player the Jomoku player this object builds the UI for
     * @param ui UI this player belongs to
     */
    public GUIPlayer(Player player, AbstractUI ui) {
        super(player, ui);
    }

    @Override
    public Stone.Position getNextPosition() {
        if (init) {
            frame = ((GUI) getUi()).getFrame();
            frame.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    lastPosition = frame.pixelPointToStonePosition(e.getPoint());
//                    System.out.println(lastPosition);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
            init = !init;
        }
        lastPosition = null;
        Stone.Position pos = null;
        while (pos == null || !getUi().getGame().canStoneBePlacedAtPosition(pos)) {
            frame.setTitle((isWhite() ? "White" : "Black") + " player: Please place a stone.");
            while (lastPosition == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GUIPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            pos = lastPosition.clone();
        }
        return pos;
    }

    @Override
    public void handleError(String msg) {
        System.err.println(msg);
    }

    @Override
    public void otherPlayerAction(Stone.Position position) {
    }

    @Override
    public void init() {
    }
}
