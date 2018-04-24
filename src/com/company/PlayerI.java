package com.company;

public interface PlayerI {

    public boolean move(Square[] availableMoves, Game gameState);

    public String toString();

    public void setScore(int score);

    public void incrementScore(int score);

    public int getScore();
}
