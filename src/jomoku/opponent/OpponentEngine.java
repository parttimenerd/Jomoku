package jomoku.opponent;

import java.util.Arrays;
import jomoku.Game;
import jomoku.Game.FieldType;
import jomoku.Player;
import jomoku.Stone;
import jomoku.Stone.Position;

/**
 * An opponent engine which is able to determine the best position of the next
 * stone. Contains all logic used by the Opponent class.
 *
 * @version 0.1
 * @author Johannes Bechberger
 */
public class OpponentEngine {

    private static final int BLOCKED_FIELD = -1;
    private static final int FREE_FIELD = 0;
    private static final int OPPONENT_FIELD = 1;
    private static final int JOKER_FIELD = 2;
    private static final int OWN_FIELD = 3;
    private Player player;
    private Game game;
    private Field[][] fieldArr;

    /**
     * Constructs an engine object. Please call the init method when player and
     * the belonging game is initialized.
     *
     * @param player player the constructed object is the opponent engine for
     */
    public OpponentEngine(Player player) {
        this.player = player;

    }

    /**
     * Initializes this engine.
     */
    public void init() {
        this.game = player.getGame();
        this.fieldArr = new Field[game.getNumberOfColumns()][game.getNumberOfRows()];
        FieldType[][] boardFieldTypes = game.getBoardFieldTypes();
        for (int i = 0; i < boardFieldTypes.length; i++) {
            FieldType[] row = boardFieldTypes[i];
            for (int j = 0; j < row.length; j++) {
                fieldArr[i][j] = new Field(new Position(i, j), convertFieldTypeToIntType(row[j]));
            }
        }
    }

    /**
     * Sets the type of the of the field at the given position to the given type
     * and calculates the changed field scores.
     *
     * @param position given position
     * @param type given field type
     */
    public void setStoneType(Stone.Position position, Game.FieldType type) {
        int typeInt = convertFieldTypeToIntType(type);
        int stonesToWin = game.getNumberOfStonesInARowToWin();
        int columns = fieldArr.length;
        int rows = fieldArr[0].length;
        int column = position.getX();
        int row = position.getY();
        fieldArr[column][row].setFieldType(typeInt);
        //Vertical row
        Field[] currentColumn = fieldArr[column];
        for (int i = 0; i < stonesToWin; i++) {
            if (row - i > 0) { //>0 to not double set the field type for i = 0
                currentColumn[row - i].vertical.setFieldType(-i, typeInt);
            }
            if (row + i < rows) {
                currentColumn[row + i].vertical.setFieldType(i, typeInt);
            }
        }
        fieldArr[column] = currentColumn;
        //Horizontal row
        for (int i = 0; i < stonesToWin; i++) {
            if (i - columns > 0) {
                fieldArr[column - i][row].horizontal.setFieldType(-i, typeInt);
            }
            if (column + i < columns) {
                fieldArr[column + i][row].horizontal.setFieldType(i, typeInt);
            }
        }
        //leftTopRightBottom diagonal
        for (int i = -stonesToWin + 1; i < stonesToWin; i++) {
            int curRow = column + i;
            int curColumn = row + i;
            if (curColumn < columns && curRow < rows && curColumn >= 0 && curRow >= 0) {
                fieldArr[curColumn][curRow].leftTopRightBottom.setFieldType(i, typeInt);
            }
        }
        //rightTopLeftBottom diagonal
        for (int i = -(stonesToWin - 1); i < stonesToWin; i++) {
            int curRow = column + i;
            int curColumn = row - i;
            if (curColumn < columns && curRow < rows && curColumn >= 0 && curRow >= 0) {
                fieldArr[curColumn][curRow].rightTopLeftBottom.setFieldType(i, typeInt);
            }
        }
    }

    /**
     * Returns the current matrix of scores.
     *
     * @return matrix of scores
     */
    public double[][] getScoreMatrix() {
        double[][] matrix = new double[fieldArr.length][fieldArr[0].length];
        for (int i = 0; i < matrix.length; i++) {
            double[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                matrix[i][j] = fieldArr[i][j].getScore();
            }
        }
        return matrix;
    }

    /**
     * Returns the current matrix of rounded scores.
     *
     * @return matrix of rounded scores
     */
    public int[][] getRoundedScoreMatrix() {
        int[][] matrix = new int[fieldArr.length][fieldArr[0].length];
        for (int i = 0; i < matrix.length; i++) {
            int[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                matrix[i][j] = (int) Math.round(fieldArr[i][j].getScore());
            }
        }
        return matrix;
    }

    /**
     * Examines the position on with the highest score.
     *
     * @return the position on the field with the highest score
     */
    public Position examineBestPosition() {
        Position maxPosition = new Position(fieldArr.length / 2, fieldArr[0].length / 2);
        double maxScore = 0;
        for (int i = 0; i < fieldArr.length; i++) {
            Field[] row = fieldArr[i];
            for (int j = 0; j < row.length; j++) {
                if (row[j].getScore() > maxScore) {
                    maxScore = row[j].getScore();
                    maxPosition = new Position(i, j);
                }
            }
        }
        return maxPosition;
    }

