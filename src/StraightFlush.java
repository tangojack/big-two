
/**Straight Flush class
 * @author Vinit Miranda
 *
 */
public class StraightFlush extends Hand {
	private static final long serialVersionUID = -7319311705716526303L;
	/**
	 * Constructor for adding the cards
	 * @param player
	 * @param cards
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	@Override
	public boolean isValid() {
		this.sort();
		int firstSuit = this.getCard(0).getSuit();
		boolean flag1 = true;
		for (int i = 1; i < 5 && flag1 == true; i++){
			if (this.getCard(i).getSuit() != firstSuit){
				flag1 = false;
			}
		}
		int rank = this.getCard(0).getRank();
		int kingIndex = -1;
		boolean flag2 = true;
		for (int i = 1; i < 5 && flag2 == true; i++){
			rank = (rank == 12) ? 0 : rank + 1;
			if (this.getCard(i).getRank() != rank){
				flag2 = false;					
			}
		}              							
		for (int i = 0; i < 5; i++){//Checking if King is in the middle of the straight,
			if (this.getCard(i).getRank() == 12){
				kingIndex = i;
			}
		}
		if (kingIndex != -1){
			if (!(kingIndex == 4 || kingIndex == 3 || kingIndex == 2)){
				flag2 = false;
			}
		}
		return flag1 && flag2;
	}

	@Override
	public String getType() {
		return "StraightFlush";
	}

}
