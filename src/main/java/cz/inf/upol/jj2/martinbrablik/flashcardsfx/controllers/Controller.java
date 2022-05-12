package cz.inf.upol.jj2.martinbrablik.flashcardsfx.controllers;

import javafx.stage.Stage;

public abstract class Controller {
	
protected Stage stage;
	
	public void setPrimaryStage(Stage stage) {
		this.stage = stage;
	}
	
	public abstract void initialize();

}
