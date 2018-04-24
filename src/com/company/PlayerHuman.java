package com.company;

import java.util.Scanner;

public class PlayerHuman extends Player {

    public PlayerHuman(char symbol) { super(symbol); }

    @Override
    public boolean move(Square[] availableMoves, Game gameState) {
        boolean isMoveValid = false;
        while (!isMoveValid) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Podaj rząd");
            int row = sc.nextInt();
            System.out.println("Podaj kolumnę");
            int column = sc.nextInt();
            Square square = new Square(row, column);
            isMoveValid = gameState.makeMove(square, this);
            System.out.println("PlayerHuman " + isMoveValid);
            if (isMoveValid)
                gameState.updateScoreForMove(square, this);
        }
        return isMoveValid;
    }
}
