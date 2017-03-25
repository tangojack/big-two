import java.io.*;
import java.net.*;
import java.util.*;
/**
 * The BigTwo class is used to model a Big Two card game. It has private instance variables for storing a deck of cards, a list of players, a list of hands played on the table, an index of the current player, and a console for providing the user interface. Below is a detailed description for the BigTwo class.
 * @author Vinit Miranda
 *
 */
public class BigTwoClient implements CardGame, NetworkGame {
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID;
	private int currentIdx;
	private String playerName;
	private String serverIP;
	private Socket sock;
	private ObjectOutputStream oos;
	private BigTwoTable table;
	private int serverPort;
	
	//My own instance variables;
	int passCounter = 0;
	boolean legal = true;
	int sizeOnTable = 0;
	boolean firstPlay = true;
	boolean everyonePassed = false;
	boolean gameStart = true;
	final private Card startCard = new Card(0,2);
	/**
	 * a constructor for creating a Big Two card game.
	 */
	public BigTwoClient(){
		playerList = new ArrayList<>();
		handsOnTable = new ArrayList<>();
		for (int i = 0; i < 4; i++){
			playerList.add(new CardGamePlayer());
		}
		table = new BigTwoTable(this);
		table.enable();
		makeConnection();
	}
	/**
	 * a method to reset the instance variables of the BigTwo class
	 */
	public void reset(){
		handsOnTable = new ArrayList<>();
		passCounter = 0;
		legal = true;
		sizeOnTable = 0;
		firstPlay = true;
		everyonePassed = false;
		gameStart = true;
	}
	@Override
	public int getNumOfPlayers() {
		return numOfPlayers;
	}

	public Deck getDeck(){
		return deck;
	}
	
	/**
	 * a method for retrieving the list of players.
	 * @return
	 */
	public ArrayList<CardGamePlayer> getPlayerList(){
		return playerList;
	}
	/**
	 * a method for retrieving the list of hands played on the table.
	 * @return
	 */
	public ArrayList<Hand> getHandsOnTable(){
		return handsOnTable;
	}
	/**a method for retrieving the index of the current player.
	 * @return
	 */
	public int getCurrentIdx(){
		return currentIdx;
	}

