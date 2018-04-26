package com.company;

public class Main {

    public static void main(String[] args) {
        PlayerHuman player1 = new PlayerHuman('#');
        //PlayerHuman player2 = new PlayerHuman('x');
        PlayerMinMax player2 = new PlayerMinMax('x', 1);
        Game game = new Game(5, player1, player2);
        game.start();
    }
}
