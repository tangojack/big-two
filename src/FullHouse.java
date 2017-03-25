
/**
 * Full House hand
 * @author Vinit Miranda
 *
 */
public class FullHouse extends Hand{


	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for adding the cards
	 * @param player
	 * @param cards
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);	
	}
	@Override
	public boolean isValid() {
		boolean flag = false;
		this.sort();
		if (this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank()){
			if (this.getCard(3).getRank() == this.getCard(4).getRank()){
				flag = true;
			}
		}
		else if (this.getCard(0).getRank() == this.getCard(1).getRank()){
			if (this.getCard(2).getRank() == this.getCard(3).getRank() && this.getCard(3).getRank() == this.getCard(4).getRank()){
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public String getType() {
		return "FullHouse";
	}

}
