package com.company;

import java.util.Random;

public class PlayerRandom extends Player {

    public PlayerRandom(char symbol) {
        super(symbol);
    }

    @Override
    public boolean move(Square[] availableMoves, Game gameState) {
        boolean isMoveValid = false;
        while (!isMoveValid) {
            Random rand = new Random();
            int randomSqIndex = rand.nextInt(availableMoves.length);
            Square square = availableMoves[randomSqIndex];
            isMoveValid = gameState.markSquareIfFree(square, this);
            System.out.println("PlayerRandom " + isMoveValid);
            if (isMoveValid)
                gameState.updateScoreForMove(square, this);
        }
        return isMoveValid;
    }
}
