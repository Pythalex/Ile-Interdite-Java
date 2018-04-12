package game.model;

/**
 * Represents the state of a case.
 * A case can be solid, flooded, or submerged.
 */
public enum State
{
	dry,
	flooded,
	submerged;

	public String toString(){
		switch (this){
			case dry:
				return "D";
			case flooded:
				return "F";
			case submerged:
				return "S";
			default:
				return "N/A";
		}
	}
}