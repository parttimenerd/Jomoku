package jomoku;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * A console based UI for this game
 *
 * @author Johannes Bechberger
 */
public class ConsoleUI {

    /**
     * Default: System.out
     */
    private PrintStream out;
    /**
     * Default: System.err
     */
    private PrintStream err;
    /**
     * Default: System.in
     */
    private InputStream in;
    private Game game;
    private Scanner scanner;
    private String[][] options_arr = {
        {"help", null, "Shows this help", ""},
        {"noIntro", null, "Starts the game without any introduction", ""},
        {"printNoBoards", null,
            "Prohibits the game from printing the current board on the console after a stone is placed on it.", ""},
        {"size", "15x15", "Size of the board being played on", "[number of columns]x[number of rows]"},
        {"xInARow", "5", "Minimum number of stones a player has to place in a row to win", "[number of stones]"},
        {"block", "",
            "Blocks the given positions for players to set their stone on.",
            "[column of blocked position]x[row of blocked position], [column]x[row] [,...]"},
        {"joker", "",
            "Places joker stones (a joker stone is a stone counting for the black and the white player) "
            + "at the given positions.",
            "[see -block]"}
    };
    private HashMap<String, String> options = new HashMap<>();
    private final String HELP_TEXT = "Argument name - Description - Format - Default value";
    private final String INTRODUCTION_TEXT = "++++Jomoku - A simple Gomoku game written in Java++++\n"
            + "Gomoku is an abstract strategy board game. Also called Gobang or Five in a Row, it is traditionally played with Go pieces (black and white stones) on a go board (19x19 intersections)[attribution needed]; however, because once placed, pieces are not moved or removed from the board, gomoku may also be played as a paper and pencil game. This game is known in several countries under different names.\n"
            + "Black plays first, and players alternate in placing a stone of their color on an empty intersection. The winner is the first player to get an unbroken row of five stones horizontally, vertically, or diagonally.\n"
            + "(http://en.wikipedia.org/wiki/Gomoku)\n"
            + "++++Usage++++\n"
            + "Just execute the JAR file on the console (for more informations use the argument \"-help\").\n"
            + "Then the player being the black player (X) or the player being the white player (O) is prompted to type in the position he or she want's to place a stone on. The format of this input is \"[column number]x[row number\"].\n"
            + "The board being played on is also printed on the console after every player action.\n"
            + "++++License++++\n"
            + "Copyright (C) 2012 Johannes Bechberger\n"
            + "This program is free software: you can redistribute it and/or modify\n"
            + "it under the terms of the GNU General Public License as published by\n"
            + "the Free Software Foundation, either version 3 of the License, or\n"
            + "(at your option) any later version.\n"
            + "<http://www.gnu.org/licenses/>";
    private final String WINNER_TEXT = "{{Player}}. You've won!!!";

    /**
     * Constructs the console UI of the game.
     *
     * @param out Normal ouput stream.
     * @param err Error ouput stream.
     */
    public ConsoleUI(PrintStream out, PrintStream err, InputStream in) {
        this.out = out;
        this.err = err;
        this.in = in;
    }

    /**
     * Constructs the default console UI of the game, with the standard IO
     * streams.
     */
    public ConsoleUI() {
        this(System.out, System.err, System.in);
    }

    /**
     * Starts the UI of the game
     *
     * @param args provided by the console
     */
    public void run(String[] args) {
        initOptionsMap(args);
        if (options.get("help") != null) {
            help();
        } else {
            if (options.get("noIntro") == null) {
                introduction();
            }
            playGame();
        }
    }

