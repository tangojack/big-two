

/**The BigTwoCard class is a subclass of the Card class, and is used to model a card used in a Big Two card game. It should override the compareTo() method it inherited from the Card class to reflect the ordering of cards used in a Big Two card game. Below is a detailed description for the BigTwoCard class.
 * @author Vinit Miranda
 *
 */
public class BigTwoCard extends Card{
	
	private static final long serialVersionUID = -5822018182891811204L;
	
	/**a constructor for building a card with the specified suit and rank. suit is an integer between 0 and 3, and rank is an integer between 0 and 12.
	 * @param suit
	 * @param rank
	 */
	public BigTwoCard(int suit, int rank){
		super(suit, rank);
	}
	
	/* method for comparing this card with the specified card for order. Returns a negative integer, zero, or a positive integer as this card is less than, equal to, or greater than the specified card.
	 * (non-Javadoc)
	 * @see Card#compareTo(Card)
	 */
	public int compareTo(Card card) {
		boolean flagRank = false;
		if(this.rank == 0 || this.rank == 1 || card.rank == 0 || card.rank == 1){
			if (this.rank == 1 && card.rank != 1){
				return 1;
			}
			else if (this.rank == 0 && card.rank != 1 && card.rank != 0){
				return 1;
			}
			else if (card.rank == 1 && this.rank != 1){
				return -1;
			}
			else if (card.rank == 0 && this.rank != 1 && this.rank != 0){
				return -1;
			}
			else{
				flagRank = true;
			}
		}
		else if (this.rank > card.rank) {
			return 1;
		}
		else if (this.rank < card.rank) {
			return -1;	
		} 
		else{
			flagRank = true;
		}
		if (flagRank == true){
			if (this.suit < card.suit) {
				return -1;
			} 
			else if (this.suit > card.suit) {
				return 1;
			}
			else{
				return 0;
			}
		}
		else {
			return 0;
		}
	}
}
