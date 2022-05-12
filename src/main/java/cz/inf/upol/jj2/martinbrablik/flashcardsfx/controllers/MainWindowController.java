package cz.inf.upol.jj2.martinbrablik.flashcardsfx.controllers;

import java.io.IOException;
import java.util.Optional;

import cz.inf.upol.jj2.martinbrablik.flashcardsfx.Main;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.Utils;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Deck;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainWindowController extends Controller {

	public static DeckEditController deckEditController;
	public static DeckViewController deckViewController;
	public static StudyController studyController;
	
	@FXML private TableView<Deck> listDecks;
	@FXML private TableColumn<Deck, String> colName;
	@FXML private TableColumn<Deck, Integer> colNewCards;
	@FXML private TableColumn<Deck, Integer> colRepeatedCards;
	@FXML private TableColumn<Deck, Integer> colCards;
	
	@FXML private Button btnCreate;
	
	@Override
	public void initialize() {
		colName.setCellValueFactory(new PropertyValueFactory<Deck, String>("name"));
		colNewCards.setCellValueFactory(new PropertyValueFactory<Deck,Integer>("newCards"));
		colRepeatedCards.setCellValueFactory(new PropertyValueFactory<Deck,Integer>("repeatedCards"));
		colCards.setCellValueFactory(new PropertyValueFactory<Deck,Integer>("size"));
		listDecks.setRowFactory(tv -> {
			TableRow<Deck> row = new TableRow<Deck>();
			row.setOnMouseClicked(this::actionClickItem);
			return row;
		});
		listDecks.setItems(Main.decks);
	}
	
	@FXML public void actionDelete() {
		Optional<ButtonType> confirmResult = Utils.confirmAction("Are you sure?", "This action will delete this deck and all cards in it.");
		if(confirmResult.isPresent()) {
			if(confirmResult.get() == ButtonType.OK) {
				Deck deck = listDecks.getSelectionModel().getSelectedItem();
				deck.delete();
			}
		}
	}
	
	@FXML public void actionCreate() throws IOException {
		FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("/fxml/deck_edit_window.fxml"));
		Parent root = loader.load();
		Stage createStage = new Stage();
		createStage.initOwner(stage);
		createStage.initModality(Modality.WINDOW_MODAL);
		createStage.setTitle("Flashcards FX - Create New Deck");	
		deckEditController = loader.getController();
		deckEditController.setPrimaryStage(createStage);
		Scene scene = new Scene(root, 400, 200);
		createStage.setScene(scene);
		createStage.show();
	}
	
	@FXML public void actionEdit() throws IOException {
		Deck deck = listDecks.getSelectionModel().getSelectedItem();
		FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("/fxml/deck_edit_window.fxml"));
		Parent root = loader.load();
		Stage editStage = new Stage();
		editStage.initOwner(stage);
		editStage.initModality(Modality.WINDOW_MODAL);
		editStage.setTitle("Flashcards FX - Edit Deck");	
		deckEditController = loader.getController();
		deckEditController.setPrimaryStage(editStage);
		deckEditController.setDeck(deck.getId());
		deckEditController.setName(deck.getName());
		deckEditController.setNewLimit(deck.getNewCardsLimit());
		deckEditController.setRepeatedLimit(deck.getRepeateedCardsLimit());
		deckEditController.setOrder(deck.getOrderingMethod());
		Scene scene = new Scene(root, 400, 200);
		editStage.setScene(scene);
		editStage.showAndWait();
		listDecks.refresh();
	}
	
	public void actionClickItem(MouseEvent e) {
		if(e.getClickCount() == 2) {
			Deck deck = listDecks.getSelectionModel().getSelectedItem();
			if(deck == null) return;
			FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("/fxml/deck_view_window.fxml"));
			Parent root;
			try {
				root = loader.load();
				double width = this.stage.getScene().getWidth();
				double height = this.stage.getScene().getHeight();
				Scene scene = new Scene(root, width, height);
				deckViewController = loader.getController();
				deckViewController.setPrimaryStage(stage);
				deckViewController.setDeck(deck);
				deckViewController.setDeckName(deck.getName());
				deckViewController.setListCards(deck.getCards());
				deckViewController.setNewCards(deck.getNewCards());
				deckViewController.setRepeatedCards(deck.getRepeatedCards());
				deckViewController.setTotal(deck.getCards().size());
				deckViewController.setTotalScore(deck.getTotalScore());
				deckViewController.setMainScene(this.stage.getScene());
				this.stage.setScene(scene);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	
	@FXML public void actionStudy() throws IOException {
		Deck deck = listDecks.getSelectionModel().getSelectedItem();
		if(deck.getCards().isEmpty()) {
			Utils.info("This deck has no cards. You have to add some cards to this deck first.");
			return;
		}
		FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("/fxml/study_window.fxml"));
		Parent root = loader.load();
		Stage studyStage = new Stage();
		studyStage.initOwner(stage);
		studyStage.initModality(Modality.WINDOW_MODAL);
		studyStage.setTitle("Flashcards FX - Study");	
		studyController = loader.getController();
		studyController.setPrimaryStage(studyStage);
		studyController.setDeck(deck);
		studyController.setDeckName(deck.getName());
		studyController.setFirstCard(deck.getCards().get(0).getFront());
		Scene scene = new Scene(root, 375, 200);
		studyStage.setScene(scene);
		studyStage.show();
	}
	
	public TableView<Deck> getDeckList() {
		return this.listDecks;
	}
}
