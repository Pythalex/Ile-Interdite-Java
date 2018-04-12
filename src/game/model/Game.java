package game.model;

import java.util.HashSet;
import java.util.Set;


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

	public static void main(String[] args){
		Game game = new Game(10, 10, 2);
	}
}

