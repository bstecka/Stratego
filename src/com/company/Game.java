package com.company;

import java.util.Arrays;

import static java.lang.Math.max;

public class Game {

    private Square[][] board;
    private Player[] players;
    private int size;
    private Square lastMarkedSquare;
    private Player cur_player;
    Square[] availableMoves;

    public Game(int size, Player player1, Player player2) {
        this.size = size;
        this.players = new Player[2];
        this.players[0] = player1;
        this.players[1] = player2;
        cur_player = player1;
        initBoard();
    }

    //////////////FORGUI
    public void switchPlayer() {
        cur_player = getOpponent(cur_player);
    }

    public void setLastMarkedSquare(Square square) {
        lastMarkedSquare = square;
    }

    public Square getLastMarkedSquare() {
        return lastMarkedSquare;
    }

    public Player getCurrentPlayer() {
        return cur_player;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Square[] getAvailableMoves() {
        return availableMoves;
    }

    public boolean moveCurrentPlayer(Square square) {
        boolean isMoveValid = markSquareIfFree(square, cur_player);
        return isMoveValid;
    }

    //////////////FORCONSOLE
    public void start() {
        for (int currentPlayer = 0; !isBoardFull(); currentPlayer ^= 1) {
            boolean isMoveValid = players[currentPlayer].move(availableMoves, this);
            System.out.println(isMoveValid);
            System.out.println(this.toString());
        }
    }

    public Player getOpponent(Player player) {
        if (player.equals(players[0]))
            return players[1];
        if (player.equals(players[1]))
            return  players[0];
        return null;
    }

    public Square[][] getBoard() {
        return board;
    }

    private void initBoard() {
        Square[] moves = new Square[size * size];
        board = new Square[size][size];
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Square square = new Square(i, j);
                moves[i*size + j] = square;
                board[i][j] = square;
            }
        }
        this.availableMoves = moves;
    }

    //marks Square
    public int getScoreForMove(Square square, Player player) {
        boolean wasMarked = square.isMarked();

        square.markSquare(player);
        int i, depth, points = 0;
        int row = square.getRow(), column = square.getColumn();

        for(i = 0; i < size && board[row][i].isMarked(); i++){}
        if(i == size)
            points += size;

        for(i = 0; i < size && board[i][column].isMarked(); i++){}
        if(i == size)
            points += size;

        int pointsInDiagonal = 0;
        for(depth = row, i = 0; column - depth < 0; depth--, i++) {}
        for(; i < size && column - depth < size && board[i][column - depth].isMarked(); i++, depth--, pointsInDiagonal++) {}
        if((i == size || column - depth == size) && pointsInDiagonal > 1)
            points += pointsInDiagonal;

        pointsInDiagonal = 0;
        for(depth = row, i = 0; column + depth >= size; depth--, i++) {}
        for(; i < size && column + depth >= 0 && board[i][column + depth].isMarked(); i++, depth--, pointsInDiagonal++) {}
        if((i == size || column + depth == -1) && pointsInDiagonal > 1)
            points += pointsInDiagonal;

        //if (!wasMarked)
        //    square.freeSquare();
        return points;
    }

    public int getWinningScore() {
        return max(players[0].getScore(), players[1].getScore());
    }

    public int updateScoreForMove(Square square, Player player) {
        int score = getScoreForMove(square, player);
        player.incrementScore(score);
        return player.getScore();
    }

    public boolean markSquareIfFree(Square square, Player player) {
        boolean found = false;
        for (int i = 0; i < availableMoves.length && !found; i++) {
            if (availableMoves[i].equals(square) && !availableMoves[i].isMarked()) {
                found = true;
                availableMoves[i].markSquare(player);
            }
        }
        if (found)
            updateScoreForMove(square, player);
        return found;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < availableMoves.length; i++) {
            if (!availableMoves[i].isMarked())
                return false;
        }
        return true;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < board.length; i++){
            builder.append(Arrays.toString(board[i]));
            if (i < board.length-1)
                builder.append("\n");
        }
        builder.append("\n");
        String strplayers = "Gracz 1: " + players[0].getScore() + ", Gracz 2: " + players[1].getScore() + "\n";
        builder.append(strplayers);
        return builder.toString();
    }

}
