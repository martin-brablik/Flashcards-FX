package cz.inf.upol.jj2.martinbrablik.flashcardsfx.models;

import java.util.Optional;

import cz.inf.upol.jj2.martinbrablik.flashcardsfx.Main;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.DataHandler;

public class Card {
	
	private int id;
	private int score;
	private String front;
	private String back;
	private boolean isNew;
	
	public static Card createNewCard(String front, String back, Deck deck) {
		Card card = new Card(front, back, deck);
		deck.getCards().add(card);
		DataHandler.saveCard(card);
		return card;
	}
	
	private Card(String front, String back, Deck deck) {
		this.id = generateId(deck);
		this.isNew = true;
		this.setFront(front);
		this.setBack(back);
	}
	
	public Card(int id, String front, String back, int score, boolean isNew) {
		this.id = id;
		this.front = front;
		this.back = back;
		this.score = score;
		this.isNew = isNew;
	}
	
	public int generateId(Deck deck) {
		int id = -1;
		for(Card c : deck.getCards()) {
			if(c.getId() > id)
				id = c.getId();
		}
		return id + 1;
	}
	
	public void delete() {
		DataHandler.deleteCard(this);
		this.getDeck().getCards().remove(this);
	}
	
	public void remove() {
		DataHandler.deleteCard(this);
	}
	
	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public Deck getDeck() {
		Optional<Deck> deck = Main.decks.stream().filter(d -> d.getCards().contains(this)).findAny();
		if(deck.isPresent())
			return deck.get();
		return null;
	}

	public void setDeck(Deck deck) {
		this.getDeck().getCards().remove(this);
		deck.getCards().add(this);
		deck.getOrderingMethod().order(deck);
	}

	public boolean isNew() {
		return isNew;
	}
	
	public void setNew(boolean value) {
		this.isNew = value;
	}
	
	/*@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Card))
			return false;
		Card other = (Card)obj;
		return this.getId() == other.id && this.getFront().equals(other.getFront()) && this.getBack().equals(other.getBack()) && this.getScore() == other.getScore() && this.isNew() == other.isNew();
	}*/
}
