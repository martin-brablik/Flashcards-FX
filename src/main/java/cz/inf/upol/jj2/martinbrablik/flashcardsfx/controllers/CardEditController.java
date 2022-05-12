package cz.inf.upol.jj2.martinbrablik.flashcardsfx.controllers;

import cz.inf.upol.jj2.martinbrablik.flashcardsfx.Main;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.Utils;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Card;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Deck;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CardEditController extends Controller {

	private Card card;
	
	@FXML private ComboBox<Deck> cbDeck;
	@FXML private TextField txtFront;
	@FXML private TextField txtBack;
	
	@Override
	public void initialize() {
		cbDeck.setItems(Main.decks);
		cbDeck.getSelectionModel().selectFirst();
	}
	
	@FXML public void actionCreate() {
		String front = txtFront.getText();
		String back = txtBack.getText();
		if(front.isBlank() || back.isBlank()) {
			Utils.alert("Invalid values", "All fields must be filled in.");
			return;
		}
		if(this.card == null) {
			Card.createNewCard(front, back, cbDeck.getSelectionModel().getSelectedItem());
			this.stage.close();
			return;
		}
		card.setFront(front);
		card.setBack(back);
		this.stage.close();
	}
	
	@FXML public void actionCancel() {
		this.stage.close();
	}
	
	public void setCard(Card c) {
		this.card = c;
	}
	
	public void selectDeck(Deck d) {
		this.cbDeck.getSelectionModel().select(d);
	}
	
	public void setFront(String front) {
		this.txtFront.setText(front);;
	}
	
	public void setBack(String bnack) {
		this.txtBack.setText(bnack);
	}
}
