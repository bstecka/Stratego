package com.company;

public class PlayerMinMaxBad extends Player{

    public PlayerMinMaxBad(char symbol) {
        super(symbol);
    }

    @Override
    public boolean move(Square[] availableMoves, Game gameState) {
        boolean isMoveValid = false;
        while (!isMoveValid) {
            Square square = minMax(availableMoves, gameState);
            System.out.println("makeMoveIfValid");
            isMoveValid = gameState.makeMoveIfValid(square, this);
            System.out.println("PlayerRandom " + isMoveValid);
            if (isMoveValid)
                gameState.updateScoreForMove(square, this);
        }
        return isMoveValid;
    }

    private Square minMax(Square[] availableMoves, Game gameState) {
        System.out.println("minMax");
        int i;
        for(i = 0; i < availableMoves.length && availableMoves[i].isMarked(); i++){
            System.out.println(i + " " + availableMoves[i] + " " + availableMoves[i].isMarked());
        }
        Square bestMove = availableMoves[i];
        int bestValue = gameState.getScoreForMove(bestMove, this);

        for (i = 0; i < availableMoves.length; i++) {
            if (!availableMoves[i].isMarked()) {
                int moveScore = -1;
                gameState.makeMoveIfValid(availableMoves[i], this);
                moveScore = maxMove(gameState.availableMoves, gameState);
                System.out.println("moveScore: " + moveScore);
                if (moveScore > bestValue) {
                    bestMove = availableMoves[i];
                    bestValue = moveScore;
                }
                //System.out.println("bf: " + availableMoves[i]);
                availableMoves[i].freeSquare();
                //System.out.println("af: " + availableMoves[i]);
            }
        }
        //for (int j = 0; j < availableMoves.length; j++) {
        //    System.out.println(availableMoves[j]);
        //}
        return bestMove;
    }

    private int maxMove(Square[] availableMoves, Game gameState) {
        System.out.println("max");
        if (gameState.isBoardFull()) {
            System.out.println(this.getScore());
            return this.getScore();
        }
        else {
            int i;
            for(i = 0; i < availableMoves.length && availableMoves[i].isMarked(); i++){}
            Square bestMove = availableMoves[i];
            int bestValue = gameState.getScoreForMove(bestMove, this);

            for (i = 0; i < availableMoves.length; i++) {
                if (!availableMoves[i].isMarked()) {
                    int moveScore = -1;
                    gameState.makeMoveIfValid(availableMoves[i], this);
                    moveScore = minMove(gameState.availableMoves, gameState);
                    System.out.println("max - moveScore: " + moveScore + ", bestvalue: " + bestValue);
                    if (moveScore > bestValue) {
                        bestMove = availableMoves[i];
                        bestValue = moveScore;
                    }
                    System.out.println("max - moveScore: " + moveScore + ", bestvalue: " + bestValue);
                    availableMoves[i].freeSquare();
                }
            }
            return bestValue;
        }
    }

    private int minMove(Square[] availableMoves, Game gameState) {
        System.out.println("min");
        if (gameState.isBoardFull()) {
            System.out.println(this.getScore());
            return this.getScore();
        }
        else {
            int i;
            for(i = 0; i < availableMoves.length && availableMoves[i].isMarked(); i++){}
            Square bestMove = availableMoves[i];
            int bestValue = gameState.getScoreForMove(bestMove, this);

            for (i = 0; i < availableMoves.length; i++) {
                if (!availableMoves[i].isMarked()) {
                    int moveScore = -1;
                    gameState.makeMoveIfValid(availableMoves[i], this);
                    moveScore = maxMove(gameState.availableMoves, gameState);
                    System.out.println("min - moveScore: " + moveScore + ", bestvalue: " + bestValue);
                    if (moveScore < bestValue) {
                        bestMove = availableMoves[i];
                        bestValue = moveScore;
                    }
                    System.out.println("min - moveScore: " + moveScore + ", bestvalue: " + bestValue);
                    availableMoves[i].freeSquare();
                }
            }
            return bestValue;
        }
    }

    /*private Square MinMax(Square[] availableMoves, Game gameState) {
        Square sq = MaxMove(availableMoves, gameState);
        System.out.println(sq);
        return sq;
    }*/

    /*private Square MaxMove(Square[] availableMoves, Game gameState) {
        if (gameState.isBoardFull()) {
            System.out.println("full");
            return null;
        }
        else {
            int i;
            for(i = 0; i < availableMoves.length && availableMoves[i].isMarked(); i++){}
            Square bestMove = availableMoves[i];
            int bestValue = gameState.getScoreForMove(bestMove, this);
            for (i = 0; i < availableMoves.length; i++) {
                boolean moveValid = gameState.makeMoveIfValid(availableMoves[i], this);
                Square move = MinMove(gameState.availableMoves, gameState);
                int value = gameState.getScoreForMove(move, this);
                if (moveValid && value > bestValue) {
                    bestMove = availableMoves[i];
                    bestValue = value;
                }
                move.freeSquare();
            }
            return bestMove;
        }
    }

    private Square MinMove(Square[] availableMoves, Game gameState) {
        int i;
        for(i = 0; i < availableMoves.length && availableMoves[i].isMarked(); i++){}
        Square bestMove = availableMoves[i];
        int bestValue = gameState.getScoreForMove(bestMove, this);
        for (i = 0; i < availableMoves.length; i++) {
            boolean moveValid = gameState.makeMoveIfValid(availableMoves[i], this);
            Square move = MaxMove(gameState.availableMoves, gameState);
            int value = gameState.getScoreForMove(move, this);
            if (moveValid && value > bestValue) {
                bestMove = availableMoves[i];
                bestValue = value;
            }
            move.freeSquare();
        }
        return bestMove;
    }*/

}
