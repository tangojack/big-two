
/**
 * Straight hand class
 * @author Vinit Miranda
 *
 */
public class Straight extends Hand{
	private static final long serialVersionUID = 4735239255262917363L;
	/**
	 * Constructor for adding the cards
	 * @param player
	 * @param cards
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValid() {
		this.sort();
		int rank = this.getCard(0).getRank();
		int kingIndex = -1;
		int twoIndex = -1;
		boolean flag = true;
		for (int i = 1; i < 5 && flag == true; i++){
			rank = (rank == 12) ? 0 : rank + 1;
			if (this.getCard(i).getRank() != rank){
				flag = false;					
			}
		}              							
		for (int i = 0; i < 5; i++){//Checking if King is in the middle of the straight,
			if (this.getCard(i).getRank() == 12){
				kingIndex = i;
			}
			if (this.getCard(i).getRank() == 1){
				twoIndex = i;
			}
		}
		if (kingIndex != -1){
			if (!(kingIndex == 4 || kingIndex == 3 || kingIndex == 2)){
				flag = false;
			}
		}
		if (twoIndex != 1){
			if (twoIndex == 1){
				flag = false;
			}
		}
		return flag;
	}
	@Override
	public String getType() {
		return "Straight";
	}

}
