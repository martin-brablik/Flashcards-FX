package cz.inf.upol.jj2.martinbrablik.flashcardsfx.oredering;

import java.util.Comparator;

import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Card;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Deck;
import javafx.collections.FXCollections;

public class NewOrder extends Order {	

	public static final String NAME = "Repeated Cards First";
	public static final String NAME_REVERSE = "New Cards First";
	
	public NewOrder(boolean reverse) {
		super(reverse);
	}
	
	@Override
	public void order(Deck d) {
		d.getCards().sort(new Comparator<Card>() {
			@Override
			public int compare(Card o1, Card o2) {
				return Boolean.compare(o1.isNew(), o2.isNew());
			}});
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