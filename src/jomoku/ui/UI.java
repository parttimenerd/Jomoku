package jomoku.ui;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Random;
import jomoku.Board;
import jomoku.Game;
import jomoku.Player;
import jomoku.Stone;
import jomoku.ui.console.ConsoleUI;
import jomoku.ui.gui.GUI;

/**
 * Main UI, handling the console arguments.
 * 
 * @author Johannes Bechberger
 * @version 1.0
 */
public class UI {

    /**
     * Default: System.out.
     */
    private PrintStream out;
    /**
     * Default: System.err.
     */
    private PrintStream err;
    private Game game;
    private AbstractUI abstractUI;
    private String[][] optionsArr = {
        {"help", null, "Shows this help", ""},
        {"gui", null, "Shows a gui", ""},
        {"noIntro", null, "Starts the game without any introduction", ""},
        {"printNoBoards", null,
            "Prohibits the game from printing the current board on the console after a stone is placed on it.", ""},
        {"size", "19x19", "Size of the board being played on", "[number of columns]x[number of rows]"},
        {"xInARow", "5", "Minimum number of stones a player has to place in a row to win", "[number of stones]"},
        {"block", "",
            "Blocks the given positions for players to set their stone on.",
            "[column of blocked position]x[row of blocked position], [column]x[row] [,...]"},
        {"joker", "",
            "Places joker stones (a joker stone is a stone counting for the black and the white player) "
            + "at the given positions.",
            "[see -block]"},
        {"whiteAuto", null, "The white player is played automatically played", ""},
        {"blackAuto", null, "The black player is played automatically", ""},
        {"showAutoDebugMatrix", null, "Shows the debug matrix for every automatic action", ""}
    };
    private HashMap<String, String> options = new HashMap<>();
    /**
     * (Beginning of the) help text
     */
    public final String HELP_TEXT = "Argument name - Description - Format - Default value";
    /**
     * Introduction text
     */
    public final String INTRODUCTION_TEXT = "++++Jomoku - A simple Gomoku game written in Java++++\n"
            + "Gomoku is an abstract strategy board game. Also called Gobang or Five in a Row, it is traditionally"
            + " played with Go pieces (black and white stones) "
            + "on a go board (19x19 intersections)[attribution needed];"
            + " however, because once placed, pieces are not moved or removed from"
            + " the board, gomoku may also be played as a paper and pencil game. "
            + "This game is known in several countries under different names.\n"
            + "Black plays first, and players alternate in placing a stone of their "
            + "color on an empty intersection. The winner is the first player to get"
            + " an unbroken row of five stones horizontally, vertically, or diagonally.\n"
            + "(http://en.wikipedia.org/wiki/Gomoku)\n"
            + "++++Usage++++\n"
            + "Just execute the JAR file on the console (for more informations use the argument \"-help\").\n"
            + "Then the player being the black player (X) or the player being the white player (O) is prompted"
            + " to type in the position he or she want's to place a stone on. The format of this input"
            + " is \"[column number]x[row number\"].\n"
            + "The board being played on is also printed on the console after every player action.\n"
            + "++++License++++\n"
            + "Copyright (C) 2012 Johannes Bechberger\n"
            + "This program is free software: you can redistribute it and/or modify\n"
            + "it under the terms of the GNU General Public License as published by\n"
            + "the Free Software Foundation, either version 3 of the License, or\n"
            + "(at your option) any later version.\n"
            + "<http://www.gnu.org/licenses/>";

    /**
     * Constructs the console UI of the game.
     *
     * @param out Normal output stream.
     * @param err Error output stream.
     */
    public UI(PrintStream out, PrintStream err) {
        this.abstractUI = abstractUI;
        this.out = out;
        this.err = err;
    }

    /**
     * Constructs the default console UI of the game, with the standard IO
     * streams.
     */
    public UI() {
        this(System.out, System.err);
    }

    /**
     * Prints the help text with the introduction text and an listing of the
     * available console arguments.
     */
    public void help() {
        out.println(INTRODUCTION_TEXT);
        out.println(HELP_TEXT);
        for (String[] arr : optionsArr) {
            out.println("-" + arr[0] + " - " + arr[2] + " - " + arr[3]
                    + (arr[1] != null && arr[1] != "" ? (" - " + arr[1]) : ""));
        }
    }

