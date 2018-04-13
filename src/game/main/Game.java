package game.main;


import java.io.IOError;
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
		for (Player p: players)
			p = new Player(this);
	}

	/**
	 * Runs the game.
	 */
	public void runGame(){

		boolean end = false;
		boolean win = true;
		int tour = 1;

		while (!end) {

			System.out.println("Tour " + tour);
			tour++;

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
			waitForInput();
		}
	}

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

