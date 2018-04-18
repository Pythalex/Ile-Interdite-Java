package game.main.view;

import game.main.model.Ile;
import game.main.model.Player;

public class CLInterface extends Interface {

    public CLInterface(){

    }

    @Override
    public void displayMessage(String msg){
        System.out.println(msg);
    }

    @Override
    public void displayState(Ile isle){
        System.out.println(isle);
    }

    @Override
    public void displayState(Player p){
        System.out.println("Player " + p.name + "(" + p.x + "," + p.y + ") Keys : " +
                (p.keys[0] ? "W-" : "") + (p.keys[1] ? "F-" : "") + (p.keys[2] ? "E-" : "") +
                (p.keys[3] ? "A-" : "") + "  Artifacts : " +
                (p.artifact[0] ? "W-" : "") + (p.artifact[1] ? "F-" : "") + (p.artifact[2] ? "E-" : "") +
                (p.artifact[3] ? "A-" : ""));
    }

    @Override
    public void displayError(Exception err){
        System.err.println(err);
    }
}
