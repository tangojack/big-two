
/** Pair hand class
 * @author Vinit Miranda
 *
 */
public class Pair extends Hand{

	private static final long serialVersionUID = 1L;
	/**
	 * Constructor for adding the cards
	 * @param player
	 * @param cards
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	@Override
	public boolean isValid() {
		boolean flag = (this.getCard(0).getRank() == this.getCard(1).getRank()) ? true : false;
		return flag;
	}

	@Override
	public String getType() {
		return "Pair";
	}

}
