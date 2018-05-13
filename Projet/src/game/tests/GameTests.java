package game.tests;

import static org.junit.Assert.*;

import game.main.model.Game;
import org.junit.Test;

public class GameTests{

    Game g = new Game(4, 4, 2);

    @Test
    public void allGatheredElementTest(){

        for (int i = 0; i < 4; i++){
            g.players[0].artifact[i] = true;
        }
        assert(g.allGatheredElements());

        for (int i = 0; i < 4; i++){
            g.players[0].artifact[i] = false;
            g.players[1].artifact[i] = true;
        }

        assert(g.allGatheredElements());

        for (int i = 0; i < 4; i++)
            g.players[1].artifact[i] = false;


        assert(!g.allGatheredElements());

        for (int i = 0; i < 2; i++)
            g.players[0].artifact[i] = true;
        for (int i = 2; i < 4; i++)
            g.players[1].artifact[i] = true;


        assert(g.allGatheredElements());
    }

    @Test
    public void victoryTest(){
        for (int i = 0; i < 2; i++)
            g.players[0].artifact[i] = true;
        for (int i = 2; i < 4; i++)
            g.players[1].artifact[i] = true;

        assert(g.allGatheredElements() && g.everyoneAtHeliport());
    }

}