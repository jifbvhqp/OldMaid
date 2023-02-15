package poker_games;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MyPanel extends JPanel implements ActionListener,MouseListener,MouseMotionListener {
	
	final int PANEL_WIDTH = 1200;
	final int PANEL_HEIGHT = 800;
	
	final int Card_H = 150;
	final int Card_W = 105;
	
	Image backgroundImage;
	Timer timer;
	JFrame frame;
	int xStart = 500;
	int yStart = 250;
	int curX = 0,curY = 0;
	int offset = 20;
	
	Graphics2D g2D;
	Card currentCursorPointedCard = null;
	Player currentCursorPointedCardPlayer = null;
	Player playerA;
	
	boolean showing_draw_card = false;
	String player_draw_card_str = "";
	
	boolean drawing_player_card = false;
	String drawing_player_card_str = "";
	
	List<Player> players;
	Thread thread,npcthread;
	
	int turn,next,n;
	boolean[] win;
	
	MyPanel(JFrame frame){
		this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
		//this.setBackground(Color.black);			
		backgroundImage = new ImageIcon("poker/bg.png").getImage();
		addMouseListener(this);
		addMouseMotionListener(this);
		this.setFocusable(true);
		this.frame = frame;
		
		initlized_game();
	
		timer = new Timer(30, this);
		timer.start();
	}
	
	public void initlized_game() {
		List<Card> cards = new ArrayList<>();
		players = new ArrayList<>();
		players.add(new Player("PlayerA"));
		players.add(new Player("PlayerB"));
		players.add(new Player("PlayerC"));
		players.add(new Player("PlayerD"));
		initialiseCard(cards);
		washCards(cards);
		sendCardsToPlayers(players,cards);
		for(Player player : players) checkIfHavePairs(player);
		turn = 0;
		n = players.size();
		win = new boolean[n];
		next = getNext_turn(win,turn);	
		playerA = this.players.get(0);		
		
		showing_draw_card = false;
		player_draw_card_str = "";
		
		drawing_player_card = false;
		drawing_player_card_str = "";
		
		currentCursorPointedCard = null;
		currentCursorPointedCardPlayer = null;
		
		thread = null;
		npcthread = null;
	}
	
	public float dot(Point p1,Point p2) {
		return p1.x*p2.x + p1.y*p2.y;
	}
	
	public Point subtract(Point p2,Point p1) {
		return new Point(p2.x-p1.x,p2.y-p1.y);
	}
	
	public boolean isInRect(Point p1,Point p2,Point p3,Point p4,Point p){
		boolean inRect = false;
		Point v1 = subtract(p2,p1);
		Point v2 = subtract(p3,p1);
		Point v3 = subtract(p4,p1);
		Point v = subtract(p,p1);
		
		if(dot(v,v1) < 0) return inRect;
		if(dot(v,v2) < 0) return inRect;
		if(dot(v,v3) < 0) return inRect;
		
		v1 = subtract(p1,p2);
		v2 = subtract(p3,p2);
		v3 = subtract(p4,p2);
		v = subtract(p,p2);

		if(dot(v,v1) < 0) return inRect;	
		if(dot(v,v2) < 0) return inRect;
		if(dot(v,v3) < 0)return inRect;
				
		v1 = subtract(p1,p3);
		v2 = subtract(p2,p3);
		v3 = subtract(p4,p3);
		v = subtract(p,p3);	
				
		if(dot(v,v1) < 0) return inRect;		
		if(dot(v,v2) < 0) return inRect;	
		if(dot(v,v3) < 0) return inRect;
			
		v1 = subtract(p1,p4);
		v2 = subtract(p2,p4);
		v3 = subtract(p3,p4);
		v = subtract(p,p4);	

		if(dot(v,v1) < 0) return inRect;	
		if(dot(v,v2) < 0) return inRect;
		if(dot(v,v3) < 0) return inRect;
		inRect = true;	
		return inRect;	
		
	}
	
	public void draw_cards_or_getCurCursorImage(Graphics2D g2D) {
		boolean cursorPointedCard = false;
		for(int i = 0; i < players.size(); ++i) {
			for(int j = 0; j < players.get(i).cards.size(); ++j) {
				int cards_size = players.get(i).cards.size();
				int x,y,xOffset,yOffset;
				Point p1,p2,p3,p4;
				Image cardImg = null;
				Card card;
				if(i == 0) cardImg = players.get(i).cards.get(j).cardUp;
				else cardImg = players.get(i).cards.get(j).cardBack;
				
				card = players.get(i).cards.get(j);
				
				if(i == 0) {
					x = xStart+offset*j;
					y = PANEL_HEIGHT - Card_H;
					xOffset = x;
					yOffset = y - 10;
				}
				else if(i == 1) {
					x = PANEL_WIDTH - Card_W;
					y = yStart+offset*j;
					xOffset = x - 10;
					yOffset = y;
				}
				else if(i == 2) {
					x = 0;
					y = yStart+offset*j;
					xOffset = x + 10;
					yOffset = y;
				}
				else {
					x = xStart+offset*j;
					y = 0;
					xOffset = x;
					yOffset = y + 10;
				}	
				
				if(i == 0 || i == 3) {
					if(j == cards_size-1) {
						p1 = new Point(x,y);
						p2 = new Point(x+Card_W,y);
						p3 = new Point(x+Card_W,y+Card_H);
						p4 = new Point(x,y+Card_H);
					}
					else {
						p1 = new Point(x,y);
						p2 = new Point(x+offset,y);
						p3 = new Point(x+offset,y+Card_H);
						p4 = new Point(x,y+Card_H);
					}
				}
				else {
					if(j == cards_size-1) {
						p1 = new Point(x,y);
						p2 = new Point(x+Card_W,y);
						p3 = new Point(x+Card_W,y+Card_H);
						p4 = new Point(x,y+Card_H);
					}
					else {
						p1 = new Point(x,y);
						p2 = new Point(x+Card_W,y);
						p3 = new Point(x+Card_W,y+offset);
						p4 = new Point(x,y+offset);
					}
				}
				if(isInRect(p1,p2,p3,p4,new Point(curX,curY))) {
					currentCursorPointedCard = card;
					currentCursorPointedCardPlayer = players.get(i);
					if(i == next) cursorPointedCard = true;
					g2D.drawImage(cardImg,xOffset,yOffset, null);	
				}						
				else {
					g2D.drawImage(cardImg,x,y, null);	
				}	
			}
		}
		if(!cursorPointedCard) {
			currentCursorPointedCard = null;
			currentCursorPointedCardPlayer = null;
		}
			
	}
	
	public void gameLoop(Graphics g) {
		g2D = (Graphics2D) g;
		g2D.drawImage(backgroundImage, 0, 0, null);
		String nxts = "";
		int nxtTextX = -1,nxtTextY = -1;
		if(next == 1) { 
			nxts = "P2";	
			nxtTextX = 1000;
			nxtTextY = 450;
		}
		else if(next == 2) {
			nxts = "P3";
			nxtTextX = 150;
			nxtTextY = 450;
		}
		else if(next == 3) {
			nxts = "P4";
			nxtTextX = 550;		
			nxtTextY = 200;
		}
		else if(next == 0) {
			nxts = "Player";
			nxtTextX = 530;
			nxtTextY = 600;
		}
		
		if(next != -1)
			if(turn == 0) {
				g2D.setColor(Color.ORANGE);
				g2D.setFont(new Font("Microsoft JhengHei", Font.BOLD + Font.ITALIC, 30));
				if(!showing_draw_card)
					g2D.drawString("輪到你了，請自"+nxts+"抽一張牌", 400,550);
				else {
					g2D.drawString("你自"+nxts+"抽到"+player_draw_card_str, 450,550);
				}
			}
			else if(turn == 1) {
				g2D.setColor(Color.BLACK);
				g2D.setFont(new Font("Microsoft JhengHei", Font.BOLD + Font.ITALIC, 30));
				if(!drawing_player_card) {
					g2D.drawString("P2正在從"+nxts+"抽一張牌", 450,550);
				}
				else {
					g2D.drawString("P2自您這抽到"+drawing_player_card_str, 450,550);
				}
				
			}
			else if(turn == 2) {
				g2D.setColor(Color.BLACK);
				g2D.setFont(new Font("Microsoft JhengHei", Font.BOLD + Font.ITALIC, 30));
				if(!drawing_player_card) {
					g2D.drawString("P3正在從"+nxts+"抽一張牌", 450,550);
				}
				else {
					g2D.drawString("P3自您這抽到"+drawing_player_card_str, 450,550);
				}
			}
			else if(turn == 3) {
				g2D.setColor(Color.BLACK);
				g2D.setFont(new Font("Microsoft JhengHei", Font.BOLD + Font.ITALIC, 30));
				if(!drawing_player_card) {
					g2D.drawString("P4正在從"+nxts+"抽一張牌", 450,550);
				}
				else {
					g2D.drawString("P4自您這抽到"+drawing_player_card_str, 450,550);
				}
			}

		
		g2D.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 40));		
		if(turn == 0) {
			g2D.setColor(Color.YELLOW);
			g2D.drawString("Player", 530,600);
			g2D.setColor(Color.BLACK);
			g2D.drawString("P2", 1000,450);
			g2D.drawString("P3", 150,450);
			g2D.drawString("P4", 550,200);	
			
			g2D.setColor(Color.ORANGE);
			g2D.drawString(nxts, nxtTextX,nxtTextY);
		}
		else if(turn == 1) {
			g2D.setColor(Color.YELLOW);
			g2D.drawString("P2", 1000,450);
			g2D.setColor(Color.BLACK);
			g2D.drawString("Player", 530,600);
			g2D.drawString("P3", 150,450);
			g2D.drawString("P4", 550,200);	
			
			g2D.setColor(Color.ORANGE);
			g2D.drawString(nxts, nxtTextX,nxtTextY);
		}
		else if(turn == 2) {
			g2D.setColor(Color.YELLOW);
			g2D.drawString("P3", 150,450);
			g2D.setColor(Color.BLACK);
			g2D.drawString("Player", 530,600);
			g2D.drawString("P2", 1000,450);
			g2D.drawString("P4", 550,200);		
			
			g2D.setColor(Color.ORANGE);
			g2D.drawString(nxts, nxtTextX,nxtTextY);
		}
		else if(turn == 3) {
			g2D.setColor(Color.YELLOW);
			g2D.drawString("P4", 550,200);
			g2D.setColor(Color.BLACK);
			g2D.drawString("Player", 530,600);
			g2D.drawString("P2", 1000,450);
			g2D.drawString("P3", 150,450);	
			
			g2D.setColor(Color.ORANGE);
			g2D.drawString(nxts, nxtTextX,nxtTextY);
		}
		
		if(win[0]) {
			g2D.setColor(Color.GREEN);
			g2D.drawString("PlayerWIN", 530,600);	
		}
		if(win[1]) {
			g2D.setColor(Color.GREEN);
			g2D.drawString("P2WIN", 1000,450);	
		}
		if(win[2]) {
			g2D.setColor(Color.GREEN);
			g2D.drawString("P3WIN", 150,450);	
		}
		if(win[3]) {
			g2D.setColor(Color.GREEN);
			g2D.drawString("P4WIN", 550,200);	
		}
		
		g2D.setColor(Color.RED);
		if(!win[0] && win[1] && win[2] && win[3]) {
			g2D.drawString("PlayerLOSE", 530,600);	
			g2D.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 100));
			g2D.drawString("GAMEOVER", 300,300);
		}
		else if(win[0] && !win[1] && win[2] && win[3]) {
			g2D.drawString("P2LOSE", 1000,450);
			g2D.setColor(Color.GREEN);
			g2D.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 100));
			g2D.drawString("YOUWIN", 400,300);
		}
		else if(win[0] && win[1] && !win[2] && win[3]) {
			g2D.drawString("P3LOSE", 150,450);
			g2D.setColor(Color.GREEN);
			g2D.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 100));
			g2D.drawString("YOUWIN", 400,300);
		}
		else if(win[0] && win[1] && win[2] && !win[3]) {
			g2D.drawString("P4LOSE", 5500,200);	
			g2D.setColor(Color.GREEN);
			g2D.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 100));
			g2D.drawString("YOUWIN", 400,300);
		}

		draw_cards_or_getCurCursorImage(g2D);
		next = getNext_turn(win,turn);
		if(turn != 0 && turn != -1 && next != -1)
			if((npcthread == null || (npcthread != null && !npcthread.isAlive())) && 		
			  (thread == null || !(thread != null && thread.isAlive())))
					NPC_draw_card();
		
	}
	
	public void paint(Graphics g) {	
		super.paint(g);
		gameLoop(g);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		repaint();
	}

	public void checkIfHavePairs(Player player) {
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
	
	public void sendCardsToPlayers(List<Player> players,List<Card> cards) {
		for(Player player : players) {
			for(int i = 0;i < 13; ++i) {
				Card card = cards.remove(0);
				player.cards.add(card);
			}
		}
		int random_idx = (int) (Math.random()*players.size());//產生從[0,10)
		players.get(random_idx).cards.add(new Card("🃏",-1));
	}
	
	public void initialiseCard(List<Card> cards) {
		for(String s : new String[] {"♣","♠","♥","♦"}) {
			for(int i = 1; i <= 13; ++i) {
				Card c = new Card(s,i);
				cards.add(c);
			}
		}
	}
	
	public void washCards(List<Card> cards) {
		Random random = new Random();
		for(int i = 0; i < cards.size(); ++i) {
			int toExchange = random.nextInt(cards.size());
			Card tempCard = cards.get(i);
			cards.set(i, cards.get(toExchange));
			cards.set(toExchange, tempCard);
		}
	}
	
	public int getNext_turn(boolean[] win,int current){
		int n = win.length;
		for(int i = current + 1; i < current + n; ++i) {
			int t = i % n;
			if(!win[t]) return t;
		}
		return -1;
	}
	
	public void NPC_draw_card(){
		if((npcthread == null || (npcthread != null && !npcthread.isAlive())) && 		
		(thread == null || !(thread != null && thread.isAlive()))
		) {
			npcthread = new Thread(() -> {
			//next = getNext_turn(win,turn);
			drawing_player_card = false;
			drawing_player_card_str = "";
			
			System.out.println("輪到"+ turn+"下個是"+next);
			checkIfHavePairs(players.get(turn));
			System.out.printf("玩家[%s]抽了 玩家[%s]的:",
					players.get(turn).name,
					players.get(next).name);
			int random_pick = (int) (Math.random()*players.get(next).cards.size());
			System.out.println(players.get(next).cards.get(random_pick));
			
			try {
				   long start = System.currentTimeMillis(),end = start + 2000;
				   while(System.currentTimeMillis() < end) {
				     // do something
				     // pause to avoid churning
					   Card img = players.get(next).cards.get(random_pick);
					   if(next == 0) {
						   drawing_player_card = true;
						   if(img.suit=="♠") drawing_player_card_str = "黑桃"+img.rank;
						   else if(img.suit == "♥") drawing_player_card_str = "紅心"+img.rank;
						   else if(img.suit == "♦") drawing_player_card_str = "方塊"+img.rank;
						   else if(img.suit == "♣") drawing_player_card_str = "梅花"+img.rank;
						   else if(img.suit == "🃏") drawing_player_card_str = "鬼牌";
						   g2D.drawImage(players.get(next).cards.get(random_pick).cardUp,
						   550,300,null);
					   }
					   else {
						   g2D.drawImage(players.get(next).cards.get(random_pick).cardBack,
						   550,300,null);
					   }
				   }
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			
			
			players.get(turn).cards.add(players.get(next).cards.get(random_pick));
			players.get(next).cards.remove(players.get(next).cards.get(random_pick));
			checkIfHavePairs(players.get(turn));
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
			drawing_player_card = false;
			drawing_player_card_str = "";
			});
		}
		npcthread.start();
		
		
	}
	
	public void Player_draw_card() {
		//System.out.println("turn = "+ turn + "next= " + next);
		//System.out.println((currentCursorPointedCard != null && 		
		//		(thread == null || (thread != null && !thread.isAlive())) && 		
		//		(npcthread == null || !(npcthread != null && npcthread.isAlive()))
		//		));
		//System.out.println("currentCursorPointedCard= " + (currentCursorPointedCard!= null));
		//System.out.println("(thread == null || (thread != null && !thread.isAlive())) ="+(thread == null || (thread != null && !thread.isAlive())));
		//System.out.println("(npcthread == null || !(npcthread != null && npcthread.isAlive())) ="+(npcthread == null || !(npcthread != null && npcthread.isAlive())));
		if(currentCursorPointedCard != null && 		
		(thread == null || (thread != null && !thread.isAlive())) && 		
		(npcthread == null || !(npcthread != null && npcthread.isAlive()))
		) {
			thread = new Thread(() -> {	
				//next = getNext_turn(win,turn);
				showing_draw_card = false;
				player_draw_card_str = "";
				System.out.println("輪到"+ turn+"下個是"+next);
				Card img = currentCursorPointedCard;
				Player tPlayer = currentCursorPointedCardPlayer;
				System.out.printf("你抽到玩家[%s]的" + img,tPlayer.name);
				System.out.println();
				
				try {
				   long start = System.currentTimeMillis(),end = start + 2000;
				   while(System.currentTimeMillis() < end) {
				     // do something
				     // pause to avoid churning
					   showing_draw_card = true;
					   if(img.suit=="♠") player_draw_card_str = "黑桃"+img.rank;
					   else if(img.suit == "♥") player_draw_card_str = "紅心"+img.rank;
					   else if(img.suit == "♦") player_draw_card_str = "方塊"+img.rank;
					   else if(img.suit == "♣") player_draw_card_str = "梅花"+img.rank;
					   else if(img.suit == "🃏") player_draw_card_str = "鬼牌";
					   
					   g2D.drawImage(img.cardUp,550,300,null);
				   }
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				playerA.cards.add(img);
				tPlayer.cards.remove(img);
				checkIfHavePairs(playerA);
				

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
				showing_draw_card = false;
				player_draw_card_str = "";
				
			});
			thread.start();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(turn == 0 && next != -1) Player_draw_card();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		curX = e.getX();
		curY = e.getY();
		// TODO Auto-generated method stub
		//System.out.println(String.valueOf(e.getX()) + " " + String.valueOf(e.getY()));
		//frame.setTitle(String.valueOf(e.getX()) + " " + String.valueOf(e.getY()));
	}

	
}
