package com.example.main_screen;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GameLabel {

    private Game game;
    private List<Game> allgames;

    public GameLabel(Game game, List<Game> allGames) {
        this.game = game;
        this.allgames = allGames;
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
        Button edit = new Button("edit");
        edit.setOnAction((ActionEvent e) -> {
            editScreen(newStage, game);
        });
        Button delete = new Button("delete");
        delete.setOnAction((ActionEvent e) -> {
            allgames.remove(game);
            newStage.close();
        });

        layout.getChildren().addAll(titleLabel, desc, iv);
        Scene scene = new Scene(layout, 400, 300);
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
    Scene editScene = new Scene(editLayout, 400, 400);
    stage.setScene(editScene);
    }
}