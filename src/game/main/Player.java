package game.main;


/**
 * Represents a player.
 */
public class Player
{
	// Player's position on the grid	
	public int x;
	public int y;

	// Player's name
	public String name;

	// The artifacts carried by the player : W - F - E - A 
	public boolean[] artifact;

	// The keys carried by the player, same order as the artifacts
	public boolean[] keys;

	// The master game instance
	public Game gameMaster;

	/**
	 * Creates and returns a player given a master
	 * game instance.
	 *
	 * @param master The master game instance
	 */
	public Player(Game master){
		gameMaster = master;
	}

}

