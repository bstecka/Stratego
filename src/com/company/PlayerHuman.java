package com.company;

import java.util.Scanner;

public class PlayerHuman extends Player {

    public PlayerHuman(int number) {
        super(number);
    }

    @Override
    public boolean move(Square[] possibleMoves, Game gameState) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj rząd");
        int row = sc.nextInt();
        System.out.println("Podaj kolumnę");
        int column = sc.nextInt();
        Square square = new Square(row, column);
        boolean isMoveValid = gameState.makeMove(square, this);
        if (isMoveValid)
            gameState.updateScoreForMove(square, this);
        return false;
    }
}
