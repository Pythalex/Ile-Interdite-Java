package game.main.controller;

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

    /**
     * Asks the player to take an action. Returns
     * the string action.
     * @return the action taken
     */
    public String askAction(){
        return commandLineAction();
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
