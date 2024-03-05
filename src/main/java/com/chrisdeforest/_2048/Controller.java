package com.chrisdeforest._2048;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class Controller extends Application implements PropertyChangeListener {
    private final static int SCORE_TILE_SIZE = 100;
    private final static int TILE_SIZE = 120;
    private final static int GRID_SIZE = 500;
    private static Label status = new Label(""), scoreVal, bestScoreVal;
    private static Game game;
    private GridPane grid;
    @Override
    public void start(Stage stage) throws IOException {
        // title, directions, how-to
        VBox info = makeInfo();
        // score and best score
        GridPane scoreAndBestScore = makeScoreGrid();
        // configuring the top section
        HBox top = makeTop(scoreAndBestScore, info);
        // main game grid
        grid = makeGrid();
        // adding all sections to the main vbox
        VBox main = new VBox();
        main.getChildren().addAll(top, grid, status);
        main.setAlignment(Pos.CENTER);
        // scene and stage settings
        Scene scene = new Scene(main);
        scene.getStylesheets().add("file:src/main/resources/styles.css");
        scene.setOnKeyPressed(keyEvent -> {
            System.out.println(keyEvent.getCode());
            switch(keyEvent.getCode()){
                case UP, W, KP_UP:          game.moveUp();      break;
                case RIGHT, D, KP_RIGHT:    game.moveRight();   break;
                case DOWN, S, KP_DOWN:      game.moveDown();     break;
                case LEFT, A, KP_LEFT:      game.moveLeft();    break;
            }
        });
        stage.setScene(scene);
        stage.setHeight(800);
        stage.setWidth(600);
        stage.setResizable(false);
        stage.setTitle("2048");
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void init() throws Exception{
        game = new Game();
        game.addPropertyChangeListener(this);
    }
    public static VBox makeInfo(){
        VBox info = new VBox();
        Label _2048 = new Label("2048");
        _2048.getStyleClass().addAll("game-title", "bold", "directions-text");
        Label directions = new Label("Join the tiles, get to ");
        directions.getStyleClass().addAll("directions-padding", "directions-text");
        Label directions1 = new Label("2048!");
        directions1.getStyleClass().addAll("bold", "directions-text");
        HBox dir = new HBox();
        dir.getChildren().addAll(directions, directions1);
        Label howTo = new Label("How to play -->\n");
        howTo.getStyleClass().addAll("directions-padding", "ul", "bold", "directions-text");
        Label blank = new Label();
        blank.setMinSize(50,35);
        info.getChildren().addAll(_2048, dir, howTo, blank);
        return info;
    }

    public static GridPane makeGrid(){
        GridPane grid = new GridPane();
        grid.setMinSize(GRID_SIZE, GRID_SIZE);
        grid.setMaxSize(GRID_SIZE, GRID_SIZE);
        grid.getStyleClass().addAll("grid");
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                Label label = new Label("0");
                if(label.getText().equals("0"))
                    label.setText("");
                label.getStyleClass().addAll("margin", "game-label");
                label.setStyle("-fx-background-color: rgb(203, 193, 178);");
                grid.add(label, i, j);
            }
        }
        return grid;
    }

    public static GridPane makeScoreGrid(){
        GridPane grid = new GridPane();
        grid.setMaxSize(SCORE_TILE_SIZE * 1.5, SCORE_TILE_SIZE * 0.75);
        grid.setMinSize(SCORE_TILE_SIZE * 1.25, SCORE_TILE_SIZE * 0.75);
        grid.getStyleClass().addAll("score-grid");
        VBox left = new VBox();
        left.setMinSize(100,60);
        left.setMaxSize(125, 60);
        Label score = new Label("SCORE");
        score.setStyle("-fx-text-fill: rgb(242, 226, 208); -fx-font-weight: bold; -fx-font-size: 11pt;");
        scoreVal = new Label("0");
        scoreVal.getStyleClass().addAll("score-text", "bold");
        left.getChildren().addAll(score, scoreVal);
        left.getStyleClass().addAll( "grid", "score-margin");
        left.setAlignment(Pos.CENTER);
        VBox right = new VBox();
        right.setMinSize(110,60);
        right.setMaxSize(125, 60);
        Label best = new Label("BEST");
        best.setStyle("-fx-text-fill: rgb(242, 226, 208); -fx-font-weight: bold; -fx-font-size: 11pt;");
        // TODO do something with this so it can be updated later on
        bestScoreVal= new Label("0");
        bestScoreVal.getStyleClass().addAll("score-text", "bold");
        right.getChildren().addAll(best, bestScoreVal);
        right.getStyleClass().addAll( "grid", "score-margin");
        right.setAlignment(Pos.CENTER);
        grid.add(left, 0, 0);
        grid.add(right, 1, 0);
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    public static HBox makeTop(GridPane scoreAndBestScore, VBox info){
        Label blank = new Label();
        blank.setMinSize(105, 25);
        Label blank2 = new Label();
        blank2.setMinSize(50,40);
        Button newGame = new Button("New Game");
        EventHandler<ActionEvent> newGameAction = event -> {
            game.newGame();
        };
        newGame.setOnAction(newGameAction);
        newGame.getStyleClass().addAll("game-new-game", "bold");
        VBox rightSide = new VBox();
        rightSide.setMinSize(175, 125);
        rightSide.setAlignment(Pos.TOP_RIGHT);
        rightSide.getChildren().addAll(scoreAndBestScore, blank2, newGame);
        HBox top = new HBox();
        top.getChildren().addAll(info, blank, rightSide);
        top.setAlignment(Pos.TOP_LEFT);
        return top;
    }

    public void clearGrid(){
        for(int i = 0; i < Game.BOARD_SIZE; i++){
            for(int j = 0; j < Game.BOARD_SIZE; j++){
                grid.getChildren().removeFirst();
            }
        }
    }

    private void updateTiles() {
        clearGrid();
        Label label;
        for(int i = 0; i < Game.BOARD_SIZE; i++){
            for(int j = 0; j < Game.BOARD_SIZE; j++){
                label = new Label(String.valueOf(game.getBoard()[i][j].getValue()));
                String textColor = game.getBoard()[i][j].getTextColor();
                String backgroundColor = game.getBoard()[i][j].getBackground();
                String fontSize = game.getBoard()[i][j].getFontSize();
                label.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: " + textColor + ";" +
                        " -fx-font-size: " + fontSize + ";");
                if(label.getText().equals("0"))
                    label.setText("");
                label.getStyleClass().addAll("margin", "game-label");
                label.setPrefSize(TILE_SIZE, TILE_SIZE);
                label.setMinSize(TILE_SIZE, TILE_SIZE);
                label.setMaxSize(TILE_SIZE, TILE_SIZE);
                grid.add(label, j, i);
            }
        }
    }
    private void updateScore(int val){
        game.setScore(game.getScore() + val);
        scoreVal.setText(String.valueOf(game.getScore()));
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch(event.getPropertyName()){
            case "initialized":
                status.setText("Click \"New Game\" to start a game");
                break;
            case "newGame":
                status.setText("New game has been started: " + event.getNewValue());
                updateTiles();
                break;
            case "up":
                status.setText("Moved up");
                updateTiles();
                break;
            case "right":
                status.setText("Moved right");
                updateTiles();
                break;
            case "down":
                status.setText("Moved down");
                updateTiles();
                break;
            case "left":
                status.setText("Moved left");
                updateTiles();
                break;
        }
    }
    // TODO start doing move functions; do initial moves first, then add animations once they work;
    // TODO also add appropriate functions in Game so that the moves are "rules" similar to "New Game"
}
