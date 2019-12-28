package gui;

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

@SuppressWarnings("serial")
public class Board extends JComponent {

	public int turnCounter = 1;  
	private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

	private final int Square_Width = 65;
	public ArrayList<Piece> White_Pieces;
	public ArrayList<Piece> Black_Pieces;

	public ArrayList<DrawingShape> Static_Shapes;
	public ArrayList<DrawingShape> Piece_Graphics;

	public Piece Active_Piece;

	private final int rows = 9;
	private final int cols = 9;
	private Integer[][] BoardGrid;
	private String board_file_path = "images" + File.separator + "board.png";
	private String active_square_file_path = "images" + File.separator + "active_square.png";

	public void initGrid() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				BoardGrid[i][j] = 0;
			}
		}

		// Image white_piece = loadImage("images/white_pieces/" + piece_name + ".png");
		// Image black_piece = loadImage("images/black_pieces/" + piece_name + ".png");

		for (int i = 0; i < rows; i++) {
			White_Pieces.add(new Piece(i, 0, true, "White.png", this));
			White_Pieces.add(new Piece(i, 1, true, "White.png", this));
		}

		for (int i = 0; i < rows; i++) {
			Black_Pieces.add(new Piece(i, 8, false, "Black.png", this));
			Black_Pieces.add(new Piece(i, 7, false, "Black.png", this));
		}

	}

	public Board() {

		BoardGrid = new Integer[rows][cols];
		Static_Shapes = new ArrayList<>();
		Piece_Graphics = new ArrayList<>();
		White_Pieces = new ArrayList<>();
		Black_Pieces = new ArrayList<>();

		initGrid();

		this.setBackground(new Color(37, 13, 64));
		this.setPreferredSize(new Dimension(584, 584));
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(1000, 1000));

		this.addMouseListener(mouseAdapter);
		this.addComponentListener(componentAdapter);
		this.addKeyListener(keyAdapter);

		this.setVisible(true);
		this.requestFocus();
		drawBoard();
	}

	private void drawBoard() {
		Piece_Graphics.clear();
		Static_Shapes.clear();
		// initGrid();

		Image board = loadImage(board_file_path);
		Static_Shapes.add(
				new DrawingImage(board, new Rectangle2D.Double(0, 0, board.getWidth(null), board.getHeight(null))));
		if (Active_Piece != null) {
			Image active_square = loadImage("images" + File.separator + "active_square.png");
			Static_Shapes.add(new DrawingImage(active_square, new Rectangle2D.Double(Square_Width * Active_Piece.getX(),
					Square_Width * Active_Piece.getY(), active_square.getWidth(null), active_square.getHeight(null))));
		}
		for (int i = 0; i < White_Pieces.size(); i++) {
			int COL = White_Pieces.get(i).getX();
			int ROW = White_Pieces.get(i).getY();
			Image piece = loadImage("images" + File.separator + "White.png");
			Piece_Graphics.add(new DrawingImage(piece, new Rectangle2D.Double(Square_Width * COL + 10,
					Square_Width * ROW + 10, piece.getWidth(null), piece.getHeight(null))));
		}
		for (int i = 0; i < Black_Pieces.size(); i++) {
			int COL = Black_Pieces.get(i).getX();
			int ROW = Black_Pieces.get(i).getY();
			Image piece = loadImage("images" + File.separator + "Black.png");
			Piece_Graphics.add(new DrawingImage(piece, new Rectangle2D.Double(Square_Width * COL + 10,
					Square_Width * ROW + 10, piece.getWidth(null), piece.getHeight(null))));
		}
		this.repaint();
	}

	public Piece getPiece(int x, int y) {
		for (Piece p : White_Pieces) {
			if (p.getX() == x && p.getY() == y) {
				return p;
			}
		}
		for (Piece p : Black_Pieces) {
			if (p.getX() == x && p.getY() == y) {
				return p;
			}
		}
		return null;
	}

	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();
			int Clicked_Row = d_Y / Square_Width;
			int Clicked_Column = d_X / Square_Width;
			boolean isWhitesTurn = true;
			if (turnCounter % 2 == 1) {
				isWhitesTurn = false;
			}

			Piece clicked_piece = getPiece(Clicked_Column, Clicked_Row);

			if (Active_Piece == null && clicked_piece != null
					&& ((isWhitesTurn && clicked_piece.isWhite()) || (!isWhitesTurn && clicked_piece.isBlack()))) {
				
				Active_Piece = clicked_piece;
				
			} else if (Active_Piece != null && Active_Piece.getX() == Clicked_Column
					&& Active_Piece.getY() == Clicked_Row) {
				
				Active_Piece = null;
				
			} else if (Active_Piece != null && Active_Piece.canMove(Clicked_Column, Clicked_Row)
					&& ((isWhitesTurn && Active_Piece.isWhite()) || (!isWhitesTurn && Active_Piece.isBlack()))) {
				
				// if piece is there, remove it so we can be there
				if (clicked_piece != null) {
					if (clicked_piece.isWhite()) {
						White_Pieces.remove(clicked_piece);
					} else {
						Black_Pieces.remove(clicked_piece);
					}
				}
				// do move
				Active_Piece.setX(Clicked_Column);
				Active_Piece.setY(Clicked_Row);

				// if piece is a pawn set has_moved to true
//                if (Active_Piece.getClass().equals(Pawn.class))
//                {
//                    Pawn castedPawn = (Pawn)(Active_Piece);
//                    castedPawn.setHasMoved(true);
//                }

				Active_Piece = null;
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

	private void adjustShapePositions(double dx, double dy) {

		Static_Shapes.get(0).adjustPosition(dx, dy);
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
		for (DrawingShape shape : Static_Shapes) {
			shape.draw(g2);
		}
		for (DrawingShape shape : Piece_Graphics) {
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
