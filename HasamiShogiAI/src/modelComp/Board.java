package modelComp;

import drawing.DrawingImage;
import drawing.DrawingShape;
import gui.BoardFrame;
import heuristics.Heuristics;
import heuristics.TestHeuristics;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

import ai_algorithm.AlphaBetaAlgorithm;
import ai_algorithm.SearchAlgorithm;


@SuppressWarnings("serial")
public class Board extends JComponent implements Cloneable {
	
	Heuristics heuristics = new TestHeuristics(this);
	SearchAlgorithm algorithm = new AlphaBetaAlgorithm(6, Board.WHITE, heuristics);

	public int turnCounter = 1;
	boolean isWhitesTurn = turnCounter % 2 == 0;
	private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	public static final int BLACK = 0, WHITE = 1, EMPTY = -1;

	private final int SQUARE_WIDTH = 65;
	private ArrayList<Piece> whitePieces;
	private ArrayList<Piece> blackPieces;

	private ArrayList<DrawingShape> staticShapes;
	private ArrayList<DrawingShape> pieceGraphics;

	private Piece activePiece;

	private boolean isGameFinished;

	private final int rows = 9;
	private final int cols = 9;
	private Integer[][] boardGrid;
	private String board_file_path = "images" + File.separator + "board.png";
	private String active_square_file_path = "images" + File.separator + "active_square.png";
	private String moves_file_path = "images" + File.separator + "moves.png";

	private HashMap<Integer, ArrayList<Piece>> mapCaptures;
	private HashMap<Position, Piece> mapPieces;

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
			boardGrid[0][i] = 1;
//			whitePieces.add(new Piece(i, 1, true, "White.png", this));
		}

		for (int i = 0; i < rows; i++) {
			blackPieces.add(new Piece(i, 8, false, "Black.png", this));
			boardGrid[8][i] = 2;
//			blackPieces.add(new Piece(i, 7, false, "Black.png", this));
		}
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if(boardGrid[i][j] == 1)
					mapPieces.put(new Position(i,j), new Piece(j, i, WHITE, this));
				
			}
		}

	}

	public Board() {

		boardGrid = new Integer[rows][cols];
		staticShapes = new ArrayList<>();
		pieceGraphics = new ArrayList<>();
		whitePieces = new ArrayList<>();
		blackPieces = new ArrayList<>();
		mapCaptures = new HashMap<Integer, ArrayList<Piece>>();
		mapPieces = new HashMap<Position, Piece>();

		initGrid();

		this.setPreferredSize(new Dimension(584, 584));
		this.addMouseListener(mouseAdapter);

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
			for (Move move : getMoves(activePiece.getY(), activePiece.getX())) {
				Image moves = loadImage(moves_file_path);
				staticShapes.add(new DrawingImage(moves, new Rectangle2D.Double(SQUARE_WIDTH * move.getToColumn() + 22,
						SQUARE_WIDTH * move.getToRow() + 22, moves.getWidth(null) - 1, moves.getHeight(null) - 1)));
			}
		}
		for (int i = 0; i < whitePieces.size(); i++) {
			int COL = whitePieces.get(i).getX();
			int ROW = whitePieces.get(i).getY();
			Image piece = loadImage("images" + File.separator + "White.png");
			pieceGraphics.add(new DrawingImage(piece, new Rectangle2D.Double(SQUARE_WIDTH * COL + 15,
					SQUARE_WIDTH * ROW + 15, piece.getWidth(null), piece.getHeight(null))));
		}
		for (int i = 0; i < blackPieces.size(); i++) {
			int COL = blackPieces.get(i).getX();
			int ROW = blackPieces.get(i).getY();
			Image piece = loadImage("images" + File.separator + "Black.png");
			pieceGraphics.add(new DrawingImage(piece, new Rectangle2D.Double(SQUARE_WIDTH * COL + 15,
					SQUARE_WIDTH * ROW + 15, piece.getWidth(null), piece.getHeight(null))));
		}
		this.repaint();

		// proveri pobednika..
		if (isGameFinished()) {
			if (whitePieces.size() == 1) {
				JOptionPane.showMessageDialog(this, "Black player wins!");
				this.removeMouseListener(mouseAdapter);
			} else if (blackPieces.size() == 1) {
				JOptionPane.showMessageDialog(this, "White player wins!");
				this.removeMouseListener(mouseAdapter);
			}
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int code = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Game over!", dialogButton);
			if (code == JOptionPane.NO_OPTION) {
				Runtime.getRuntime().exit(0);
			} else if (code == JOptionPane.YES_OPTION) {
				initGrid();
				drawBoard();
				addMouseListener(mouseAdapter);
				turnCounter = 1;
			}
		}
