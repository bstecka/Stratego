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
        if (gameState.makeMoveIfValid(square, this)) {
            gameState.updateScoreForMove(square, this);
            return true;
        }
        return false;
    }

    private Pair<Square, Integer> minMax(Square[] availableMoves, int depth, Player player, Game gameState) {
        int i, bestWorstValue;
        Square bestMove = null;
        for(i = 0; i < availableMoves.length && availableMoves[i].isMarked(); i++){}
        if (depth == 0 || i >= availableMoves.length) {
            bestWorstValue = this.getScore() - gameState.getOpponent(this).getScore();
            if (bestWorstValue == 2) {
                System.out.println(gameState);
                System.out.println(bestWorstValue);
            }
        }
        else {
            if (player == this)
                bestWorstValue = -10000;
            else
                bestWorstValue = 10000;
            bestMove = availableMoves[i];
            //bestWorstValue = gameState.getScoreForMove(bestMove, this);//here we do another marking of square bestMove
            ///two lines above questionable
            //bestMove.freeSquare();
            int scoreBeforeMove;
            for (i = 0; i < availableMoves.length; i++){
                if (!availableMoves[i].isMarked()) {
                    scoreBeforeMove = player.getScore();
                    availableMoves[i].markSquare(player);
                    gameState.updateScoreForMove(availableMoves[i], player);
                    int currentValue = minMax(availableMoves, depth - 1, gameState.getOpponent(player), gameState).getValue();
                    if (player == this && currentValue > bestWorstValue || player != this && currentValue < bestWorstValue) {
                        bestWorstValue = currentValue;
                        bestMove = availableMoves[i];
                    }
                    availableMoves[i].freeSquare();
                    player.setScore(scoreBeforeMove);
                    System.out.println("plScore " + player.getScore());
                }
            }
        }
        return new Pair<>(bestMove, bestWorstValue);
    }

}
