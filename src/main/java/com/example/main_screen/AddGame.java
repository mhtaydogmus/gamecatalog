package com.example.main_screen;

import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddGame {

    private GameCatalogApp mainApp;
    private String selectedImagePath = null;

    public AddGame(GameCatalogApp mainApp) {
        this.mainApp = mainApp;
    }

    private String copyImageToImgFolder(String imageUri) {
        try {
            System.out.println("Source image URI: " + imageUri);

            URI uri = new URI(imageUri);
            File sourceFile = new File(uri);

            if (!sourceFile.exists()) {
                System.out.println("Source file does not exist: " + sourceFile.getAbsolutePath());
                throw new IOException("Source image file not found");
            }

            String fileName = sourceFile.getName();
            System.out.println("File name: " + fileName);

            File targetDir = new File(System.getProperty("user.home"), "GameCatalogApp/images");
            //File targetDir = new File("src/main/resources/img");
            if (!targetDir.exists()) {
                boolean created = targetDir.mkdirs();
                System.out.println("Created target directory: " + created);
            }

            File targetFile = new File(targetDir, fileName);
            System.out.println("Target path: " + targetFile.getAbsolutePath());

            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully");
            return Paths.get("images", fileName).toString().replace("\\", "/");

            //return "images/" + fileName;
            //return "/img/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Failed to copy image file: " + e.getMessage());
            alert.showAndWait();
            return null;
        }
    }

    public void start(Stage addGameStage) {
        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-background-color: #fdfdfd;");
        formContainer.setAlignment(Pos.TOP_LEFT);

        Label header = new Label("Add Game");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(12);
        formGrid.setPadding(new Insets(10));

        String[] labels = {
                "Title", "Genre", "Developer", "Publisher", "Platforms",
                "Translators", "Steam ID", "Release Year", "Playtime",
                "Format", "Language", "Rating", "Tags"
        };

        TextField[] fields = new TextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            Label label = new Label(labels[i] + ":");
            label.setStyle("-fx-font-weight: bold;");
            TextField field = new TextField();
            fields[i] = field;

            formGrid.add(label, 0, i);
            formGrid.add(field, 1, i);
        }

        Label imageLabel = new Label("Image:");
        imageLabel.setStyle("-fx-font-weight: bold;");
        Button chooseImageBtn = new Button("Choose Image");
        ImageView imagePreview = new ImageView();
        imagePreview.setFitWidth(100);
        imagePreview.setFitHeight(150);
        imagePreview.setPreserveRatio(true);

        chooseImageBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Game Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(addGameStage);
            if (selectedFile != null) {
                selectedImagePath = selectedFile.toURI().toString();
                imagePreview.setImage(new Image(selectedImagePath));
            }
        });
        mainApp.refreshCurrentView();
        HBox imageRow = new HBox(10, chooseImageBtn, imagePreview);
        imageRow.setAlignment(Pos.CENTER_LEFT);
        formGrid.add(imageLabel, 0, labels.length);
        formGrid.add(imageRow, 1, labels.length);

        Button submitBtn = new Button("Add Game");
        submitBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        submitBtn.setOnAction(e -> {
            try {
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].getText().trim().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING,
                                "Please fill in all fields. " + labels[i] + " is empty.");
                        alert.showAndWait();
                        return;
                    }
                }

                String title = fields[0].getText().trim();
                String genre = fields[1].getText().trim();
                String developer = fields[2].getText().trim();
                String publisher = fields[3].getText().trim();

                String platformsText = fields[4].getText().trim();
                List<String> platforms = platformsText.isEmpty() ?
                        new ArrayList<>() : Arrays.asList(platformsText.split(","));

                String translatorsText = fields[5].getText().trim();
                List<String> translators = translatorsText.isEmpty() ?
                        new ArrayList<>() : Arrays.asList(translatorsText.split(","));

                String steamId = fields[6].getText().trim();
                String releaseYear = fields[7].getText().trim();
                String playtime = fields[8].getText().trim();
                String format = fields[9].getText().trim();

                String languageText = fields[10].getText().trim();
                List<String> languages = languageText.isEmpty() ?
                        new ArrayList<>() : Arrays.asList(languageText.split(","));

                double rating;
                try {
                    rating = Double.parseDouble(fields[11].getText().trim());

                    if (rating < 0 || rating > 10) {
                        Alert alert = new Alert(Alert.AlertType.WARNING,
                                "Rating must be between 0 and 10.");
                        alert.showAndWait();
                        return;
                    }
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.WARNING,
                            "Rating must be a valid number (e.g., 8.5).");
                    alert.showAndWait();
                    return;
                }

                String tagsText = fields[12].getText().trim();
                List<String> tags = tagsText.isEmpty() ?
                        new ArrayList<>() : Arrays.asList(tagsText.split(","));

                if (selectedImagePath == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an image.");
                    alert.showAndWait();
                    return;
                }

                String imgPath = copyImageToImgFolder(selectedImagePath);
                if (imgPath == null) {
                    return;
                }

                System.out.println("Creating new game with image path: " + imgPath);

                Game newGame = new Game(title, genre, developer, publisher,
                        platforms, translators, steamId, releaseYear,
                        playtime, format, languages, rating, tags, imgPath);

                mainApp.addNewGame(newGame);
                mainApp.refreshCurrentView();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Added");
                alert.setHeaderText(null);
                alert.setContentText("New Game Added Successfully!");
                alert.showAndWait();

                addGameStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Error: " + ex.getMessage());
                alert.showAndWait();
            }
        });



        VBox wrapper = new VBox(20);
        wrapper.getChildren().addAll(header, formGrid, submitBtn);

        Scene scene = new Scene(wrapper, 600, 800);
        addGameStage.setScene(scene);
        addGameStage.setTitle("Add New Game");
        addGameStage.show();
    }
    public void ichangedsmthGithubPushit(){
        int nothing=0;
        return;
    }
}