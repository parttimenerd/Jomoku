package jomoku.ui.console;

import java.util.Scanner;
import jomoku.Player;
import jomoku.Stone;
import jomoku.ui.AbstractPlayer;
import jomoku.ui.AbstractUI;
import jomoku.ui.ParseHelper;

/**
 * Builds the UI of a Jomoku player.
 *
 * @author Johannes Bechberger
 */
public class ConsolePlayer extends AbstractPlayer {

    private Scanner scanner;

    /**
     * Constructs an console player.
     *
     * @param player the Jomoku player this object builds the UI for
     * @param ui UI this player belongs to
     */
    public ConsolePlayer(Player player, AbstractUI ui) {
        super(player, ui);
        scanner = new Scanner(System.in);
    }

    @Override
    public Stone.Position getNextPosition() {
        Stone.Position pos = null;
        String stringRep = isWhite() ? "White" : "Black";
        System.out.print(stringRep + " player: ");
        while (pos == null) {
            String input = scanner.nextLine();
            try {
                pos = ParseHelper.parseStringAsPosition(input);
                if (!this.getGame().canStoneBePlacedAtPosition(pos)) {
                    pos = null;
                    handleError("You can't place a stone at this position.");
                    System.out.print("Try again: ");
                }
            } catch (NumberFormatException ex) {
                handleError(ex.getMessage());
                System.out.print("Try again: ");
            }
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
