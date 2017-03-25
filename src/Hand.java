/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards. It has a private instance variable for storing the player who plays this hand. It also has methods for getting the player of this hand, checking if it is a valid hand, getting the type of this hand, getting the top card of this hand, and checking if it beats a specified hand. Below is a detailed description for the Hand class.
 * @author Vinit Miranda
 *
 */
public abstract class Hand extends CardList{
	private static final long serialVersionUID = 1231L;
	private CardGamePlayer player;
	
	/**a constructor for building a hand with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Hand(CardGamePlayer player, CardList cards){
		this.player = player;
		for (int i = 0; i < cards.size(); i++){
			this.addCard(cards.getCard(i));
		}
	}
	
	/**– a method for retrieving the player of this hand.
	 * @return
	 */
	public CardGamePlayer getPlayer(){
		return player;
	}
	
	/**a method for retrieving the top card of this hand.
	 * @return
	 */
	public Card getTopCard(){
		this.sort();
		if(this.getType() == "Single"){
			return this.getCard(0);
		}
		else if(this.getType() == "Pair"){
			return this.getCard(1);
		}
		else if (this.getType() == "Triple"){
			return this.getCard(2);
		}
		else if (this.getType() == "FullHouse"){
			CardList c = new CardList();
			if(this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank()){
				c.addCard(this.getCard(0));
				c.addCard(this.getCard(1));
				c.addCard(this.getCard(2));
			}
			else{
				c.addCard(this.getCard(2));
				c.addCard(this.getCard(3));
				c.addCard(this.getCard(4));
			}
			c.sort();
			return c.getCard(2);
		}
		else if (this.getType() == "Quad"){
			CardList c = new CardList();
			if(this.getCard(0).getRank() == this.getCard(1).getRank()){
				c.addCard(this.getCard(0));
				c.addCard(this.getCard(1));
				c.addCard(this.getCard(2));
				c.addCard(this.getCard(3));
			}
			else{
				c.addCard(this.getCard(1));
				c.addCard(this.getCard(2));
				c.addCard(this.getCard(3));
				c.addCard(this.getCard(4));
			}
			c.sort();
			return c.getCard(3);
		}
		else{
			return this.getCard(4);
		}
		
	}
	
	/**a method for checking if this hand beats a specified hand.
	 * @param hand
	 * @return
	 */
	public boolean beats(Hand hand){
		String type = hand.getType();
		String a = this.getType();
		int x = 0, y = 0;
		String order[] = {"Single", "Pair", "Triple", "Straight", "Flush", "FullHouse", "Quad", "StraightFlush"};
		for (int i = 0; i < 7; i++){
			if (type.equals(order[i])){
				x = i;
			}
			if (a.equals(order[i])){
				y = i;
			}
		}
		if (y > x){
			return true;
		}
		else if (x > y){
			return false;
		}
		else if (a.equals("Flush") && type.equals("Flush")){
			if (this.getCard(0).getSuit() > hand.getCard(0).getSuit()){
				return true;
			}
			else if(this.getCard(0).getSuit() == hand.getCard(0).getSuit()){
				if (this.getTopCard().compareTo(hand.getTopCard()) == 1)
					return true;
				else
					return false;
			}
			else
				return false;
				
		}
		else{
			if (this.getTopCard().compareTo(hand.getTopCard()) == 1)
				return true;
			else
				return false;
		}
	}
	/**a method for checking if this is a valid hand.
	 * @return
	 */
	public abstract boolean isValid();
	
	/**a method for returning a string specifying the type of this hand.
	 * @return
	 */
	public abstract String getType();	
}
