package jomoku.opponent;

import java.util.Random;
import jomoku.Board;
import jomoku.Game;
import jomoku.Player;
import jomoku.Stone;

/**
 * A matrix of the board stones, inheriting the current score (the imortance to
 * set a stone on this field) of each field.
 *
 * @author Johannes Bechberger
 * @version 0.2
 */
public class BoardMatrix {

    private Board board;
    private Board opponentBoard;
    private boolean[][] opponentBoardArr;
    private int[][] boardArr;
    private double[][] matrix;
    private int rows;
    private int columns;
    private int inARow;
    private double base = 3;
    private double middleBonusFactor = 0.0001;

    /**
     * Constructs a BoardMatrix for the given player.
     *
     * @param player given player
     */
    public BoardMatrix(Player player) {
        this.board = player.getBoard();
        this.opponentBoard = player.getGame().getOpponent(player).getBoard();
        this.opponentBoardArr = player.getGame().getCanStoneBePlacedBooleanWhithoutSelf(player);
        this.columns = board.getNumberOfColumns();
        this.rows = board.getNumberOfRows();
        this.matrix = new double[columns][rows];
        this.inARow = board.getGame().getNumberOfStonesInARowToWin();
        this.boardArr = board.getSimpleStoneIntArray();
    }

    /**
     * Calculates the matrix of the inherited board.
     *
     * @return the calculated matrix
     */
    public double[][] calculateMatrix() {
        matrix = calculateMatrix(boardArr, opponentBoardArr);
        return matrix;
    }

    /**
     * Calculates the randomized matrix of the inherited board.
     *
     * @param randomness fraction of the end value which is random
     * @return the calculated and randomized matrix
     */
    public double[][] calculateMatrix(double randomness) {
        matrix = calculateMatrix(boardArr, opponentBoardArr, randomness);
        return matrix;
    }

    //public static void main(String[] args) {
//        BoardMatrix board = new BoardMatrix();
//        double[][] matrix = new double[][]{
//            {1, 0, 0, 0},
//            {0, 0, 1, 0},
//            {0, 0, 1, 0}
//        };
//        System.out.println(board.calculateValueForPositionVertical(matrix, 1, 2));
//        matrix = board.calculateMatrix(matrix);
//        for (int i = 0; i < matrix.length; i++) {
//            System.out.println(Arrays.toString(matrix[i]));
//        }
    //}
    private double[][] calculateMatrix(int[][] fieldArr, boolean[][] opponentArray, double randomness) {
        return ramndomize(calculateMatrix(fieldArr, opponentArray), randomness);
    }

    private double[][] calculateMatrix(int[][] fieldArr, boolean[][] opponentArray) {
        return calculateMatrix(intToDoubleMatrix(fieldArr), opponentArray);
    }

    private double[][] calculateMatrix(double[][] fieldArr, boolean[][] opponentArray) {
        double[][] matrixArr = new double[fieldArr.length][fieldArr[0].length];
        double[][] rotatedBoardArrDouble = changeColumnsAndRows(fieldArr);
        double[][] rotatedMatrix = new double[matrixArr[0].length][matrixArr.length];
        matrixArr = calculateMatrixValuesVerticalAndDiagonalsLeftTopToRightBottom(fieldArr, opponentArray);
        rotatedMatrix = addMatrizes(rotatedMatrix,
                calculateMatrixValuesVerticalAndDiagonalsLeftTopToRightBottom(rotatedBoardArrDouble, changeColumnsAndRows(opponentArray)));

        matrixArr = addMatrizes(matrixArr, changeColumnsAndRows(rotatedMatrix));
        matrixArr = addMiddleBonus(matrixArr, middleBonusFactor);
        return matrixArr;
    }

    /**
     * Converts a matrix of integer value to one of double values.
     *
     * @param intMatrix matrix of integer values
     * @return matrix of double values
     */
    public static double[][] intToDoubleMatrix(int[][] intMatrix) {
        double[][] retArr = new double[intMatrix.length][intMatrix[0].length];
        for (int i = 0; i < intMatrix.length; i++) {
            int[] row = intMatrix[i];
            for (int j = 0; j < row.length; j++) {
                retArr[i][j] = row[j];
            }
        }
        return retArr;
    }

    private double[][] calculateMatrixValuesVerticalAndDiagonalsLeftTopToRightBottom(double[][] origArray, boolean[][] opponentArray) {
        double[][] arr = new double[origArray.length][origArray[0].length];
        for (int i = 0; i < arr.length; i++) {
            double[] row = arr[i];
            for (int j = 0; j < row.length; j++) {
                arr[i][j] = calculateValueForPositionVertical(origArray, opponentArray, i, j)
                        + calculateValueForPositionDiagonalsLeftTopToRightBottom(origArray, opponentArray, i, j);
                //System.out.println("+" + arr[i][j] + " - " +calculateValueForPosition(orig_array, i, j));
            }
        }
        return arr;
    }

