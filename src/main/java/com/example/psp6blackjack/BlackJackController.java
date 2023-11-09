package com.example.psp6blackjack;

import com.example.psp6blackjack.game.BlackJack;
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
import java.util.concurrent.atomic.AtomicInteger;

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
    private ImageView currentCardImage;
    private BlackJack blackJack;
    private ArrayList<ImageView> playerCards;
    private Double lastCard;

    @FXML
    private void startNewGame(){
        blackJack = new BlackJack();
        winText.setVisible(false);
        loseText.setVisible(false);
        hitButton.setDisable(false);
        board.getChildren().remove(9, board.getChildren().size());
        playerCards= new ArrayList<>();
        newGameButton.setVisible(false);
    }
    @FXML
    private void getCard(){
        String currentCard[] = blackJack.getCard().split("/");
        String currentCardPath = "cards/"+currentCard[0]+".png";
        InputStream cardStream = getClass().getResourceAsStream(currentCardPath);
        Console.log(currentCard);
        currentCardImage = new ImageView();
        currentCardImage.setImage(new Image(cardStream));
        currentCardImage.setFitHeight(deck.getFitHeight());
        currentCardImage.setFitWidth(deck.getFitWidth());
        currentCardImage.setLayoutX(deck.getLayoutX());
        currentCardImage.setLayoutY(deck.getLayoutY());
        board.getChildren().add(currentCardImage);
        Thread newPlayerCardThread = new Thread(new NewPlayerCardThread());
        ArrayList<Thread> threads=new ArrayList<>();
        for(int i = 0; i<playerCards.size();i++){
            threads.add(new Thread(new OldPlayerCardsThread(playerCards.get(i), playerCards.size()-i)));
        }
        Console.log(threads.size());
        newPlayerCardThread.start();
        threads.forEach(thread -> thread.start());
        try {
            newPlayerCardThread.join();
            threads.get(threads.size()-1).join();
        }catch (Exception e){

        }
        playerCards.add(currentCardImage);
        if(playerCards.size()>=2){
            stayButton.setDisable(false);
        }
        if(currentCard[1].equals("Lose")){
            endGame(false);
        }
    }
    @FXML
    private void dealerStage(){

    }


    private class NewPlayerCardThread implements Runnable {
        @Override
        public void run() {
            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.seconds(1));
            transition.setNode(currentCardImage);
            lastCard = playerCardsPane.getLayoutX() + playerCardsPane.getPrefWidth()/2 +
                    blackJack.getPlayerHandKol()*25 -deck.getFitWidth();
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
            Console.log(playerXLayout);
            transition.setToX(playerXLayout);
            transition.play();
        }
    }
    private void endGame(boolean end){
        if(end){
            winText.setVisible(true);
        }else loseText.setVisible(true);
        newGameButton.setVisible(true);
        stayButton.setDisable(true);
        hitButton.setDisable(true);
    }
}