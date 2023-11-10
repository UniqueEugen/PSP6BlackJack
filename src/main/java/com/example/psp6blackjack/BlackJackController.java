package com.example.psp6blackjack;

import com.example.psp6blackjack.game.BlackJack;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import myLibrary.console.Console;

import java.io.InputStream;
import java.util.ArrayList;

public class BlackJackController {
    @FXML
    private Button hitButton;
    @FXML
    private Button stayButton;
    @FXML
    private Button newGameButton;
    @FXML
    private Text winText;
    @FXML
    private Text loseText;
    @FXML
    private Pane playerCardsPane;
    @FXML
    private Pane dealerCardsPane;
    @FXML
    private ImageView deck;
    @FXML
    private AnchorPane board;
    @FXML
    private Text blackJackText;
    @FXML
    private Text dealerSum;
    @FXML
    private Text playerSum;
    private ImageView currentCardImage;
    private BlackJack blackJackGame;
    private ArrayList<ImageView> playerCards;
    private ArrayList<ImageView> dealerCards;
    private Double lastCard;

    @FXML
    private void startNewGame(){
        blackJackGame = new BlackJack();
        winText.setVisible(false);
        loseText.setVisible(false);
        hitButton.setDisable(false);
        blackJackText.setVisible(false);
        playerSum.setVisible(false);
        dealerSum.setVisible(false);
        board.getChildren().remove(12, board.getChildren().size());
        playerCards= new ArrayList<>();
        dealerCards = new ArrayList<>();
        newGameButton.setVisible(false);
        getCard();
        getCard();
        dealerGetStartCards();
    }

