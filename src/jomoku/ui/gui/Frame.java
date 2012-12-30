package jomoku.ui.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import jomoku.Game;
import jomoku.Game.FieldType;
import jomoku.Stone;
import jomoku.Stone.Position;
import jomoku.ui.AbstractPlayer;

/**
 * Visualizes a game and builds up the GUI the users see.
 *
 * @author Johannes Bechberger
 */
public class Frame extends javax.swing.JFrame {

    private Game game;
    private Game.FieldType[][] fields;
    private ConcurrentLinkedQueue<FieldTypePositionContainer> fieldsToAddQueue = new ConcurrentLinkedQueue<>();
    private boolean forcePaintAll = false;
    private Stone.Position currentStonePosition;
    private int shift = 15;
    private int yAddShift = 22;
    private int padding = 1;
//    private Stone.Position currentPos;
    private StonePositionGUIArea[][] guiAreas;

    /**
     * Creates new form Frame
     */
    public Frame() {
        initComponents();
        this.game = new Game(14, 14);
    }

    /**
     * Builds a frame visualizing the given game.
     *
     * @param game given game
     */
    public Frame(Game game) {
        initComponents();
        this.game = game;
        fields = game.getBoardFieldTypes();
        guiAreas = new StonePositionGUIArea[fields.length][fields[0].length];
    }

    @Override
    public void paint(Graphics g) {
        //super.paint(g);
        int boardWidth = getWidth() - (shift * 2);
        int boardHeight = getHeight() - (shift * 2) - yAddShift;
        double fieldWidth = boardWidth * 1.0 / game.getNumberOfColumns();
        double fieldHeight = boardHeight * 1.0 / game.getNumberOfRows();
        int fieldWidthInt = (int) Math.round(fieldWidth);
        int fieldHeightInt = (int) Math.round(fieldHeight);
        int fieldStoneWidth = (int) Math.round(fieldWidth) - (padding * 2);
        int fieldStoneHeight = (int) Math.round(fieldHeight) - (padding * 2);
        int x, y;
        if (!fieldsToAddQueue.isEmpty() && !forcePaintAll) {
            FieldTypePositionContainer field = fieldsToAddQueue.poll();
            while (field != null) {
                fields[field.position.getColumn()][field.position.getRow()] = field.fieldType;
                x = (int) Math.round(fieldWidth * field.position.getColumn()) + shift;
                y = (int) Math.round(fieldHeight * field.position.getRow()) + shift + yAddShift;
                if (field.fieldType != Game.FieldType.BLOCKED) {
                    if (field.fieldType != Game.FieldType.FREE) {
                        g.setColor(field.fieldType.getColor());
                        g.fillOval(x + padding, y + padding, fieldStoneWidth, fieldStoneHeight);
                    }
                } else {
                    g.setColor(field.fieldType.getColor());
                    g.fillRect(x, y, fieldWidthInt, fieldHeightInt);
                }
                field = fieldsToAddQueue.poll();
            }
        } else {
            g.setColor(Game.FieldType.FREE.getColor());
            g.fillRect(shift, shift + yAddShift, boardWidth, boardHeight);
            for (int i = 0; i < fields.length; i++) {
                Game.FieldType[] row = fields[i];
                for (int j = 0; j < row.length; j++) {
                    Game.FieldType type = row[j];
                    x = (int) Math.round(fieldWidth * i) + shift;
                    y = (int) Math.round(fieldHeight * j) + shift + yAddShift;
                    if (type != Game.FieldType.BLOCKED) {
                        if (type != Game.FieldType.FREE) {
                            g.setColor(type.getColor());
                            g.fillOval(x + padding, y + padding, fieldStoneWidth, fieldStoneHeight);
                        }
                    } else {
                        g.setColor(type.getColor());
                        g.fillRect(x, y, fieldWidthInt, fieldHeightInt);
                    }
                    guiAreas[i][j] = new StonePositionGUIArea(i, j, new Rectangle(x, y, fieldWidthInt, fieldHeightInt));
                }
            }
            g.setColor(Color.darkGray);
            y = shift + yAddShift;
            for (int i = 0; i < fields.length + 1; i++) {
                x = (int) Math.round(fieldWidth * i) + shift;
                g.drawLine(x, y, x, y + boardHeight);
            }
            x = shift;
            for (int i = 0; i < fields[0].length + 1; i++) {
                y = (int) Math.round(fieldHeight * i) + shift + yAddShift;
                g.drawLine(x, y, x + boardWidth, y);
            }
            forcePaintAll = false;
        }
    }

