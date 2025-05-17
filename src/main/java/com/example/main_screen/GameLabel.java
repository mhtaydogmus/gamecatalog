package com.example.main_screen;

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class GameLabel {
    private static final Logger logger = Logger.getLogger(GameLabel.class.getName());

    public GameLabel() {

    }

    public static void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("game_catalog.log", true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
        ImageView gameImageView;
        try {
            Path fullPath = Paths.get(System.getProperty("user.home"), "GameCatalogApp", game.getImage());
            Image image = new Image(fullPath.toUri().toString(), 150, 225, true, true);
            if (image.isError()) throw new Exception();
            gameImageView = new ImageView(image);
        } catch (Exception e) {
            InputStream fallbackStream = getClass().getResourceAsStream("/images/notfound2.png");
            if (fallbackStream != null) {
                Image fallbackImage = new Image(fallbackStream, 150, 225, true, true);
                gameImageView = new ImageView(fallbackImage);
            } else {
                System.err.println("Could not load fallback image from resources: /images/notfound2.png");
                gameImageView = new ImageView();
            }
            System.err.println("Failed to load game image for: " + game.getTitle());
        }


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

        // description
        String tagsDescription = game.getDescription(game.getTags());
        Label gameDescription = new Label(tagsDescription);
        gameDescription.getStyleClass().add("game_description");
        gameDescription.setMaxWidth(175);
        gameDescription.setWrapText(true);
        gameDescription.setMaxHeight(60);
        gameDescription.setPadding(new Insets(0, 0, 0, 10));

        // add to vbox
        itemBox.getChildren().addAll(button, gameTitle, gameDescription, selectBox);

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

        Label titleLabel = new Label("Title: " + game.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        String imgSource = game.getFullImagePath();
        Image cover;

        try {
            if (imgSource.startsWith("file:") || imgSource.startsWith("http")) {
                cover = new Image(imgSource, 200, 300, true, true);
                if (cover.isError()) throw new IOException("Failed to load image from URL: " + imgSource);
            } else {
                File imageFile = new File(System.getProperty("user.home"), "GameCatalogApp/images/" + imgSource);
                if (imageFile.exists()) {
                    cover = new Image(imageFile.toURI().toString(), 200, 300, true, true);
                    if (cover.isError()) throw new IOException("Image exists but failed to load: " + imageFile.getAbsolutePath());
                } else {
                    throw new FileNotFoundException("Image file not found: " + imageFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            logger.severe("Image load failed, using fallback: " + e.getMessage());
            File fallback = new File(System.getProperty("user.home"), "GameCatalogApp/images/notfound2.png");
            cover = new Image(fallback.toURI().toString(), 200, 300, true, true);
        }



        ImageView iv = new ImageView(cover);

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

        Button edit = new Button("Edit");
        edit.setOnAction((ActionEvent e) -> {
                editScreen(newStage, game);
        });

        /*
        edit.setOnAction(e -> {
            try {
                editScreen(new Stage(), game);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
*/

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

    public void editScreen(Stage stage, Game game) {
        try {
            logger.info("Opening edit screen for game: " + game.getTitle());
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

            String imagePath = System.getProperty("user.home") + File.separator + "GameCatalogApp" + game.getImage();
            ImageView imagePreview = new ImageView(new Image(new File(imagePath).toURI().toString(),
                    150, 200, true, true));
            imagePreview.setUserData(game.getImage());




            Button imageButton = new Button("Choose Image");
            imageButton.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Game Image");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
                File selectedFile = fileChooser.showOpenDialog(stage);

                if (selectedFile != null) {
                    try {
                        String userImagePath = System.getProperty("user.home") + File.separator + "GameCatalogApp" + File.separator + "images" + File.separator;

                        File appImageDir = new File(userImagePath);

                        logger.info("Selected File: " + selectedFile.getAbsolutePath());
                        logger.info("Target Directory: " + appImageDir.getAbsolutePath());

                        if (!appImageDir.exists()) {
                            boolean created = appImageDir.mkdirs();
                            logger.info("Directory created: " + created);
                        }

                        File copiedFile = new File(appImageDir, selectedFile.getName());
                        if (copiedFile.exists()) {
                            String relativePath = "/images/" + copiedFile.getName();
                            String absolutePath = System.getProperty("user.home") + File.separator + "GameCatalogApp" + relativePath;

                            imagePreview.setImage(new Image(new File(absolutePath).toURI().toString(), 150, 200, true, true));
                            imagePreview.setUserData(relativePath);
                            logger.info("Image copied successfully.");
                        }


                        logger.info("Copying to: " + copiedFile.getAbsolutePath());

                        java.nio.file.Files.copy(
                                selectedFile.toPath(),
                                copiedFile.toPath(),
                                java.nio.file.StandardCopyOption.REPLACE_EXISTING
                        );

                        if (copiedFile.exists()) {
                            String relativePath = "/images/" + copiedFile.getName();
                            String absolutePath = System.getProperty("user.home") + File.separator + "GameCatalogApp" + relativePath;

                            imagePreview.setImage(new Image(new File(absolutePath).toURI().toString(), 150, 200, true, true));
                            imagePreview.setUserData(relativePath);


                            logger.info("Image copied successfully.");
                        } else {
                            logger.severe("Error: Image file could not be copied.");
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("File Error");
                            alert.setHeaderText("An error occurred while copying the image.");
                            alert.setContentText("Please try again.");
                            alert.showAndWait();
                        }
                    } catch (IOException ex) {
                        logger.severe("IOException occurred: " + ex.getMessage());
                        ex.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("File Error");
                        alert.setHeaderText("An error occurred while copying the image.");
                        alert.setContentText("Please try again.");
                        alert.showAndWait();
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

                for (Game g : mainApp.getallGames()) {
                    if (g != game && steamIdText.equals(g.getSteamid())) {
                        errorLabel.setText("Steam ID already exists for another game.");
                        return;
                    }
                }


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

                if (steamIdText.isEmpty()) {
                    errorLabel.setText("Steam ID cannot be empty.");
                    return;
                }

                try {
                    String absoluteImagePath = System.getProperty("user.home") + File.separator + "GameCatalogApp" + image;
                    File imageFile = new File(absoluteImagePath);
                    if (!imageFile.exists()) {
                        errorLabel.setText("Selected image file not found.");
                        return;
                    }

                    Image testImage = new Image(imageFile.toURI().toString(), 10, 10, true, true);
                    if (testImage.isError()) {
                        errorLabel.setText("Image failed to load.");
                        return;
                    }


                    errorLabel.setText("");

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
            stage.show();
            logger.info("Edit screen shown successfully.");
        }
        catch (Exception e) {
            logger.severe("Error opening edit screen: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred while opening the edit screen.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    public void ichangedsmthGithubPushit(){
        int nothing=0;
        return;
    }
    public Game getGame() {
        return game;
    }
}

