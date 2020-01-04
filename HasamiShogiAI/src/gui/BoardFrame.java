package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JLabel;

import modelComp.Board;

public class BoardFrame extends JFrame {
	
    Component component;
    
    public BoardFrame()
    {
    	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Hasami Shogi");
        this.setResizable(false);
        component = new Board();
        this.add(component, BorderLayout.CENTER);
//      this.add(new JLabel("Hello"), BorderLayout.NORTH);
        
        this.setLocation(400, 100);
        
        this.pack();
        this.setVisible(true);
    }
}
