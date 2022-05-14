package cz.inf.upol.jj2.martinbrablik.flashcardsfx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.SystemUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Card;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.models.Deck;
import cz.inf.upol.jj2.martinbrablik.flashcardsfx.oredering.Order;

public class DataHandler {
	
	private static String dataPath = SystemUtils.IS_OS_WINDOWS ? System.getenv("appdata") + "/FlashcardsFX/" : System.getenv("HOME") + "/.flashcardsfx/";
	
	public static void deleteCard(Card card) {
		try {
			File cardFile = new File(dataPath + card.getDeck().getId() + "/" + card.getId() + ".xml");
			cardFile.delete();
		}
		catch(SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteDeck(Deck deck) {
		File dataDirectory = new File(dataPath);
		try {
			for(File file : dataDirectory.listFiles()) {
				if(file.getName().equals(deck.getId() + ".xml")) {
					file.delete();
				}
				if(file.getName().equals(Integer.toString(deck.getId())) && file.isDirectory()) {
					File deckDataDirectory = new File(dataPath + file.getName());
					for(File cardFile : deckDataDirectory.listFiles())
						cardFile.delete();
					file.delete();
				}
			}
		}
		catch(SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadData() {
		loadDecks();
	}
	
	private static void loadDecks() {
		File dataDirectory = new File(dataPath);
		if(!dataDirectory.exists())
			return;
		try {
			for(File file : dataDirectory.listFiles()) {
				if(file.isFile()) {
					DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
					Document doc = documentBuilder.parse(file);
					Element root = doc.getDocumentElement();
					int deckId = Integer.parseInt(root.getAttribute("id"));
					String deckName = "";
					int deckNewCardsLimit = -1;
					int deckRepeatedCardsLimit = -1;
					Order deckOrderingMethod = null;
					for(int i = 0; i < root.getChildNodes().getLength(); i++) {
						Node node = root.getChildNodes().item(i);
						switch(node.getNodeName()) {
							case "name":
								deckName = node.getTextContent();
								break;
							case "newCardsLimit":
								deckNewCardsLimit = Integer.parseInt(node.getTextContent());
								break;
							case "repeatedCardsLimit":
								deckRepeatedCardsLimit = Integer.parseInt(node.getTextContent());
								break;
							case "order":
								deckOrderingMethod = Order.parse(node.getTextContent());
								break;
						}
					}
					if(deckName.length() > 0 && deckNewCardsLimit >= 0 && deckRepeatedCardsLimit >= 0 && deckOrderingMethod != null) {
						Deck deck = new Deck(deckId, deckName, deckOrderingMethod, deckNewCardsLimit, deckRepeatedCardsLimit);
						loadCards(deck);
						Main.decks.add(deck);
						deck.getOrderingMethod().order(deck);
					}
				}
			}
		}
		catch(SecurityException | ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void loadCards(Deck deck) {
		File cardsDirectory = new File(dataPath + deck.getId());
		try {
			for(File file : cardsDirectory.listFiles()) {
				if(file.isFile()) {
					DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
					Document doc = documentBuilder.parse(file);
					Element root = doc.getDocumentElement();
					int cardId =Integer.parseInt(root.getAttribute("id"));
					String cardFront = "";
					String cardBack = "";
					int cardScore = -1;
					boolean cardIsNew = false;
					for(int i = 0; i < root.getChildNodes().getLength(); i++) {
						Node node = root.getChildNodes().item(i);
						switch(node.getNodeName()) {
							case "front":
								cardFront = node.getTextContent();
								break;
							case "back":
								cardBack = node.getTextContent();
								break;
							case "score":
								cardScore = Integer.parseInt(node.getTextContent());
								break;
							case "isNew":
								cardIsNew = Boolean.parseBoolean(node.getTextContent());
								break;
						}
					}
					Card card = new Card(cardId, cardFront, cardBack, cardScore, cardIsNew);
					deck.getCards().add(card);
				}
			}
		}
		catch(SecurityException | ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveCard(Card card) {
		try {
			if(Files.notExists(Path.of(dataPath)))
				Files.createDirectory(Path.of(dataPath));
			Path deckFilePath = Path.of(dataPath, Integer.toString(card.getDeck().getId()) + ".xml");
			if(!Files.notExists(deckFilePath))
				saveDeck(card.getDeck());
			Path cardFilePath = Path.of(dataPath, Integer.toString(card.getDeck().getId()), card.getId() + ".xml");
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document doc = documentBuilder.newDocument();
			Element rootElement = doc.createElement("card");
			Element frontElement = doc.createElement("front");
			Element backElemnt = doc.createElement("back");
			Element scoreElement = doc.createElement("score");
			Element isNewElement = doc.createElement("isNew");
			rootElement.setAttribute("id", Integer.toString(card.getId()));
			frontElement.setTextContent(card.getFront());
			backElemnt.setTextContent(card.getBack());
			scoreElement.setTextContent(Integer.toString(card.getScore()));
			isNewElement.setTextContent(Boolean.toString(card.isNew()));
			doc.appendChild(rootElement);
			rootElement.appendChild(frontElement);
			rootElement.appendChild(backElemnt);
			rootElement.appendChild(scoreElement);
			rootElement.appendChild(isNewElement);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			FileOutputStream fos = new FileOutputStream(cardFilePath.toString());
			StreamResult result = new StreamResult(fos);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			fos.close();
		}
		catch(IOException | ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateDeck(int deckId, Deck newDeck) {
		File dataDirectory = new File(dataPath);
		try {
			for(File file : dataDirectory.listFiles()) {
				if(file.isFile() && file.getName().equals(Integer.toString(deckId) + ".xml")) {
					DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
					Document doc = documentBuilder.newDocument();
					Element rootElement = doc.createElement("deck");
					Element nameElement = doc.createElement("name");
					Element newCardsLimitElement = doc.createElement("newCardsLimit");
					Element repeatedCardsLimitElement = doc.createElement("repeatedCardsLimit");
					Element orderingElement = doc.createElement("order");
					rootElement.setAttribute("id", Integer.toString(newDeck.getId()));
					nameElement.setTextContent(newDeck.getName());
					newCardsLimitElement.setTextContent(Integer.toString(newDeck.getNewCardsLimit()));
					repeatedCardsLimitElement.setTextContent(Integer.toString(newDeck.getRepeateedCardsLimit()));
					orderingElement.setTextContent(newDeck.getOrderingMethod().toString());
					doc.appendChild(rootElement);
					rootElement.appendChild(nameElement);
					rootElement.appendChild(newCardsLimitElement);
					rootElement.appendChild(repeatedCardsLimitElement);
					rootElement.appendChild(orderingElement);
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(file);
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.transform(source, result);
				}
			}
		}
		catch(SecurityException | ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveDeck(Deck deck) {
		createDirectory(deck);
		createDeckFile(deck);
	}
	
	private static void createDirectory(Deck deck) {
		try {
			if(Files.notExists(Path.of(dataPath)))
				Files.createDirectory(Path.of(dataPath));
			Path deckDirPath = Path.of(dataPath, Integer.toString(deck.getId()));
			if(Files.notExists(deckDirPath))
				Files.createDirectory(deckDirPath);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	private static void createDeckFile(Deck deck) {
		try {
			if(Files.notExists(Path.of(dataPath)))
				Files.createDirectory(Path.of(dataPath));
			Path deckFilePath = Path.of(dataPath, deck.getId() + ".xml");
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document doc = documentBuilder.newDocument();
			Element rootElement = doc.createElement("deck");
			Element nameElement = doc.createElement("name");
			Element newCardsLimitElement = doc.createElement("newCardsLimit");
			Element repeatedCardsLimitElement = doc.createElement("repeatedCardsLimit");
			Element orderingElement = doc.createElement("order");
			rootElement.setAttribute("id", Integer.toString(deck.getId()));
			nameElement.setTextContent(deck.getName());
			newCardsLimitElement.setTextContent(Integer.toString(deck.getNewCardsLimit()));
			repeatedCardsLimitElement.setTextContent(Integer.toString(deck.getRepeateedCardsLimit()));
			orderingElement.setTextContent(deck.getOrderingMethod().toString());
			doc.appendChild(rootElement);
			rootElement.appendChild(nameElement);
			rootElement.appendChild(newCardsLimitElement);
			rootElement.appendChild(repeatedCardsLimitElement);
			rootElement.appendChild(orderingElement);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			FileOutputStream fos = new FileOutputStream(deckFilePath.toString());
			StreamResult result = new StreamResult(fos);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			fos.close();
		}
		catch(IOException | ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
	}
}
