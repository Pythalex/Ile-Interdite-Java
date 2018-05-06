package game.main.model;

public enum Treasure {

    WaterKey(0),
    FireKey(1),
    EarthKey(2),
    AirKey(3),
    None(4),
    RisingWater(5);

    public final int id;

    Treasure(int id){
        this.id = id;
    }

    @Override
    public String toString(){
        switch(this){
            case None:
                return "nothing";
            case WaterKey:
                return "water key";
            case FireKey:
                return "fire key";
            case EarthKey:
                return "earth key";
            case AirKey:
                return "air key";
            case RisingWater:
            default:
                return "rising water";
        }
    }

}
