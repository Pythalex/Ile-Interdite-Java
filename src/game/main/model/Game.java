package game.main.model;

import game.main.view.CLInterface;
import game.main.view.Interface;

import java.io.IOException;
import java.util.Random;

/**
 * The class managing the game rules
 */

public class Game
{
	// The game's isle
	public Ile isle;

	// List containing the players in the game
	public Player[] players;

	// The interface
	public Interface intfc;

	// obtainedKey
	public boolean[] keys;

	/**
	 * Creates a game given the dimension width, height, and the
	 * number of players.
	 * @param width The isle grid width
	 * @param height The isle grid height
	 * @param nbOfPlayers The number of players to include in
	 * 	the game
	 */
	public Game(int width, int height, int nbOfPlayers){
		// Create the isle
		isle = new Ile(this, width, height);

		// Create the players
		players = new Player[nbOfPlayers];
		char name = 'A';
		for (int i = 0; i < nbOfPlayers; i++) {
			players[i] = new Player(this, "" + name);
			name = (char)((int)name + 1);
		}

		// The chosen interface
		intfc = new CLInterface();

		// the keys
		keys = new boolean[4];
		for (int i = 0; i < 4; i++)
			keys[i] = false;
	}

	/**
	 * Runs the game.
	 */
	public void runGame(){

		boolean end = false;
		boolean win = true;
		int tour = 1;

		intfc.displayState(isle);

		while (!end) {

			intfc.displayMessage("Turn " + tour);
			tour++;

			// player turns
			for (Player p: players){

				printPlayersStates();

				intfc.displayMessage("Player " + p.name + " has to play.");

				// 3 actions at max
				makeAction(p, 3);

				// search for keys
				searchKeys(p);

				// flood 3 cases
				isle.floodCases(3);

				// defeat check
				boolean allSubmerged = true;
				for (Case c: isle.cases){
					if (c.state != State.submerged){
						allSubmerged = false;
						break;
					}
				}
				if (allSubmerged)
					end = true;

				intfc.displayState(isle);
			}

			intfc.displayMessage("Turn ended. Press input");
			waitForInput();
			intfc.displayMessage("Go for next turn.");
		}
	}

	/**
	 * Asks the player p to take action
	 * @param p the player who is playing
	 * @param actionLeft The number of action left
	 */
	public void makeAction(Player p, int actionLeft){

		// While the player can do something
		while (actionLeft > 0) {

			// Get an action
			String action = p.takeAction();
			intfc.displayMessage("Player " + p.name + " : " + action);

			/*
				If the action is valid, the game computes the result.
				If it's not, the player is notified in testAction and
				is asked an action again.
			 */
			if (testAction(action, p)) {

				if (action.equals("pass")) {
					return;
				} else if (action.equals("moveup")) {
					p.y--;
				} else if (action.equals("movedown")) {
					p.y++;
				} else if (action.equals("moveleft")) {
					p.x--;
				} else if (action.equals("moveright")) {
					p.x++;
				} else if (action.equals("searchkey")) {

				} else {
					intfc.displayError(new Exception("Action " + action + " is invalid but passed " +
							"the checks. Game will now shut down."));
					System.exit(1);
				}

				actionLeft--;
			}
		}
	}

	/**
	 * Checks whether the action is valid.
	 * Displays a message if it isn't.
	 * @param action the action to test
	 * @param p the player taking the action
	 * @return action is valid ?
	 */
	public boolean testAction(String action, Player p){
		if (action.equals("pass")){
			return true;
		}
		else if (action.equals("moveup")){
			if (isle.playerCanMove(p.x, p.y - 1)) {
				return true;
			}
			else {
				intfc.displayMessage("You cannot move up.");
				return false;
			}
		}
		else if (action.equals("movedown")){
			if (isle.playerCanMove(p.x, p.y + 1)) {
				return true;
			}
			else {
				intfc.displayMessage("You cannot move down.");
				return false;
			}
		}
		else if (action.equals("moveleft")){
			if (isle.playerCanMove(p.x - 1, p.y)) {
				return true;
			}
			else {
				intfc.displayMessage("You cannot move to the left.");
				return false;
			}
		}
		else if (action.equals("moveright")) {
			if (isle.playerCanMove(p.x + 1, p.y)) {
				return true;
			}
			else {
				intfc.displayMessage("You cannot move to the right.");
				return false;
			}
		} else {
			intfc.displayMessage("Action not understood.");
			return false;
		}
	}

	/**
	 * Makes the player look for a key on his
	 * current case.
	 * @param p the player
	 */
	public void searchKeys(Player p){
		// player position
		int x = p.x;
		int y = p.y;

		// 1/3 change for all events
		Random rdm = new Random();
		int dice = rdm.nextInt(3);

		switch (dice){
			// find a key
			case 0:
				// choose a key which is not own by a player
				int chosenKey = -1;
				do {
					chosenKey = rdm.nextInt(4);
				} while(keys[chosenKey]);
				keys[chosenKey] = true;
				p.keys[chosenKey] = true;
				String elm = (chosenKey == 0 ? "water" : (chosenKey == 1 ? "fire" : (chosenKey == 2 ? "earth" : "air")));
				intfc.displayMessage("Player " + p.name + " found the " + elm + " key.");
				break;
			// find nothing
			case 1:
				intfc.displayMessage("Player " + p.name + " found nothing in the area.");
				break;
			// flood the case
			default:
				intfc.displayMessage("Damn ! Player " + p.name + " triggered a trap, water comes from " +
					"nowhere and the entire area is suddenly flooded !");
				isle.cases[p.y * isle.width + p.x].flood();
				break;
		}
	}

	/**
	 * Prints the players state, i.e. their
	 * position, keys, artifacts ...
	 */
	public void printPlayersStates(){
		for (Player p: players)
			intfc.displayState(p);
	}

	/**
	 * DEBUG
	 * Wait for user input to continue
	 */
	public void waitForInput(){
		try {
			System.in.read();
		} catch (IOException e) {
			intfc.displayError(e);
		}
	}

	public static void main(String[] args){
		Game game = new Game(3, 3, 2);
		game.runGame();
	}
}

