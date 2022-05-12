package cz.inf.upol.jj2.martinbrablik.flashcardsfx;

import cz.inf.upol.jj2.martinbrablik.flashcardsfx.controllers.MainWindowController;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Deck;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.oredering.NewOrder;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.oredering.Order;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.oredering.ScoreOrder;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.oredering.TimeOrder;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static MainWindowController mainWindowController;
	public static final ObservableList<Order> orderingMethods = FXCollections.observableArrayList();
	public static final ObservableList<Deck> decks = FXCollections.observableArrayList();

	public static void main(String[] args) {
		orderingMethods.add(new TimeOrder(false));
		orderingMethods.add(new TimeOrder(true));
		orderingMethods.add(new ScoreOrder(false));
		orderingMethods.add(new ScoreOrder(true));
		orderingMethods.add(new NewOrder(false));
		orderingMethods.add(new NewOrder(true));
		DataHandler.loadData();
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("/fxml/main_window.fxml"));
		Parent root = loader.load();
		mainWindowController = loader.getController();
		mainWindowController.setPrimaryStage(stage);
		stage.setTitle("Flashcards FX");
		stage.setScene(new Scene(root, 600,400));
		stage.show();
	}
}
