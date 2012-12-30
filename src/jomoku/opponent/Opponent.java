package jomoku.opponent;

import java.util.Arrays;
import jomoku.Board;
import jomoku.Player;
import jomoku.Stone.Position;
import jomoku.ui.AbstractPlayer;
import jomoku.ui.AbstractUI;

/**
 * Models an opponent player
 *
 * @author Johannes Bechberger
 * @version 0.3
 */
public abstract class Opponent extends AbstractPlayer {

    private final double opponentImportanceFactor = 1 + (Math.random() * 0.2);
    private double randomizationFactor = 0.000;
    private Board own_board;
    private Board opponent_board;

    /**
     * 
     * @param player the Jomoku player this opponent builds the UI for and plays with
     * @param ui UI this player belongs to
     */
    public Opponent(Player player, AbstractUI ui) {
        super(player, ui);
    }

    @Override
    public void init() {
        own_board = getPlayer().getBoard();
        opponent_board = getUi().getGame().getOpponent(getPlayer()).getBoard();
    }

    @Override
    public Position getNextPosition() {
        BoardMatrix b_matrix = new BoardMatrix(own_board);
        BoardMatrix b_matrix_opponent = new BoardMatrix(opponent_board);
        double[][] matrix = b_matrix.calculateMatrix();
        double[][] matrix_opponent = b_matrix_opponent.calculateMatrix(randomizationFactor);
//        System.out.println("++++++++++++++++++++++++++++");
//        System.out.println(b_matrix);
//        for (int i = 0; i < matrix.length; i++) {
//            double[] ds = matrix_opponent[i];
//            System.out.println(Arrays.toString(ds));
//        }
//        System.out.println("-----------------");
//        System.out.println(b_matrix_opponent);
//        for (int i = 0; i < matrix_opponent.length; i++) {
//            double[] ds = matrix_opponent[i];
//            System.out.println(Arrays.toString(ds));
//        }

        matrix = BoardMatrix.addMatrizes(BoardMatrix.multiplyMatrixWithScalar(matrix, 1 / opponentImportanceFactor),
                BoardMatrix.multiplyMatrixWithScalar(matrix_opponent, opponentImportanceFactor));
        if (getUi().getUI().getOption("showAutoDebugMatrix") != null) {
            int[][] print_matrix = BoardMatrix.doubleToPositivizedIntMatrix(matrix);
            for (int i = 0; i < matrix_opponent.length; i++) {
                int[] ds = print_matrix[i];
                System.out.println(Arrays.toString(ds));
            }
        }

        BoardMatrix.MatrixValueContainer maximum = BoardMatrix.getMaximumValuePosition(matrix, getGame());
        if (maximum.getColumn() < 0) {
            Position pos = new Position(Math.round(getGame().getNumberOfColumns() / 2), Math.round(getGame().getNumberOfRows() / 2));
            if (getGame().canStoneBePlacedAtPosition(pos)) {
                return pos;
            } else {
                return new Position(0, 0);
            }
        }
        return new Position(maximum.getColumn(), maximum.getRow());
    }
}
