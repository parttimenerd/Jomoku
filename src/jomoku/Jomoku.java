/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jomoku;

import jomoku.ui.UI;
import jomoku.ui.console.ConsoleUI;

/**
 * Main class.
 * 
 * @author Johannes Bechberger
 * @version 1.0
 */
public class Jomoku {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new UI().run(args);
    }
}
