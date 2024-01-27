package Cards;

public enum Value {
    Zero, One, Two, Three, Four, Five, Six, Seven, Eight, Nine,
    DrawTwo, Skip, Reverse, Wild, Wild_Four;

    private static final Value[] values = Value.values();
    public static Value getValue(int i){
        return Value.values[i];
    }
}