    private void initOptionsMap(String[] args) {
        for (int i = 0; i < options_arr.length; i++) {
            String[] option = options_arr[i];
            options.put(option[0], option[1]);
        }
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                String name = arg.substring(1);
                if (i < args.length - 1 && !args[i + 1].startsWith("-")) {
                    options.put(name, args[i + 1]);
                    i += 1;
                } else {
                    options.put(name, "true");
                }
            }
        }
    }

    private void help() {
        out.println(INTRODUCTION_TEXT);
        out.println(HELP_TEXT);
        for (String[] arr : options_arr) {
            out.println("-" + arr[0] + " - " + arr[2] + " - " + arr[3] + (arr[1] != null && arr[1] != "" ? (" - " + arr[1]) : ""));
        }
    }

    private void introduction() {
        out.println(INTRODUCTION_TEXT);
    }

    /**
     * Input loop
     */
    private void playGame() {
        initGame();
        printBoard();
        scanner = new Scanner(in);
        boolean is_white_player = new Random().nextBoolean();
        String input;
        do {
            String player_rep = (is_white_player ? "white" : "black");
            out.print(player_rep.toUpperCase() + " Player: ");
            input = scanner.nextLine();
            switch (input) {
                case "exit":
                    System.exit(0);
                    break;
                case "help":
                    help();
                    break;
                case "intro":
                    introduction();
                    break;
                default:
                    try {
                        Stone.Position pos = parseStringAsPosition(input);
                        if (game.placeStone(pos, is_white_player)) {
                            is_white_player = !is_white_player;
                            printBoard();
                        } else {
                            err.println("You can't place a stone at this position.");
                        }
                    } catch (NumberFormatException ex) {
                        err.println(ex.getMessage());
                    }
            }
            Player winner = game.winner();
            if (winner != null) {
                out.println(WINNER_TEXT.replaceAll("\\{\\{Player\\}\\}", player_rep.toUpperCase())
                        .replaceAll("\\{\\{player\\}\\}", player_rep));
                System.exit(0);
            }
        } while (true);
    }

    private void initGame() {
        String size_arg = options.get("size");
        int stones_to_win = 5;
        try {
            stones_to_win = Integer.parseInt(options.get("xInARow"));
        } catch (Exception ex) {
            handleSevereError("xInARow parameter argument has the wrong format.");
        }
        try {
            int[] arr = parseStringAsIntegerValuePair(size_arg);
            game = new Game(arr[0], arr[1], stones_to_win);
        } catch (Exception e) {
            handleSevereError("Size parameter argument has the wrong format.");
        }
        initBlockedFields();
        initJokerFields();
    }

    /**
     * Important: Call this method only after the variable game is
     * instantiated!!!
     */
    private void initBlockedFields() {
        try {
            game.blockStonePositions(parseStringAsPositionArray(options.get("block")));
        } catch (NumberFormatException ex) {
            handleSevereError("Block parameter argument has the wrong format");
        }
    }

    /**
     * Important: Call this method only after the variable game is
     * instantiated!!!
     */
    private void initJokerFields() {
        try {
            game.placeJokerStones(parseStringAsPositionArray(options.get("joker")));
        } catch (NumberFormatException ex) {
            handleSevereError("Joker parameter argument has the wrong format.");
        }
    }

    private Stone.Position[] parseStringAsPositionArray(String str) throws NumberFormatException {
        if (str != null && !str.isEmpty()) {
            str = str.replace(" ", "");
            if (!str.matches("(\\d+x\\d+,)*\\d+x\\d+$")) {
                throw new NumberFormatException("Input has wrong format - expected \"(\\d+x\\d+,)*\\d+x\\d+\".");
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

    private int[] parseStringAsIntegerValuePair(String str) throws NumberFormatException {
        if (str.matches("\\d+x\\d+$")) {
            String[] arr = str.split("x");
            return new int[]{
                        Integer.parseInt(arr[0]),
                        Integer.parseInt(arr[1])
                    };
        } else {
            throw new NumberFormatException("Input has wrong format - expected \"\\d+x\\d+\".");
        }
    }

    private Stone.Position parseStringAsPosition(String str) throws NumberFormatException {
        int[] arr = parseStringAsIntegerValuePair(str);
        return new Stone.Position(arr[0], arr[1]);
    }

    private void handleError(String message) {
        err.println(message + " Use -help to get help.");
    }

    private void handleSevereError(String message) {
        handleError(message);
        System.exit(1);
    }

    /**
     *
     * @return whether it's actually printed or not according to the options
     */
    private boolean printBoard() {
        if (options.get("printNoBoards") != null) {
            return false;
        }
        printBoard(game.getBoardFieldTypes());
        return true;
    }

    private void printBoard(Game.FieldType[][] array) {
        int c_str_length = (array.length - 1 + "").length();
        int r_str_length = array.length >= 1 ? (array[0].length - 1 + "").length() : 0;
        //Print column indezes
        for (int i = 0; i < c_str_length; i++) {
            printWhiteSpace(r_str_length);
            for (int j = 0; j < array.length; j++) {
                String str = " ";
                String j_str = j + "";
                if (c_str_length - i <= j_str.length()) {
                    str = j_str.charAt(i - (c_str_length - j_str.length())) + "";
                }
                out.print(" | " + str);
            }
            out.println();
        }
        //Print rest of table
        for (int j = 0; j < array[0].length; j++) {
            String r_str = j + "";
            printWhiteSpace(r_str_length - r_str.length());
            out.print(r_str);
            for (int i = 0; i < array.length; i++) {
                out.print(" | " + getStringRepresentationOfFieldType(array[i][j]));
            }
            out.println();
        }
    }

    private void printWhiteSpace(int length) {
        for (int i = 0; i < length; i++) {
            out.print(" ");
        }
    }

    private String getStringRepresentationOfFieldType(Game.FieldType type) {
        switch (type) {
            case BLACK:
                return "X";
            case WHITE:
                return "O";
            case JOKER:
                return "+";
            case BLOCKED:
                return "#";
            default:
                return " ";
        }
    }
}
