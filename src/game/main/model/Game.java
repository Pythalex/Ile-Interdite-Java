package game.main.model;

import game.main.view.CLInterface;
import game.main.view.GUInterface;
import game.main.view.Interface;
import javafx.util.Pair;

import javax.lang.model.type.ArrayType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

	// artifacts
	List<Event> artifacts = new ArrayList<>(Arrays.asList(Event.Element_water, Event.Element_fire,
			Event.Element_earth, Event.Element_air));

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
		generateIsland();

		// find the helicopter position to place the players on it
		Case helicopCase = isle.foundCaseByEvent(Event.Helicopter);

		// Create the players
		players = new Player[nbOfPlayers];
		char name = 'A';
		for (int i = 0; i < nbOfPlayers; i++) {
			players[i] = new Player(this, "" + name);
			name = (char)((int)name + 1);

			players[i].x = helicopCase.x;
			players[i].y = helicopCase.y;

			helicopCase.players.add(players[i]);
		}

		// the keys
		keys = new boolean[4];
		for (int i = 0; i < 4; i++)
			keys[i] = false;

		// the interface
		intfc = new GUInterface(this);
	}

	/**
	 * Generates the island to include all the required element for a game,
	 * like heliport, artifacts.
	 */
	public void generateIsland(){

		Random rdm = new Random();
		List<Pair<Integer, Integer>> chosenPositions = new ArrayList<>();

		Pair<Integer, Integer> pos;

		// choose a place for heliport
		pos = generateRandomPosition(0, isle.width, 0, isle.height);
		chosenPositions.add(pos);
		// set the case to heliport
		isle.cases[pos.getValue() * isle.width + pos.getKey()].event = Event.Helicopter;

		// choose places for artifacts
		List<Event> artifacts = new ArrayList<Event>(Arrays.asList(Event.Element_water, Event.Element_fire,
				Event.Element_earth, Event.Element_air));
		for (Event artifact: artifacts){
			do {
				pos = generateRandomPosition(0, isle.width, 0, isle.height);
			} while (checkAlreadyChosenPosition(chosenPositions, pos));
			chosenPositions.add(pos);

			// Set the case to artifact
			isle.cases[pos.getValue() * isle.width + pos.getKey()].event = artifact;
		}
	}

	/**
	 * Runs the game.
	 */
	public void runGame(){

		boolean end = false;
		boolean win = false;
		int tour = 1;

		intfc.displayState(isle);

		Case heliport = isle.foundCaseByEvent(Event.Helicopter);
		Case elementWater = isle.foundCaseByEvent(Event.Element_water);
		Case elementFire = isle.foundCaseByEvent(Event.Element_fire);
		Case elementEarth = isle.foundCaseByEvent(Event.Element_earth);
		Case elementAir = isle.foundCaseByEvent(Event.Element_air);
		List<Case> elementsCases = new ArrayList<>(Arrays.asList(elementWater, elementEarth, elementEarth, elementAir));

		while (!end) {

			intfc.displayMessage("Turn " + tour);
			tour++;

			// player turns
			for (Player p: players){

				// Don't play turn if game is finished
				if (end)
					break;

				printPlayersStates();

				intfc.displayMessage("Player " + p.name + " has to play.");

				// 3 actions at max
				makeAction(p, 3);

				// search for keys
				searchKeys(p);

				// flood 3 cases
				isle.floodCases(3);

				// Update the view state
				intfc.displayState(isle);

				/* DEFEAT CHECKS */

				// if one player is on a submerged case, he's dead and the game is finished
				Player submergedPlayer = checkSubmergedPlayer();
				if (submergedPlayer != null) {
					end = true;
					intfc.displayMessage("Player " + submergedPlayer.name + " drowned.");
				}

				// If the helicopter is submerged, the game is finished
				if (heliport.isSubmerged()){
					end = true;
					intfc.displayMessage("The helicopter got submerged, the players are trap on the island !");
				}

				// If any artifact is submerged, the game is finished
				for (Case artifact: elementsCases){
					if (artifact.isSubmerged()){
						end = true;
						intfc.displayMessage("The " + artifact.event.getName() +
								" got submerged, the players lose !");
						break;
					}
				}

				// Victory test
				if (allGatheredElements() && everyoneAtHeliport()){
					end = true;
					win = true;
				}
			}

			// Next turn
			if (!end) {
				intfc.displayMessage("Turn ended. Press input");
				waitForInput();
				intfc.displayMessage("Go for next turn.");
			}
		}

		if (win){
			System.out.println("Congrats ! You won !");
		} else {
			System.out.println("Too bad, you lose.");
		}
		System.out.println("Press enter to quit.");
		waitForInput();
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
					removePlayerRefFromCase(p);
					p.y--;
					addPlayerRefToCase(p);
				} else if (action.equals("movedown")) {
					removePlayerRefFromCase(p);
					p.y++;
					addPlayerRefToCase(p);
				} else if (action.equals("moveleft")) {
					removePlayerRefFromCase(p);
					p.x--;
					addPlayerRefToCase(p);
				} else if (action.equals("moveright")) {
					removePlayerRefFromCase(p);
					p.x++;
					addPlayerRefToCase(p);
				} else if (action.equals("dry")){
					getPlayerCase(p).dry();
				} else if (action.equals("getartifact")) {
					// Get the player's case
					Case c = getPlayerCase(p);
					// give artifact
					p.artifact[artifacts.indexOf(c.event)] = true;
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
	 * Displays a message to pc if it isn't.
	 * @param action the action to test
	 * @param p the player taking the action
	 * @return action is valid ?
	 */
	public boolean testAction(String action, Player p){

		Case c = getPlayerCase(p);

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
		} else if (action.equals("dry")) {
			if (c.isDry()){
				intfc.displayMessage("Your current case is already dry.");
				return false;
			} else if (c.isSubmerged()){
				intfc.displayMessage("Your current case is submerged.");
				return false;
			} else {
				return true;
			}
		} else if (action.equals("getartifact")) {
			// Get the player's case

			// Get the artifacts event objects
			Event caseEvent = c.event;

			// If the case event is an artifact
			if (artifacts.contains(caseEvent)) {
				// Get the event index
				int index = artifacts.indexOf(caseEvent);
				// Player has the key ?
				if (p.keys[index]){
					intfc.displayMessage("Player " + p.name + " got the " + caseEvent.getName());
					return true;
				} else {
					intfc.displayMessage("You don't have the " + caseEvent.getName() + " key.");
				}
			} else {
				intfc.displayMessage("There is no artifact on your current case.");
			}

			return false;
		} else {
			intfc.displayMessage("Action not understood.");
			return false;
		}
	}

	/**
	 * Adds the player reference to the case player list.
	 * @param p the player
	 */
	public void removePlayerRefFromCase(Player p){
		getPlayerCase(p).players.remove(p);
	}

	/**
	 * Removes the player reference from the case player list.
	 * @param p the player
	 */
	public void addPlayerRefToCase(Player p){
		getPlayerCase(p).players.add(p);
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
				getPlayerCase(p).flood();
				break;
		}
	}

	/**
	 * Checks if a player is on a submerged case. Returns
	 * the first found player's ref, or null if everybody is
	 * on a solid or flooded case.
	 * @return returns the player ref or null
	 */
	public Player checkSubmergedPlayer(){
		for (Player p: players)
			if (getPlayerCase(p).isSubmerged())
				return p;
		return null;
	}

	/**
	 * Checks if all elements have been gathered.
	 * @return true if all elements have been gathered.
	 */
	public boolean allGatheredElements(){
		boolean[] arts = {false, false, false, false};

		for (Player p: players)
			for (int i = 0; i < arts.length; i++)
				arts[i] = arts[i] || p.artifact[i];

		boolean all = true;
		for (boolean b: arts)
			all = all && b;

		return all;
	}

	/**
	 * @return whether every players is on the heliport.
	 */
	public boolean everyoneAtHeliport(){
		boolean everyone = true;
		Case helicopCase = isle.foundCaseByEvent(Event.Helicopter);

		for (Player p: players)
			everyone = everyone && (p.x == helicopCase.x && p.y == helicopCase.y);

		return everyone;
	}

	/**
	 * Returns the ref of the case on which is positioned the given player.
	 * @param p the player
	 * @return the ref of the case.
	 */
	public Case getPlayerCase(Player p){
		return isle.cases[p.y * isle.width + p.x];
	}

	/**
	 * Generates a random x, y position within the given range.
	 * @param xmin the minimal x coordinate included
	 * @param xbound the bound x coordinate excluded
	 * @param ymin the minimal y coordinate included
	 * @param ybound the bound y coordinate excluded
	 * @return (x, y)
	 */
	public Pair<Integer, Integer> generateRandomPosition(int xmin, int xbound, int ymin, int ybound){
		Random rdm = new Random();
		return new Pair<>(xmin + rdm.nextInt(xbound), ymin + rdm.nextInt(ybound));
	}

	/**
	 * Checks if the given position has already been chosen.
	 * @param chosen the list of chosen positions
	 * @param newPos the new position to test
	 * @return whether the position is in chosen list.
	 */
	public boolean checkAlreadyChosenPosition(List<Pair<Integer, Integer>> chosen, Pair<Integer, Integer> newPos){
		for (Pair<Integer, Integer> p: chosen)
			if (p.getKey() == newPos.getKey() && p.getValue() == newPos.getValue())
				return true;
		return false;
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
		int width = 4;
		int height = 4;
		int players = 2;

		if (args.length == 4){
			width = Integer.parseInt(args[1]);
			height = Integer.parseInt(args[2]);
			players = Integer.parseInt(args[3]);

			if (width < 0 || width > 10 || height < 0 || height > 10 || players < 2 || players > 4){
				System.err.println("USAGE: gamerunfile width height players\nwidth : [3, 10]\nheight : [3:10]\n" +
								   "players : [2:4]");
				System.exit(1);
			}
		}

		Game game = new Game(width, height, players);
		game.runGame();
	}
}

