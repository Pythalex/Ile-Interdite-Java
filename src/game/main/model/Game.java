package game.main.model;

// interfaces
import game.main.view.GUInterface;

// used for random position generation
import javafx.util.Pair;

import java.util.*;

// used for image loading
import java.io.IOException;

/**
 * The class managing the game rules
 */

public class Game extends Observable
{
	// The game's isle
	public Ile isle;

	// List containing the players in the game
	public Player[] players;
	public Player currentPlayer;
	// Classes not already assigned to a player
	public List<CharacterClasses> availableClasses = new ArrayList<>(Arrays.asList(CharacterClasses.Pilot,
			CharacterClasses.Engineer, CharacterClasses.Explorer, CharacterClasses.Diver));

	// obtainedKey
	public boolean[] keys;

	// artifacts
	List<Event> artifacts = new ArrayList<>(Arrays.asList(Event.Element_water, Event.Element_fire,
			Event.Element_earth, Event.Element_air));

	// interface
	public GUInterface intfc;

	// flood cards
	public CaseCardSet floodSet;
	// treasure cards
	public TreasureCardSet treasureSet;
	public TreasureCardSet discardTreasureSet;

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

		// Used for class assignment
		Random rdm = new Random();

		// Create the players
		players = new Player[nbOfPlayers];
		char name = 'A';
		for (int i = 0; i < nbOfPlayers; i++) {
			// Create the player
			players[i] = new Player(this);
			name = (char)((int)name + 1);

			// Place the player to the helicopter case
			players[i].x = helicopCase.x;
			players[i].y = helicopCase.y;
			helicopCase.players.add(players[i]);

			// class assignment - Assign randomly from available classes
			players[i].chClass = availableClasses.get(rdm.nextInt(availableClasses.size()));
			// The class is no longer available
			availableClasses.remove(players[i].chClass);
		}
		currentPlayer = players[0];

		// the keys
		keys = new boolean[4];
		for (int i = 0; i < 4; i++)
			keys[i] = false;

		// the interface
		intfc = new GUInterface(this);
		addObserver(intfc);
		notifyObservers();

		// the flood card set
		floodSet = new CaseCardSet(isle.numberOfCases);

