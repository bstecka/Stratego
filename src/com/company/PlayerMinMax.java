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
        Square square = minMax(availableMoves, depth, this, gameState).getKey();
        System.out.println("Square " + square.getRow() + " " + square.getColumn());
        if (gameState.makeMoveIfValid(square, this)) {
            gameState.updateScoreForMove(square, this);
            return true;
        }
        return false;
    }

    private Pair<Square, Integer> minMax(Square[] availableMoves, int depth, Player player, Game gameState) {
        System.out.println("minMax");
        int i, bestValue;
        Square bestMove = null;
        for(i = 0; i < availableMoves.length && availableMoves[i].isMarked(); i++){
            //System.out.println(i + " " + availableMoves[i] + " " + availableMoves[i].isMarked());
        }
        if (depth == 0 || i >= availableMoves.length) {
            bestValue = player.getScore() - gameState.getOpponent(player).getScore();
        }
        else {
            //System.out.println("else");
            bestMove = availableMoves[i];
            bestValue = gameState.getScoreForMove(bestMove, this);
            int scoreBeforeMove;
            //System.out.println(bestMove.getRow() + " " + bestMove.getColumn() + " " + bestValue);
            for (i = 0; i < availableMoves.length; i++){
                if (!availableMoves[i].isMarked()) {
                    scoreBeforeMove = this.getScore();
                    availableMoves[i].markSquare(player);
                    gameState.updateScoreForMove(availableMoves[i], this);
                    int currentValue = minMax(availableMoves, depth - 1, gameState.getOpponent(player), gameState).getValue();
                    if (player == this && currentValue > bestValue || player != this && currentValue < bestValue) {
                        bestValue = currentValue;
                        bestMove = availableMoves[i];
                    }
                    availableMoves[i].freeSquare();
                    player.setScore(scoreBeforeMove);
                }
            }
        }
        return new Pair<>(bestMove, bestValue);
    }

}
