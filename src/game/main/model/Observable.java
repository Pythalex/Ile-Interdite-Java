package game.main.model;

import game.main.view.Observer;

import java.util.ArrayList;

public abstract class Observable {

    private ArrayList<Observer> observers;
    public Observable() {
        this.observers = new ArrayList<Observer>();
    }
    public void addObserver(Observer o) {
        observers.add(o);
    }

    /**
     * Lorsque l'Ã©tat de l'objet observÃ© change, il est convenu d'appeler la
     * mÃ©thode [notifyObservers] pour prÃ©venir l'ensemble des observateurs
     * enregistrÃ©s.
     * On le fait ici concrÃ¨tement en appelant la mÃ©thode [update] de chaque
     * observateur.
     */
    public void notifyObservers() {
        for(Observer o : observers) {
            o.update();
        }
    }
}
