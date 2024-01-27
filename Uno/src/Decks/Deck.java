package Decks;

import Cards.Card;
import Cards.Color;
import Cards.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    private Card[] cards;
    private int cardsInDeck;

    public Deck(){
        cards = new Card[108];
    }

    public void initializeADeck() {
        Color[] colors = Color.values();
        cardsInDeck = 0;

        for (int i = 0; i < colors.length; i++) {
            Color color = colors[i];

            // Because we have one zero
            cards[cardsInDeck++] = new Card(color, Value.getValue(0));

            for (int j = 1; j < 10; j++) {
                cards[cardsInDeck++] = new Card(color, Value.getValue(j));
                cards[cardsInDeck++] = new Card(color, Value.getValue(j));
            }


            Value[] values = new Value[]{Value.DrawTwo, Value.Skip, Value.Reverse};
            for (int j = 1; j < 10; j++) {
                cards[cardsInDeck++] = new Card(color, Value.getValue(j));
                cards[cardsInDeck++] = new Card(color, Value.getValue(j));
            }

            Value[] values1 = new Value[]{Value.Wild, Value.Wild_Four};
            for (Value value : values1) {
                for (int j = 0; j < 4; j++) {
                    cards[cardsInDeck++] = new Card(Color.Wild, value);
                }
            }
        }
    }


    public void replaceDeckWith(ArrayList<Card> cards){
        this.cards = cards.toArray(new Card[cards.size()]);
        this.cardsInDeck = this.cards.length;
    }

    public boolean isEmpty(){
        return cardsInDeck == 0;
    }

    public void shuffle() {
        Random random = new Random();

        for (int i = cards.length - 1; i > 0; i--) {
            int randomValue = random.nextInt(i + 1);
            Card randomCard = cards[randomValue];
            cards[randomValue] = cards[i];
            cards[i] = randomCard;
        }
    }
    public Card drawCard() throws IllegalArgumentException{
        if (isEmpty()){
            throw new IllegalArgumentException("Can't draw a card since the deck is empty");
        }
        return cards[--cardsInDeck];
    }

    public Card[] drawCard(int numberOfCardsToDraw){
        if (numberOfCardsToDraw < 0){
            throw new IllegalArgumentException("Must draw positive cards but tried to draw "+numberOfCardsToDraw+" cards.");
        }
        if(numberOfCardsToDraw > cardsInDeck){
            throw new IllegalArgumentException("Can't draw "+numberOfCardsToDraw+" cards since there are only "+cardsInDeck+" cards.");
        }

        Card[] drawnCards = new Card[numberOfCardsToDraw];
        for (int i = 0; i < numberOfCardsToDraw; i++){
            drawnCards[i] = cards[--cardsInDeck];
        }
        return drawnCards;
    }
}
