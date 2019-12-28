package gui;


public class HasamiShogiGUI {
    
    public BoardFrame boardframe;
    
    public static void main(String[] args) {
        HasamiShogiGUI gui = new HasamiShogiGUI();
        gui.boardframe = new BoardFrame();
        gui.boardframe.setVisible(true);
        //rty
        
    }
}