    /**
     * Prints the introduction text.
     */
    public void introduction() {
        out.println(INTRODUCTION_TEXT);
    }

    /**
     * Inits the game and starts it (it shows only the help if the program is
     * called with the -help argument).
     *
     * @param args console arguments
     */
    public void run(String[] args) {
        initGame(args);
        if (options.get("help") != null) {
            help();
        } else {
            abstractUI.init(options.get("whiteAuto") != null, options.get("blackAuto") != null);
            playGame();
        }
    }

    private void initGame(String[] args) {
        initOptionsMap(args);
        String size_arg = options.get("size");
        int stones_to_win = 5;
        try {
            stones_to_win = Integer.parseInt(options.get("xInARow"));
        } catch (Exception ex) {
            handleSevereError("xInARow parameter argument has the wrong format.");
        }
        try {
            int[] arr = ParseHelper.parseStringAsIntegerValuePair(size_arg);
            game = new Game(arr[0], arr[1], stones_to_win);
            initBlockedFields();
            initJokerFields();
            abstractUI = getAbstractUIAccordingToOptions(game);
        } catch (Exception e) {
            handleSevereError("Size parameter argument has the wrong format.");
        }
    }

    private AbstractUI getAbstractUIAccordingToOptions(Game game) {
        if (options.get("gui") != null) {
            return new GUI(this, game);
        } else {
            return new ConsoleUI(this, game);
        }
    }

    private void playGame() {
        boolean is_white_player = new Random().nextBoolean();
        AbstractPlayer current;
        AbstractPlayer other;
        while (true) {
            if (is_white_player) {
                current = abstractUI.getWhitePlayer();
                other = abstractUI.getBlackPlayer();
            } else {
                current = abstractUI.getBlackPlayer();
                other = abstractUI.getWhitePlayer();
            }
            Stone.Position nextPosition = current.getNextPosition();
            game.placeStone(current.getPlayer(), nextPosition);
            abstractUI.handlePlayerActionOccured(current, nextPosition);
            other.otherPlayerAction(nextPosition);
            is_white_player = !is_white_player;
            Player winner = game.winner();
            boolean replay;
            if (winner != null || game.drawn()) {
                if (winner != null) {
                    replay = abstractUI.handleWin(winner.isWhite() ? abstractUI.getWhitePlayer() : abstractUI.getBlackPlayer());
                } else {
                    replay = abstractUI.handleDrawn();
                }
                if (replay) {
                    replay();
                } else {
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Configures the UI to replay.
     */
    public void replay() {
        game = new Game(game.getNumberOfColumns(), game.getNumberOfRows(), game.getNumberOfStonesInARowToWin());
        initBlockedFields();
        initJokerFields();
        abstractUI.replay(game);
        Board.resetNumberOfFreeFields(game);
    }

    private void initOptionsMap(String[] args) {
        for (int i = 0; i < optionsArr.length; i++) {
            String[] option = optionsArr[i];
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

    /**
     * Important: Call this method only after the variable game is
     * instantiated!!!
     */
    private void initBlockedFields() {
        try {
            game.blockStonePositions(ParseHelper.parseStringAsPositionArray(options.get("block")));
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
            game.placeJokerStones(ParseHelper.parseStringAsPositionArray(options.get("joker")));
        } catch (NumberFormatException ex) {
            handleSevereError("Joker parameter argument has the wrong format.");
        }
    }

    /**
     * Prints the error message.
     * 
     * @param message error message
     */
    public void handleError(String message) {
        err.println(message + " Use -help to get help.");
    }

    /**
     * Prints the error message and aborts the program.
     * 
     * @param message error message
     */
    public void handleSevereError(String message) {
        handleError(message);
        System.exit(1);
    }

    /**
     * Returns the value of the option.
     * 
     * @param name option name
     * @return option value
     */
    public String getOption(String name) {
        return options.get(name);
    }
}
