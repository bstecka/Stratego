package com.company;

public class Square {

    private Player player;
    private int row, column;

    public Square(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Square(Player player) { this.player = player; }

    void markSquare(Player player) { this.player = player; }

    void freeSquare() { this.player = null; }

    public Player getPlayer() { return player; }

    public boolean isMarked() { return player != null; }

    int getColumn() { return column; }

    int getRow() { return row; }

    public String toString() {
        if (player == null)
            return " ";
        else
            return player.toString();
    }
}
