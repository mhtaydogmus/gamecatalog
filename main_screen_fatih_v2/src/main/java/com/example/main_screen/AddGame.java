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
<<<<<<< HEAD:main_screen_fatih_v2/src/main/java/com/example/main_screen/AddGame.java
import java.util.Arrays;
=======
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
>>>>>>> 306bc9f (update 2):src/main/java/com/example/main_screen/AddGame.java

public class AddGame {

    private GameCatalogApp mainApp;  // Reference to GameCatalogApp
    private String selectedImagePath = null; // Path to selected image

    public AddGame(GameCatalogApp mainApp) {
        this.mainApp = mainApp;
    }

    private String copyImageToImgFolder(String imageUri) {
        try {
            System.out.println("Source image URI: " + imageUri);

            // Extract file name from the original path
            URI uri = new URI(imageUri);
            File sourceFile = new File(uri);

            if (!sourceFile.exists()) {
                System.out.println("Source file does not exist: " + sourceFile.getAbsolutePath());
                throw new IOException("Source image file not found");
            }

            String fileName = sourceFile.getName();
            System.out.println("File name: " + fileName);

            // Create target directory if it doesn't exist
            File targetDir = new File("src/main/resources/img");
            if (!targetDir.exists()) {
                boolean created = targetDir.mkdirs();
                System.out.println("Created target directory: " + created);
            }

            // Copy the file to the target directory
            File targetFile = new File(targetDir, fileName);
            System.out.println("Target path: " + targetFile.getAbsolutePath());

            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully");

            // Return the relative path for storage in JSON
            return "/img/" + fileName;
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

        // Image selection
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
<<<<<<< HEAD:main_screen_fatih_v2/src/main/java/com/example/main_screen/AddGame.java
                selectedImagePath = selectedFile.toURI().toString(); // e.g., file:/C:/images/game.png
=======
                selectedImagePath = selectedFile.toURI().toString();
>>>>>>> 306bc9f (update 2):src/main/java/com/example/main_screen/AddGame.java
                imagePreview.setImage(new Image(selectedImagePath));
            }
        });

        HBox imageRow = new HBox(10, chooseImageBtn, imagePreview);
        imageRow.setAlignment(Pos.CENTER_LEFT);
        formGrid.add(imageLabel, 0, labels.length);
        formGrid.add(imageRow, 1, labels.length);

        // Submit button
        Button submitBtn = new Button("Add Game");
        submitBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        submitBtn.setOnAction(e -> {
            try {
                // First, validate that all required fields have values
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

                // For list fields, handle empty input
                String platformsText = fields[4].getText().trim();
<<<<<<< HEAD:main_screen_fatih_v2/src/main/java/com/example/main_screen/AddGame.java
                java.util.List<String> platforms = platformsText.isEmpty() ?
                        new java.util.ArrayList<>() : Arrays.asList(platformsText.split(","));

                String translatorsText = fields[5].getText().trim();
                java.util.List<String> translators = translatorsText.isEmpty() ?
                        new java.util.ArrayList<>() : Arrays.asList(translatorsText.split(","));
=======
                List<String> platforms = platformsText.isEmpty() ?
                        new ArrayList<>() : Arrays.asList(platformsText.split(","));

                String translatorsText = fields[5].getText().trim();
                List<String> translators = translatorsText.isEmpty() ?
                        new ArrayList<>() : Arrays.asList(translatorsText.split(","));
>>>>>>> 306bc9f (update 2):src/main/java/com/example/main_screen/AddGame.java

                String steamId = fields[6].getText().trim();
                String releaseYear = fields[7].getText().trim();
                String playtime = fields[8].getText().trim();
                String format = fields[9].getText().trim();

                String languageText = fields[10].getText().trim();
<<<<<<< HEAD:main_screen_fatih_v2/src/main/java/com/example/main_screen/AddGame.java
                java.util.List<String> languages = languageText.isEmpty() ?
                        new java.util.ArrayList<>() : Arrays.asList(languageText.split(","));
=======
                List<String> languages = languageText.isEmpty() ?
                        new ArrayList<>() : Arrays.asList(languageText.split(","));
>>>>>>> 306bc9f (update 2):src/main/java/com/example/main_screen/AddGame.java

                // Validate the rating is a valid number
                double rating;
                try {
                    rating = Double.parseDouble(fields[11].getText().trim());
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.WARNING,
                            "Rating must be a valid number (e.g., 8.5)");
                    alert.showAndWait();
                    return;
                }

                String tagsText = fields[12].getText().trim();
<<<<<<< HEAD:main_screen_fatih_v2/src/main/java/com/example/main_screen/AddGame.java
                java.util.List<String> tags = tagsText.isEmpty() ?
                        new java.util.ArrayList<>() : Arrays.asList(tagsText.split(","));
=======
                List<String> tags = tagsText.isEmpty() ?
                        new ArrayList<>() : Arrays.asList(tagsText.split(","));
>>>>>>> 306bc9f (update 2):src/main/java/com/example/main_screen/AddGame.java

                if (selectedImagePath == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an image.");
                    alert.showAndWait();
                    return;
                }

                // Copy the image to the img folder and get the relative path
                String imgPath = copyImageToImgFolder(selectedImagePath);
                if (imgPath == null) {
                    return; // Error happened in copyImageToImgFolder
                }

                System.out.println("Creating new game with image path: " + imgPath);

                Game newGame = new Game(title, genre, developer, publisher,
                        platforms, translators, steamId, releaseYear,
                        playtime, format, languages, rating, tags, imgPath);

                mainApp.addNewGame(newGame);
<<<<<<< HEAD:main_screen_fatih_v2/src/main/java/com/example/main_screen/AddGame.java
=======
                mainApp.refreshCurrentView();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Added");
                alert.setHeaderText(null);
                alert.setContentText("New Game Added Successfully!");
                alert.showAndWait();


>>>>>>> 306bc9f (update 2):src/main/java/com/example/main_screen/AddGame.java
                addGameStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Error: " + ex.getMessage());
                alert.showAndWait();
            }
        });

<<<<<<< HEAD:main_screen_fatih_v2/src/main/java/com/example/main_screen/AddGame.java
=======

>>>>>>> 306bc9f (update 2):src/main/java/com/example/main_screen/AddGame.java
        VBox wrapper = new VBox(20);
        wrapper.getChildren().addAll(header, formGrid, submitBtn);

        Scene scene = new Scene(wrapper, 600, 800);
        addGameStage.setScene(scene);
        addGameStage.setTitle("Add New Game");
        addGameStage.show();
    }
}