package Cards;

public enum Color {

    Red,
    Blue,
    Green,
    Yellow,
    Wild;

    private static final Color[] colors = Color.values();
    public static Color getColor(int i){
        return Color.colors[i];
    }
}
