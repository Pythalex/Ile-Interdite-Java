package game.main.model;

public class CaseCardSet extends CardSet<Integer> {

    /**
     * Creates a card set with each case index as a card.
     */
    public CaseCardSet(int nbOfCases){

        // init the set
        super();

        // add a card for each case index
        for (int i = 0; i < nbOfCases; i++){
            add(i);
        }
        // shuffle the deck
        shuffle();
    }

}
