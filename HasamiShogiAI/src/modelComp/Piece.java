package modelComp;

public class Piece implements Cloneable {
	private static final int BLACK = 0, WHITE = 1;
	private int x;
	private int y;
	private final boolean is_white;
	private final int color;
	private String file_path;
	public Board board;

	public Piece(int x, int y, boolean is_white, String file_path, Board board) {
		this.is_white = is_white;
		if (is_white)
			color = WHITE;
		else
			color = BLACK;
		this.x = x;
		this.y = y;
		this.file_path = file_path;
		this.board = board;
	}
	
	public Piece(int x, int y, int color, Board board) {
		this.color = color;
		is_white = color == Board.WHITE ? true : false;
		this.x = x;
		this.y = y;
		this.board = board;
	}

	public String getFilePath() {
		return file_path;
	}

	public void setFilePath(String path) {
		this.file_path = path;
	}
	
	public int getColor() {
		return color;
	}

	public boolean isWhite() {
		return is_white;
	}

	public boolean isBlack() {
		return !is_white;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean canMove(int destination_x, int destination_y) {

		Piece possiblePiece = board.getPiece(destination_x, destination_y);

		// Ako je odabrano polje zauzeto
		if (possiblePiece != null)
			return false;

		// Ogranicavanje poteza na gore-dole-levo-desno
		if (this.getX() != destination_x && this.getY() != destination_y)
			return false;
		// Odredjujemo u kom smeru se krecemo
		String direction = "";
		if (destination_y > this.getY())
			direction = "south";
		if (destination_y < this.getY())
			direction = "north";
		if (destination_x > this.getX())
			direction = "east";
		if (destination_x < this.getX())
			direction = "west";

		// Sprecavamo preskakanje figura
		if (direction.equals("south")) {
			int spacesToMove = Math.abs(destination_y - this.getY());
			for (int i = 1; i < spacesToMove; i++) {
				Piece piece = board.getPiece(this.getX(), this.getY() + i);
				if (piece != null)
					return false;
			}
		}

		if (direction.equals("north")) {
			int spacesToMove = Math.abs(destination_y - this.getY());
			for (int i = 1; i < spacesToMove; i++) {
				Piece piece = board.getPiece(this.getX(), this.getY() - i);
				if (piece != null)
					return false;
			}
		}

		if (direction.equals("east")) {
			int spacesToMove = Math.abs(destination_x - this.getX());
			for (int i = 1; i < spacesToMove; i++) {
				Piece piece = board.getPiece(this.getX() + i, this.getY());
				if (piece != null)
					return false;
			}
		}

		if (direction.equals("west")) {
			int spacesToMove = Math.abs(destination_x - this.getX());
			for (int i = 1; i < spacesToMove; i++) {
				Piece piece = board.getPiece(this.getX() - i, this.getY());
				if (piece != null)
					return false;
			}
		}

		return true;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
