package com.company;

public abstract class Player implements PlayerI {

    char symbol;
    int score;

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

    public void incrementScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }
}
