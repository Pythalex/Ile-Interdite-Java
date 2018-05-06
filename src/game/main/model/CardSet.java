package game.main.model;

import java.util.Collections;
import java.util.LinkedList;

public class CardSet<R> {

    // the card set
    LinkedList<R> set;

    public CardSet(){
        set = new LinkedList<>();
    }

    /**
     * Add the card on the top of the deck
     * @param card the card to add
     */
    public void add(R card){
        set.add(card);
    }

    /**
     * Draws the card, i.e. the user get the card ref and the
     * card is removed from the deck.
     * @return the drawn card.
     */
    public R draw(){
        R card = peek();
        if (card != null)
            set.remove(card);
        return card;
    }

    /**
     * Empties the set
     */
    public void empty(){
        set.clear();
    }

    /**
     * Indicates whether the card is in the deck.
     * @param card the card to test
     * @return true if the card is in the deck.
     */
    public boolean in(R card){
        return set.indexOf(card) != -1;
    }

    /**
     * Indicates whether the deck is empty.
     * @return set is empty
     */
    public boolean isEmpty(){
        return set.isEmpty();
    }

    /**
     * Takes a peek at the top card of the deck.
     * @return the card ref.
     */
    public R peek(){
        if (set.size() > 0)
            return set.get(0);
        else
            return null;
    }


    /**
     * Insert the card at the bottom of the deck.
     * @param card the card to place at the bottom.
     */
    public void placeAtBottom(R card){
        set.add(card);
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle(){
        Collections.shuffle(set);
    }

    /**
     * Returns the deck size.
     * @return set size
     */
    public int size(){
        return set.size();
    }
}
