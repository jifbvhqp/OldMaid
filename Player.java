package poker_games;
import java.util.ArrayList;
import java.util.List;

public class Player {
	String name;
	List<Card> cards = new ArrayList<Card>();
	
	public Player(String name) {
		this.name = name;
	}
}
