package jomoku.ui.gui;

import jomoku.Game;
import jomoku.Player;
import jomoku.Stone.Position;
import jomoku.ui.AbstractPlayer;
import jomoku.ui.AbstractUI;
import jomoku.ui.UI;

/**
 * A GUI for this game.
 *
 * @author Johannes Bechberger
 * @version 1.0
 */
public class GUI extends AbstractUI {

    private final String WINNER_TEXT = "{{Player}}. You've won!!! (Overall actions: {{actions}})";
    private final String REMMI_TEXT = "Remmi. No Player has won. (Overall actions: {{actions}})";
    private int actions = 0;
    private Frame frame;
    private Runnable frameRunnable;

    /**
     *
     * @param ui main UI this UI belongs to
     * @param game game this object is the UI for
     */
    public GUI(UI ui, Game game) {
        super(ui, game);
    }

    @Override
    public void init(boolean whiteAuto, boolean blackAuto) {
        if (whiteAuto) {
            setWhitePlayer(new GUIOpponent(new Player(getGame(), Player.PlayerType.WHITE), this));
        } else {
            setWhitePlayer(new GUIPlayer(new Player(getGame(), Player.PlayerType.WHITE), this));
        }
        if (blackAuto) {
            setBlackPlayer(new GUIOpponent(new Player(getGame(), Player.PlayerType.BLACK), this));
        } else {
            setBlackPlayer(new GUIPlayer(new Player(getGame(), Player.PlayerType.BLACK), this));
        }
        init();
        frameRunnable = new Runnable() {

            @Override
            public void run() {
                frame = new Frame(getGame());
                frame.setVisible(true);
            }
        }; 
        frameRunnable.run();
        while (frame == null);
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
//        frame = new Frame(getGame());
        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//        frame.setVisible(true);
//            }
//        });
    }

    /**
     * 
     * @return the frame belonging to this GUI
     */
    public Frame getFrame() {
        return frame;
    }

    @Override
    public void handlePlayerActionOccured(AbstractPlayer player, Position nextPosition) {
        frame.addField(player.isWhite() ? Game.FieldType.WHITE : Game.FieldType.BLACK, nextPosition);
        frame.repaint();
        actions += 1;
    }

    @Override
    public boolean handleWin(AbstractPlayer winner) {
        String player_rep = winner.isWhite() ? "white" : "black";
        String text = WINNER_TEXT.replaceAll("\\{\\{Player\\}\\}", player_rep.toUpperCase())
                .replaceAll("\\{\\{player\\}\\}", player_rep).replaceAll("\\{\\{actions\\}\\}", actions + "");
        return replayWinDrawnDialog(text);
    }

    @Override
    public boolean handleDrawn() {
        String text = REMMI_TEXT.replaceAll("\\{\\{actions\\}\\}", actions + "");
        return replayWinDrawnDialog(text);
    }
    
    private boolean replayWinDrawnDialog(String text){
        WinnerDrawnDialog dialog = new WinnerDrawnDialog(frame, text); 
        return dialog.wantsToReplay();
    }

    @Override
    public void replay(Game game) {
        super.replay(game);
        frame.setTitle("");
        frame.setGame(game);
        actions = 0;
        getBlackPlayer().replay(game);
        getWhitePlayer().replay(game);
    }
    
}
