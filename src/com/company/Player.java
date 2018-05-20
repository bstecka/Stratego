package com.company;

public abstract class Player implements PlayerI {

    private char symbol;
    private int score;

    public Player(char symbol) {
        this.symbol = symbol;
        this.score = 0;
    }

    public String toString() {
        return "" + symbol;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore(int score) { this.score += score; }

    public int getScore() {
        return score;
    }

    public int getNumber() {
        return symbol == '1' ? 1 : 2;
    }
}
