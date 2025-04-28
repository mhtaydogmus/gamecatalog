package com.example.main_screen;
import com.example.main_screen.Game;


import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameLabel {

    private Game game;
    private CheckBox selectBox;
    private GameCatalogApp mainApp;


    public GameLabel(Game game, GameCatalogApp mainApp) {
        this.game = game;
        this.selectBox = new CheckBox();
        this.mainApp = mainApp;
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
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #f9f9f9;");

        // Title
        Label titleLabel = new Label("Title: " + game.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        // Larger game image
        String imgSource = game.getImage();
        Image cover;
        if (imgSource.startsWith("file:") || imgSource.startsWith("http")) {
            cover = new Image(imgSource, 200, 300, true, true);
        } else {
            cover = new Image(getClass().getResource(imgSource).toExternalForm(), 200, 300, true, true);
        }
        ImageView iv = new ImageView(cover);

        // All other fields
        Label genreLabel = new Label("Genre: " + game.getGenre());
        Label devLabel = new Label("Developer: " + game.getDeveloper());
        Label pubLabel = new Label("Publisher: " + game.getPublisher());
        Label releaseLabel = new Label("Release Year: " + game.getReleaseYear());
        Label steamidLabel = new Label("Steam ID: " + game.getSteamid());
        Label playtimeLabel = new Label("Playtime: " + game.getPlaytime());
        Label formatLabel = new Label("Format: " + game.getFormat());
        Label ratingLabel = new Label("Rating: " + game.getRating());

        Label platformsLabel = new Label("Platforms: " + String.join(", ", game.getPlatforms()));
        Label translatorsLabel = new Label("Translators: " + String.join(", ", game.getTranslators()));
        Label languagesLabel = new Label("Languages: " + String.join(", ", game.getLanguage()));
        Label tagsLabel = new Label("Tags: " + game.getDescription(game.getTags()));

        // Buttons
        Button edit = new Button("Edit");
        edit.setOnAction((ActionEvent e) -> editScreen(newStage, game));

        Button delete = new Button("Delete");
        delete.setOnAction((ActionEvent e) -> {
            mainApp.getallGames().remove(game);
            GameLoader.saveGames(mainApp.getallGames());
            mainApp.refreshCurrentView();
            newStage.close();

        });

        layout.getChildren().addAll(
                titleLabel, iv,
                genreLabel, devLabel, pubLabel, releaseLabel,
                steamidLabel, playtimeLabel, formatLabel, ratingLabel,
                platformsLabel, translatorsLabel, languagesLabel, tagsLabel,
                edit, delete
        );

        Scene scene = new Scene(layout, 500, 800);
        newStage.setTitle(game.getTitle() + " - Details");
        newStage.setScene(scene);
        newStage.show();
    }

    private void editScreen(Stage stage, Game game) {
        VBox editLayout = new VBox(10);
        editLayout.setPadding(new Insets(20));

        Label titleLabel = new Label("Edit Game");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        TextField titleField = new TextField(game.getTitle());
        TextField genreField = new TextField(game.getGenre());
        TextField developerField = new TextField(game.getDeveloper());
        TextField publisherField = new TextField(game.getPublisher());
        TextField platformsField = new TextField(String.join(",", game.getPlatforms()));
        TextField translatorsField = new TextField(String.join(",", game.getTranslators()));
        TextField steamIdField = new TextField(game.getSteamid());
        TextField releaseYearField = new TextField(game.getReleaseYear());
        TextField playtimeField = new TextField(game.getPlaytime());
        TextField formatField = new TextField(game.getFormat());
        TextField languageField = new TextField(String.join(",", game.getLanguage()));
        TextField ratingField = new TextField(String.valueOf(game.getRating()));
        TextField tagsField = new TextField(String.join(",", game.getTags()));

        String imagePath = "/images/" + game.getImage();
        ImageView imagePreview = new ImageView(new Image(getClass().getResourceAsStream(imagePath), 150, 200, true, true));
        //ImageView imagePreview = new ImageView(new Image(game.getImage(), 150, 200, true, true));
        //imagePreview.setUserData(game.getImage());


        Button imageButton = new Button("Choose Image");
        imageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Game Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                try {
                    File appImageDir = new File("src/main/resources/images/");
                    if (!appImageDir.exists()) appImageDir.mkdirs();

                    // Copy file to app image folder
                    File copiedFile = new File(appImageDir, selectedFile.getName());
                    java.nio.file.Files.copy(
                            selectedFile.toPath(),
                            copiedFile.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING
                    );

                    String relativePath = "/images/" + selectedFile.getName();
                    imagePreview.setImage(new Image(getClass().getResource(relativePath).toExternalForm(), 150, 200, true, true));
                    imagePreview.setUserData(relativePath);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String image = (String) imagePreview.getUserData();
            String genreText = genreField.getText().trim();
            String developerText = developerField.getText().trim();
            String publisherText = publisherField.getText().trim();
            String platformsText = platformsField.getText().trim();
            String translatorsText = translatorsField.getText().trim();
            String steamIdText = steamIdField.getText().trim();
            String releaseYearText = releaseYearField.getText().trim();
            String playtimeText = playtimeField.getText().trim();
            String formatText = formatField.getText().trim();
            String languageText = languageField.getText().trim();
            String tagsText = tagsField.getText().trim();
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
                Image testImage = new Image(image, 10, 10, true, true);
                if (testImage.isError()) {
                    errorLabel.setText("Image failed to load.");
                    return;
                }

                game.setTitle(title);
                game.setImage(image);
                game.setGenre(genreText);
                game.setDeveloper(developerText);
                game.setPublisher(publisherText);
                game.setSteamid(steamIdText);
                game.setReleaseYear(releaseYearText);
                game.setPlaytime(playtimeText);
                game.setFormat(formatText);
                game.setRating(rating);

                game.setPlatforms(List.of(platformsText.split("\\s*,\\s*")));
                game.setTranslators(List.of(translatorsText.split("\\s*,\\s*")));
                game.setLanguage(List.of(languageText.split("\\s*,\\s*")));
                game.setTags(List.of(tagsText.split("\\s*,\\s*")));

                GameLoader.saveGames(mainApp.getallGames());
                mainApp.refreshCurrentView();
                System.out.println("Save button clicked!");

                stage.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                errorLabel.setText("Error updating game.");
            }

        });


        cancelButton.setOnAction(e -> stage.close());

        editLayout.getChildren().addAll(
                titleLabel,
                new Label("Title:"), titleField,
                new Label("Genre:"), genreField,
                new Label("Developer:"), developerField,
                new Label("Publisher:"), publisherField,
                new Label("Platforms (comma-separated):"), platformsField,
                new Label("Translators (comma-separated):"), translatorsField,
                new Label("Steam ID:"), steamIdField,
                new Label("Release Year:"), releaseYearField,
                new Label("Playtime:"), playtimeField,
                new Label("Format:"), formatField,
                new Label("Language (comma-separated):"), languageField,
                new Label("Rating (0-10):"), ratingField,
                new Label("Tags (comma-separated):"), tagsField,
                new Label("Image:"), imagePreview, imageButton,
                errorLabel,
                saveButton, cancelButton
        );


        ScrollPane scrollPane = new ScrollPane(editLayout);
        scrollPane.setFitToWidth(true);
        Scene editScene = new Scene(scrollPane, 450, 800);
        stage.setScene(editScene);
    }

    public Game getGame() {
        return game;
    }
}