//		for(int i = 0; i < 9; i++){
//			for(int j = 0; j < 9; j++) {
//				System.out.print(boardGrid[i][j] + " ");
//			}
//			System.out.println();
//		}
		isWhitesTurn = turnCounter % 2 == 0 ? true : false;

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

	public void setPiece(Piece piece, int x, int y) {
		
	}

	private MouseAdapter mouseAdapter = new MouseAdapter() {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(SwingUtilities.isRightMouseButton(e)) {
				
				if(isWhitesTurn) { 							// promeni kasnije
					ArrayList<Piece> whitePiecesTmp = new ArrayList<>();
					ArrayList<Piece> blackPiecesTmp = new ArrayList<>();
					
					for(Piece piece : getWhitePieces()) {
						try {
							whitePiecesTmp.add((Piece) piece.clone());
						} catch (CloneNotSupportedException e1) {
							e1.printStackTrace();
						}
					}
					
					for(Piece piece : getBlackPieces()) {
						try {
							blackPiecesTmp.add((Piece) piece.clone());
						} catch (CloneNotSupportedException e1) {
							e1.printStackTrace();
						}
					}
					
					
					Move move = algorithm.aiMove(BoardFrame.board);
					whitePieces.clear();
					whitePieces.addAll(whitePiecesTmp);
					blackPieces.clear();
					blackPieces.addAll(blackPiecesTmp);
					moveHuman(move);
					drawBoard();
					System.out.println(move);
//					Move move = algorithm.aiMove(BoardFrame.board);
//					moveHuman(move);
//					drawBoard();
				}
				
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();
			int clickedRow = d_Y / SQUARE_WIDTH;
			int clickedColumn = d_X / SQUARE_WIDTH;
			isWhitesTurn = true;
			if (turnCounter % 2 == 1) {
				isWhitesTurn = false;
			}

			Piece clickedPiece = getPiece(clickedColumn, clickedRow);

			if (activePiece == null && clickedPiece != null
					&& ((isWhitesTurn && clickedPiece.isWhite()) || (!isWhitesTurn && clickedPiece.isBlack()))) {

				activePiece = clickedPiece;

			} else if (activePiece != null && activePiece.getX() == clickedColumn && activePiece.getY() == clickedRow) {

				activePiece = null;

			} else if (activePiece != null && activePiece.canMove(clickedColumn, clickedRow)
					&& ((isWhitesTurn && activePiece.isWhite()) || (!isWhitesTurn && activePiece.isBlack()))) {

				Move move = new Move(activePiece.getY(), activePiece.getX(), clickedRow, clickedColumn);
				moveHuman(move);
				activePiece = null;
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
	
	public void updateGrid() {
		
		for(int i = 0; i < 9; i++) 
			for(int j = 0; j < 9; j++) 
				boardGrid[i][j] = EMPTY;
	
		for(Piece piece : whitePieces) 
			boardGrid[piece.getY()][piece.getX()] = WHITE;
		
		for(Piece piece : blackPieces) 
			boardGrid[piece.getY()][piece.getX()] = BLACK;
	}
	
	public void moveMachine(Move move) {
		boardGrid[move.getToRow()][move.getToColumn()] = boardGrid[move.getFromRow()][move.getFromColumn()];
		boardGrid[move.getFromRow()][move.getFromColumn()] = EMPTY;
	}

	public void moveHuman(Move move) {
		Piece piece = getPiece(move.getFromColumn(), move.getFromRow());
		moveHuman(piece, move.getToColumn(), move.getToRow());
		turnCounter++;
	}

	public void moveHuman(Piece pieceToMove, int toColumn, int toRow) {

		if (pieceToMove == null)
			return;
		pieceToMove.setX(toColumn);
		pieceToMove.setY(toRow);
		ArrayList<Piece> captures = checkNDoSandwich(pieceToMove);
		mapCaptures.put(turnCounter, captures);
	}

	public void unmoveHuman(Move move) {
		
		turnCounter--;
		isWhitesTurn = isWhitesTurn == true ? false : true;
		
		boolean bool = getPiece(move.getToColumn(), move.getToRow()) != null ? true : false;
		int color = isWhitesTurn() ? Board.WHITE : Board.BLACK;
		ArrayList<Piece> pieces = isWhitesTurn() ? getWhitePieces() : getBlackPieces();
		pieces.remove(getPiece(move.getToColumn(), move.getToRow()));
		if(bool)
			pieces.add(new Piece(move.getFromColumn(), move.getFromRow(), color, this));

		// undo captures
		verifyAndUndoCaptures();

//		drawBoard(); // TODO
	}

	private void verifyAndUndoCaptures() {
		ArrayList<Piece> captures = mapCaptures.get(turnCounter);
		if (captures == null)
			return;

		for (Piece piece : captures) {
			if (!isWhitesTurn)
				whitePieces.add(piece);
			else
				blackPieces.add(piece);
		}
		mapCaptures.put(turnCounter, null);
	}

	public ArrayList<Move> getMoves(int row, int column) {
		ArrayList<Move> toReturn = new ArrayList<>();
		int newRow, newColumn;

		// NORTH
		newRow = row - 1;
		while (newRow >= 0 && getPiece(column, newRow) == null) {
			Move move = new Move(row, column, newRow, column);
			toReturn.add(move);
			newRow--;
		}

		// SOUTH
		newRow = row + 1;
		while (newRow <= 9 && getPiece(column, newRow) == null) {
			Move move = new Move(row, column, newRow, column);
			toReturn.add(move);
			newRow++;
		}

		// WEST
		newColumn = column - 1;
		while (newColumn >= 0 && getPiece(newColumn, row) == null) {
			Move move = new Move(row, column, row, newColumn);
			toReturn.add(move);
			newColumn--;
		}

		// EAST

		newColumn = column + 1;
		while (newColumn <= 9 && getPiece(newColumn, row) == null) {
			Move move = new Move(row, column, row, newColumn);
			toReturn.add(move);
			newColumn++;
		}

		return toReturn;
	}

	public ArrayList<Move> getAllMoves() {
		ArrayList<Move> toReturn = new ArrayList<>();
		ArrayList<Piece> playerPieces = null;
		playerPieces = isWhitesTurn ? getWhitePieces() : getBlackPieces();

		for (Piece piece : playerPieces) {
			toReturn.addAll(getMoves(piece.getY(), piece.getX()));
		}
		return toReturn;
	}

	public int getColor(Piece piece) {
		if (piece == null)
			return EMPTY;
		return piece.isWhite() ? WHITE : BLACK;
	}

	public ArrayList<Piece> checkNDoSandwich(Piece activePiece) {

		ArrayList<Piece> captures = new ArrayList<>();
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
				if (p.isBlack())
					blackPieces.remove(p);
				else if (p.isWhite())
					whitePieces.remove(p);
				captures.add(p);
				i++;
			}
		}
		// SOUTH
		i = row + 1;
		j = column;

		while (i <= 8 && (getColor(getPiece(j, i)) == opponentColor)) {
			i++;
		}
		if (i <= 8 && (getColor(getPiece(j, i)) == myColor)) {
			i--;
			while (i > row) {
				Piece p = getPiece(j, i);
				if (p.isBlack())
					blackPieces.remove(p);
				else if (p.isWhite())
					whitePieces.remove(p);
				captures.add(p);
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
				if (p.isBlack())
					blackPieces.remove(p);
				else if (p.isWhite())
					whitePieces.remove(p);
				captures.add(p);
				j++;
			}
		}
		// EAST
		i = row;
		j = column + 1;

		while (j <= 8 && (getColor(getPiece(j, i)) == opponentColor)) {
			j++;
		}
		if (j <= 8 && (getColor(getPiece(j, i)) == myColor)) {
			j--;
			while (j > column) {
				Piece p = getPiece(j, i);
				if (p.isBlack())
					blackPieces.remove(p);
				else if (p.isWhite())
					whitePieces.remove(p);
				captures.add(p);
				j--;
			}
		}

		return captures;
	}

	public boolean isGameFinished() {
		if (whitePieces.size() < 2 || blackPieces.size() < 2) {
			isGameFinished = true;
			return true;
		}
		isGameFinished = false;
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

	public boolean isWhitesTurn() {
		return isWhitesTurn;
	}

	public void setWhitesTurn(boolean isWhitesTurn) {
		this.isWhitesTurn = isWhitesTurn;
	}

	public void setGameFinished(boolean isGameFinished) {
		this.isGameFinished = isGameFinished;
	}

	public ArrayList<Piece> getWhitePieces() {
		return whitePieces;
	}

	public ArrayList<Piece> getBlackPieces() {
		return blackPieces;
	}
	
	private static class Position {
		public final int row;
		public final int column;

		public Position(int row, int column) {
			this.row = row;
			this.column = column;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Position)) {
				return false;
			}
			Position pos = (Position) obj;
			return pos.hashCode() == this.hashCode();
		}

		@Override
		public int hashCode() {
			return 1000 * row + column;
		}
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
