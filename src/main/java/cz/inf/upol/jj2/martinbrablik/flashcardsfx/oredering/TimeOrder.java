package cz.inf.upol.jj2.martinbrablik.flashcardsfx.oredering;

import java.util.Comparator;

import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Card;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Deck;
import javafx.collections.FXCollections;

public class TimeOrder extends Order {
	
	public static final String NAME = "Order by Time (Ascending)";
	public static final String NAME_REVERSE = "Order by Time (Descending)";
	
	public TimeOrder(boolean reverse) {
		super(reverse);
	}
	
	@Override
	public void order(Deck d) {
		d.getCards().sort(Comparator.comparing(Card::getId));
		if(isReverse)
			FXCollections.reverse(d.getCards());
	}
	
	public String getName() {
		return isReverse ? NAME_REVERSE : NAME;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
