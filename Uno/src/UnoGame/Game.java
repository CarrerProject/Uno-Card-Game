package UnoGame;

import Cards.Card;

import java.util.ArrayList;

public abstract class Game {

    abstract void initializeGame();

    abstract void applyCustomRules();

    public void play() {
        initializeGame();
        while (!isGameFinished()) {
            applyCustomRules();
        }
        announceWinner();
    }

    abstract boolean isGameFinished();

    abstract void announceWinner();
}
