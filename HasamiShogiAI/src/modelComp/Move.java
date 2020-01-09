package modelComp;

public class Move {
	
	private int fromRow;
	private int fromColumn;
	private int toRow; 
	private int toColumn;
	public double heuristicValue;
	
	public Move(int fromRow, int fromColumn, int toRow, int toColumn) {
		this.fromRow = fromRow;
		this.fromColumn = fromColumn;
		this.toRow = toRow;
		this.toColumn = toColumn;
	}
	
	public Move(int fromRow, int fromColumn, int toRow, int toColumn, double heuristicValue) {
		this.fromRow = fromRow;
		this.fromColumn = fromColumn;
		this.toRow = toRow;
		this.toColumn = toColumn;
		this.heuristicValue = heuristicValue;
	}
	
	public Move(Move move, double heuristicValue) {
		if(move != null) {
			move.heuristicValue = heuristicValue;
			this.fromRow = move.fromRow;
			this.fromColumn = move.fromColumn;
			this.toRow = move.toRow;
			this.toColumn = move.toColumn;
		}
		
		this.heuristicValue = heuristicValue;
	}
	
	public int getFromRow() {
		return fromRow;
	}
	
	public int getFromColumn() {
		return fromColumn;
	}
	
	public int getToColumn() {
		return toColumn;
	}
	
	public int getToRow() {
		return toRow;
	}
	
	@Override
	public String toString() {
		return "" + fromRow + fromColumn + toRow + toColumn;
	}
	
}