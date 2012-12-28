package jomoku.ui;

import jomoku.Stone;

/**
 * Utility methods helping to parse console input by the user.
 * 
 * @author Johannes Bechberger
 * @version 1.0
 */
public class ParseHelper {

    /**
     * Parses a string as a position.
     * 
     * @param str string, format: [column number]x[row number]
     * @return the position the string represents
     * @throws NumberFormatException the string has the wrong format
     */
    public static Stone.Position parseStringAsPosition(String str) throws NumberFormatException {
        int[] arr = parseStringAsIntegerValuePair(str);
        return new Stone.Position(arr[0], arr[1]);
    }

    /**
     * Parses a string as a pair of integer values.
     * 
     * @param str string, format: [first number]x[second number]
     * @return [first number, second number]
     * @throws NumberFormatException the string has the wrong format
     */
    public static int[] parseStringAsIntegerValuePair(String str) throws NumberFormatException {
        if (str.matches("\\d+x\\d+$")) {
            String[] arr = str.split("x");
            return new int[]{Integer.parseInt(arr[0]), Integer.parseInt(arr[1])};
        } else {
            throw new NumberFormatException("Input has wrong format - expected \"\\d+x\\d+\".");
        }
    }

    /**
     * Parses a string as an array of positions.
     * 
     * @param str string, format: [column number of first position]x[row number of first position], [...]x[...], ...
     * @return array of positions
     * @throws NumberFormatException the string has the wrong format
     */
    public static Stone.Position[] parseStringAsPositionArray(String str) throws NumberFormatException {
        if (str != null && !str.isEmpty()) {
            str = str.replace(" ", "");
            if (!str.matches("(\\d+x\\d+,)*\\d+x\\d+$")) {
                throw new NumberFormatException("Input has wrong format" + " - expected \"(\\d+x\\d+,)*\\d+x\\d+\".");
            }
            String[] pos_str_arr = str.split(",");
            Stone.Position[] pos_arr = new Stone.Position[pos_str_arr.length];
            for (int i = 0; i < pos_str_arr.length; i++) {
                pos_arr[i] = parseStringAsPosition(pos_str_arr[i]);
            }
            return pos_arr;
        }
        return new Stone.Position[0];
    }

}
