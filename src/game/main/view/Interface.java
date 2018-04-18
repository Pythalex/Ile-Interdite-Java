package game.main.view;

import game.main.model.Ile;
import game.main.model.Player;

public abstract class Interface {

    public Interface(){

    }

    /**
     * Displays the given message.
     * @param msg the message to display
     */
    public abstract void displayMessage(String msg);

    /**
     * Displays the isle's state.
     * @param isle the isle to display
     */
    public abstract void displayState(Ile isle);

    /**
     * Displays the player's state.
     * @param p the player to display
     */
    public abstract void displayState(Player p);

    /**
     * Displays the given error.
     * @param err the error to display
     */
    public abstract void displayError(Exception err);
}