	public void start(Deck deck){
		table.enable();
		ArrayList<CardGamePlayer> players = getPlayerList();
		
		//Resetting
		for (int i = 0; i < players.size(); i++)
			players.get(i).removeAllCards();
		handsOnTable = new ArrayList<>();
		//Dealing cards
		for (int i = 0; i < players.size(); i++){
			for (int j = 0; j < 13; j++){
				players.get(i).addCard(deck.getCard(j + (i * 13)));
			}
		}
		//Determining who has the 3 of diamonds
		for (int i = 0; i < players.size(); i++){
			players.get(i).sortCardsInHand();
			if(players.get(i).getCardsInHand().contains(startCard)){
				currentIdx = i;
			}
		}
		table.printMsg(playerList.get(currentIdx).getName() + "'s turn:\n");
		//Setting start Player
		//table.setActivePlayer(currentIdx);
		//table.repaint();
	}
	
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		CardGameMessage move = new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx);
		sendMessage(move);
	}
	public void checkMove(int playerID, int[] cardIdx) {
		ArrayList<CardGamePlayer> players = getPlayerList();
		CardList cardList = new CardList();
		CardList hand = new CardList();
		CardGamePlayer player = new CardGamePlayer();
		Hand validHand = null;
		player = players.get(currentIdx); 							//Getting current player
		cardList = player.getCardsInHand();							//Getting the cards in hand of current player
		hand = player.play(cardIdx);								//Returns a hand from the array
	
		
		//Pass in FIRST PLAY
		if(hand == null && firstPlay == true){
			legal = false;
		}
		else{
			sizeOnTable = firstPlay ? 0 : handsOnTable.get(handsOnTable.size() - 1).size();
			//Pass if NOT FIRST PLAY
			if(hand == null){	
				passCounter++;
				table.printMsg("{Pass}\n");
				if (passCounter == 3){	//Reset the hand on table
					everyonePassed = true; 
					sizeOnTable = 0;
					passCounter = 0;
					legal = true;
					handsOnTable = new ArrayList<>();
				}
				currentIdx = (currentIdx == 3) ? 0 : currentIdx + 1; //Setting index
				//table.setActivePlayer(currentIdx);			 //Setting player
				firstPlay = everyonePassed ? true : false;
				everyonePassed = false;
			}
			else {
				validHand = composeHand(player, hand);//If not pass, then compose a validHand
				//validHand.print();
				if(validHand != null && (firstPlay || handsOnTable.size() == 0 || (sizeOnTable == validHand.size() && validHand.beats(handsOnTable.get(handsOnTable.size() - 1))))){					 			 //If not null, then play the validHand
					if (gameStart && validHand.contains(startCard)){
						legal = false;
						gameStart = false;
					}
					if (!gameStart){
						for (int i = 0; i < validHand.size(); i++)	  	//Remove the cards from the cardList(cards in hand) 
							cardList.removeCard(validHand.getCard(i));
						if (!endOfGame()){
							passCounter = 0;
							table.printMsg("{" + validHand.getType() + "} " + validHand.toString() + "\n");
							handsOnTable.add(validHand);		 	 //Add the hand to handsOnTable
							currentIdx = (currentIdx == 3) ? 0 : currentIdx + 1;  
							//table.setActivePlayer(currentIdx);
							legal = true;
							firstPlay = everyonePassed ? true : false;
							everyonePassed = false;
						}
					}	
				}
				else{
					legal = false;
				}
			}	
		}
		if (gameStart){
			legal = false;
		}
		if (!legal){
			table.printMsg("Not a legal move!!!\n");
			legal = true;	
		}
		String result = "";
		if (endOfGame()){
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			int i = 0;
			table.disable();
			result += "Game ends\n";
			for (CardGamePlayer x : players){
				if (x.getNumOfCards() != 0)
					result += playerList.get(currentIdx).getName() + " has " + x.getNumOfCards() + " cards in hand.\n";
				else
					result += playerList.get(currentIdx).getName() + " wins the game.\n"; 
				i++;
			}
			table.showEnd(result);
		}
		else{
			table.printMsg(playerList.get(currentIdx).getName() + "'s turn:\n");
		}
		table.resetSelected();
		
		table.repaint();
	
	}
	
	public boolean endOfGame() {
		if(playerList.get(currentIdx).getCardsInHand().isEmpty())
			return true;
		else
			return false;
	}
	
	
	/**
	 * a method for starting a Big Two card game. It should create a Big Two card game, create and shuffle a deck of cards, and start the game with the deck of cards.
	 * @param args
	 */
	public static void main(String args[]){
		BigTwoClient game = new BigTwoClient();
	}

	/**
	 * a method for returning a valid hand from the specified list of cards of the player. Returns null is no valid hand can be composed from the specified list of cards.
	 * @param player
	 * @param hand
	 * @return
	 */
	public static Hand composeHand(CardGamePlayer player, CardList hand){
		if (hand.size() == 1){
			Single single = new Single(player, hand);
			//addCards(single, hand);
			if (single.isValid()){
				return single;
			}
			else
				return null;	
		}
		else if (hand.size() == 2){
			Pair pair = new Pair(player, hand);
			//addCards(pair, hand);
			if (pair.isValid()){	
				return pair;
			}
			else
				return null;
		}
		else if (hand.size() == 3){
			Triple triple = new Triple(player, hand);
			//addCards(triple, hand);
			if (triple.isValid()){
				return triple;
			}
			else
				return null;
		}
		else if (hand.size() == 5){
			StraightFlush straightFlush = new StraightFlush(player, hand);
			if (straightFlush.isValid()){
				return straightFlush;
			}
			
			Quad quad = new Quad(player, hand);
			if (quad.isValid()){
				return quad;
			}

			FullHouse fullHouse = new FullHouse(player, hand);
			if (fullHouse.isValid()){
				return fullHouse;
			}
			
			Flush flush = new Flush(player, hand);
			if (flush.isValid()){
				return flush;
			}
			
			Straight straight = new Straight(player, hand);
			if (straight.isValid()){
				return straight;
			}
			return null;
		}
		else
			return null;
	}
	@Override
	public int getPlayerID() {
		return playerID;
	}
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	@Override
	public String getPlayerName() {
		return playerName;
	}
	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;		
	}
	@Override
	public String getServerIP() {
		return serverIP;
	}
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	@Override
	public int getServerPort() {
		return serverPort;
	}
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	@Override //DONE
	public void makeConnection() {
		try {
			sock = new Socket(serverIP, serverPort);
			oos = new ObjectOutputStream(sock.getOutputStream());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		Thread thread = new Thread(new ServerHandler());
		thread.start();
		//table.printMsg(playerName+ "LOL\n");
		sendMessage(new CardGameMessage(CardGameMessage.JOIN,
				-1, playerName)); //JOIN

		sendMessage(new CardGameMessage(CardGameMessage.READY,
				-1, null));			   //READY
	}
	public class ServerHandler implements Runnable{
		public void run(){
			try{
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				while (true) {
					parseMessage((CardGameMessage)ois.readObject());
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public synchronized void parseMessage(GameMessage message) {
		
		// parses the message based on it type
		switch (message.getType()) {
		case CardGameMessage.PLAYER_LIST:
			setPlayerID(message.getPlayerID());
			String names[] = (String[])message.getData();
			int i = 0;
			for (CardGamePlayer player : playerList){
				player.setName(names[i]);
				i++;
			}
			table.setActivePlayer(message.getPlayerID());
			table.disableConnect();
			table.repaint();
			break;
		case CardGameMessage.JOIN:
			// adds a player to the game
			String name = (String)(message.getData());
			playerList.get(message.getPlayerID()).setName(name);
			numOfPlayers = 0;
			for (CardGamePlayer player : playerList){
				if (player.getName() != null){
					numOfPlayers++;
				}
			}
			table.repaint();
			break;
		case CardGameMessage.FULL:
			table.printMsg("The Server is full");
			break;
		case CardGameMessage.QUIT:
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " left the game\n");
			playerList.get(message.getPlayerID()).setName(null);
			for (CardGamePlayer player : playerList){
				player.removeAllCards();
			}
			reset();
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			table.repaint();
			break;
		case CardGameMessage.READY:
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " is ready.\n");
			table.repaint();
			break;
		case CardGameMessage.START:
			BigTwoDeck bigTwoDeck = (BigTwoDeck)message.getData();
			start(bigTwoDeck);
			table.repaint();
			break;
		case CardGameMessage.MOVE:
			int[] moves  = (int[]) message.getData();
			checkMove(message.getPlayerID(), moves);
			break;
		case CardGameMessage.MSG:
			table.printChatMsg(message.getData().toString() + "\n");
			break;
		default:
			System.out.println("Wrong message type: " + message.getType());
			break;
		}
	}
	@Override
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
