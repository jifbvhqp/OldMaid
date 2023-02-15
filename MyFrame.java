package poker_games;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MyFrame extends JFrame{
	MyPanel panel;
	MyFrame(){
		
		panel = new MyPanel(this);
		this.setTitle("Old Maid");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
