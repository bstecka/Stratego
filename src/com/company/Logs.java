package com.company;

public class Logs {

    public static void main(String[] args) {
        //makeGamesAvA(8, 5);
        makeGamesAvAH(7, 5);
    }

    public static void makeGamesMvA(int size, int depth) {
        for (int i = 3; i <= size; i++) {
            for (int j = 1; j <= depth; j++) {
                PlayerMinMax player1 = new PlayerMinMax('#', j);
                PlayerAlphaBeta player2 = new PlayerAlphaBeta('x', j);
                Game game = new Game(i, player1, player2);
                game.start();
                System.out.println("minmax vs alphabeta, size: " + i + ", depth: " + j + " " + j);
                //System.out.println(game.getWinningScore() + " " + game.getLosingScore());
                System.out.println("calls: " + player1.calls + " " + player2.calls);
                PlayerAlphaBeta player11 = new PlayerAlphaBeta('x', j);
                PlayerMinMax player21 = new PlayerMinMax('#', j);
                game = new Game(i, player11, player21);
                game.start();
                System.out.println("alphabeta vs minmax, size: " + i + ", depth: " + j + " " + j);
                //System.out.println(game.getWinningScore() + " " + game.getLosingScore());
                System.out.println("calls: " + player11.calls + " " + player21.calls);
                game.printWinner();
            }
        }
    }

    public static void makeGamesAvA(int size, int depth) {
        for (int i = 3; i <= size; i++) {
            for (int j = 1; j <= depth; j++) {
                PlayerAlphaBeta player1 = new PlayerAlphaBeta('#', j, 1);
                PlayerAlphaBeta player2 = new PlayerAlphaBeta('x', j, 1);
                Game game = new Game(i, player1, player2);
                game.start();
                System.out.println("alphabeta vs alphabeta, size: " + i + ", depth: " + j + " " + j);
                System.out.println(game.getWinningScore() + " " + game.getLosingScore());
                System.out.println("calls: " + player1.calls + " " + player2.calls);
            }
        }
    }

    public static void makeGamesAvAH(int size, int depth) {
        for (int i = 3; i <= size; i++) {
            for (int j = 1; j <= depth; j++) {
                PlayerAlphaBeta player1 = new PlayerAlphaBeta('#', j, 1);
                PlayerAlphaBeta player2 = new PlayerAlphaBeta('x', j, 2);
                Game game = new Game(i, player1, player2);
                game.start();
                System.out.println("alphabeta vs alphabetaH, size: " + i + ", depth: " + j + " " + j);
                //System.out.println(game.getWinningScore() + " " + game.getLosingScore());
                System.out.println("calls: "+ player1.calls + " " + player2.calls);
                PlayerAlphaBeta player11 = new PlayerAlphaBeta('x', j, 2);
                PlayerMinMax player21 = new PlayerMinMax('#', j, 1);
                game = new Game(i, player11, player21);
                game.start();
                System.out.println("alphabetaH vs alphabeta, size: " + i + ", depth: " + j + " " + j);
                //System.out.println(game.getWinningScore() + " " + game.getLosingScore());
                System.out.println("calls: " + player11.calls + " " + player21.calls);
                game.printWinner();
            }
        }
    }

    public static void makeGamesMvAAgainstMvM(int size, int depth) {
        for (int i = 3; i <= size; i++) {
            for (int j = 1; j <= depth; j++) {
                for (int k = 1; k <= depth; k++) {
                    if (j != k) {
                        PlayerAlphaBeta player1 = new PlayerAlphaBeta('x', j);
                        PlayerMinMax player2 = new PlayerMinMax('#', k);
                        Game game = new Game(i, player1, player2);
                        game.start();
                        System.out.println("alphabeta vs minmax, size: " + i + ", depth: " + j + " " + k);
                        System.out.println(game.getWinningScore() + " " + game.getLosingScore());
                        System.out.println("calls: " + player1.calls + " " + player2.calls);
                    }
                }
            }
        }
    }

    public static void makeGamesMvM(int size, int depth) {
        for (int i = 8; i <= size; i++) {
            for (int j = 1; j <= depth; j++) {
                for (int k = 1; k <= depth; k++) {
                    if (j != k) {
                        PlayerMinMax player1 = new PlayerMinMax('x', j);
                        PlayerMinMax player2 = new PlayerMinMax('#', k);
                        Game game = new Game(i, player1, player2);
                        game.start();
                        System.out.println("minmax vs minmax, size: " + i + ", depth: " + j + " " + k);
                        System.out.println(player1.getScore() + " " + player2.getScore());
                        System.out.println("calls: " + player1.calls + " " + player2.calls);
                    }
                }
            }
        }
    }

    public static void makeGamesMvMH(int size, int depth) {
        for (int i = 3; i <= size; i++) {
            for (int j = 1; j <= depth; j++) {
                for (int k = j; k <= depth; k++) {
                    if (j != k) {
                        PlayerMinMax player1 = new PlayerMinMax('x', j, 1);
                        PlayerMinMax player2 = new PlayerMinMax('#', k, 2);
                        Game game = new Game(i, player1, player2);
                        game.start();
                        System.out.println("minmax vs minmaxH, size: " + i + ", depth: " + j + " " + k);
                        System.out.println(player1.getScore() + " " + player2.getScore());
                        System.out.println("calls: " + player1.calls + " " + player2.calls);

                        player1 = new PlayerMinMax('#', j, 2);
                        player2 = new PlayerMinMax('x', k, 1);
                        game = new Game(i, player1, player2);
                        game.start();
                        System.out.println("minmaxH vs minmax, size: " + i + ", depth: " + j + " " + k);
                        System.out.println(player1.getScore() + " " + player2.getScore());
                        System.out.println("calls: " + player1.calls + " " + player2.calls);
                    }
                }
            }
        }
    }
}