    private double calculateValueForPositionVertical(double[][] origArray, boolean[][] opponentArray, int column, int row) {
        if (origArray[column][row] != 0) {
            return Integer.MIN_VALUE;
        }
        boolean rowBlocked = false;
        double l1 = 0, l2 = 0, missing1 = 0, missing2 = 0;
        for (int i = column; i < column + inARow; i++) {
            if (i >= origArray.length || rowBlocked) {
                missing1 += 1;
            } else {
                rowBlocked = !opponentArray[i][row];
                l1 += origArray[i][row];
            }
        }
        rowBlocked = false;
        for (int i = column; i > column - inARow - 1; i--) {
            if (i < 0 || rowBlocked) {
                missing2 += 1;
            } else {
                rowBlocked = !opponentArray[i][row];
                l2 += origArray[i][row];
            }
        }
        if ((missing1 != 0 || missing2 != 0) && missing1 + missing2 < inARow) {
            return 0;
        } else {
            return l1 > 0 || l2 > 0 ? Math.pow(base, (l1 > 0 ? l1 : 0) + (l2 > 0 ? l2 : 0)) : 0;
        }
    }

    private double calculateValueForPositionDiagonalsLeftTopToRightBottom(double[][] origArray, boolean[][] opponentArray, int column, int row) {
        if (origArray[column][row] != 0) {
            return Integer.MIN_VALUE;
        }
        double l1 = 0, l2 = 0, missing1 = 0, missing2 = 0;
        double columns = origArray.length;
        double rows = origArray[0].length;
        boolean rowBlocked = false;

        for (int i = 0; i < inARow; i++) {
            if (i + column >= columns || i + row >= rows || rowBlocked) {
                missing1 += 1;
            } else {
                rowBlocked = !opponentArray[column + i][row + i];
                l1 += origArray[column + i][row + i];
            }
        }
        rowBlocked = false;
        for (int i = 0; i < inARow; i++) {
            if (column - i < 0 || row - i < 0 || rowBlocked) {
                missing2 += 1;
            } else {
                rowBlocked = !opponentArray[column - i][row - i];
                l2 += origArray[column - i][row - i];
            }
        }
        if ((missing1 != 0 || missing2 != 0) && missing1 + missing2 < inARow) {
            return 0;
        } else {
            return l1 > 0 || l2 > 0 ? Math.pow(base, (l1 > 0 ? l1 : 0) + (l2 > 0 ? l2 : 0)) : 0;
        }
    }

    private static double[][] getDiagonals(double[][] arr) {
        double[][] diagonal_arr = new double[arr[0].length][arr.length];
        for (int c_i = 0; c_i < arr.length; c_i++) {
            double[] row = arr[c_i];
            for (int r_i = 0; r_i < row.length; r_i++) {
                diagonal_arr[r_i][(c_i + r_i) % arr.length] = row[r_i];
            }
        }
        return diagonal_arr;
    }

    private static double[][] changeColumnsAndRows(double[][] arr) {
        double[][] ret_arr = new double[arr[0].length][arr.length];
        for (int c_i = 0; c_i < arr.length; c_i++) {
            double[] row = arr[c_i];
            for (int r_i = 0; r_i < row.length; r_i++) {
                ret_arr[r_i][c_i] = row[r_i];
            }
        }
        return ret_arr;
    }

    private static boolean[][] changeColumnsAndRows(boolean[][] arr) {
        boolean[][] ret_arr = new boolean[arr[0].length][arr.length];
        for (int c_i = 0; c_i < arr.length; c_i++) {
            boolean[] row = arr[c_i];
            for (int r_i = 0; r_i < row.length; r_i++) {
                ret_arr[r_i][c_i] = row[r_i];
            }
        }
        return ret_arr;
    }

    private static double[][] reverseColumns(double[][] arr) {
        int columns = arr.length;
        double[][] ret_arr = new double[arr.length][arr[0].length];
        for (int c_i = 0; c_i < arr.length; c_i++) {
            double[] row = arr[c_i];
            for (int r_i = 0; r_i < row.length; r_i++) {
                ret_arr[columns - c_i - 1][r_i] = row[r_i];
            }
        }
        return ret_arr;
    }

    private static double[][] reverseRows(double[][] arr) {
        int rows = arr[0].length;
        double[][] ret_arr = new double[arr.length][arr[0].length];
        for (int c_i = 0; c_i < arr.length; c_i++) {
            double[] row = arr[c_i];
            for (int r_i = 0; r_i < row.length; r_i++) {
                ret_arr[c_i][rows - r_i - 1] = row[r_i];
            }
        }
        return ret_arr;
    }

