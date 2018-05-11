package com.company;

import javafx.util.Pair;

public class PlayerMinMax extends Player {

    private int depth;
    private int type;

    public PlayerMinMax(char symbol, int depth) {
        super(symbol);
        this.depth = depth;
        this.type = 2;
    }

    @Override
    public boolean move(Square[] availableMoves, Game gameState) {
        Square square = minMax(availableMoves, gameState, this, depth).getKey();
        if (gameState.markSquareIfFree(square, this)) {
            gameState.setLastMarkedSquare(square);
            return true;
        }
        return false;
    }

    private int getBestWorstValue(Game gameState) {
        int score = 0;
        switch (type) {
            case 1:
                score = getScoreDifference(gameState);
                break;
            case 2:
                int factoredScoreDifference = gameState.getBoard().length * gameState.getBoard().length * 10 * getScoreDifference(gameState);
                int factoredMiddleDiff = prioritizeMiddleEvaluation(gameState, this);
                factoredMiddleDiff -= prioritizeMiddleEvaluation(gameState, gameState.getOpponent(this));
                score = factoredScoreDifference + factoredMiddleDiff;
                break;
        }
        return score;
    }

    private int getScoreDifference(Game gameState) {
        return this.getScore() - gameState.getOpponent(this).getScore();
    }

    private int prioritizeMiddleEvaluation(Game gameState, Player player) {
        int score = 0;
        Square[][] board = gameState.getBoard();
        int middle = board.length / 2;
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                if (board[i][j].getPlayer() == player) { //abs(środek - indeks) ma być najbliższy zeru
                    score += middle - Math.abs(middle - j);
                    score += middle - Math.abs(middle - i);
                }
            }
        }
        return score;
    }

    private Pair<Square, Integer> minMax(Square[] availableMoves, Game gameState, Player player, int depth) {
        int i, bestWorstValue;
        Square bestMove = null;
        for(i = 0; i < availableMoves.length && availableMoves[i].isMarked(); i++){}
        if (depth == 0 || i >= availableMoves.length) {
            bestWorstValue = getBestWorstValue(gameState);
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
