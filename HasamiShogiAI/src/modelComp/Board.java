package modelComp;

import drawing.DrawingImage;
import drawing.DrawingShape;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

import App.HasamiShogiMain;

@SuppressWarnings("serial")
public class Board extends JComponent {

	public int turnCounter = 1;  
	private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	private static final int BLACK = 0, WHITE = 1, EMPTY = -1;

	private final int SQUARE_WIDTH = 65;
	public ArrayList<Piece> whitePieces;
	public ArrayList<Piece> blackPieces;

	public ArrayList<DrawingShape> staticShapes;
	public ArrayList<DrawingShape> pieceGraphics;

	public Piece activePiece;

	private final int rows = 9;
	private final int cols = 9;
	private Integer[][] boardGrid;
	private String board_file_path = "images" + File.separator + "board.png";
	private String active_square_file_path = "images" + File.separator + "active_square.png";
	private String moves_file_path = "images" + File.separator + "moves.png";

	private void initGrid() {
		
		whitePieces.clear();
		blackPieces.clear();
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				boardGrid[i][j] = 0;
			}
		}

		for (int i = 0; i < rows; i++) {
			whitePieces.add(new Piece(i, 0, true, "White.png", this));
//			whitePieces.add(new Piece(i, 1, true, "White.png", this));
		}

		for (int i = 0; i < rows; i++) {
			blackPieces.add(new Piece(i, 8, false, "Black.png", this));
//			blackPieces.add(new Piece(i, 7, false, "Black.png", this));
		}

	}

	public Board() {

		boardGrid = new Integer[rows][cols];
		staticShapes = new ArrayList<>();
		pieceGraphics = new ArrayList<>();
		whitePieces = new ArrayList<>();
		blackPieces = new ArrayList<>();

		initGrid();

		this.setBackground(new Color(37, 13, 64));
		this.setPreferredSize(new Dimension(584, 584));
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(1000, 1000));

		this.addMouseListener(mouseAdapter);
		this.addComponentListener(componentAdapter);
		this.addKeyListener(keyAdapter);

		this.setVisible(true);
