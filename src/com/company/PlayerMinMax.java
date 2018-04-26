package com.company;

import javafx.util.Pair;

public class PlayerMinMax extends Player {

    int depth;

    public PlayerMinMax(char symbol, int depth) {
        super(symbol);
        this.depth = depth;
    }

    @Override
    public boolean move(Square[] availableMoves, Game gameState) {
        Square square = minMax(availableMoves, gameState, this, depth).getKey();
        if (gameState.makeMoveIfValid(square, this)) {
            gameState.updateScoreForMove(square, this);
            return true;
        }
        return false;
    }

    private Pair<Square, Integer> minMax(Square[] availableMoves, Game gameState, Player player, int depth) {
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
            for (i = 0; i < availableMoves.length; i++){
                if (!availableMoves[i].isMarked()) {
                    scoreToRestore = player.getScore();
                    availableMoves[i].markSquare(player);
                    gameState.updateScoreForMove(availableMoves[i], player);
                    int currentValue = minMax(availableMoves, gameState, gameState.getOpponent(player), depth - 1).getValue();
                    if (player == this && currentValue > bestWorstValue || player != this && currentValue < bestWorstValue) {
                        bestWorstValue = currentValue;
                        bestMove = availableMoves[i];
                    }
                    availableMoves[i].freeSquare();
                    player.setScore(scoreToRestore);
                }
            }
        }
        return new Pair<>(bestMove, bestWorstValue);
    }

}
