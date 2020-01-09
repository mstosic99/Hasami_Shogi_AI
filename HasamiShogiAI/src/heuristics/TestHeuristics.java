package heuristics;

import modelComp.Board;
import modelComp.Piece;

public class TestHeuristics implements Heuristics {

	Board board;

	public TestHeuristics(Board board) {
		this.board = board;
	}

	@Override
	public double evaluate(int playerColor, int depth) {
		double evaluation = 0;
		int multiplier = 1;

		if (!board.isWhitesTurn()) {
			multiplier = -1;
		}

		if (board.isGameFinished()) {
			return -100 + depth;
		}

		for (Piece p : board.getWhitePieces()) {
			double plus = evaluationBonus(Board.WHITE, p);
			evaluation++;
			evaluation += plus;
		}

		for (Piece p : board.getBlackPieces()) {
			double plus = -evaluationBonus(Board.BLACK, p); // positional bonus.
			evaluation--;
			evaluation += plus;
		}
		return multiplier * evaluation + (Math.random() * 0.1);
	}

	private double evaluationBonus(int color, Piece p) {
		double evaluation = 0;
		
		Position[] vhNeighbours = new Position[]{new Position(p.getY()+1, p.getX()),
				 new Position(p.getY()-1, p.getX()),
				 new Position(p.getY(), p.getX()+1),
				 new Position(p.getY(), p.getX()-1)};
		Position[] diagonalNeighbours = new Position[]{new Position(p.getY()+1, p.getX()+1),
				 new Position(p.getY()-1, p.getX()-1),
				 new Position(p.getY()-1, p.getX()+1),
				 new Position(p.getY()+1, p.getX()-1)};
		
		// Capturing more
		for(Position position : vhNeighbours) {
			int opponentColor = color == Board.WHITE ? Board.BLACK : Board.WHITE;
			if((opponentColor == Board.WHITE && board.getWhitePieces().contains(board.getPiece(position.column, position.row)))
			|| (opponentColor == Board.BLACK && board.getBlackPieces().contains(board.getPiece(position.column, position.row)))) {
				evaluation += 0.15;
			}
		}
		// Defending more
		for(Position position : diagonalNeighbours){
			if((color == Board.WHITE && board.getWhitePieces().contains(board.getPiece(position.column, position.row)))
			|| (color == Board.BLACK && board.getBlackPieces().contains(board.getPiece(position.column, position.row)))) {
				evaluation += 0.05;
			}
		}
		// Focusing corners
		if((p.getY()==0 && p.getX()==0) || 
		   (p.getY()==0 && p.getX()==8) ||
		   (p.getY()==8 && p.getX()==0) ||
		   (p.getY()==8 && p.getX()==8)){
			evaluation += 0.09;
		}
		// Advancing 
		if(color==Board.WHITE){
			evaluation += p.getY()/2d * 0.05;
		}else{
			evaluation += (8-p.getY())/2d * 0.05;
		}
		
		return evaluation;
	
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
	
	public double evaluateFast(int player, int depth){
		double eval=0;
		int fator=1;
		
		if(board.isWhitesTurn() == false)
			fator=-1;
		
		if(board.isGameFinished()){
				return -100+depth;
		}
		for(Piece p : board.getWhitePieces()){
			double plus = p.getY()/2d * 0.1; //positional bonus.
			eval++;
			eval += plus;
		}
		for(Piece p : board.getWhitePieces()){
			double plus = -(9-1-p.getY())/2d * 0.1;  //positional bonus.
			eval--;
			eval += plus;
		}
		
		return fator*eval + (Math.random()*0.1);
	}

}
