package UnoGame;

import Cards.Card;
import Cards.Color;
import Cards.Value;
import Decks.Deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static Cards.Value.*;

public class MyGame extends Game{
    private int currentPlayer;
    private String[] playersIds;
    private Deck deck;
    private ArrayList<ArrayList<Card>> playerHand;
    private ArrayList<Card> stockPile;
    private Color validColor;
    private Value validValue;

    boolean gameDirection;

    Scanner s = new Scanner(System.in);
    int numOfPlayers ;

    @Override
    void initializeGame() {
        deck = new Deck();
        deck.initializeADeck();
        deck.shuffle();
        stockPile = new ArrayList<Card>();

        currentPlayer = 0;
        gameDirection = false;
        playerHand = new ArrayList<ArrayList<Card>>();

        dealCardToPlayers();
        System.out.println("Enter the number of Players");
        numOfPlayers = s.nextInt();
        initializePlayers(numOfPlayers);


        Card topCard = getTopCard();
        deck.drawCard();
        System.out.println("Starting the game with: " + topCard.getValue() + "_" + topCard.getColor());
        handleActionCard(topCard);

    }

    @Override
    void applyCustomRules() {
        String currentPlayerId = getCurrentPlayer();
        ArrayList<Card> currentPlayerHand = getPlayerHand(currentPlayerId);
        Card topCard = getTopCard();

        System.out.println("Current Player: " + currentPlayerId);
        System.out.println("Top Card: " + topCard.getValue() + "_" + topCard.getColor());
        System.out.println("Your Hand: " + currentPlayerHand);

        // Assuming you have a method to get user input for the player's move
        Card selectedCard = getUserSelectedCard(currentPlayerHand);
        Color declaredColor = null;

        if (selectedCard.getValue() == Value.Wild) {
            declaredColor = getUserSpecifiedColor();
        }

        submitPlayerCard(currentPlayerId, selectedCard, declaredColor);
    }
    private Card getUserSelectedCard(ArrayList<Card> hand) {
        System.out.println("Choose a card from your hand:");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println(i + ": " + hand.get(i).getValue() + "_" + hand.get(i).getColor());
        }

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        if (choice < 0 || choice >= hand.size()) {
            System.out.println("Invalid choice, please choose a valid card.");
            return getUserSelectedCard(hand);
        }

