package com.company.GUI;

import com.company.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Spinner;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.company.Constants.*;

public class Board extends Application {
    private int tileSize = 80, boardSize = 5;
    private Group tileGroup = new Group();
    private Game game;
    private Square[][] board;
    private Tile[][] tiles;
    private Text text, text2;
    private Circle nextPlayerCircle;
    private String opponentType;
    private boolean startingPlayerIsHuman;

    private Parent createContent() {
        startingPlayerIsHuman = true;
        makeOpponentDialog();
        initializeGame();
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double maxHeight = primaryScreenBounds.getHeight();
        while (boardSize * tileSize > maxHeight - 100 && tileSize > 1)
            tileSize--;
        Pane root = new Pane();
        root.setPrefSize(boardSize * tileSize, boardSize * tileSize + 30);
        tiles = new Tile[boardSize][boardSize];
        for (int y = 0; y < boardSize; y++){
            for (int x = 0; x < boardSize; x++){ //x+y) % 3 == 0 ? 0 : ((x+y) % 3 == 1 ? 1 : 2)
                Tile tile = new Tile(this, board[y][x], 0, x, y, tileSize);
                tiles[y][x] = tile;
                initializeGame();
                tileGroup.getChildren().add(tile);
            }
        }
        root.getChildren().addAll(tileGroup);
        addExtras(root);
        return root;
    }

    private void makeOpponentDialog() {
        List<String> choices = new ArrayList<>();
        choices.add(Constants.TYPE_HUMAN);
        choices.add(Constants.TYPE_MINMAX);
        choices.add(Constants.TYPE_ALPHABETA);

        ChoiceDialog<String> dialog = new ChoiceDialog<>(Constants.TYPE_HUMAN, choices);
        dialog.setTitle("Stratego");
        dialog.setHeaderText("Wybierz przeciwnika");
        dialog.setContentText("Przeciwnik:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            System.out.println("Opponent choice: " + result.get());
            opponentType = result.get();
        }
        if (opponentType.equals(Constants.TYPE_MINMAX) || opponentType.equals(Constants.TYPE_ALPHABETA))
            makeStartingPlayerDialog();
    }

    private void makeStartingPlayerDialog() {
        List<String> choices = new ArrayList<>();
        choices.add(Constants.ORDER_FIRST);
        choices.add(Constants.ORDER_SECOND);

        ChoiceDialog<String> dialog = new ChoiceDialog<>(Constants.ORDER_FIRST, choices);
        dialog.setTitle("Stratego");
        dialog.setHeaderText("W jakiej kolejności chcesz zacząć?");
        dialog.setContentText("Zaczynam:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            System.out.println("Order choice: " + result.get());
            if (result.get().equals(Constants.ORDER_SECOND))
                startingPlayerIsHuman = false;
        }
    }

    private void addExtras(Pane root) {
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
        root.getChildren().addAll(hb, nextPlayerCircle);
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

    public void showEndGameAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Koniec gry");
        Player[] players = game.getPlayers();
        String winner = players[0].getScore() > players[1].getScore() ? players[0].toString() : players[1].toString();
        alert.setHeaderText("Zwycięzca - Gracz " + winner);
        alert.setContentText(getPlayerScores(0) + getPlayerScores(1));
        alert.show();
    }

    private void initializeGame() {
        Player player1;
        Player player2;
        switch (opponentType){
            case Constants.TYPE_MINMAX:
                if (startingPlayerIsHuman) {
                    player1 = new PlayerHuman('1');
                    player2 = new PlayerMinMax('2', 3);
                }
                else {
                    player1 = new PlayerMinMax('1', 3);
                    player2 = new PlayerHuman('2');
                }
                break;
            case Constants.TYPE_ALPHABETA:
                if (startingPlayerIsHuman) {
                    player1 = new PlayerHuman('1');
                    player2 = new PlayerAlphaBeta('2', 3);
                }
                else {
                    player1 = new PlayerAlphaBeta('1', 3);
                    player2 = new PlayerHuman('2');
                }
                break;
            default:
                player1 = new PlayerHuman('1');
                player2 = new PlayerHuman('2');
                break;
        }
        game = new Game(boardSize, player1, player2);
        board = game.getBoard();
    }

    public Player getCurrentPlayer() {
        return game.getCurrentPlayer();
    }

    public void makeMove(Square square) {
        switch (opponentType) {
            case Constants.TYPE_HUMAN:
                makeMoveHuman(square);
                break;
            case Constants.TYPE_MINMAX:
                makeMoveVsComputer(square);
                break;
            case Constants.TYPE_ALPHABETA:
                makeMoveVsComputer(square);
                break;
            default:
                break;
        }
    }

    private void makeMoveVsComputer(Square square) {
        makeMoveHuman(square);
        if (!game.isBoardFull())
            computersMove();
    }

    private void computersMove() {
        game.getCurrentPlayer().move(game.getAvailableMoves(), game);
        Square lastMarkedSquare = game.getLastMarkedSquare();
        Tile tile = tiles[lastMarkedSquare.getRow()][lastMarkedSquare.getColumn()];
        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    tile.markTile(game.getCurrentPlayer());
                    text.setText(getPlayerScores(0));
                    text2.setText(getPlayerScores(1));
                    game.switchPlayer();
                    updatePlayerCircle();
                    if (game.isBoardFull()) {
                        showEndGameAlert();
                    }
                })
        );
        timer.play();
    }

    private void makeMoveHuman(Square square) {
        game.moveCurrentPlayer(square);
        game.switchPlayer();
        if (game.isBoardFull()) {
            showEndGameAlert();
        }
        text.setText(getPlayerScores(0));
        text2.setText(getPlayerScores(1));
        updatePlayerCircle();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Stratego");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setWidth(boardSize * tileSize);
        primaryStage.setHeight(boardSize * tileSize + 90);
        primaryStage.show();
        if (!startingPlayerIsHuman)
            computersMove();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