    private int convertFieldTypeToIntType(Game.FieldType type) {
        switch (type) {
            case FREE:
                return FREE_FIELD;
            case BLOCKED:
                return BLOCKED_FIELD;
            case JOKER:
                return JOKER_FIELD;
            case WHITE:
                return player.isWhite() ? OWN_FIELD : OPPONENT_FIELD;
            case BLACK:
                return !player.isWhite() ? OWN_FIELD : OPPONENT_FIELD;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void printScoreMatrix() {
        int[][] printMatrix = getRoundedScoreMatrix();
        for (int i = 0; i < printMatrix.length; i++) {
            int[] row = printMatrix[i];
            System.out.println(Arrays.toString(row));
        }
    }

    private class Field {

        private double middleBonusFactor = 0.01;
        private double ownMiddleBonusFactor;
        private Stone.Position position;
        private int fieldType;
        private Row leftTopRightBottom = new Row();
        private Row rightTopLeftBottom = new Row();
        private Row vertical = new Row();
        private Row horizontal = new Row();
        private double scoreSum = 0;
        private boolean fieldNeedsRecalculation = false;

        /**
         * Constructs a field object at the given position containing four rows
         * for each direction.
         *
         * @param position given position on the board
         * @param fieldType field type of this field
         */
        public Field(Position position, int fieldType) {
            this.position = position;
            this.fieldType = fieldType;
            this.ownMiddleBonusFactor = (1 - position.getDistance(fieldArr.length / 2, fieldArr[0].length / 2))
                    * middleBonusFactor;
            this.scoreSum = ownMiddleBonusFactor;
        }

        private void recalculateScore() {
            if (fieldNeedsRecalculation) {
                scoreSum = leftTopRightBottom.getScore()
                        + rightTopLeftBottom.getScore()
                        + vertical.getScore()
                        + horizontal.getScore();
                scoreSum += scoreSum * ownMiddleBonusFactor;
                fieldNeedsRecalculation = false;
            }
        }

        /**
         *
         * @return the score of this field
         */
        public double getScore() {
            recalculateScore();
            return scoreSum;
        }

        /**
         * Sets the field type of this field.
         *
         * @param type new field type
         */
        public void setFieldType(int type) {
            fieldType = type;
            vertical.setCenterFieldType(type);
            horizontal.setCenterFieldType(type);
            leftTopRightBottom.setCenterFieldType(type);
            rightTopLeftBottom.setCenterFieldType(type);
        }

        private class Row {

            private static final int BLOCKED_SCORE = -1;
            private double opponentFactor = 1.0;
            private int centerIndex;
            private int[] stones;
            private double score = 0;
            private double ownScore = 0;
            private double opponentScore = 0;
            private boolean isBlocked = false;

            /**
             * Constructs a new row.
             */
            public Row() {
                stones = new int[game.getNumberOfStonesInARowToWin() * 2 - 1];
                centerIndex = stones.length / 2;
            }

            /**
             * Recalculates the score of this row
             */
            private void recalculateScore() {
                if (isBlocked) {
                    score = BLOCKED_SCORE;
                    ownScore = BLOCKED_SCORE;
                    opponentScore = BLOCKED_SCORE;
                } else if (stones[centerIndex] != 0) {
                    isBlocked = true;
                    fieldNeedsRecalculation = true;
                } else {
                    ownScore = calculateScore(OWN_FIELD);
                    opponentScore = calculateScore(OPPONENT_FIELD);
                    score = ownScore + opponentScore * opponentFactor;
                }
            }

            //TODO write score calculation
            private double calculateScore(int ownFieldType) {
                int rowLength = calcFieldRowLengthAroundCenter(ownFieldType, false);
                int rowWithFreeSpacesLength = calcFieldRowLengthAroundCenter(ownFieldType, true);
                double res = Math.pow(2, rowLength) + Math.pow(1.1, rowWithFreeSpacesLength);
                return res;
            }

            private int calcFieldRowLengthAroundCenter(int ownFieldType, boolean withFreeSpace) {
                boolean blockedLeft = false;
                boolean blockedRight = false;
                int length = 0;
                for (int i = 1; i < centerIndex; i++) {
                    int curLeftType = stones[centerIndex - i];
                    int curRightType = stones[centerIndex + i];
                    if (!blockedLeft) {
                        if (isFieldOkay(curLeftType, ownFieldType, withFreeSpace)) {
                            length++;
                        } else {
                            blockedLeft = true;
                        }
                    }
                    if (!blockedRight) {
                        if (isFieldOkay(curRightType, ownFieldType, withFreeSpace)) {
                            length++;
                        } else {
                            blockedRight = true;
                        }
                    }
                }
                return length;
            }

            private boolean isFieldOkay(int type, int ownFieldType, boolean isFreeFieldOkay) {
                return type == ownFieldType || type == JOKER_FIELD || (isFreeFieldOkay && type == FREE_FIELD);
            }

            /**
             *
             * @return the calculated score of this row at the center, 0 if this
             * row is blocked
             */
            public double getScore() {
                return score;
            }

            /**
             *
             * @param position relative to the rows position, 0 is center
             * @param type field type
             */
            public void setFieldType(int position, int type) {
                stones[position + centerIndex] = type;
                recalculateScore();
                fieldNeedsRecalculation = true;
            }

            /**
             * Sets the field type of the center field.
             *
             * @param type new field type
             */
            public void setCenterFieldType(int type) {
                stones[centerIndex] = type;
                recalculateScore();
                fieldNeedsRecalculation = true;
            }

            /**
             *
             * @return Is this row blocked?
             */
            public boolean isIsBlocked() {
                return isBlocked;
            }
        }
    }
}
