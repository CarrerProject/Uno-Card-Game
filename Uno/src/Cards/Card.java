package Cards;

public class Card {
    private final Color color;
    private final Value value;

    public Card(final Color color, final Value value){
        this.color = color;
        this.value = value;
    }

    public Color getColor() {
        return this.color;
    }

    public Value getValue() {
        return this.value;
    }

    public String toString(){
        return value+"_"+color;
    }
}
