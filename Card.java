package poker_games;

public class Card {
	String suit;
	int rank;
	
	public Card(String suit,int rank) {
		this.suit = suit;
		this.rank = rank;
	}
	
	public boolean sameRank(Card c1) {
		return this.rank == c1.rank;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Card)) return false;
		Card card = (Card)obj;
		return this.suit.equals(card.suit) && this.rank == card.rank;
	}
	
	@Override
	public String toString() {
		return String.format("[%s %d]",suit, rank);
	}
	
}
