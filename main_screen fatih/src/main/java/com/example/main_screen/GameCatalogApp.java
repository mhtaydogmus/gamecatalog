package com.example.main_screen;

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
import javafx.stage.Stage;

import java.util.List;

public class GameCatalogApp extends Application {

    @Override
    public void start(Stage stage) {
        // root
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");
        root.setId("top_label");

        // top bar
        HBox topBar = new HBox(20);
        topBar.setId("top_bar");
        topBar.setMaxWidth(Double.MAX_VALUE);
        topBar.setAlignment(Pos.CENTER);

        // left section (logo)
        VBox leftBox = new VBox(10);
        leftBox.setId("top_left");
        leftBox.setAlignment(Pos.CENTER_LEFT);
        ImageView logoImage = new ImageView(new Image("/img/test.jpg"));
        logoImage.setFitWidth(100);
        logoImage.setFitHeight(100);
        leftBox.getChildren().add(logoImage);

        // mid section (title)
        VBox midBox = new VBox();
        midBox.setId("top_mid");
        midBox.setAlignment(Pos.CENTER);
        Label appName = new Label("GAME APP");
        midBox.getChildren().add(appName);
        HBox.setHgrow(midBox, Priority.ALWAYS);

        // right section (search functions)
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
        dropdownMenu.setItems(FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));
        dropdownMenu.getSelectionModel().selectFirst();

        searchContainer.getChildren().addAll(searchBy, dropdownMenu);
        rightBox.getChildren().addAll(searchBox, searchContainer);

        topBar.getChildren().addAll(leftBox, midBox, rightBox);

        // separator line between top bar and grid
        Region separator = new Region();
        separator.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        //shadow
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(5);
        separator.setEffect(shadow);

        // grid layout
        GridPane gridLayout = new GridPane();
        gridLayout.setHgap(100);
        gridLayout.setVgap(30);
        gridLayout.setAlignment(Pos.CENTER);
        gridLayout.setStyle("-fx-background-color: transparent;");


        //int numberOfItems = 20;
        GameLoader gameLoader = new GameLoader();
        List<Game> games = gameLoader.loadGames("json/games.json");
        //System.out.println("Loaded games: " + games.size());

        if (games != null) {
            int columns = 3;
            int row = 0, col = 0;

            for (Game game : games) {
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

        //scroll function
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridLayout);
        scrollPane.setFitToWidth(true);  // ensuring it fits the width of the window
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);  // make vertical scroll bar always visible
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // no horizontal line
        scrollPane.setStyle("-fx-background-color: transparent;");

        // add to root
        root.getChildren().addAll(topBar, separator, gridLayout,scrollPane);


        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Game Catalog App");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