        return hand.get(choice);
    }


    @Override
    boolean isGameFinished() {
        for (String player: this.playersIds){
            if (hasEmptyHand(player)){
                return true;
            }
        }
        return false;
    }

    @Override
    void announceWinner() {

    }

    private void dealCardToPlayers(){
        ArrayList<Card> hand = new ArrayList<>(Arrays.asList(deck.drawCard(7)));
        playerHand.add(hand);

    }

    private void handleActionCard(Card card) {

        switch (card.getValue()) {
            case Skip -> {
                System.out.println(playersIds + " is skipped!!");
                advanceTurn();
            }
            case Reverse -> {
                System.out.println("The game direction is changed!!");
                gameDirection ^= true;
                advanceTurn();
            }
            case DrawTwo -> {
                advanceTurn();
                deck.drawCard(2);
            }
            case Wild_Four -> {
                advanceTurn();
                deck.drawCard(4);
            }
            case Wild -> {
            Color chosenColor = getUserSpecifiedColor();
            setCardColor(chosenColor);
            System.out.println(playersIds[currentPlayer] + " chose the color: " + chosenColor);
            advanceTurn();
            }
        }
    }

    private Color getUserSpecifiedColor() {
        System.out.println("Choose one of the colors:\n1- Red,2- Blue,3- Green,4- Yellow");
        Scanner s = new Scanner(System.in);
        int userInput = s.nextInt();
        Color color = Color.Red;

        if (userInput == 1)
            color = Color.Red;
        else if (userInput == 2) {
            color = Color.Blue;
        }else if (userInput == 3) {
            color = Color.Green;
        }else if (userInput == 4) {
            color = Color.Yellow;
        }
        else {
            System.out.println("Invalid choice, you must choose a color from 1 - 4");
            userInput = s.nextInt();
        }
        return color;
    }

    private void advanceTurn(){
            if (!gameDirection) {
                currentPlayer = (currentPlayer + 1) % playersIds.length;
            }
            else{
                currentPlayer = (currentPlayer - 1) % playersIds.length;
                if (currentPlayer == -1) {
                    currentPlayer = playersIds.length - 1;
                }
            }
    }

    private Card getTopCard(){
        return new Card(validColor,validValue);
    }

    private String getCurrentPlayer(){
        return this.playersIds[this.currentPlayer];
    }

    private String getPreviousPlayer(int i){
        int index = this.currentPlayer - i;
        if (index == -1)
            index = playersIds.length - 1;
        return this.playersIds[index];
    }

    private String[] getPlayer(){
        return playersIds;
    }

    private ArrayList<Card> getPlayerHand(String pid){
        int index = Arrays.asList(playersIds).indexOf(pid);
        return playerHand.get(index);
    }

    private int getPlayerHandSize(String pid){
        return getPlayerHand(pid).size();
    }

    private Card getPlayerCard(String pid, int choice){
        ArrayList<Card> hand = getPlayerHand(pid);
        return hand.get(choice);
    }

    private boolean hasEmptyHand(String pid){
        return getPlayerHand(pid).isEmpty();
    }

    private boolean validCardPlay(Card card){
        return card.getColor() == validColor || card.getValue() == validValue;
    }

    private void checkPlayerTurn(String pid){
        if (this.playersIds[this.currentPlayer] != pid){
            System.out.println("It's not "+ pid+"'s turn");
        }
    }

    private void submitDraw(String pid){
        checkPlayerTurn(pid);

        if (deck.isEmpty()){
            deck.replaceDeckWith(stockPile);
            deck.shuffle();
        }

        getPlayerHand(pid).add(deck.drawCard());
        advanceTurn();
    }

    private void setCardColor(Color color){
        validColor = color;
    }

    private void submitPlayerCard(String pid, Card card, Color declaredColor){
        checkPlayerTurn(pid);

        ArrayList<Card> pHand = getPlayerHand(pid);

        if (!validCardPlay(card)){
            if (card.getColor() == Color.Wild){
                validColor = card.getColor();
                validValue = card.getValue();
            }

            if (card.getColor() != validColor){
                System.out.println("Invalid player move, Expected color: "+validColor+" but got color "+card.getColor());

            } else if (card.getValue() != validValue) {
                System.out.println("Invalid player move, Expected value: "+validValue+" but got value "+card.getValue());
            }
        }
        pHand.remove(card);

        if (hasEmptyHand(this.playersIds[currentPlayer])){
            System.out.println(playersIds[currentPlayer]+" won the game! thank you for playing");
            System.exit(0);
        }
        validValue = card.getValue();
        validColor = card.getColor();
        stockPile.add(card);

        advanceTurn();

        if (card.getColor() == Color.Wild){
            validColor = declaredColor;
        }
        if (card.getValue() == DrawTwo){
            pid = playersIds[currentPlayer];
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            System.out.println(pid+" drew 2  cards");
        }
        if (card.getValue() == Value.Wild_Four){
            pid = playersIds[currentPlayer];
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            System.out.println(pid+" drew 4  cards");
        }
        if (card.getValue() == Skip){
            System.out.println(playersIds[currentPlayer]+" was skipped!");
            advanceTurn();
        }
         if (card.getValue() == Reverse){
            System.out.println(pid+" change the game direction!");
            gameDirection ^= true;
             if (!gameDirection) {
                 currentPlayer = (currentPlayer + 2) % playersIds.length;
             }
             else{
                 currentPlayer = (currentPlayer - 2) % playersIds.length;
                 if (currentPlayer == -1) {
                     currentPlayer = playersIds.length - 1;
                 }
                 if (currentPlayer == -2)
                     currentPlayer = playersIds.length - 2;
             }
        }
    }
    private void initializePlayers(int numOfPlayers) {
        if (numOfPlayers < 2 || numOfPlayers > 10) {
            throw new IllegalArgumentException("Number of players must be between 2 and 10.");
        }

        playersIds = new String[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            playersIds[i] = "Player " + (i + 1);
            playerHand.add(new ArrayList<>());
        }
    }

}