    private class NewPlayerCardThread implements Runnable {
        @Override
        public void run() {
            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.seconds(1));
            transition.setNode(currentCardImage);
            lastCard = playerCardsPane.getLayoutX() + playerCardsPane.getPrefWidth()/2 +
                    blackJackGame.getPlayerHandKol()*25 -deck.getFitWidth();
            Console.log(lastCard);
            transition.setToX(lastCard);
            transition.setToY(playerCardsPane.getLayoutY());
            transition.play();
        }
    }

    @AllArgsConstructor
    private class OldPlayerCardsThread implements Runnable {
        private ImageView currentCard;
        private Integer cardNum;
        @Override
        public void run() {
            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.seconds(1));
            transition.setNode(currentCard);
            Double playerXLayout = lastCard-cardNum*50;
            transition.setToX(playerXLayout);
            transition.play();
        }
    }

    private class NewDealerCardThread implements Runnable {
        @Override
        public void run() {
            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.seconds(1));
            transition.setNode(currentCardImage);
            Double cardPlace = dealerCardsPane.getLayoutX() + (blackJackGame.getDealerHandKol())*120+60;
            Console.log(cardPlace + " Y " + dealerCardsPane.getLayoutY());
            Console.log(dealerCardsPane.getLayoutX());
            transition.setToX(cardPlace);
            transition.play();
        }
    }

    @AllArgsConstructor
    private class UpendHiddenCardFirst implements  Runnable{
        @Override
        public void run() {
            ScaleTransition transition = new ScaleTransition();
            transition.setDuration(Duration.seconds(0.7));
            transition.setNode(dealerCards.get(1));
            transition.setByX(-1f);
            transition.play();
        }
    }
    private class UpendHiddenCard implements  Runnable{
        @Override
        public void run() {
            Thread firstPart = new Thread(new UpendHiddenCardFirst());
            firstPart.run();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                firstPart.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ScaleTransition transition = new ScaleTransition();
            transition.setDuration(Duration.seconds(0.7));
            dealerCards.get(0).setVisible(true);
            transition.setNode(dealerCards.get(0));
            transition.setByX(dealerCards.get(2).getFitWidth());
            transition.play();
        }
    }

    @FXML
    private void getCard() {
        String currentCard[] = blackJackGame.getCard().split("/");
        showNewCard(currentCard[0]);
        setPlayerCard(currentCard[1].equals("Lose"));
    }
    private void setPlayerCard(Boolean isEndGame) {
        Thread newPlayerCardThread = new Thread(new NewPlayerCardThread());
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < playerCards.size(); i++) {
            threads.add(new Thread(new OldPlayerCardsThread(playerCards.get(i), playerCards.size() - i)));
        }
        Console.log(threads.size());
        newPlayerCardThread.start();
        threads.forEach(thread -> thread.start());
        try {
            newPlayerCardThread.join();
            //threads.get(threads.size() - 1).join();
        } catch (Exception e) {
            Console.log(e);
        }
        playerCards.add(currentCardImage);
        if (playerCards.size() >= 2) {
            stayButton.setDisable(false);
        }

        if(isEndGame)endGame(!isEndGame);
    }

    private void dealerGetStartCards(){
        blackJackGame.setHiddenCard();
        showNewCard("rubashca");
        dealerCards.add(currentCardImage);
        Thread dealerCardThread = new Thread(new NewDealerCardThread());
        dealerCardThread.start();
        try {
            dealerCardThread.join();
        }catch (InterruptedException e){
            Console.log(e+ "  eception!");
        }
        String currentCard[] = blackJackGame.getDealerCard().split("/");
        showNewCard(currentCard[0]);
        dealerCards.add(currentCardImage);
        dealerCardThread = new Thread(new NewDealerCardThread());
        dealerCardThread.start();
        currentCard = blackJackGame.getHiddenCard().split("/");
        ImageView hiddenCardImage = getNewCard(currentCard[0]);
        hiddenCardImage.setFitWidth(1);
        hiddenCardImage.setLayoutX(dealerCardsPane.getLayoutX()+40);
        hiddenCardImage.setVisible(false);
        Console.log(dealerCards);
        dealerCards.add(0,hiddenCardImage);
        Console.log(dealerCards);
        board.getChildren().add(board.getChildren().size()-2, dealerCards.get(0));
    }

    private void showNewCard(String currentCard){
        this.currentCardImage =  getNewCard(currentCard);
        currentCardImage.setLayoutX(deck.getLayoutX());
        board.getChildren().add(currentCardImage);
    }

    private ImageView getNewCard(String currentCard){
        String currentCardPath = "cards/" + currentCard + ".png";
        InputStream cardStream = getClass().getResourceAsStream(currentCardPath);
        ImageView cardImage = new ImageView();
        cardImage.setImage(new Image(cardStream));
        cardImage.setFitHeight(deck.getFitHeight());
        cardImage.setFitWidth(deck.getFitWidth());
        cardImage.setLayoutY(deck.getLayoutY());
        return cardImage;
    }

    @FXML
    private void dealerStage(){
        boolean flag = true;
        hitButton.setDisable(true);
        stayButton.setDisable(true);
        String hiddenCard[] = blackJackGame.getHiddenCardOpen().split("/");
        Thread upendHiddenCard = new Thread(new UpendHiddenCard());
        upendHiddenCard.start();
        /*try {
            Thread.sleep(2050);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }*/
        if (!hiddenCard[1].equals("Continue")) {
            endGame(!hiddenCard[1].equals("Lose"));
            flag=false;
        }
        while (flag) {
            String currentCard[] = blackJackGame.getDealerCard().split("/");
            showNewCard(currentCard[0]);
            Thread dealerCardThread = new Thread(new NewDealerCardThread());
            dealerCardThread.start();
            if (!currentCard[1].equals("Continue")) {
                endGame(!currentCard[1].equals("Lose"));
                flag=false;
            }
        }
    }
    private void endGame(boolean end){
        if(end){
            winText.setVisible(true);
            if(blackJackGame.isBlackJack()){
                blackJackText.setVisible(true);
            }
        }else loseText.setVisible(true);
        newGameButton.setVisible(true);
        stayButton.setDisable(true);
        hitButton.setDisable(true);
        playerSum.setText(blackJackGame.getPlayerSum());
        playerSum.setVisible(true);
        dealerSum.setText(blackJackGame.getDealerSum());
        dealerSum.setVisible(true);
    }
}