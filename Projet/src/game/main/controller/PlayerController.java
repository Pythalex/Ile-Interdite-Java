package game.main.controller;

import game.main.model.Game;
import game.main.model.Player;

import java.util.Scanner;

public class PlayerController {

    public PlayerController(){

    }

    // Contains the action given by pressing the GUI button
    public String actionBuffer;

    /**
     * Asks the player to take an action. Returns
     * the string action.
     * @return the action taken
     */
    public String askAction(){
        actionBuffer = "";
        while (actionBuffer.equals("")){
            // Java doesn't seem to like while (condition); with multithreading,
            // so I added this micro sleep to fix the problem.
            Game.sleep(10);
        }
        return actionBuffer.toLowerCase();
    }
}
