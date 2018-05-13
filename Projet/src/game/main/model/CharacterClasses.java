package game.main.model;

/**
 * Represents the different character classes in the game. Each
 * class have a particular asset.
 */
public enum CharacterClasses {

    Default,    // The basic character, no special trait
    Pilot,      // Can move anywhere dry instead of only neighbours
    Engineer,   // Can dry two zones instead of only one
    Explorer,   // Can move and dry diagonally
    Diver;      // Can move through a submerged case

    public String toString(){
        switch (this){
            case Default:
                return "Vanilla";
            case Pilot:
                return "Pilot";
            case Engineer:
                return "Engineer";
            case Explorer:
                return "Explorer";
            case Diver:
                return "Diver";
            default:
                return "ChClass not found";
        }
    }
}
