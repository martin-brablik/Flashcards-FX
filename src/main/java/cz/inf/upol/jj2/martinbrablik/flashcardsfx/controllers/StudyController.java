package cz.inf.upol.jj2.martinbrablik.flashcardsfx.controllers;

import java.util.LinkedList;
import java.util.Queue;

import cz.inf.upol.jj2.martinbrablik.flashcardsfx.DataHandler;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.Utils;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Card;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Deck;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class StudyController extends Controller {
	
	private Deck deck;
	private int deckPosition = 0;
	private int newCards = 0;
	private int repeatedCards = 0;
	private Queue<Card> cardsAgain = new LinkedList<Card>();
	private boolean repeating = false;

	@FXML private Label lbName;
	@FXML private Label lbFront;
	@FXML private Label lbBack;
	@FXML private Button btnShow;
	@FXML private Button btnAgain;
	@FXML private Button btnHard;
	@FXML private Button btnGood;
	@FXML private Button btnEasy;
	
	@Override
	public void initialize() {
		toggleButtons(false);
	}
	
	@FXML public void actionShow() {
		Card currentCard = repeating ? cardsAgain.peek() : deck.getCards().get(deckPosition);
		lbBack.setText(currentCard.getBack());
		if(currentCard.isNew() || currentCard.getScore() == 0 || cardsAgain.contains(currentCard))
			toggleButtonsNew(true);
		else
			toggleButtons(true);
		btnShow.setDisable(true);
	}
	
	@FXML public void actionAgain() {
		Card currentCard = repeating ? cardsAgain.poll() : deck.getCards().get(deckPosition);
		if(!currentCard.isNew()) {
			switch(currentCard.getScore()) {
				case 0:
					break;
				case 1:
					currentCard.setScore(0);
					break;
				default:
					currentCard.setScore(currentCard.getScore() - 2);
			}
			currentCard.setNew(false);
			repeatedCards++;
		}
		else newCards++;
		cardsAgain.add(currentCard);
		toggleButtons(false);
		btnShow.setDisable(false);
		deckPosition++;
		DataHandler.saveCard(currentCard);
		next();
	}
	
	@FXML public void actionHard() {
		Card currentCard = deck.getCards().get(deckPosition);
		currentCard.setScore(currentCard.getScore() - 1);
		toggleButtons(false);
		btnShow.setDisable(false);
		repeatedCards++;
		deckPosition++;
		DataHandler.saveCard(currentCard);
		next();
	}
	
	@FXML public void actionGood() {
		Card currentCard = repeating ? cardsAgain.poll() : deck.getCards().get(deckPosition);
		currentCard.setScore(currentCard.getScore() + 1);
		currentCard.setNew(false);
		toggleButtons(false);
		btnShow.setDisable(false);
		if(currentCard.isNew()) newCards++;
		else repeatedCards++;
		deckPosition++;
		DataHandler.saveCard(currentCard);
		next();
	}
	
	@FXML public void actionEasy() {
		Card currentCard = repeating ? cardsAgain.poll() : deck.getCards().get(deckPosition);
		currentCard.setScore(currentCard.getScore() + 2);
		currentCard.setNew(false);
		toggleButtons(false);
		btnShow.setDisable(false);
		if(currentCard.isNew()) newCards++;
		else repeatedCards++;
		deckPosition++;
		DataHandler.saveCard(currentCard);
		next();
	}
	
	private void next() {
		if(deckPosition == deck.getCards().size()) {
			repeating = true;
		}
		if(repeating) {
			Card card = cardsAgain.peek();
			if(card != null) {
				lbFront.setText(card.getFront());
				lbBack.setText("");
			}
			else finish();
		}
		else {
			if(newCards == deck.getNewCardsLimit() && deck.getCards().get(deckPosition).isNew() || repeatedCards == deck.getRepeateedCardsLimit() && !deck.getCards().get(deckPosition).isNew()) {
				deckPosition++;
				next();
			}
			else {
				Card card = deck.getCards().get(deckPosition);
				lbFront.setText(card.getFront());
				lbBack.setText("");
			}
		}
	}
	
	public void finish() {
		Utils.info("You're done with this deck for now.");
		this.stage.close();
	}
	
	private void toggleButtons(boolean value) {
		btnAgain.setText("Again (-2)");
		btnHard.setText("Hard (-1)");
		btnGood.setText("Good (+1)");
		btnEasy.setText("Easy (+2)");
		btnAgain.setDisable(!value);
		btnAgain.setVisible(value);
		btnHard.setDisable(!value);
		btnHard.setVisible(value);
		btnGood.setDisable(!value);
		btnGood.setVisible(value);
		btnEasy.setDisable(!value);
		btnEasy.setVisible(value);
	}
	
	private void toggleButtonsNew(boolean value) {
		btnAgain.setText("Again (0)");
		btnGood.setText("Good (+1)");
		btnEasy.setText("Easy (+2)");
		btnAgain.setDisable(!value);
		btnAgain.setVisible(value);
		btnHard.setDisable(true);
		btnHard.setVisible(false);
		btnGood.setDisable(!value);
		btnGood.setVisible(value);
		btnEasy.setDisable(!value);
		btnEasy.setVisible(value);
	}
	
	public void setDeck(Deck deck) {
		this.deck = deck;
	}
	
	public void setDeckName(String name) {
		this.lbName.setText(name);
	}
	
	public void setFirstCard(String cardFront) {
		this.lbFront.setText(cardFront);
	}
}
