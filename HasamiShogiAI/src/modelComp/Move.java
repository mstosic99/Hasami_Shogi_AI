package modelComp;

public class Move {
	
	private int fromRow;
	private int fromColumn;
	private int toRow; 
	private int toColumn;
	private int heuristicValue;
	
	public Move(int fromRow, int fromColumn, int toRow, int toColumn) {
		this.fromRow = fromRow;
		this.fromColumn = fromColumn;
		this.toRow = toRow;
		this.toColumn = toColumn;
	}
	
	public Move(int fromRow, int fromColumn, int toRow, int toColumn, int HeuristicValue) {
		this.fromRow = fromRow;
		this.fromColumn = fromColumn;
		this.toRow = toRow;
		this.toColumn = toColumn;
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
	
}