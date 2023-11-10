package com.example.psp6blackjack.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import myLibrary.console.Console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class BlackJack {
    @AllArgsConstructor
    private class Card{
        String value;
        String type;

        @Override
        public String toString(){
            return value + "-" + type;
        }
        public int getValue() {
            if ("TVDK".contains(value)) {
                if (value.equals("T")) {
                    if (playerAceCount == 0) {
                        return 11;
                    } else
                        return 1;
                } else
                    return 10;
            } else return Integer.parseInt(value);
        }
        public boolean isAce(){
            return value.equals("T");
        }
    }
    ArrayList<Card> deck;

    //dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    Integer dealerSum;
    Integer dealerAceCount;

    //player
    ArrayList<Card> playerHand;
    Integer playerSum;
    Integer playerAceCount;
    public BlackJack(){
        startGame();
    }
    private void startGame(){
        buildDeck();
        shuffleDeck();
        Console.log("SHUFFLED DECK SIZE: " + deck.size() + " DECK: " + deck);

        dealerHand = new ArrayList();
        dealerSum=0;
        dealerAceCount=0;

        //dealer Start Deck
        /*hiddenCard = deck.remove(deck.size()-1); // get ond remove last card (this is hidden card)
        dealerSum+=hiddenCard.getValue();
        dealerAceCount+= hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size()-1); // get ond remove last card (this is open card)
        dealerSum+=card.getValue();
        dealerAceCount+= card.isAce() ? 1 : 0;
        dealerHand.add(card);

        Console.log("DEALER:\n"+ hiddenCard + "\n"+ dealerHand + "\n" + dealerSum + "\n" + dealerAceCount);*/

        //Player start deck
        playerHand = new ArrayList();
        playerSum=0;
        playerAceCount=0;
/*
        for(int i=0; i<2;i++){
            card = deck.remove(deck.size()-1); // get ond remove last card (this is open card)
            playerSum+=card.getValue();
            playerAceCount+= card.isAce() ? 1 : 0;
            playerHand.add(card);
        }
        Console.log("Player:\n"+ playerHand.get(0) + "\n"+ playerHand.get(1) + "\n" + playerSum + "\n"
                + playerAceCount);*/
    }

    private void buildDeck(){
        deck = new ArrayList<>();
        String[] values = {"T", "2", "3", "4", "5", "6", "7", "8", "9", "10", "V", "D", "K"};
        String[] types = {"B", "C", "K", "P"};
        Arrays.stream(types).forEach(type->
                Arrays.stream(values).forEach(value->{
                    Card card = new Card(value, type);
                    deck.add(card);
                }));
        deck.addAll(deck);
        Console.log("BUILD DECK SIZE: " + deck.size() + " DECK: " + deck);
    }
    private void shuffleDeck() {
        Random rnd = new Random();
        for (int i = deck.size() - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Card temp = deck.get(index);
            deck.set(index, deck.get(i));
            deck.set(i, temp);
        }
    }
    public String getCard(){
        Card card = deck.remove(deck.size()-1);
        playerHand.add(card);
        playerSum+=card.getValue();
        playerAceCount+= card.isAce() ? 1 : 0;
        String isEndGame="";
        if(playerSum>21) isEndGame="Lose"; else isEndGame="continue";
        Console.log(playerHand);
        return card.toString()+"/"+isEndGame;
    }

    public void setHiddenCard() {
        hiddenCard = deck.remove(deck.size()-1); // get ond remove last card (this is hidden card)
        //dealerSum+=hiddenCard.getValue();
        dealerAceCount+= hiddenCard.isAce() ? 1 : 0;
    }

    public String getHiddenCardOpen() {
        dealerSum+=hiddenCard.getValue();
        String res = isEndGame();
        if(dealerSum<21 && playerSum==21 && playerHand.size()==2) res="Win";
        return hiddenCard.toString()+"/"+res;
    }
    public String getHiddenCard(){
        return hiddenCard.toString() + "/continue";
    }

    public String getDealerCard(){
        Card card = deck.remove(deck.size()-1); // get ond remove last card (this is open card)
        dealerSum+=card.getValue();
        dealerAceCount+= card.isAce() ? 1 : 0;
        dealerHand.add(card);
        Console.log(dealerHand+ "check");
        return card.toString() + "/" +isEndGame();
    }

    private String isEndGame(){
        Console.log("DEALER HAND: " + dealerHand + "sum " + dealerSum);
        Console.log("PLAYER HAND: "+ playerHand + "sum" + playerSum);
        String endGame = dealerSum > 21 ? "Win":
                         dealerSum < 17 ? "Continue":
                         dealerSum < playerSum ? "Win": "Lose";
        Console.log(endGame);
        return endGame;
    }

    public int getPlayerHandKol(){
        return playerHand.size()-1;
    }
    public int getDealerHandKol(){
        return dealerHand.size()-1;
    }
    public boolean isBlackJack(){
        Console.log(playerHand.size());
        if(playerHand.size()==2 && playerSum==21){
            Console.log("fsdfsfdsdf");
            return true;
        }
        return false;
    }
    public String getPlayerSum(){
        return playerSum.toString();
    }
    public String getDealerSum(){
        return dealerSum.toString();
    }
}
