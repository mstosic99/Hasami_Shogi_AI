package ai_algorithm;

import heuristics.Heuristics;
import modelComp.Board;
import modelComp.Move;

public class AlphaBetaAlgorithm implements SearchAlgorithm {

	private Heuristics heuristics;
	private final int MAX_DEPTH;
	private final double INFINITY = 1000;
	private final int AI_COLOR;

	public AlphaBetaAlgorithm(int MAX_DEPTH, int AI_COLOR, Heuristics heuristics) {
		this.MAX_DEPTH = MAX_DEPTH;
		this.AI_COLOR = AI_COLOR;
		this.heuristics = heuristics;
	}
	
	@Override
	public Move aiMove(Board board) {
		Move move = abMinimax(board, AI_COLOR, 0, -INFINITY, INFINITY);
		return move;
	}

	private Move abMinimax(Board board, int playerColor, int currentDepth, double alpha, double beta) {
		if (board.isGameFinished() || currentDepth == MAX_DEPTH) {
			return new Move(null, heuristics.evaluate(playerColor, currentDepth));
		}
		
		Move bestMove = null;
		double bestScore = -INFINITY;
		for(Move move : board.getAllMoves()) {
			board.moveHuman(move);
			Move currentMove = abMinimax(board, playerColor, currentDepth + 1, -beta, -Math.max(alpha, bestScore));
			double currentScore = -currentMove.heuristicValue;
			
			if(currentScore > bestScore) {
				bestScore = currentScore;
				bestMove = move;
				if(bestScore >= beta) {
					board.unmoveHuman(move);
					return new Move(bestMove, bestScore);
				}
			}
			
			board.unmoveHuman(move);
		}
		
		return new Move(bestMove, bestScore);
	}

	
}
