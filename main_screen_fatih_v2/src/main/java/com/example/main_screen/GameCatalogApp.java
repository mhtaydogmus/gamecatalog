package com.example.main_screen;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GameCatalogApp extends Application {

    private GridPane gridLayout;
    private List<Game> allGames;

    @Override
    public void start(Stage stage) {
        // root layout
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");
        root.setId("top_label");

        // top bar
        HBox topBar = new HBox(20);
        topBar.setId("top_bar");
        topBar.setMaxWidth(Double.MAX_VALUE);
        topBar.setAlignment(Pos.CENTER);

        // logo section
        VBox leftBox = new VBox(10);
        leftBox.setId("top_left");
        leftBox.setAlignment(Pos.CENTER_LEFT);
        ImageView logoImage = new ImageView(new Image("file:/img/test.jpg"));
        logoImage.setFitWidth(100);
        logoImage.setFitHeight(100);
        leftBox.getChildren().add(logoImage);

        // title section
        VBox midBox = new VBox();
        midBox.setId("top_mid");
        midBox.setAlignment(Pos.CENTER);
        Label appName = new Label("GAME APP");
        midBox.getChildren().add(appName);
        HBox.setHgrow(midBox, Priority.ALWAYS);

        // search section
        VBox rightBox = new VBox(10);
        rightBox.setId("top_right");
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        TextField searchBox = new TextField();
        searchBox.setPromptText("Search...");
        searchBox.setPrefWidth(150);

        VBox searchContainer = new VBox(10);
        searchContainer.setId("search_container");
        searchContainer.setAlignment(Pos.TOP_LEFT);
        Label searchBy = new Label("Search By");

        ComboBox<String> dropdownMenu = new ComboBox<>();
        dropdownMenu.setId("dropdown_menu");
        dropdownMenu.setItems(FXCollections.observableArrayList(
                "Title", "Tags", "Release Year", "Genre", "Developer", "Publisher",
                "SteamID", "Release Year", "Play Time", "Format", "Rating", "Platforms",
                "Translators", "Languages"));
        dropdownMenu.getSelectionModel().select("Title");

        Button addJsonButton = new Button("Add JSON");
        addJsonButton.setOnAction(e -> openFileChooser(stage));

        Button addGameButton = new Button("Add Game");
        addGameButton.setOnAction(e -> openAddGameWindow());

        searchContainer.getChildren().addAll(searchBy, dropdownMenu, addJsonButton, addGameButton);
        rightBox.getChildren().addAll(searchBox, searchContainer);

        topBar.getChildren().addAll(leftBox, midBox, rightBox);

        // separator
        Region separator = new Region();
        separator.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(5);
        separator.setEffect(shadow);

        // gridPane for games
        gridLayout = new GridPane();
        gridLayout.setHgap(100);
        gridLayout.setVgap(30);
        gridLayout.setAlignment(Pos.CENTER);
        gridLayout.setStyle("-fx-background-color: transparent;");

        // scrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        // load and display games
        GameLoader gameLoader = new GameLoader();
        allGames = gameLoader.loadGames("/json/games.json");
        displayGames(allGames);

        // real time search filter
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            String query = newValue.toLowerCase().trim();
            String selectedCriterion = dropdownMenu.getSelectionModel().getSelectedItem();
            List<Game> filteredGames = allGames.stream()
                    .filter(game -> {
                        switch (selectedCriterion) {
                            case "Genre":
                                return game.getGenre().toLowerCase().contains(query);
                            case "Developer":
                                return game.getDeveloper().toLowerCase().contains(query);
                            case "Publisher":
                                return game.getPublisher().toLowerCase().contains(query);
                            case "SteamID":
                                return game.getSteamid().toLowerCase().contains(query);
                            case "Release Year":
                                return game.getReleaseYear().toLowerCase().contains(query);
                            case "Play Time":
                                return game.getPlaytime().toLowerCase().contains(query);
                            case "Format":
                                return game.getFormat().toLowerCase().contains(query);
                            case "Rating":
                                return String.valueOf(game.getRating()).contains(query);
                            case "Platforms":
                                return game.getPlatforms().stream()
                                        .anyMatch(platform -> platform.toLowerCase().contains(query));
                            case "Translators":
                                return game.getTranslators().stream()
                                        .anyMatch(translator -> translator.toLowerCase().contains(query));
                            case "Languages":
                                return game.getLanguage().stream()
                                        .anyMatch(language -> language.toLowerCase().contains(query));
                            case "Tags":
                                return game.getTags().stream()
                                        .anyMatch(tag -> tag.toLowerCase().contains(query));
                            case "Title":
                            default:
                                return game.getTitle().toLowerCase().contains(query);
                        }
                    })
                    .collect(Collectors.toList());

            displayGames(filteredGames);
        });

        // adding to root
        root.getChildren().addAll(topBar, separator, scrollPane);

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Game Catalog App");
        stage.show();
    }

    private void displayGames(List<Game> gamesToShow) {
        gridLayout.getChildren().clear();
        int columns = 3;
        int row = 0, col = 0;
        for (Game game : gamesToShow) {
            GameLabel gameLabel = new GameLabel(game);
            VBox itemBox = gameLabel.createItem();
            gridLayout.add(itemBox, col, row);

            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }
    }

    private void openFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Game> newGames = objectMapper.readValue(selectedFile, new TypeReference<List<Game>>() {});
                allGames.addAll(newGames);
                objectMapper.writeValue(new File("src/main/resources/json/games.json"), allGames);
                displayGames(allGames);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openAddGameWindow() {
        AddGame addGameApp = new AddGame(this);
        Stage addGameStage = new Stage();
        addGameApp.start(addGameStage);
    }

    // Method to add the new game to the catalog and refresh the UI
    public void addNewGame(Game newGame) {
        allGames.add(newGame);  // Add the new game to the list
        displayGames(allGames); // Refresh the displayed list
    }

    public static void main(String[] args) {
        launch();
    }
}
