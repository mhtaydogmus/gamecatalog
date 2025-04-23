package com.example.main_screen;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class GameLabel {

    private Game game;
    private CheckBox selectBox;
    private GameCatalogApp mainApp;


    public GameLabel(Game game) {
        this.game = game;
        this.selectBox = new CheckBox();
    }

    public VBox createItem() {
        VBox itemBox = new VBox(10);
        itemBox.setId("game_item");
        itemBox.setAlignment(Pos.CENTER);

        // game img
        ImageView gameImageView = new ImageView(new Image(getClass().getResource(game.getImage()).toString()));
        gameImageView.setFitWidth(150);
        gameImageView.setFitHeight(225);
        gameImageView.setId("game_image");


        // btn for redirect
        Button button = new Button();
        button.setGraphic(gameImageView);
        button.setOnAction(event -> openGamePage());
        button.getStyleClass().add("game_item_button");
        button.setStyle("-fx-padding: 0;");

        // game title
        Label gameTitle = new Label(game.getTitle());
        gameTitle.getStyleClass().add("game_item_label");

        gameTitle.setMaxWidth(175);
        gameTitle.setWrapText(true);
        gameTitle.setPadding(new Insets(0, 0, 0, 10));

        //description
        String tagsDescription = game.getDescription(game.getTags());
        Label gameDescription = new Label(tagsDescription);
        gameDescription.getStyleClass().add("game_description");
        gameDescription.setMaxWidth(175);
        gameDescription.setWrapText(true);
        gameDescription.setMaxHeight(60);
        gameDescription.setPadding(new Insets(0, 0, 0, 10));



        // add to vbox
        itemBox.getChildren().addAll(button, gameTitle, gameDescription,selectBox);

        return itemBox;
    }
    public boolean isSelected() {
        return selectBox.isSelected();
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
        Button edit = new Button("edit");
        edit.setOnAction((ActionEvent e) -> {
            editScreen(newStage, game);
        });
        Button delete = new Button("delete");
        delete.setOnAction((ActionEvent e) -> {
            mainApp.getallGames().remove(game);
            newStage.close();
        });

        layout.getChildren().addAll(desc, iv,delete,edit);
        Scene scene = new Scene(layout, 450, 600);
        newStage.setTitle(game.getTitle() + " - Details");
        newStage.setScene(scene);
        newStage.show();
    }

    private void editScreen(Stage stage,Game game) {
        VBox editLayout = new VBox(10);

        Label titleLabel = new Label("Edit Game");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        TextField titleField = new TextField(game.getTitle());
        ImageView imagePreview = new ImageView(game.getImage());
        Button imageButton = new Button();
        imageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Game Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                String imageUrl = selectedFile.toURI().toString();
                Image image = new Image(imageUrl);
                imagePreview.setImage(image);
            }
        });
        TextField ratingField = new TextField(String.valueOf(game.getRating()));
        Button cancelButton = new Button("Cancel");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String image = imagePreview.getImage().getUrl();
            String ratingText = ratingField.getText().trim();
            if (title.isEmpty() || image.isEmpty() || ratingText.isEmpty()) {
                errorLabel.setText("All fields are required.");
                return;
            }
            double rating;
            try {
                rating = Double.parseDouble(ratingText);
                if (rating < 0 || rating > 10) {
                    errorLabel.setText("Rating must be between 0 and 10.");
                    return;
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Rating must be a number.");
                return;
            }
            try {
                Image testImage;
                if (image.startsWith("file:") || image.startsWith("http")) {
                    testImage = new Image(image, 10, 10, true, true);
                } else {
                    testImage = new Image(getClass().getResource(image).toExternalForm(), 10, 10, true, true);
                }

                if (testImage.isError()) {
                    errorLabel.setText("Image failed to load.");
                    return;
                }
                game.setTitle(title);
                game.setImage(image);
                game.setRating(rating);
                stage.close();
                openGamePage();
            } catch (Exception e2) {
                errorLabel.setText("Invalid image path or resource.");
            }
        });
        cancelButton.setOnAction(e -> stage.close());
        editLayout.getChildren().addAll(
                titleLabel,
                new Label("Title:"), titleField,
                imagePreview, imageButton,
                new Label("Rating (0-10):"), ratingField,
                errorLabel,
                saveButton, cancelButton
        );
        Scene editScene = new Scene(editLayout, 400, 800);
        stage.setScene(editScene);
    }
    public Game getGame() {
        return game;
    }
}

