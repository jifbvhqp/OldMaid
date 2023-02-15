package poker_games;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Card {
	String suit;
	int rank;
	Image cardUp,cardBack;
	public Card(String suit,int rank) {
		this.suit = suit;
		this.rank = rank;
		String filename = "poker/";
		if(this.suit == "♠") filename += ("Spades" + String.valueOf(this.rank)+".jpg");
		else if(this.suit == "♥") filename += ("Heart" + String.valueOf(this.rank)+".jpg");
		else if(this.suit == "♦") filename += ("Diamond" + String.valueOf(this.rank)+".jpg");	
		else if(this.suit == "♣") filename += ("Club" + String.valueOf(this.rank)+".jpg");	
		else if(this.suit == "🃏") filename += ("Joker1"+".jpg");	
		cardUp = new ImageIcon(filename).getImage();
		cardBack = new ImageIcon("southeast.jpg").getImage();		
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
