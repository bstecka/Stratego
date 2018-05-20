package com.company.GUI;

import com.company.Player;
import com.company.PlayerHuman;
import com.company.Square;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Tile extends Rectangle {
    private int color;
    private Board board;
    private Square square;

    public Tile(Board board, Square square, int color, int x, int y, int size) {
        this.board = board;
        this.square = square;
        this.color = color;
        setWidth(size);
        setHeight(size);
        relocate(x * size, y * size);
        setFill(getColor());
        setStroke(Color.valueOf("#3A5C6A"));
        setOnMousePressed(e -> {
            if (!square.isMarked() && board.getCurrrentPlayer() instanceof PlayerHuman) {
                switch (board.getCurrrentPlayer().getNumber()) {
                    case 1:
                        setFill(Color.valueOf("#F66467"));
                        break;
                    case 2:
                        setFill(Color.valueOf("#7EC79E"));
                        break;
                }
                board.makeMove(square);
                board.switchPlayer();
            }
        });
    }

    public void markTile(Player player) {
        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> fillColor(player.getNumber()))
        );
        timer.play();
    }

    private void fillColor(int number) {
        switch (number) {
            case 1:
                setFill(Color.valueOf("#F66467"));
                break;
            case 2:
                setFill(Color.valueOf("#7EC79E"));
                break;
        }
    }

    Color getColor() {
        switch (color) {
            case 0:
                return Color.valueOf("#F8DCB0");
            case 1:
                return Color.valueOf("#7EC79E");
            case 2:
                return Color.valueOf("#F66467");
            default: return Color.BLACK;
        }
    }
}
