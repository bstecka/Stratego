package com.company.GUI;

import com.company.*;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Board extends Application {
    public int tileSize = 80;
    public int boardSize = 5;
    public double maxWidth;
    public double maxHeight;
    private Group tileGroup = new Group();
    private Game game;
    private Square[][] board;
    Text text;
    Text text2;
    Circle nextPlayerCircle;

    private Parent createContent() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        maxHeight = primaryScreenBounds.getHeight();
        maxWidth = primaryScreenBounds.getWidth();
        while (boardSize * tileSize > maxHeight - 100 && tileSize > 1)
            tileSize--;
        Pane root = new Pane();
        root.setPrefSize(boardSize * tileSize, boardSize * tileSize + 30);
        for (int y = 0; y < boardSize; y++){
            for (int x = 0; x < boardSize; x++){ //x+y) % 3 == 0 ? 0 : ((x+y) % 3 == 1 ? 1 : 2)
                Tile tile = new Tile(this, board[y][x], 0, x, y, tileSize);
                initializeGame();
                tileGroup.getChildren().add(tile);
            }
        }
        TextFlow flow = new TextFlow();
        text = new Text(getPlayerScores(0));
        text2 = new Text(getPlayerScores(1));
        applyTextFormatting(text, Color.valueOf("#F66467"));
        applyTextFormatting(text2, Color.valueOf("#7EC79E"));
        flow.setTextAlignment(TextAlignment.CENTER);
        flow.getChildren().addAll(text, text2);
        flow.setMinWidth(boardSize * tileSize);
        HBox hb = new HBox();
        hb.relocate(0, boardSize * tileSize + 7);
        hb.getChildren().add(flow);
        hb.setSpacing(10);
        nextPlayerCircle = new Circle();
        nextPlayerCircle.setFill(Color.valueOf("#F66467"));
        nextPlayerCircle.setCenterX(28.0f);
        nextPlayerCircle.setCenterY(boardSize * tileSize + 31);
        nextPlayerCircle.setRadius(15.0f);
        root.getChildren().addAll(tileGroup, hb, nextPlayerCircle);
        return root;
    }

    private void updatePlayerCircle() {
        if (game.getCurrentPlayer().getNumber() == 1)
            nextPlayerCircle.setFill(Color.valueOf("#F66467"));
        else
            nextPlayerCircle.setFill(Color.valueOf("#7EC79E"));
    }

    private void applyTextFormatting(Text text, Color color) {
        text.setFill(color);
        text.setStyle("-fx-font: 20 calibri;");
        text.setWrappingWidth(boardSize * tileSize);
        text.setTextAlignment(TextAlignment.CENTER);
    }

    public String getPlayerScores(int index) {
        Player[] players = game.getPlayers();
        String str = "Gracz " + players[index].toString() + ": " + players[index].getScore() + " punktów\n";
        return str;
    }

    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Koniec gry");
        Player[] players = game.getPlayers();
        String winner = players[0].getScore() > players[1].getScore() ? players[0].toString() : players[1].toString();
        alert.setHeaderText("Zwycięzca - Gracz " + winner);
        alert.setContentText(getPlayerScores(0) + getPlayerScores(1));
        alert.showAndWait();
    }

    public void initializeGame() {
        PlayerAlphaBeta player1 = new PlayerAlphaBeta('#', 2);
        PlayerAlphaBeta player2 = new PlayerAlphaBeta('x', 3);
        game = new Game(boardSize, player1, player2);
        board = game.getBoard();
    }

    public Player getCurrrentPlayer() {
        return game.getCurrentPlayer();
    }

    public void makeMove(Square square) {
        game.moveCurrentPlayer(square);
        game.switchPlayer();
        if (game.isBoardFull()) {
            showAlert();
        }
        text.setText(getPlayerScores(0));
        text2.setText(getPlayerScores(1));
        updatePlayerCircle();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeGame();
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Stratego");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setWidth(boardSize * tileSize);
        primaryStage.setHeight(boardSize * tileSize + 90);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
