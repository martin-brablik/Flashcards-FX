package cz.inf.upol.jj2.martinbrablik.flashcardsfx.models;

import java.util.Optional;

import cz.inf.upol.jj2.martinbrablik.flashcardsfx.Main;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.DataHandler;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.oredering.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Deck {
	
	public static int nullId = -1;
	
	public static Deck createNewDeck(String name, Order orderingMethod, int newCardsLimit, int repeatedCardsLimit) {
		Deck deck = new Deck(name, orderingMethod, newCardsLimit, repeatedCardsLimit);
		Main.decks.add(deck);
		DataHandler.saveDeck(deck);
		return deck;
	}
	
	public static Deck findById(int id) {
		Optional<Deck> deck = Main.decks.stream().filter(d -> d.getId() == id).findFirst();
		return deck.get();
	}
	
	private int id = Deck.nullId;
	private String name;
	private int newCardsLimit = 0;
	private int repeateedCardsLimit = 0;
	private Order orderingMethod;
	private ObservableList<Card> cards;
	
	private Deck(String name, Order orderingMethod, int newCardsLimit, int repeatedCardsLimit) {
		this.id = generateId();
		this.name = name;
		this.orderingMethod = orderingMethod;
		this.newCardsLimit = newCardsLimit;
		this.repeateedCardsLimit = repeatedCardsLimit;
		this.cards = FXCollections.observableArrayList();
	}
	
	public Deck(int id, String name, Order orderingMethod, int newCardsLimit, int repeatedCardsLimit) {
		this.id = id;
		this.name = name;
		this.orderingMethod = orderingMethod;
		this.newCardsLimit = newCardsLimit;
		this.repeateedCardsLimit = repeatedCardsLimit;
		this.cards = FXCollections.observableArrayList();
	}
	
	
	public int generateId() {
		int id = -1;
		for(Deck d : Main.decks) {
			if(d.getId() > id)
				id = d.getId();
		}
		return id + 1;
	}
	
	public void delete() {
		this.getCards().forEach(c -> c.remove());
		this.getCards().clear();
		Main.decks.remove(this);
		DataHandler.deleteDeck(this);
	}
	
	public Card findCardById(int id) {
		for(Card c : this.cards) {
			if(c.getId() == id)
				return c;
		}
		return null;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNewCardsLimit() {
		return newCardsLimit;
	}
	public void setNewCardsLimit(int newCardsLimit) {
		this.newCardsLimit = newCardsLimit;
	}
	public int getRepeateedCardsLimit() {
		return repeateedCardsLimit;
	}
	public void setRepeateedCardsLimit(int repeateedCardsLimit) {
		this.repeateedCardsLimit = repeateedCardsLimit;
	}
	public ObservableList<Card> getCards() {
		return cards;
	}
	public void setCards(ObservableList<Card> cards) {
		this.cards = cards;
	}

	public Order getOrderingMethod() {
		return orderingMethod;
	}

	public void setOrderingMethod(Order orderingMethod) {
		this.orderingMethod = orderingMethod;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

	public int getSize() {
		return this.getCards().size();
	}

	public int getNewCards() {
		return (int)this.getCards().stream().filter(c -> c.isNew()).count();
	}
	
	public int getRepeatedCards() {
		return (int)this.getCards().stream().filter(c -> !c.isNew()).count();
	}

	public int getTotalScore() {
		int score = 0;
		for(Card c : this.cards)
			score += c.getScore();
		return score;
	}
}
