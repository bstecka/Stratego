package com.company;

public interface PlayerI {

    boolean move(Square[] availableMoves, Game gameState);

    String toString();

    void setScore(int score);

    void incrementScore(int score);

    int getScore();
}
