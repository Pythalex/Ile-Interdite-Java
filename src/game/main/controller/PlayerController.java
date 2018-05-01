package game.main.controller;

import game.main.model.Game;
import game.main.model.Player;

import java.util.Scanner;

public class PlayerController {

    // The player ID, is used to identify the player when asking for action
    public String ID;

    public PlayerController(){
        this.ID = "undefined";
    }

    public PlayerController(String ID){
        this.ID = ID;
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

    /**
     * Asks the player to take an action via
     * the command line.
     * @return the action taken
     */
    public String commandLineAction(){
        System.out.print("Player " + ID + ", your action : ");
        Scanner scan = new Scanner(System.in);
        return scan.next().toLowerCase();
    }
}
