
/**Quad hand class
 * @author Vinit Miranda
 *
 */
public class Quad extends Hand{
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor for adding the cards
	 * @param player
	 * @param cards
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValid() {
		this.sort();
		boolean flag = true;
		int firstRank = this.getCard(0).getRank();
		int secondRank = this.getCard(1).getRank();
		int start, end;
		if(firstRank != secondRank){
			start = 2;
			end = 5;
		}
		else{
			start = 1;
			end = 4;
		}
		for (int i = start; i < end && flag == true; i++){
			if (this.getCard(i).getRank() != secondRank){
				flag = false;
			}
		}		
		return flag;
	}
	
	@Override
	public String getType() {
		return "Quad";
	}

}