    /**
     * Prompts the given player to place a stone.
     *
     * @param player given player
     * @return position of the stone placed by the given player
     */
    public Stone.Position promptPosition(AbstractPlayer player) {
        setTitle(player.toString().toUpperCase() + ": Please click to place a stone.");
        while (currentStonePosition == null);
        Stone.Position position = currentStonePosition;
        currentStonePosition = null;
        return position;
    }

    /**
     * Sets the type of the field specified by the given position.
     *
     * @param type new type of the field
     * @param position given position of the field
     */
    public void addField(Game.FieldType type, Stone.Position position) {
        fieldsToAddQueue.add(new FieldTypePositionContainer(type, position));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(50, 50));
        setPreferredSize(new java.awt.Dimension(600, 600));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 608, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 485, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        currentStonePosition = pixelPointToStonePosition(evt.getPoint());
    }//GEN-LAST:event_formMouseClicked

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        forcePaintAll = true;
        repaint();
    }//GEN-LAST:event_formComponentResized

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
//        currentPos = pixelPointToStonePosition(evt.getPoint());//System.out.println("--- " + pixelPointToStonePosition(evt.getPoint()));
//        System.out.println(evt.getPoint());
    }//GEN-LAST:event_formMouseMoved

    /**
     * Calculates the position of the stone at the given pixel point.
     *
     * @param point pixel point
     * @return the position of the stone
     */
    public Stone.Position pixelPointToStonePosition(Point point) {
//        double fieldWidth = (getWidth() - (shift * 2)) / game.getNumberOfColumns();
//        double fieldHeight = (getHeight() - (shift * 2) - yAddShift) / game.getNumberOfRows();
//        Stone.Position pos = new Stone.Position((int) Math.round((point.getX() - shift) / fieldWidth),
//                (int) Math.round((point.getY() - shift  - yAddShift) / fieldHeight));
        for (int i = 0; i < guiAreas.length; i++) {
            StonePositionGUIArea[] row = guiAreas[i];
            for (int j = 0; j < row.length; j++) {
                StonePositionGUIArea stonePositionGUIArea = row[j];
                if (stonePositionGUIArea.isInGUIArea(point)) {
                    return stonePositionGUIArea.getStonePosition();
                }
            }
        }
        return null;
    }

    /**
     * Replaces the current game with the given.
     *
     * @param game given game
     */
    public void setGame(Game game) {
        this.game = game;
        fields = game.getBoardFieldTypes();
        forcePaintAll = true;
        repaint();
    }

//    public Position getCurrentPos() {
//        return currentPos;
//    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Frame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private class FieldTypePositionContainer {

        public final FieldType fieldType;
        public final Stone.Position position;

        public FieldTypePositionContainer(FieldType fieldType, Position position) {
            this.fieldType = fieldType;
            this.position = position;
        }
    }

    private class StonePositionGUIArea extends Stone.Position {

        private Rectangle guiArea;

        /**
         *
         * @param column column number of the stone, 0 is the first column
         * @param row row number of the stone, 0 is the first row
         * @param guiArea area displaying this stone on the GUI
         */
        public StonePositionGUIArea(int column, int row, Rectangle guiArea) {
            super(column, row);
            this.guiArea = guiArea;
        }

        /**
         *
         * @return the inherited Stone.Position
         */
        public Stone.Position getStonePosition() {
            return new Stone.Position(getColumn(), getRow());
        }

        /**
         * Does the GUI Area of this point contains the given point?
         *
         * @param point given point
         * @return Does the GUI Area of this point contains the given point?
         */
        public boolean isInGUIArea(Point point) {
            return guiArea.contains(point);
        }

        /**
         * Does the GUI Area of this point contains the given point?
         *
         * @param x x coordinate of the given point
         * @param y y coordinate of the given point
         * @return Does the GUI Area of this point contains the given point?
         */
        public boolean isInGUIArea(int x, int y) {
            return guiArea.contains(x, y);
        }
    }
}
