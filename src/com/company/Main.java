package com.company;

public class Main {

    public static void main(String[] args) {
        PlayerHuman player1 = new PlayerHuman('#');
        PlayerRandom player2 = new PlayerRandom('x');
        Game game = new Game(5, player1, player2);
        game.start();
    }
}
