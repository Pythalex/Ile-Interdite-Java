package game.main.model;


import game.main.controller.PlayerController;

/**
 * Represents a player.
 */
public class Player
{
	// Player's position on the grid	
	public int x;
	public int y;

	// Player's name
	public String name = "undefined";

	// The artifacts carried by the player : W - F - E - A 
	public boolean[] artifact;

	// The keys carried by the player, same order as the artifacts
	public boolean[] keys;

	// the player controller associated with the player
	public PlayerController pc;

	// The master game instance
	public Game gameMaster;

	/**
	 * Creates and returns a player given a master
	 * game instance. The name will be 'undefined'.
	 * To name the player, set it manually or use
	 * Player(Game master, String name) instead.
	 *
	 * @param master The master game instance
	 */
	public Player(Game master){
		gameMaster = master;

		artifact = new boolean[4];
		keys = new boolean[4];

		pc = new PlayerController(name);
	}

	/**
	 * Creates and returns a player given a master
	 * game instance. The player is named with
	 * the name given in argument.
	 *
	 * @param master The master game instance
	 */
	public Player(Game master, String name){
		this(master);
		this.name = name;
		pc.ID = name;
	}

	/**
	 * The player takes an action.
	 * @return the action
	 */
	public String takeAction(){
		return pc.askAction();
	}


	/**
	 * Initiates boolean array items
	 */
	public void initiateItems(){
		for (int i = 0; i < 4; i++){
			artifact[i] = false;
			keys[i] = false;
		}
	}

}

