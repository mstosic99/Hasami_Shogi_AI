package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import javax.swing.JFrame;
import javax.swing.JLabel;


import modelComp.Board;

public class BoardFrame extends JFrame {
	
    public static Board board;
    
    public BoardFrame()
    {
    	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Hasami Shogi");
        this.setResizable(false);
        board = new Board();
        this.add(board, BorderLayout.CENTER);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image icon = kit.getImage("images/hasamishogi.png");
        setIconImage(icon);
//      this.add(new JLabel("Hello"), BorderLayout.NORTH);
        
        this.setLocation(400, 100);
        
        this.pack();
        this.setVisible(true);
    }
    
    public Board getBoard() {
		return board;
	}
    
    public void setBoard(Board board) {
		this.board = board;
	}
}
