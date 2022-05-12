package cz.inf.upol.jj2.martinbrablik.flashcardsfx;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextFormatter.Change;

public class Utils {

	public static Change filterInteger(Change change) {
		String newText = change.getControlNewText();
	    if (newText.matches("([1-9][0-9]*)?")) { 
	        return change;
	    }
	    return null;
	}
	
	public static void alert(String head, String body) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(head);
		alert.setContentText(body);
		alert.showAndWait();
	}
	
	public static Optional<ButtonType> confirmAction(String head, String body) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete");
		alert.setHeaderText(head);
		alert.setContentText(body);
		return alert.showAndWait();
	}
	
	public static void info(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
