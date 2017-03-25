
/**Triple Hand class
 * @author Vinit Miranda
 *
 */
public class Triple extends Hand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor for adding the cards
	 * @param player
	 * @param cards
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	@Override
	public boolean isValid() {
		boolean flag = (this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank()) ? true : false;
		return flag;
	}

	@Override
	public String getType() {
		return "Triple";
	}

}
