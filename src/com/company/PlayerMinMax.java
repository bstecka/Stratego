package com.company;

import javafx.util.Pair;

public class PlayerMinMax extends Player {

    private int depth;
    private int type;
    public int calls;
    public long time;
    public int count;

    public PlayerMinMax(char symbol, int depth) {
        super(symbol);
        this.depth = depth;
        this.type = 1;
        this.calls = 0;
        this.count = 0;
        this.time = 0;
    }

    public PlayerMinMax(char symbol, int depth, int type) {
        super(symbol);
        this.depth = depth;
        this.type = type;
        this.calls = 0;
        this.count = 0;
        this.time = 0;
    }

    @Override
    public boolean move(Square[] availableMoves, Game gameState) {
        long start = System.nanoTime();
        Square square = minMax(availableMoves, gameState, this, depth).getKey();
        if (gameState.markSquareIfFree(square, this)) {
            gameState.setLastMarkedSquare(square);
            if (count < 10){
                long end = System.nanoTime();
                time += (end - start);
                count++;
            }
            return true;
        }
        return false;
    }

    public long getAverageTime(){
        return count > 0 ? time/count : time;
    }

    private int getBestWorstValue(Game gameState) {
        int score = 0;
        switch (type) {
            case 1:
                score = getScoreDifference(gameState);
                break;
            case 2:
                int factoredScoreDifference = 100 * getScoreDifference(gameState);
                int factoredMiddleDiff = prioritizeMiddleEvaluation(gameState, this);
                factoredMiddleDiff -= prioritizeMiddleEvaluation(gameState, gameState.getOpponent(this));
                score = factoredScoreDifference + factoredMiddleDiff;
                break;
            case 3:
                score = 100 * getScoreDifference(gameState) + countEmptySquares(gameState);
                break;
        }
        return score;
    }

    private int getScoreDifference(Game gameState) {
        return this.getScore() - gameState.getOpponent(this).getScore();
    }

    private int countEmptySquares(Game gameState) {
        Square[][] board = gameState.getBoard();
        int count = 0, count_marked = 0;
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                if (board[i][j].isMarked()) {
                    count += countEmptyNeighbours(board, i, j);
                    count_marked++;
                }
            }
        }
        return count_marked > 0 ? count/count_marked : count;
    }

    private int countEmptyNeighbours(Square[][] board, int i, int j) {
        int count = 0;
        for (int k = i - 1; k < i + 1; k++){
            for (int l = j - 1; l < j + 1; l++){
                if (k >= 0 && l>= 0 && k < board.length && l < board.length) {
                    if (!board[k][l].isMarked())
                        count++;
                }
            }
        }
        return count;
    }


    private int prioritizeMiddleEvaluation(Game gameState, Player player) {
        int score = 0, count = 0;
        Square[][] board = gameState.getBoard();
        int middle = board.length / 2;
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                if (board[i][j].getPlayer() == player) { //abs(środek - indeks) ma być najbliższy zeru
                    score += middle - Math.abs(middle - j);
                    score += middle - Math.abs(middle - i);
                    count++;
                }
            }
        }
        return score/(count+1) + 1;
    }

    private Pair<Square, Integer> minMax(Square[] availableMoves, Game gameState, Player player, int depth) {
        calls++;
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
