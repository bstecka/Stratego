package com.company;

public class Main {

    public static void main(String[] args) {
        PlayerHuman player1 = new PlayerHuman('#');
        //PlayerHuman player2 = new PlayerHuman('x');
        PlayerAlphaBeta player2 = new PlayerAlphaBeta('x', 3);
        Game game = new Game(4, player1, player2);
        game.start();
    }
}