//		this.requestFocus(); 
		drawBoard();
	}

	private void drawBoard() {
		pieceGraphics.clear();
		staticShapes.clear();

		Image board = loadImage(board_file_path);
		staticShapes.add(
				new DrawingImage(board, new Rectangle2D.Double(0, 0, board.getWidth(null), board.getHeight(null))));
		if (activePiece != null) {
			Image active_square = loadImage(active_square_file_path);
			staticShapes.add(new DrawingImage(active_square, new Rectangle2D.Double(SQUARE_WIDTH * activePiece.getX(),
					SQUARE_WIDTH * activePiece.getY(), active_square.getWidth(null), active_square.getHeight(null))));
			for(Move move : getMoves(activePiece.getY(), activePiece.getX())) {
				Image moves = loadImage(moves_file_path);
				staticShapes.add(new DrawingImage(moves,new Rectangle2D.Double(SQUARE_WIDTH * move.getToColumn(), 
						SQUARE_WIDTH * move.getToRow(), moves.getWidth(null)-1, moves.getHeight(null)-1)));
			}
		}
		for (int i = 0; i < whitePieces.size(); i++) {
			int COL = whitePieces.get(i).getX();
			int ROW = whitePieces.get(i).getY();
			Image piece = loadImage("images" + File.separator + "White.png");
			pieceGraphics.add(new DrawingImage(piece, new Rectangle2D.Double(SQUARE_WIDTH * COL + 10,
					SQUARE_WIDTH * ROW + 10, piece.getWidth(null), piece.getHeight(null))));
		}
		for (int i = 0; i < blackPieces.size(); i++) {
			int COL = blackPieces.get(i).getX();
			int ROW = blackPieces.get(i).getY();
			Image piece = loadImage("images" + File.separator + "Black.png");
			pieceGraphics.add(new DrawingImage(piece, new Rectangle2D.Double(SQUARE_WIDTH * COL + 10,
					SQUARE_WIDTH * ROW + 10, piece.getWidth(null), piece.getHeight(null))));
		}
		this.repaint();
		
		//check winner...
		if(checkWinningCondition()) {
			if(whitePieces.size() == 1) {
				JOptionPane.showMessageDialog(this, "Black player wins!");
				this.removeMouseListener(mouseAdapter);
			} else if(blackPieces.size() == 1) {
				JOptionPane.showMessageDialog(this, "White player wins!");
				this.removeMouseListener(mouseAdapter);
			}
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int code = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Game over!", dialogButton);
			if(code == JOptionPane.NO_OPTION) {
				Runtime.getRuntime().exit(0);
			} else if(code == JOptionPane.YES_OPTION) {
				initGrid();
				drawBoard();
				addMouseListener(mouseAdapter);
				turnCounter = 1;
			}
		}
		
	}

	public Piece getPiece(int x, int y) {
		for (Piece p : whitePieces) {
			if (p.getX() == x && p.getY() == y) {
				return p;
			}
		}
		for (Piece p : blackPieces) {
			if (p.getX() == x && p.getY() == y) {
				return p;
			}
		}
		return null;
	}
	
	public void setPiece(int x, int y, int color) {
		
	}
	
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();
			int clickedRow = d_Y / SQUARE_WIDTH;
			int clickedColumn = d_X / SQUARE_WIDTH;
			boolean isWhitesTurn = true;
			if (turnCounter % 2 == 1) {
				isWhitesTurn = false;
			}
			
			Piece clickedPiece = getPiece(clickedColumn, clickedRow);

			if (activePiece == null && clickedPiece != null
					&& ((isWhitesTurn && clickedPiece.isWhite()) || (!isWhitesTurn && clickedPiece.isBlack()))) {
				
				activePiece = clickedPiece;
				
				
			} else if (activePiece != null && activePiece.getX() == clickedColumn
					&& activePiece.getY() == clickedRow) {
				
				activePiece = null;
				
			} else if (activePiece != null && activePiece.canMove(clickedColumn, clickedRow)
					&& ((isWhitesTurn && activePiece.isWhite()) || (!isWhitesTurn && activePiece.isBlack()))) {
				
				Move move = new Move(activePiece.getY(), activePiece.getX(), clickedRow, clickedColumn);
				move(move);
				activePiece = null;
				turnCounter++;
			}

			drawBoard();
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
		}

	};
	
	public void move(Move move) {
		Piece piece = getPiece(move.getFromColumn(), move.getFromRow());
		move(piece, move.getToColumn(), move.getToRow());
	}
	
	public void move(int fromRow, int fromColumn, int toRow, int toColumn) {
		
		Piece piece = getPiece(fromColumn, fromRow);
		move(piece, toColumn, toRow);
	}
	
	public void move(Piece pieceToMove, int toColumn, int toRow) {
		
		if(pieceToMove == null)
			return;
		pieceToMove.setX(toColumn);
		pieceToMove.setY(toRow);
		checkNDoSandwich(pieceToMove);
		
	}
	
	public ArrayList<Move> getMoves(int row, int column) {
		ArrayList<Move> toReturn = new ArrayList<>();
		int newRow, newColumn;
		
		// NORTH
		newRow = row - 1;
		while(newRow >= 0 && getPiece(column, newRow) == null) {
			Move move = new Move(row, column, newRow, column);
			toReturn.add(move);
			newRow--;
		}
		
		// SOUTH
		newRow = row + 1;
		while(newRow <= 9 && getPiece(column, newRow) == null) {
			Move move = new Move(row, column, newRow, column);
			toReturn.add(move);
			newRow++;
		}
		
		// WEST
		newColumn = column - 1;
		while(newColumn >= 0 && getPiece(newColumn, row) == null) {
			Move move = new Move(row, column, row, newColumn);
			toReturn.add(move);
			newColumn--;
		}
		
		// EAST
		
		newColumn = column + 1;
		while(newColumn <= 9 && getPiece(newColumn, row) == null) {
			Move move = new Move(row, column, row, newColumn);
			toReturn.add(move);
			newColumn++;
		}
		
		return toReturn;
	}

	public int getColor(Piece piece) {
		if(piece == null)
			return EMPTY;
		return piece.isWhite() ? WHITE : BLACK;
	}

	public void checkNDoSandwich(Piece activePiece) {
		
//		if(turnCounter < 3)				 // debug
//			return;
		int i, j, myColor, opponentColor, row, column;
		
		row = activePiece.getY();
		column = activePiece.getX();
		
		myColor = getColor(activePiece);
		opponentColor = myColor == BLACK ? WHITE : BLACK;
		
		// NORTH
		i = row - 1;
		j = column;
		
		while (i >= 0 && (getColor(getPiece(j, i)) == opponentColor)) {
			i--;
		}
		if (i >= 0 && (getColor(getPiece(j, i)) == myColor)) {
			i++;
			while (i < row) {					
				Piece p = getPiece(j, i);
				if(p.isBlack())
					blackPieces.remove(p);
				else if(p.isWhite())
					whitePieces.remove(p);
				i++;
			}
		}
		//SOUTH
		i = row + 1;
		j = column;
		
		while (i <= 8 && (getColor(getPiece(j, i)) == opponentColor)) {
			i++;
		}
		if (i <= 8 && (getColor(getPiece(j, i)) == myColor)) {
			i--;
			while (i > row) {
				Piece p = getPiece(j, i);
				if(p.isBlack())
					blackPieces.remove(p);
				else if(p.isWhite())
					whitePieces.remove(p);
				i--;
			}
		}
		
		// WEST
		i = row;
		j = column - 1;
		
		while (j >= 0 && (getColor(getPiece(j, i)) == opponentColor)) {
			j--;
		}
		if (j >= 0 && (getColor(getPiece(j, i)) == myColor)) {
			j++;
			while (j < column) {					
				Piece p = getPiece(j, i);
				if(p.isBlack())
					blackPieces.remove(p);
				else if(p.isWhite())
					whitePieces.remove(p);
				j++;
			}
		}
		//EAST
		i = row;
		j = column + 1;
		
		while (j <= 8 && (getColor(getPiece(j, i)) == opponentColor)) {
			j++;
		}
		if (j <= 8 && (getColor(getPiece(j, i)) == myColor)) {
			j--;
			while (j > column) {					
				Piece p = getPiece(j, i);
				if(p.isBlack())
					blackPieces.remove(p);
				else if(p.isWhite())
					whitePieces.remove(p);
				j--;
			}
		}
		
		return;
	}
	
	private boolean checkWinningCondition() {
		if(whitePieces.size() < 2 || blackPieces.size() < 2)
			return true;
		return false;
	}

	private void adjustShapePositions(double dx, double dy) {

		staticShapes.get(0).adjustPosition(dx, dy);
		this.repaint();

	}

	private Image loadImage(String imageFile) {
		try {
			return ImageIO.read(new File(imageFile));
		} catch (IOException e) {
			return NULL_IMAGE;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		drawBackground(g2);
		drawShapes(g2);
	}

	private void drawBackground(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
	}

	private void drawShapes(Graphics2D g2) {
		for (DrawingShape shape : staticShapes) {
			shape.draw(g2);
		}
		for (DrawingShape shape : pieceGraphics) {
			shape.draw(g2);
		}
	}

	private ComponentAdapter componentAdapter = new ComponentAdapter() {

		@Override
		public void componentHidden(ComponentEvent e) {

		}

		@Override
		public void componentMoved(ComponentEvent e) {

		}

		@Override
		public void componentResized(ComponentEvent e) {

		}

		@Override
		public void componentShown(ComponentEvent e) {

		}
	};

	private KeyAdapter keyAdapter = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}

		@Override
		public void keyTyped(KeyEvent e) {

		}
	};

}
