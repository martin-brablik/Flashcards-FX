package cz.inf.upol.jj2.martinbrablik.flashcardsfx.oredering;

import java.util.Optional;

import cz.inf.upol.jj2.martinbrablik.flashcardsfx.Main;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Deck;

public abstract class Order {
	
	public boolean isReverse;
	public abstract void order(Deck d);
	public abstract String getName();
	public abstract String toString();
	
	public Order(boolean reverse) {
		this.isReverse = reverse;
	}
	
	public static Order parse(String name) {
		Optional<Order> order = Main.orderingMethods.stream().filter(o -> o.getName().equals(name)).findFirst();
		return order.isPresent() ? order.get() : null;
	}
}
