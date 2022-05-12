package cz.inf.upol.jj2.martinbrablik.flashcardsfx.controllers;

import java.io.IOException;
import java.util.Optional;

import cz.inf.upol.jj2.martinbrablik.flashcardsfx.DataHandler;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.Utils;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Card;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Deck;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DeckViewController extends Controller {
	
	private Scene mainScene;
	private CardEditController cardEditController;
	private Deck deck;
	
	@FXML private Label lbName; 
	@FXML private Label lbScore; 
	@FXML private Label lbNew;
	@FXML private Label lbRepeated;
	@FXML private Label lbTotal;
	@FXML private Button btnShowCards;
	@FXML private TableView<Card> listCards;
	@FXML private TableColumn<Card, String> colFront;
	@FXML private TableColumn<Card, String> colBack;
	@FXML private TableColumn<Card, Integer> colScore;
	
	@Override
	public void initialize() {
		colFront.setCellValueFactory(new PropertyValueFactory<Card, String>("front"));
		colBack.setCellValueFactory(new PropertyValueFactory<Card,String>("back"));
		colScore.setCellValueFactory(new PropertyValueFactory<Card,Integer>("score"));
		listCards.setRowFactory(tv -> {
			TableRow<Card> row = new TableRow<Card>();
			row.setOnMouseClicked(this::actionItemClick);
			return row;
		});
	}
	
	public void loadCardEditWindow() {
		try {
			Card card = listCards.getSelectionModel().getSelectedItem();
			FXMLLoader loader = new FXMLLoader(DeckViewController.class.getResource("/fxml/card_edit_window.fxml"));
			Parent root;
			root = loader.load();
			Stage editStage = new Stage();
			editStage.initOwner(stage);
			editStage.initModality(Modality.WINDOW_MODAL);
			editStage.setTitle("Flashcards FX - Edit card");	
			cardEditController = loader.getController();
			cardEditController.setPrimaryStage(editStage);
			cardEditController.setCard(card);
			cardEditController.setBack(card.getBack());
			cardEditController.setFront(card.getFront());
			cardEditController.selectDeck(card.getDeck());
			Scene scene = new Scene(root, 400, 200);
			editStage.setScene(scene);
			editStage.showAndWait();
			listCards.refresh();
			DataHandler.saveCard(listCards.getSelectionModel().getSelectedItem());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@FXML public void actionEdit() {
		loadCardEditWindow();
	}
	
	public void actionItemClick(MouseEvent e) {
		if(e.getClickCount() == 2) {
			loadCardEditWindow();
		}
	}
	
	@FXML public void actionDeleteCard() {
		Optional<ButtonType> confirmResult = Utils.confirmAction("Are you sure?", "This action will delete this card.");
		if(confirmResult.isPresent()) {
			if(confirmResult.get() == ButtonType.OK)
				listCards.getSelectionModel().getSelectedItem().delete();
		}
	}
	
	@FXML public void actionCreate() throws IOException {
		FXMLLoader loader = new FXMLLoader(DeckViewController.class.getResource("/fxml/card_edit_window.fxml"));
		Parent root = loader.load();
		Stage createStage = new Stage();
		createStage.initOwner(stage);
		createStage.initModality(Modality.WINDOW_MODAL);
		createStage.setTitle("Flashcards FX - Add New Card");	
		cardEditController = loader.getController();
		cardEditController.setPrimaryStage(createStage);
		Scene scene = new Scene(root, 400, 200);
		createStage.setScene(scene);
		createStage.show();
	}
	
	@FXML public void actionBack() throws IOException {
		this.stage.setScene(mainScene);
	}

	public void setMainScene(Scene scene) {
		this.mainScene = scene;
	}
	
	public void setDeckName(String deckName) {
		lbName.setText(deckName);
	}

	public void setTotalScore(int totalScore) {
		lbScore.setText("Score: " + totalScore);
	}

	public void setNewCards(int newCards) {
		lbNew.setText(deck.getNewCardsLimit() == 0 ? "New Cards: " + newCards : "New Cards: " + newCards + " / " + deck.getNewCardsLimit());
	}

	public void setRepeatedCards(int repeatedCards) {
		lbRepeated.setText(deck.getRepeateedCardsLimit() == 0 ? "Repeated Cards: " + repeatedCards : "Repeated Cards: " + repeatedCards + " / " + deck.getRepeateedCardsLimit());
	}
	
	public void setTotal(int total) {
		this.lbTotal.setText("Total: " + total);
	}
	
	public void setDeck(Deck d) {
		this.deck = d;
	}
	
	public void setTitle(String text) {
		this.lbName.setText(text);
	}
	public void setListCards(ObservableList<Card> cards) {
		listCards.setItems(cards);
	}
}