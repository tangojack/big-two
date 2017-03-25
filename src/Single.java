
/** Single Card class
 * @author Vinit Miranda
 *
 */
public class Single extends Hand {
	private static final long serialVersionUID = 112L;
	/**
	 * Constructor for adding the cards
	 * @param player
	 * @param cards
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	@Override
	public boolean isValid(){
		return true;
	}
	@Override
	public String getType() {
		return "Single";
	}

}
