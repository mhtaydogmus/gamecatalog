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
import javafx.scene.text.Text;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

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

    // CSS Styles
    private final String BACKGROUND_STYLE = "-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);";
    private final String HEADER_STYLE = "-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #343a40; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 0, 1);";
    private final String LABEL_STYLE = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #495057;";
    private final String FIELD_STYLE = "-fx-background-radius: 5px; -fx-border-radius: 5px; -fx-border-color: #ced4da; -fx-border-width: 1px; -fx-padding: 8px; -fx-background-color: white;";
    private final String BUTTON_STYLE = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px; -fx-cursor: hand; -fx-padding: 10px 20px;";
    private final String BUTTON_HOVER_STYLE = "-fx-background-color: #3e8e41; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px; -fx-cursor: hand; -fx-padding: 10px 20px;";
    private final String IMAGE_BUTTON_STYLE = "-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px; -fx-cursor: hand; -fx-padding: 8px 15px;";
    private final String IMAGE_BUTTON_HOVER_STYLE = "-fx-background-color: #5a6268; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px; -fx-cursor: hand; -fx-padding: 8px 15px;";
    private final String FORM_CONTAINER_STYLE = "-fx-background-color: white; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3); -fx-padding: 25px;";

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
            if (!targetDir.exists()) {
                boolean created = targetDir.mkdirs();
                System.out.println("Created target directory: " + created);
            }

            File targetFile = new File(targetDir, fileName);
            System.out.println("Target path: " + targetFile.getAbsolutePath());

            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully");
            return Paths.get("images", fileName).toString().replace("\\", "/");
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Failed to copy image file: " + e.getMessage());
            alert.showAndWait();
            return null;
        }
    }

    public void start(Stage addGameStage) {
        // Get screen dimensions
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        double screenHeight = screenBounds.getHeight();
        double screenWidth = screenBounds.getWidth();

        // Appropriate window size calculation
        double windowHeight = Math.min(800, screenHeight * 0.9);
        double windowWidth = Math.min(650, screenWidth * 0.8);

        // Main background pane
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle(BACKGROUND_STYLE);
        mainLayout.setPadding(new Insets(15));

        // Center content wrapper adjust spacing based on available height
        double verticalSpacing = windowHeight < 700 ? 15 : 25;
        VBox formWrapper = new VBox(verticalSpacing);
        formWrapper.setAlignment(Pos.TOP_CENTER);
        formWrapper.setStyle(FORM_CONTAINER_STYLE);
        formWrapper.setMaxWidth(Math.min(550, windowWidth - 40));

        // Header with icon and text - smaller on small screens
        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER);

        Label header = new Label("Add New Game");
        header.setStyle(windowHeight < 700 ?
                "-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #343a40; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 0, 1);" :
                HEADER_STYLE);

        headerBox.getChildren().add(header);

        // Separator under header
        Separator separator = new Separator();
        separator.setPadding(new Insets(5, 0, 10, 0));

        // Form grid adjust spacing based on screen size
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(windowHeight < 700 ? 10 : 15);
        formGrid.setAlignment(Pos.CENTER);

        String[] labels = {
                "Title", "Genre", "Developer", "Publisher", "Platforms",
                "Translators", "Steam ID", "Release Year", "Playtime",
                "Format", "Language", "Rating", "Tags"
        };

        TextField[] fields = new TextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            Label label = new Label(labels[i] + ":");
            label.setStyle(LABEL_STYLE);
            label.setMinWidth(80);

            TextField field = new TextField();
            field.setStyle(FIELD_STYLE);
            field.setPrefWidth(windowWidth < 600 ? 250 : 300);
            if (labels[i].equals("Rating")) {
                field.setPromptText("0-10");
            } else if (labels[i].equals("Platforms") || labels[i].equals("Tags") || labels[i].equals("Language")) {
                field.setPromptText("Comma separated values");
            }

            fields[i] = field;

            formGrid.add(label, 0, i);
            formGrid.add(field, 1, i);
        }

        // Image selection section
        Label imageLabel = new Label("Image:");
        imageLabel.setStyle(LABEL_STYLE);
        imageLabel.setMinWidth(80);

        Button chooseImageBtn = new Button("Choose Image");
        chooseImageBtn.setStyle(IMAGE_BUTTON_STYLE);
        chooseImageBtn.setOnMouseEntered(e -> chooseImageBtn.setStyle(IMAGE_BUTTON_HOVER_STYLE));
        chooseImageBtn.setOnMouseExited(e -> chooseImageBtn.setStyle(IMAGE_BUTTON_STYLE));

        // Image preview with placeholder border smaller on small screens
        double imageSize = windowHeight < 700 ? 80 : 100;
        double imageHeightFactor = windowHeight < 700 ? 120 : 150;

        StackPane imageContainer = new StackPane();
        imageContainer.setMinSize(imageSize, imageHeightFactor);
        imageContainer.setMaxSize(imageSize, imageHeightFactor);
        imageContainer.setStyle("-fx-border-color: #ced4da; -fx-border-width: 1px; -fx-border-style: dashed;");

        ImageView imagePreview = new ImageView();
        imagePreview.setFitWidth(imageSize);
        imagePreview.setFitHeight(imageHeightFactor);
        imagePreview.setPreserveRatio(true);

        imageContainer.getChildren().add(imagePreview);

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
                imageContainer.setStyle("-fx-border-color: transparent;");
            }
        });

        HBox imageRow = new HBox(15, chooseImageBtn, imageContainer);
        imageRow.setAlignment(Pos.CENTER_LEFT);
        formGrid.add(imageLabel, 0, labels.length);
        formGrid.add(imageRow, 1, labels.length);

        // Submit button adjust size based on screen
        Button submitBtn = new Button("Add Game");
        submitBtn.setStyle(BUTTON_STYLE);
        submitBtn.setPrefWidth(windowWidth < 600 ? 150 : 200);
        submitBtn.setOnMouseEntered(e -> submitBtn.setStyle(BUTTON_HOVER_STYLE));
        submitBtn.setOnMouseExited(e -> submitBtn.setStyle(BUTTON_STYLE));

        // Center the submit button
        HBox buttonBox = new HBox(submitBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 5, 0));

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
                
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.setStyle("-fx-background-color: white; -fx-border-color: #4CAF50; -fx-border-width: 2px;");

                alert.showAndWait();
                addGameStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Error: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        // Add all components to the formWrapper
        formWrapper.getChildren().addAll(headerBox, separator, formGrid, buttonBox);

        // Center the form in the layout
        mainLayout.setCenter(formWrapper);

        // Create scrollable container
        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // Create scene with adaptive dimensions
        Scene scene = new Scene(scrollPane, windowWidth, windowHeight);
        addGameStage.setScene(scene);
        addGameStage.setTitle("Add New Game");

        // Center the window on screen
        addGameStage.setX((screenWidth - windowWidth) / 2);
        addGameStage.setY((screenHeight - windowHeight) / 2);

        // Add window resize listener to maintain aspect ratio and visibility
        addGameStage.setResizable(true);
        addGameStage.setMinWidth(500);
        addGameStage.setMinHeight(600);

        addGameStage.show();
    }

    public void ichangedsmthGithubPushit(){
        int nothing=0;
        return;
    }
}