		// the treasure card set
		treasureSet = new TreasureCardSet();
		discardTreasureSet = new TreasureCardSet();
		discardTreasureSet.empty();
	}

	/**
	 * Removes the player reference from the case player list.
	 * @param p the player
	 */
	public void addPlayerRefToCase(Player p){
		getPlayerCase(p).players.add(p);
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
	 * Flood the given number of cases to flood.
	 * @param nbOfCaseToFlood the number of cases to flood.
	 */
	public void floodCases(int nbOfCaseToFlood){
		// repeat operation for each case
		for (int i = 0; i < nbOfCaseToFlood; i++){
			// draw the flood card
			int index = floodSet.draw();
			// flood the case
			isle.floodCase(index);
			// place the flood card at the bottom of the deck
			floodSet.placeAtBottom(index);
		}
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
	 * Asks the player p to take action
	 * @param p the player who is playing
	 * @param actionLeft The number of action left
	 */
	public void makeAction(Player p, int actionLeft){

		// While the player can do something
		while (actionLeft > 0) {

			// Get an action
			String action = p.takeAction();

			/*
				If the action is valid, the game computes the result.
				If it's not, the player is notified in testAction and
				is asked an action again.
			 */
			if (testAction(action, p)) {

				if (action.equals("pass")) {
					return;
				} else if (action.equals("moveup")) {
					movePlayer(p, p.x, p.y - 1);
				} else if (action.equals("moveupleft")){
					movePlayer(p, p.x - 1, p.y - 1);
				} else if (action.equals("moveleft")) {
					movePlayer(p, p.x - 1, p.y);
				} else if (action.equals("movedownleft")){
					movePlayer(p, p.x - 1, p.y + 1);
				} else if (action.equals("movedown")) {
					movePlayer(p, p.x, p.y + 1);
				} else if (action.equals("movedownright")) {
					movePlayer(p, p.x + 1, p.y + 1);
				} else if (action.equals("moveright")) {
					movePlayer(p, p.x + 1, p.y);
				} else if (action.equals("moveupright")) {
					movePlayer(p, p.x + 1, p.y - 1);
				} else if (action.equals("moveto")) {
					boolean playerChoseValidCase = false;
					while (!playerChoseValidCase) {

						Pair<Integer, Integer> pos = intfc.askPosition();
						if (isle.playerCanMove(pos.getKey(), pos.getValue())) {
							movePlayer(p, pos.getKey(), pos.getValue());
							playerChoseValidCase = true;
						} else {
							message("The case is not accessible.");
						}
					}
				} else if (action.equals("dry")){
					boolean playerChoseValidCase = false;
					while(!playerChoseValidCase) {
						playerChoseValidCase = makePlayerDryCase(p);
					}
				} else if (action.equals("doubledry")){
					for (int i = 0; i < 2; i++){
						boolean playerChoseValidCase = false;
						while(!playerChoseValidCase) {
							playerChoseValidCase = makePlayerDryCase(p);
						}
					}
				} else if (action.equals("getartifact")) {
					// Get the player's case
					Case c = getPlayerCase(p);
					// give artifact
					p.artifact[artifacts.indexOf(c.event)] = true;
					// remove artifact from the case
					c.event = Event.None;
				} else {
					System.err.println("Action " + action + " is invalid but passed " +
							"the checks. Game will now shut down.");
					System.exit(1);
				}

				actionLeft--;
			}

			notifyObservers();
		}
	}

	/**
	 * Make the player choose a case and dry it if valid.
	 * @param p the player
	 * @return whether the action is successful (valid case)
	 */
	public boolean makePlayerDryCase(Player p){
		// Ask for position and check if in the vicinity (neighbour or self)
		Pair<Integer, Integer> pos = intfc.askPosition();
		int x = p.x - pos.getKey();
		int y = p.y - pos.getValue();
		double length = Math.sqrt(x*x + y*y);
		if (length <= 1){
			// if dryable case
			Case c = isle.getCase(pos.getKey(), pos.getValue());
			if (c.isFlooded()){
				c.dry();
			} else {
				message("The case cannot be dried.");
				return false;
			}
		} else {
			message("The case is not in your neighbourhood (1 case).");
			return false;
		}
		return true;
	}

	/**
	 * Called when a player is on submerged case.
	 * Assumes the player can leave the area.
	 * @param p the player
	 * @return Whether the player survived
	 */
	public void makePlayerLeaveSubmergedCase(Player p){

		Pair<Integer, Integer> pos;

		boolean valid = false;
		while(!valid) {

			// Ask for position and check if in the vicinity (neighbour or self)
			pos = intfc.askPosition(p.chClass.toString());
			int x = p.x - pos.getKey();
			int y = p.y - pos.getValue();

			double length = Math.sqrt(x * x + y * y);
			if (length == 1) {
				// if movable case
				Case c = isle.getCase(pos.getKey(), pos.getValue());
				if (!c.isSubmerged()) {
					movePlayer(p, pos.getKey(), pos.getValue());
					valid = true;
				} else {
					message("The case cannot be dried.");
				}
			} else {
				message("The case is not in your neighbourhood (1 case).");
			}
		}
	}

	public void message(String msg){
		intfc.message(msg);
	}

	/**
	 * Make the player move to x, y, and update the case references.
	 * @param p the player to move
	 * @param x the new position x
	 * @param y the new position y
	 */
	public void movePlayer(Player p, int x, int y){
		removePlayerRefFromCase(p);
		p.x = x;
		p.y = y;
		addPlayerRefToCase(p);
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
	 * Indicates whether the given player can move on a neighbour
	 * case. i.e. if at least of the adjacent cases is not submerged.
	 * @param p the player
	 * @return player can move
	 */
	public boolean playerCanMoveTowardNeighbourCases(Player p){
		List<Case> cases = new ArrayList<>();
		if (isle.playerCanMove(p.x, p.y - 1))
			cases.add(isle.getCase(p.x, p.y - 1));
		if (isle.playerCanMove(p.x, p.y + 1))
			cases.add(isle.getCase(p.x, p.y + 1));
		if (isle.playerCanMove(p.x - 1, p.y))
			cases.add(isle.getCase(p.x - 1, p.y));
		if (isle.playerCanMove(p.x + 1, p.y))
			cases.add(isle.getCase(p.x + 1, p.y));
		boolean hasAtLeastOneNeighbourFlooded = false;
		for (Case neighbour: cases)
			hasAtLeastOneNeighbourFlooded = hasAtLeastOneNeighbourFlooded || !neighbour.isSubmerged();

		return hasAtLeastOneNeighbourFlooded;
	}

	/**
	 * Runs the game.
	 */
	public void runGame(){

		boolean end = false;
		boolean win = false;
		int tour = 1;

		Case heliport = isle.foundCaseByEvent(Event.Helicopter);
		Case elementWater = isle.foundCaseByEvent(Event.Element_water);
		Case elementFire = isle.foundCaseByEvent(Event.Element_fire);
		Case elementEarth = isle.foundCaseByEvent(Event.Element_earth);
		Case elementAir = isle.foundCaseByEvent(Event.Element_air);
		List<Case> elementsCases = new ArrayList<>(Arrays.asList(elementWater, elementFire,
				elementEarth, elementAir));

		while (!end) {

			message("Turn " + tour);
			tour++;

			// player turns
			for (Player p: players){

				currentPlayer = p;

				// Don't play turn if game is finished
				if (end)
					break;

				message( p.chClass + " has to play.");

				// 3 actions at max
				makeAction(p, 3);

				// search for keys
				searchKeys(p);

				sleep(1000);

				// flood 3 cases
				floodCases(3);

				notifyObservers();

				sleep(500);

				/* DEFEAT CHECKS */

				// if one player is on a submerged case
				Player submergedPlayer = checkSubmergedPlayer();
				while (submergedPlayer != null) {
					// make him move if possible, else he dies
					if (playerCanMoveTowardNeighbourCases(submergedPlayer)){
						makePlayerLeaveSubmergedCase(submergedPlayer);
					} else {
						end = true;
						message(submergedPlayer.chClass + " drowned, you lose.");
						break;
					}
					// do it for the next submerged player if there is one
					submergedPlayer = checkSubmergedPlayer();
					// update graphics in case of multiple submerged players (can happen, really)
					notifyObservers();
				}

				// If the helicopter is submerged, the game is finished
				if (heliport.isSubmerged()){
					end = true;
					message("The helicopter got submerged, the players are trap on the island !");
				}

				// If any artifact is submerged, the game is finished
				for (Case artifact: elementsCases){
					if (artifact.isSubmerged()){
						end = true;
						message("The " + artifact.event.getName() +
								" got submerged, the players lose !");
						break;
					}
				}

				// Victory test
				if (allGatheredElements() && everyoneAtHeliport()){
					end = true;
					win = true;
				}

				sleep(1000);

				// update view
				notifyObservers();
			}

			// Next turn
			if (!end) {
				message("Turn ended. Go for next turn.");
				sleep(1000);
			}

			// update view
			notifyObservers();
		}

		if (win){
			message("Congrats ! You won !");
		} else {
			message("Too bad, you lose.");
		}
		message("You can exit the game, now.");
	}

	/**
	 * Adds the player reference to the case player list.
	 * @param p the player
	 */
	public void removePlayerRefFromCase(Player p){
		getPlayerCase(p).players.remove(p);
	}

	/**
	 * Makes the player look for a key on his
	 * current case.
	 * @param p the player
	 */
	public void searchKeys(Player p){
		Treasure found = treasureSet.draw();
		message(p.chClass + " found " + found);

		switch(found){
			case WaterKey:
			case FireKey:
			case EarthKey:
			case AirKey:
				p.keys[found.id] = true;
				break;
			case RisingWater:
				getPlayerCase(p).flood();
				message("Damn ! " + p.chClass +
						" triggered a trap, water comes from nowhere and the entire area is suddenly flooded !");
				break;
			case None:
			default:
				break;
		}

		// If the discard deck has every cards, the pile is shuffled and become the new deck
		discardTreasureSet.add(found);
		if (treasureSet.isEmpty()){
			for (int i = 0; i < discardTreasureSet.size(); i++){
				Treasure card = discardTreasureSet.draw();
				treasureSet.add(card);
			}
			treasureSet.shuffle();
		}
	}

	/**
	 * Sleeps for given miliseconds.
	 * @param ms the miliseconds to wait
	 */
	public static void sleep(int ms){
		try{
			Thread.sleep(ms);
		} catch (Exception e){
			e.printStackTrace();
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
		} else if (action.equals("moveup")){
			if (isle.playerCanMove(p.x, p.y - 1)) {
				return true;
			}
			else {
				message("The case is not accessible.");
				return false;
			}
		} else if (action.equals("moveupleft")){
			if (p.chClass == CharacterClasses.Explorer){
				if (isle.playerCanMove(p.x - 1, p.y - 1)){
					return true;
				} else {
					message("The case is not accessible.");
				}
			} else {
				message("You are not an explorer.");
			}
			return false;
		} else if (action.equals("moveleft")){
			if (isle.playerCanMove(p.x - 1, p.y)) {
				return true;
			}
			else {
				message("The case is not accessible.");
				return false;
			}
		} else if (action.equals("movedownleft")){
			if (p.chClass == CharacterClasses.Explorer){
				if (isle.playerCanMove(p.x - 1, p.y + 1)){
					return true;
				} else {
					message("The case is not accessible.");
				}
			} else {
				message("You are not an explorer.");
			}
			return false;
		} else if (action.equals("movedown")){
			if (isle.playerCanMove(p.x, p.y + 1)) {
				return true;
			}
			else {
				message("The case is not accessible.");
				return false;
			}
		} else if (action.equals("movedownright")){
			if (p.chClass == CharacterClasses.Explorer){
				if (isle.playerCanMove(p.x + 1, p.y + 1)){
					return true;
				} else {
					message("The case is not accessible.");
				}
			} else {
				message("You are not an explorer.");
			}
			return false;
		} else if (action.equals("moveright")) {
			if (isle.playerCanMove(p.x + 1, p.y)) {
				return true;
			}
			else {
				message("The case is not accessible.");
				return false;
			}
		} else if (action.equals("moveupright")){
			if (p.chClass == CharacterClasses.Explorer){
				if (isle.playerCanMove(p.x + 1, p.y - 1)){
					return true;
				} else {
					message("The case is not accessible.");
				}
			} else {
				message("You are not an explorer.");
			}
			return false;
		} else if (action.equals("moveto")) {
			if (p.chClass == CharacterClasses.Pilot){
				return true;
			} else {
				message("You are not a pilot.");
			}
			return false;
		} else if (action.equals("dry")) {
			List<Case> cases = new ArrayList<>();
			cases.add(c);
			if (isle.playerCanMove(p.x, p.y - 1))
				cases.add(isle.getCase(p.x, p.y - 1));
			if (isle.playerCanMove(p.x, p.y + 1))
				cases.add(isle.getCase(p.x, p.y + 1));
			if (isle.playerCanMove(p.x - 1, p.y))
				cases.add(isle.getCase(p.x - 1, p.y));
			if (isle.playerCanMove(p.x + 1, p.y))
				cases.add(isle.getCase(p.x + 1, p.y));
			boolean hasAtLeastOneNeighbourFlooded = false;
			for (Case neighbour: cases)
				hasAtLeastOneNeighbourFlooded = hasAtLeastOneNeighbourFlooded || neighbour.isFlooded();

			if (hasAtLeastOneNeighbourFlooded)
				return true;
			else {
				message("You have not at least one flooded neighbour case.");
				return false;
			}
		} else if (action.equals("doubledry")) {
			if (p.chClass == CharacterClasses.Engineer){
				List<Case> cases = new ArrayList<>();
				cases.add(c);
				if (isle.playerCanMove(p.x, p.y - 1))
					cases.add(isle.getCase(p.x, p.y - 1));
				if (isle.playerCanMove(p.x, p.y + 1))
					cases.add(isle.getCase(p.x, p.y + 1));
				if (isle.playerCanMove(p.x - 1, p.y))
					cases.add(isle.getCase(p.x - 1, p.y));
				if (isle.playerCanMove(p.x + 1, p.y))
					cases.add(isle.getCase(p.x + 1, p.y));
				int numberOfFloodedNeighbours = 0;
				for (Case neighbour: cases)
					numberOfFloodedNeighbours += (neighbour.isFlooded() ? 1 : 0);

				if (numberOfFloodedNeighbours >= 2){
					return true;
				} else {
					message("You have not at least 2 flooded neighbour cases.");
					return false;
				}
			} else {
				message("You are not an Engineer.");
				return false;
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
					message(p.chClass + " got the " + caseEvent.getName());
					return true;
				} else {
					message("You don't have the " + caseEvent.getName() + " key.");
				}
			} else {
				message("There is no artifact on your current case.");
			}

			return false;
		} else {
			message("Action not understood.");
			return false;
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

