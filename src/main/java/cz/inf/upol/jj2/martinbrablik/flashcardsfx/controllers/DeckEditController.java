package cz.inf.upol.jj2.martinbrablik.flashcardsfx.controllers;

import java.util.function.UnaryOperator;

import cz.inf.upol.jj2.martinbrablik.flashcardsfx.Main;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.DataHandler;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.Utils;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Deck;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.oredering.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.converter.IntegerStringConverter;

public class DeckEditController extends Controller {

	private int deckId = Deck.nullId;
	
	@FXML private TextField txtName;
	@FXML private ComboBox<Order> cbOrder;
	@FXML private TextField txtNewLimit;
	@FXML private TextField txtRepeatedLimit;
	@FXML private Button btnCreate;
	@FXML private Button btnCancel;
	
	public void setDeck(int id) {
		this.deckId = id;
	}
	
	@Override
	public void initialize() {
		UnaryOperator<Change> integerFilter = change -> Utils.filterInteger(change);
		txtNewLimit.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		txtRepeatedLimit.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
		cbOrder.setItems(Main.orderingMethods);
		cbOrder.getSelectionModel().selectFirst();
		
		if(this.deckId != Deck.nullId) {
			Deck d = Deck.findById(deckId);
			if(d != null) {
				this.txtName.setText(d.getName());
				this.cbOrder.setValue(d.getOrderingMethod());
				this.txtNewLimit.setText(Integer.toString(d.getNewCardsLimit()));
				this.txtRepeatedLimit.setText(Integer.toString(d.getRepeateedCardsLimit()));
				cbOrder.getSelectionModel().select(d.getOrderingMethod());
			}
		}
	}
	
	@FXML public void actionCreate() {
		String name = txtName.getText();
		Order order = cbOrder.getValue();
		int newLimit = 0;
		int repeatedLimit = 0;
		try {
			newLimit = Integer.parseInt(txtNewLimit.getText());
			repeatedLimit = Integer.parseInt(txtRepeatedLimit.getText());
		}
		catch(NumberFormatException e) {
			Utils.alert("Invalid values", "All fields must be filled in.");
			return;
		}
		if(name.isEmpty()) {
			Utils.alert("Invalid values", "All fields must be filled in.");
			return;
		}
		if(this.deckId == Deck.nullId)
			Deck.createNewDeck(name, order, newLimit, repeatedLimit);
		else {
			Deck d = Deck.findById(this.deckId);
			if(d != null) {
				d.setName(name);
				d.setOrderingMethod(order);
				d.setNewCardsLimit(newLimit);
				d.setRepeateedCardsLimit(repeatedLimit);
				d.getOrderingMethod().order(d);
				DataHandler.updateDeck(d.getId(), d);
			}
		}
		stage.close();
	}
	
	@FXML public void actionCancel() {
		stage.close();
	}
	
	public void setName(String name) {
		this.txtName.setText(name);
	}
	
	public void setNewLimit(int limit) {
		this.txtNewLimit.setText(Integer.toString(limit));
	}
	
	public void setRepeatedLimit(int limit) {
		this.txtRepeatedLimit.setText(Integer.toString(limit));
	}
	
	public void setOrder(Order order) {
		this.cbOrder.getSelectionModel().select(order);
	}
}
