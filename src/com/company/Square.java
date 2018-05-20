package com.company;

public class Square {

    private Player player;
    private int row, column;

    public Square(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public void markSquare(Player player) { this.player = player; }

    void freeSquare() { this.player = null; }

    public Player getPlayer() { return player; }

    public boolean isMarked() { return player != null; }

    public int getColumn() { return column; }

    public int getRow() { return row; }

    public String toString() {
        if (player == null)
            return " ";
        else
            return player.toString();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Square))
            return false;
        Square square = (Square) o;
        return this.row == square.row && this.column == square.column;
    }
}
