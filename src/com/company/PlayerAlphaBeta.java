package com.company;

import javafx.util.Pair;

public class PlayerAlphaBeta extends Player {

    private int depth, init_alpha, init_beta;

    public PlayerAlphaBeta(char symbol, int depth) {
        super(symbol);
        this.depth = depth;
        init_alpha = Integer.MIN_VALUE;
        init_beta = Integer.MAX_VALUE;
    }

    @Override
    public boolean move(Square[] availableMoves, Game gameState) {
        Square square = minMaxAlphaBeta(availableMoves, gameState, this, depth, init_alpha, init_beta).getKey();
        if (gameState.markSquareIfFree(square, this)) {
            gameState.setLastMarkedSquare(square);
            return true;
        }
        return false;
    }

    private boolean cutoff(Player player, int value, int alpha, int beta) {
        //return (player == this && value >= beta || player != this && value <= alpha);
        return player == this ? value >= beta : value <= alpha;
    }

    private Pair<Square, Integer> minMaxAlphaBeta(Square[] availableMoves, Game gameState, Player player, int depth, int alpha, int beta) {
        int i, bestWorstValue;
        Square bestMove = null;
        for(i = 0; i < availableMoves.length && availableMoves[i].isMarked(); i++){}
        if (depth == 0 || i >= availableMoves.length) {
            bestWorstValue = this.getScore() - gameState.getOpponent(this).getScore();
        }
        else {
            if (player == this)
                bestWorstValue = Integer.MIN_VALUE;
            else
                bestWorstValue = Integer.MAX_VALUE;
            bestMove = availableMoves[i];
            int scoreToRestore;
            boolean wasCut = false;
            for (i = 0; i < availableMoves.length && !wasCut; i++){
                if (!cutoff(player, bestWorstValue, alpha, beta)){
                    if (!availableMoves[i].isMarked()) {
                        scoreToRestore = player.getScore();
                        availableMoves[i].markSquare(player);
                        gameState.updateScoreForMove(availableMoves[i], player);
                        int currentValue = minMaxAlphaBeta(availableMoves, gameState, gameState.getOpponent(player), depth - 1, alpha, beta).getValue();
                        if (player == this && currentValue > bestWorstValue || player != this && currentValue < bestWorstValue) {
                            bestWorstValue = currentValue;
                            bestMove = availableMoves[i];
                        }
                        if(player == this && bestWorstValue > alpha)
                            alpha = bestWorstValue;
                        else if(player != this && bestWorstValue < beta)
                            beta = bestWorstValue;
                        availableMoves[i].freeSquare();
                        player.setScore(scoreToRestore);
                    }
                }
                else
                    wasCut = true;
            }
        }
        return new Pair<>(bestMove, bestWorstValue);
    }
}
