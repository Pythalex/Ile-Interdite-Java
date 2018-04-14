package game.main.model;

import java.io.IOException;

/**
 * The class managing the game rules
 */

public class Game
{
	// The game's isle
	public Ile isle;

	// List containing the players in the game
	public Player[] players;

	/**
	 * Creates a game given the dimension width, height, and the
	 * number of players.
	 * @param width The isle grid width
	 * @param height The isle grid height
	 * @param nbOfPlayers The number of players to include in
	 * 	the game
	 */
	public Game(int width, int height, int nbOfPlayers){
		isle = new Ile(this, width, height);
		players = new Player[nbOfPlayers];
		char name = 'A';
		for (int i = 0; i < nbOfPlayers; i++) {
			players[i] = new Player(this, "" + name);
			name = (char)((int)name + 1);
		}
	}

	/**
	 * Runs the game.
	 */
	public void runGame(){

		boolean end = false;
		boolean win = true;
		int tour = 1;

		while (!end) {

			System.out.println("Turn " + tour);
			tour++;

			for (Player p: players){
				System.out.println("Player " + p.name + " has to play.");
				makeAction(p);
			}

			isle.floodCases(3); // flood 3 cases

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

			System.out.println(isle.toString());
			System.out.println("Turn ended. Press input");
			waitForInput();
			System.out.println("Go for next turn.");
		}
	}

	/**
	 * Asks the player p to take action
	 * @param p the player who is playing
	 */
	public void makeAction(Player p){
		String action = p.takeAction();
		System.out.println("Player " + p.name + " : " + action);

		// end turn
		if (action.equals("pass")){
			return;
		}
	}

	/**
	 * DEBUG
	 * Wait for user input to continue
	 */
	public void waitForInput(){
		try {
			System.in.read();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public static void main(String[] args){
		Game game = new Game(3, 3, 2);
		game.runGame();
	}
}

