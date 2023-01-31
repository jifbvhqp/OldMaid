package poker_games;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
public class poker_main {
	
	public static void main(String str[]) throws InterruptedException {
		List<Card> cards = new ArrayList<>();
		List<Player> players = new ArrayList<>();
		players.add(new Player("PlayerA"));
		players.add(new Player("PlayerB"));
		players.add(new Player("PlayerC"));
		players.add(new Player("PlayerD"));
		initialiseCard(cards);
		washCards(cards);
		sendCardsToPlayers(players,cards);
		for(Player player : players) {
			checkIfHavePairs(player);
		}
		//printPlayersCards(players);
		
		int turn = 0,n = players.size();
		boolean[] win = new boolean[n];
		
		while(true) {
			int next = getNext_turn(win,turn);//(turn+1)%n;
			if(next == -1) break;
			if(turn == 0) {
				checkIfHavePairs(players.get(turn));
				//printOthersCards(players,win);
				printPlayersCards(players,win);
				//printPlayerCards(players.get(turn));
				System.out.printf("請輸入你要抽玩家[%s]的哪張牌 輸入index:",
								  players.get(next).name);
				printOptionalIndex(players.get(next));
				System.out.println();
				int idx;
				Scanner scanner = new Scanner(System.in);
				try {
					idx = scanner.nextInt();
				}catch(Exception e) {
					System.out.println("請重新輸入");
					continue;
				}
				
				System.out.printf("你抽到了玩家[%s]的 ",players.get(next).name);
				Thread.sleep(1000);
				System.out.println(players.get(next).cards.get(idx));
				players.get(turn).cards.add(players.get(next).cards.get(idx));
				players.get(next).cards.remove(players.get(next).cards.get(idx));
				checkIfHavePairs(players.get(turn));
			}
			else {
				
				if(win[0]) {
					printPlayersCards(players,win);
				}
				
				checkIfHavePairs(players.get(turn));
				System.out.printf("玩家[%s]抽了 玩家[%s]的:",
						players.get(turn).name,
						players.get(next).name);
				int random_pick = (int) (Math.random()*players.get(next).cards.size());
				Thread.sleep(2000);
				//System.out.println(random_pick);
				System.out.println(players.get(next).cards.get(random_pick));
				players.get(turn).cards.add(players.get(next).cards.get(random_pick));
				players.get(next).cards.remove(players.get(next).cards.get(random_pick));
				checkIfHavePairs(players.get(turn));	
				
			}
			if(players.get(turn).cards.size() == 0) {
				System.out.println(players.get(turn).name+"贏了");
				win[turn] = true;
				turn = getNext_turn(win,turn);
			}

			if(players.get(next).cards.size() == 0) {
				System.out.println(players.get(next).name+"贏了");	
				win[next] = true;
				turn = getNext_turn(win,turn);
			}else {
				turn = next;
			}

			Thread.sleep(1000);
		}
		int loser = -1;
		System.out.print("輸家是");
		for(int i = 0; i < n; ++i) {
			if(!win[i]) {
				loser = i;
				break;
			}
		}
		System.out.print(players.get(loser).name);
	}
	
	public static int getNext_turn(boolean[] win,int current) {
		int n = win.length;
		for(int i = current + 1; i < current + n; ++i) {
			int t = i % n;
			if(!win[t]) return t;
		}
		return -1;
	}
	
	public static void checkIfHavePairs(Player player) {
		Map<Integer, List<Integer>> countMap = new HashMap<>();
		for(int j = 0; j < player.cards.size(); ++j) {
			if(!countMap.containsKey(player.cards.get(j).rank)) {
				List<Integer> t = new ArrayList<>();
				t.add(j);
				countMap.put(player.cards.get(j).rank,t);
			}else {
				List<Integer> t = countMap.get(player.cards.get(j).rank);
				t.add(j);
				countMap.put(player.cards.get(j).rank,t);
			}		
		}
		List<Card> players_cards = new ArrayList<>();
		for(int j = 1; j <= 13; ++j) {
			if(countMap.containsKey(j)) {
				if(countMap.get(j).size() == 2 || countMap.get(j).size() == 4) {
					//
				}
				else if(countMap.get(j).size() == 3) {	
					players_cards.add(player.cards.get(countMap.get(j).get(0)));
				}
				else {
					players_cards.add(player.cards.get(countMap.get(j).get(0)));
				}
			}
		}
		if(countMap.containsKey(-1)) players_cards.add(player.cards.get(countMap.get(-1).get(0)));
		player.cards = players_cards;			
	}
	
	public static void sendCardsToPlayers(List<Player> players,List<Card> cards) {
		for(Player player : players) {
			for(int i = 0;i < 13; ++i) {
				Card card = cards.remove(0);
				player.cards.add(card);
			}
		}
		int random_idx = (int) (Math.random()*players.size());//產生從[0,10)
		players.get(random_idx).cards.add(new Card("🃏",-1));
	}
	
	public static void printOptionalIndex(Player player) {
		System.out.print("[");
		for(int i = 0;i < player.cards.size(); ++i) {
			String s = (i == player.cards.size() - 1) ? String.valueOf(i) : String.valueOf(i) + " ";
			System.out.print(s);
		}
		System.out.print("]");
	}
	
	public static void printPlayerCards(Player player) {
		System.out.printf("玩家[%s]的手牌是: ",player.name);
		System.out.println(player.cards);
	}
	
	public static void printOthersCards(List<Player> players,boolean[] win) {
		for(int i = 0; i < players.size(); ++i) {
			if(win[i]) continue;
			System.out.printf("玩家[%s]有: ",players.get(i).name);
			System.out.println(players.get(i).cards.size()+"張手牌");
		}
	}
	
	public static void printPlayersCards(List<Player> players,boolean[] win) {
		for(int i = 0; i < players.size(); ++i) {
			if(win[i]) continue;
			System.out.printf("玩家[%s]的手牌是: ",players.get(i).name);
			System.out.println(players.get(i).cards);
		}
	}
	
	public static void initialiseCard(List<Card> cards) {
		for(String s : new String[] {"♣","♠","♥","♦"}) {
			for(int i = 1; i <= 13; ++i) {
				Card c = new Card(s,i);
				cards.add(c);
			}
		}
	}
	
	public static void washCards(List<Card> cards) {
		Random random = new Random();
		for(int i = 0; i < cards.size(); ++i) {
			int toExchange = random.nextInt(cards.size());
			Card tempCard = cards.get(i);
			cards.set(i, cards.get(toExchange));
			cards.set(toExchange, tempCard);
		}
	}
}
