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
//		double max = 0;
//		Move maxMove = null;
//		for(Move move2 : board.getAllMoves()) {
//			max = -INFINITY;
//			if(move.heuristicValue > max) {
//				max = move2.heuristicValue;
//				maxMove = move2;
//			}
//		}
		return move;
	}

	public Move abMinimax(Board board, int playerColor, int currentDepth, double alpha, double beta, boolean maximize) {
		if (board.isGameFinished() || currentDepth == MAX_DEPTH) {
			return new Move(null, heuristics.evaluate(playerColor, currentDepth));
		}
		if(maximize) {
			Move bestMove = null;
			double bestScore = -INFINITY;
			for(Move move : board.getAllMoves()) {
				board.moveHuman(move);
				bestMove = abMinimax(board, playerColor, currentDepth + 1, alpha, beta, false);
				bestScore = Math.max(bestScore, bestMove.heuristicValue);
				alpha = Math.max(alpha, bestScore);
				
				if(alpha >= beta) {
					board.unmoveHuman(move);
					return new Move(bestMove, bestScore);
				}
				
				board.unmoveHuman(move);
			}
			return new Move(bestMove, bestScore);
		}
		else {
			Move bestMove = null;
			double bestScore = INFINITY;
			for(Move move : board.getAllMoves()) {
				board.moveHuman(move);
				bestMove = abMinimax(board, playerColor, currentDepth + 1, alpha, beta, false);
				bestScore = Math.min(bestScore, bestMove.heuristicValue);
				beta = Math.min(beta, bestScore);
				
				if(alpha >= beta) {
					board.unmoveHuman(move);
					return new Move(bestMove, bestScore);
				}
				
				board.unmoveHuman(move);
			}
			return new Move(bestMove, bestScore);
		}
		
		
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


