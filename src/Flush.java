
/** Flush hand class
 * @author Vinit Miranda
 *
 */
public class Flush extends Hand {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for adding the cards
	 * @param player
	 * @param cards
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValid() {
		this.sort();
		int firstSuit = this.getCard(0).getSuit();
		boolean flag = true;
		for (int i = 1; i < 5 && flag == true; i++){
			if (this.getCard(i).getSuit() != firstSuit){
				flag = false;
			}
		}
		return flag;
	}

	@Override
	public String getType() {
		return "Flush";
	}

}
