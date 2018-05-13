package game.main.model;

public class TreasureCardSet extends CardSet<Treasure> {

    private int nbOfKeys = 5;
    private int nbOfRisingWater = 3;

    public TreasureCardSet(){
        for (int i = 0; i < nbOfKeys; i++){
            add(Treasure.WaterKey);
            add(Treasure.FireKey);
            add(Treasure.EarthKey);
            add(Treasure.AirKey);
        }
        for (int i = 0; i < nbOfRisingWater; i++){
            add(Treasure.RisingWater);
        }
        shuffle();
    }

}
