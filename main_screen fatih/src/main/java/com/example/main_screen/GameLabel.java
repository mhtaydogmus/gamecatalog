package com.example.main_screen;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameLabel {

    private Game game;


    public GameLabel(Game game) {
        this.game = game;
    }

    public VBox createItem() {
        VBox itemBox = new VBox(10);
        itemBox.setStyle("");

        // game img
        ImageView gameImageView = new ImageView(new Image(getClass().getResource(game.getImage()).toString()));
        gameImageView.setFitWidth(150);
        gameImageView.setFitHeight(225);

        // btn for redirect
        Button button = new Button();
        button.setGraphic(gameImageView);
        button.setOnAction(event -> openGamePage());

        // game title
        Label gameTitle = new Label(game.getTitle());
        gameTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        //description
        Label gameDescription = new Label(game.getDescription());
        gameDescription.setStyle("-fx-font-size: 12px; -fx-text-fill: grey;");
        gameDescription.setMaxWidth(175);
        gameDescription.setWrapText(true);
        gameDescription.setMaxHeight(60);

        // add to vbox
        itemBox.getChildren().addAll(button, gameTitle, gameDescription);

        return itemBox;
    }
    private void openGamePage() {
        // create new window
        Stage newStage = new Stage();

        // create a new scene
        VBox newPageLayout = new VBox(20);
        Label titleLabel = new Label("Title: " + game.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        Label descriptionLabel = new Label("Description: " + game.getDescription());
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: grey;");

        ImageView gameImageView = new ImageView(new Image(getClass().getResource(game.getImage()).toString()));
        gameImageView.setFitWidth(200);
        gameImageView.setFitHeight(200);

        newPageLayout.getChildren().addAll(titleLabel, descriptionLabel, gameImageView);

        Scene newScene = new Scene(newPageLayout, 400, 300);

        newStage.setTitle(game.getTitle() + " - Details");

        newStage.setScene(newScene);
        newStage.show();
    }
}

