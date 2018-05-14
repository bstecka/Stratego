package com.company;

import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerAlphaBeta extends Player {

    private int depth, init_alpha, init_beta;
    private int type;
    public int calls;

    public PlayerAlphaBeta(char symbol, int depth) {
        super(symbol);
        this.depth = depth;
        init_alpha = Integer.MIN_VALUE;
        init_beta = Integer.MAX_VALUE;
        calls = 0;
        type = 1;
    }

    public PlayerAlphaBeta(char symbol, int depth, int type) {
        super(symbol);
        this.depth = depth;
        init_alpha = Integer.MIN_VALUE;
        init_beta = Integer.MAX_VALUE;
        calls = 0;
        this.type = type;
    }

    @Override
    public boolean move(Square[] availableMoves, Game gameState) {
        if (type == 1) {
            Square square = minMaxAlphaBeta(availableMoves, gameState, this, depth, init_alpha, init_beta).getKey();
            if (gameState.markSquareIfFree(square, this)) {
                gameState.setLastMarkedSquare(square);
                return true;
            }
            return false;
        } else {
            Square square = minMaxAlphaBetaH(availableMoves, gameState, this, depth, init_alpha, init_beta).getKey();
            if (gameState.markSquareIfFree(square, this)) {
                gameState.setLastMarkedSquare(square);
                return true;
            }
            return false;
        }
    }

    private boolean cutoff(Player player, int value, int alpha, int beta) {
        //return (player == this && value >= beta || player != this && value <= alpha);
        return player == this ? value >= beta : value <= alpha;
    }

    private int getScoreDifference(Game gameState) {
        return this.getScore() - gameState.getOpponent(this).getScore();
    }

    private Pair<Square, Integer> minMaxAlphaBeta(Square[] availableMoves, Game gameState, Player player, int depth, int alpha, int beta) {
        calls++;
        int i, bestWorstValue;
        Square bestMove = null;
        for(i = 0; i < availableMoves.length && availableMoves[i].isMarked(); i++){}
        if (depth == 0 || i >= availableMoves.length) {
            bestWorstValue = getScoreDifference(gameState);
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

    private Pair<Square, Integer> minMaxAlphaBetaH(Square[] availableMoves, Game gameState, Player player, int depth, int alpha, int beta) {
        calls++;
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

            ArrayList<Pair<Square, Integer>> sortedMoves = new ArrayList<>();
            for (i = 0; i < availableMoves.length; i++) {
                if (!availableMoves[i].isMarked()) {
                    int scoreForMoveThis = gameState.getScoreForMove(availableMoves[i], this);
                    availableMoves[i].freeSquare();
                    sortedMoves.add(new Pair<Square, Integer>(availableMoves[i], scoreForMoveThis));
                }
            }
            Comparator<Pair<Square, Integer>> MoveComparator = new Comparator<Pair<Square, Integer>>() {
                public int compare(Pair<Square, Integer> s1, Pair<Square, Integer> s2) {
                    return s1.getValue().compareTo(s2.getValue());
                }
            };
            if (player == this)
                sortedMoves.sort(MoveComparator);
            else
                sortedMoves.sort(MoveComparator.reversed());

            for (i = 0; i < sortedMoves.size() && !wasCut; i++){
                Square currentMove = sortedMoves.get(i).getKey();
                if (!cutoff(player, bestWorstValue, alpha, beta)){
                    if (!currentMove.isMarked()) {
                        scoreToRestore = player.getScore();
                        currentMove.markSquare(player);
                        gameState.updateScoreForMove(currentMove, player);
                        int currentValue = minMaxAlphaBetaH(availableMoves, gameState, gameState.getOpponent(player), depth - 1, alpha, beta).getValue();
                        if (player == this && currentValue > bestWorstValue || player != this && currentValue < bestWorstValue) {
                            bestWorstValue = currentValue;
                            bestMove = currentMove;
                        }
                        if (player == this && bestWorstValue > alpha)
                            alpha = bestWorstValue;
                        else if(player != this && bestWorstValue < beta)
                            beta = bestWorstValue;
                        currentMove.freeSquare();
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
