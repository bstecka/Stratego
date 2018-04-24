package com.company;

public abstract class Player implements PlayerI {

    int number, score;

    public Player(int number) {
        this.number = number;
        this.score = 0;
    }

    public String toString() {
        return "" + number;
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