    /**
     * Adds the second matrix to the first.
     *
     * @param matrix1 the first matrix
     * @param matrix2 the second matrix
     * @return the resulting matrix
     */
    public static double[][] addMatrizes(double[][] matrix1, double[][] matrix2) {
        if (matrix1.length == matrix2.length && matrix1[0].length == matrix2[0].length) {
            for (int i = 0; i < matrix2.length; i++) {
                double[] row = matrix2[i];
                for (int j = 0; j < row.length; j++) {
                    matrix1[i][j] += row[j];
                }
            }
        }
        return matrix1;
    }

    /**
     * Multiplies the given matrix with the given scalar
     *
     * @param matrix given matrix
     * @param scalar given scalar
     * @return resulting matrix
     */
    public static double[][] multiplyMatrixWithScalar(double[][] matrix, double scalar) {
        for (int i = 0; i < matrix.length; i++) {
            double[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                matrix[i][j] += row[j] * scalar;
            }
        }
        return matrix;
    }

    private static double[][] addMiddleBonus(double[][] matrix, double middleBonusFactor) {
        double column_center = matrix.length / 2;
        double row_center = matrix[0].length / 2;
        double max_distance = Math.sqrt(Math.pow(column_center, 2) + Math.pow(row_center, 2));
        for (int i = 0; i < matrix.length; i++) {
            double[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                matrix[i][j] = matrix[i][j] + (Math.sqrt(Math.pow(i - column_center, 2) + Math.pow(j - row_center, 2)) / max_distance * middleBonusFactor);
            }
        }
        return matrix;
    }

    /**
     * Randomizes the given matrix.
     *
     * @param matrix given matrix
     * @param randomizationFactor fraction of the end value which is random
     * @return resulting matrix
     */
    public static double[][] ramndomize(double[][] matrix, double randomizationFactor) {
        if (randomizationFactor == 0) {
            return matrix;
        }
        Random random = new Random();
        double column_center = matrix.length / 2;
        double row_center = matrix[0].length / 2;
        double max_distance = Math.sqrt(Math.pow(column_center, 2) + Math.pow(row_center, 2));
        for (int i = 0; i < matrix.length; i++) {
            double[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                matrix[i][j] = matrix[i][j] + (random.nextDouble() - 0.5) * (matrix[i][j] * randomizationFactor) * 2;
            }
        }
        return matrix;
    }

    /**
     * Calculates the maximum of the given score matrix.
     *
     * @param matrix given score matrix
     * @param game game the board the matrix is calculated of belongs to
     * @return
     */
    public static MatrixValueContainer getMaximumValuePosition(double[][] matrix, Game game) {
        MatrixValueContainer lastMaximum = new MatrixValueContainer(-1, -1, 0);
        for (int i = 0; i < matrix.length; i++) {
            double[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                if (row[j] > lastMaximum.value) {
                    Stone.Position pos = new Stone.Position(i, j);
                    if (game.canStoneBePlacedAtPosition(pos)) {
                        lastMaximum = new MatrixValueContainer(i, j, row[j]);
                    }
                }
            }
        }
        return lastMaximum;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < boardArr.length; i++) {
            if (i > 0) {
                str += "\n";
            }
            int[] is = boardArr[i];
            for (int j = 0; j < is.length; j++) {
                str += (is[j] != 0 ? "X" : " ");
            }
        }
        return str;
    }

    /**
     * Converts a matrix of values value to one of rounded integer values, when
     * one of the values is smaller than -1 its replaced by -1.
     *
     * @param doubleMatrix matrix of double values
     * @return matrix of integer values
     */
    public static int[][] doubleToPositivizedIntMatrix(double[][] doubleMatrix) {
        int[][] retArr = new int[doubleMatrix.length][doubleMatrix[0].length];
        for (int i = 0; i < doubleMatrix.length; i++) {
            double[] row = doubleMatrix[i];
            for (int j = 0; j < row.length; j++) {
                retArr[i][j] = (int) Math.round(row[j] < 0 ? -1 : row[j]);
            }
        }
        return retArr;
    }

    /**
     * Container of a matrix value.
     */
    public static class MatrixValueContainer extends Stone.Position {

        private double value;

        /**
         * Constructs a matrix value container.
         *
         * @param column column number, 0 is the first column
         * @param row row number, 0 is the first row
         * @param value value at this position of the matrix
         */
        public MatrixValueContainer(int column, int row, double value) {
            super(column, row);
            this.value = value;
        }

        /**
         *
         * @return the value at this position of the matrix
         */
        public double getValue() {
            return value;
        }
    }
}
