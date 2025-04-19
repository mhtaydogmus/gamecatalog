package com.example.main_screen;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameLabel {

    private Game game;

    public GameLabel(Game game) {
        this.game = game;
    }

    public VBox createItem() {
        VBox itemBox = new VBox(10);

        // --- LOAD IMAGE ---
        String imgSource = game.getImage();
        // imgSource should be something like "file:/…/game_images/uuid-filename.jpg"
        Image cover;
        if (imgSource.startsWith("file:") || imgSource.startsWith("http")) {
            // user-provided / external image
            cover = new Image(imgSource, 150, 225, true, true);
        } else {
            // packaged resource fallback (e.g. "/img/default.png")
            cover = new Image(
                    getClass().getResource(imgSource).toExternalForm(),
                    150, 225, true, true
            );
        }

        ImageView gameImageView = new ImageView(cover);

        // --- BUTTON WRAPPING THE IMAGE ---
        Button button = new Button();
        button.setGraphic(gameImageView);
        button.setOnAction(e -> openGamePage());

        // --- TITLE & DESCRIPTION ---
        Label gameTitle = new Label(game.getTitle());
        gameTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        String tagsDescription = game.getDescription(game.getTags());
        Label gameDescription = new Label(tagsDescription);
        gameDescription.setStyle("-fx-font-size: 12px; -fx-text-fill: grey;");
        gameDescription.setMaxWidth(175);
        gameDescription.setWrapText(true);
        gameDescription.setMaxHeight(60);

        itemBox.getChildren().addAll(button, gameTitle, gameDescription);
        return itemBox;
    }

    private void openGamePage() {
        Stage newStage = new Stage();
        VBox layout = new VBox(20);

        Label titleLabel = new Label("Title: " + game.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        String tagsDescription = game.getDescription(game.getTags());
        Label desc = new Label(tagsDescription);
        desc.setStyle("-fx-font-size: 14px; -fx-text-fill: grey;");

        // load the same Image again at a larger size
        String imgSource = game.getImage();
        Image cover;
        if (imgSource.startsWith("file:") || imgSource.startsWith("http")) {
            cover = new Image(imgSource, 200, 200, true, true);
        } else {
            cover = new Image(
                    getClass().getResource(imgSource).toExternalForm(),
                    200, 200, true, true
            );
        }
        ImageView iv = new ImageView(cover);

        layout.getChildren().addAll(titleLabel, desc, iv);
        Scene scene = new Scene(layout, 400, 300);
        newStage.setTitle(game.getTitle() + " - Details");
        newStage.setScene(scene);
        newStage.show();
    }
}