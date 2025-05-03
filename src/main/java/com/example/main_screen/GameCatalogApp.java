package com.example.main_screen;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import javafx.application.Application;
import javafx.application.Platform;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;





public class GameCatalogApp extends Application {

    private GridPane gridLayout;
    private List<Game> allGames;
    Color backgroundColor=Color.rgb(255, 255, 255);
    Color lineColor=Color.BLACK;
    private boolean isNightMode = false;
    private Region separator;

    private List<String> selectedTags = new ArrayList<>();
    private List<GameLabel> displayedGameLabels = new ArrayList<>();


    @Override
    public void start(Stage stage) {
        // root layout
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
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
        leftBox.setMaxWidth(Region.USE_PREF_SIZE);
        HBox.setHgrow(leftBox, Priority.NEVER);
        leftBox.setMaxWidth(250);

        ImageView logoImage = new ImageView(new Image(getClass().getResource("/img/test.jpg").toString()));
        logoImage.setFitWidth(100);
        logoImage.setFitHeight(100);

        Button toggleButton =  new Button("🌙");
        toggleButton.setOnAction(event -> toggleNightMode(stage, root));

        toggleButton.setOnAction(e -> {
            toggleNightMode(stage, root);
            if (!isNightMode) {
                toggleButton.setText("🌙");
            } else {
                toggleButton.setText("☀️");
            }
        });
        toggleButton.setStyle(
                "-fx-font-size: 10px;" +
                        "-fx-min-width: 30px;" +
                        "-fx-min-height: 35px;" +
                        "-fx-max-width: 35px;" +
                        "-fx-max-height: 35px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-background-color: #eeeeee;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-cursor: hand;"
        );
        Button helpButton =  new Button("HELP");
        helpButton.setStyle(
                "-fx-font-size: 8px;" +
                        "-fx-min-width: 30px;" +
                        "-fx-min-height: 35px;" +
                        "-fx-max-width: 35px;" +
                        "-fx-max-height: 35px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-background-color: #eeeeee;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-cursor: hand;");

        helpButton.setOnAction(event -> {
            System.out.println("help clicked");
            showHelpDialog();
        });


        HBox insideLeftBox=new HBox(35);
        insideLeftBox.getChildren().addAll(toggleButton,helpButton);

        leftBox.getChildren().addAll(logoImage,insideLeftBox);
        // title section
        VBox midBox = new VBox();
        midBox.setId("top_mid");
        midBox.setAlignment(Pos.CENTER);
        Label appName = new Label("GAME APP");
        Button exportBtn = new Button("Export Selected Games");

        exportBtn.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-max-width: 300px;" +
                        "-fx-max-height: 80px;" +
                        "-fx-background-color: #eeeeee;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-cursor: hand;" +
                        "-fx-margin: 0 0 0 10px;"
        );



        exportBtn.setOnAction(e -> exportSelectedGames());
        midBox.getChildren().addAll(appName,exportBtn);

        midBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(midBox, Priority.ALWAYS);
        appName.setPadding(new Insets(0, 80, 0, 0));

        // search section
        VBox rightBox = new VBox(10);
        rightBox.setId("top_right");
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        rightBox.setMaxWidth(Region.USE_PREF_SIZE);
        HBox.setHgrow(rightBox, Priority.NEVER);

        TextField searchBox = new TextField();
        searchBox.setPromptText("Search...");
        searchBox.setPrefWidth(150);

        VBox searchContainer = new VBox(10);
        searchContainer.setId("search_container");
        searchContainer.setAlignment(Pos.TOP_LEFT);

        HBox searchContainerBtns= new HBox(10);
        searchContainerBtns.setId("srcbtns");

        HBox srcAndFltr= new HBox(65);
        srcAndFltr.setId("srcAndFltr");

        HBox addBtns=new HBox(10);
        addBtns.setId("addBtns");

        Label searchBy = new Label("Search By");
        searchBy.setId("src_by");
        Label filterBy = new Label("Filter By");
        filterBy.setId("fltr_by");


        ComboBox<String> dropdownMenu = new ComboBox<>();
        dropdownMenu.setId("dropdown_menu");
        dropdownMenu.setItems(FXCollections.observableArrayList("Title","Tags", "Genre","Developer","Publisher"
                ,"SteamID","Release Year","Play Time","Format","Rating","Platforms","Translators","Languages"));
        dropdownMenu.getSelectionModel().select("Title");

        //filter func btn
        Button filterTagsButton = new Button("Filter by Tags");
        filterTagsButton.setId("filterTagsButton");

        filterTagsButton.setOnAction(e -> {
            System.out.println("Total games after refresh: " + allGames.size());
            List<String> allTags = allGames.stream()
                    .flatMap(game -> game.getTags().stream())
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            Dialog<List<String>> dialog = new Dialog<>();
            dialog.setTitle("Select Tags");
            dialog.setHeaderText("Choose tags to filter games");

            ButtonType applyButtonType = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);

            VBox tagBox = new VBox(8);
            tagBox.setPadding(new Insets(10));

            List<CheckBox> checkBoxes = new ArrayList<>();
            for (String tag : allTags) {
                CheckBox cb = new CheckBox(tag);
                if (selectedTags.contains(tag)) {
                    cb.setSelected(true);
                }
                checkBoxes.add(cb);
            }
            tagBox.getChildren().addAll(checkBoxes);

            ScrollPane scrollPane = new ScrollPane(tagBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(250);

            dialog.getDialogPane().setPrefWidth(350);
            dialog.getDialogPane().setContent(scrollPane);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == applyButtonType) {
                    return checkBoxes.stream()
                            .filter(CheckBox::isSelected)
                            .map(CheckBox::getText)
                            .collect(Collectors.toList());
                }
                return null;
            });

            dialog.showAndWait().ifPresent(result -> {
                selectedTags = result;
                applyFilters(searchBox.getText().trim(), dropdownMenu.getSelectionModel().getSelectedItem());
            });
        });

        Button addManualBtn = new Button("Add Manually");
        addManualBtn.setOnAction(e -> openAddGameForm());
        addManualBtn.setId("addManuelBtn");

        //add json
        Button addJsonButton = new Button("Add JSON");
        addJsonButton.getStyleClass().add("add_json_button");
        addJsonButton.setOnAction(e -> openFileChooser(stage));
        rightBox.getChildren().add(addJsonButton);


        searchContainerBtns.getChildren().addAll(dropdownMenu,filterTagsButton);
        srcAndFltr.getChildren().addAll(searchBy,filterBy);
        addBtns.getChildren().addAll(addManualBtn,addJsonButton);
        searchContainer.getChildren().addAll(srcAndFltr,searchContainerBtns,addBtns);
        rightBox.getChildren().addAll(searchBox, searchContainer);

        topBar.getChildren().addAll(leftBox, midBox, rightBox);
        rightBox.setMaxWidth(250);

        //responsive topbar
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        HBox.setHgrow(midBox, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.ALWAYS);


        // separator
        separator = new Region();

        // gridPane for games
        gridLayout = new GridPane();
        gridLayout.setHgap(100);
        gridLayout.setVgap(30);
        gridLayout.setAlignment(Pos.CENTER);


        // scrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");


        // load and display games
        GameLoader gameLoader = new GameLoader();
        allGames = GameLoader.loadGames();
        displayGames(allGames);

        // real time search filter its function goes to filter method on bottom
        searchBox.textProperty().addListener((obs, oldVal, newVal) ->
                applyFilters(newVal.trim(), dropdownMenu.getSelectionModel().getSelectedItem())
        );
        dropdownMenu.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
                applyFilters(searchBox.getText().trim(), newVal)
        );


        //seperator and gridbackgorund fix
        gridLayout.setStyle("-fx-background-color: " + toHexString(backgroundColor) + ";");
        separator.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        DropShadow initialShadow = new DropShadow();
        initialShadow.setOffsetX(2);
        initialShadow.setOffsetY(2);
        initialShadow.setRadius(5);
        initialShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        separator.setEffect(initialShadow);



        // adding to root
        root.getChildren().addAll(topBar, separator, scrollPane);

        Scene scene = new Scene(root, 1024, 768);
        //stage.setFullScreen(true); uncomment this to make fullscreen when opened


        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            displayGames(allGames);
        });



        scene.getStylesheets().add(getClass().getResource("/light.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Game Catalog App");
        stage.setMaximized(true);
        stage.show();
    }


    private void openAddGameForm() {
        Stage addGameStage = new Stage();
        AddGame addGame = new AddGame(this);
        addGame.start(addGameStage);
    }

    private void showHelpDialog() {
        List<String> imagePaths = List.of(
                "/img/help/step1.png",
                "/img/help/step2.png",
                "/img/help/step3.png",
                "/img/help/step4.png",
                "/img/help/step5.png",
                "/img/help/step6.png",
                "/img/help/step7.png",
                "/img/help/step8.png",
                "/img/help/step9.png"
        );

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("User Manual");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(800);
        imageView.setFitHeight(600);
        imageView.setPreserveRatio(true);

        int[] currentIndex = {0};

        // Ensure the image is loaded correctly from resources
        imageView.setImage(new Image(getClass().getResource(imagePaths.get(currentIndex[0])).toString()));

        Button prevBtn = new Button("⟵ Previous");
        Button nextBtn = new Button("Next ⟶");

        prevBtn.setOnAction(e -> {
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                imageView.setImage(new Image(getClass().getResource(imagePaths.get(currentIndex[0])).toString()));
            }
        });

        nextBtn.setOnAction(e -> {
            if (currentIndex[0] < imagePaths.size() - 1) {
                currentIndex[0]++;
                imageView.setImage(new Image(getClass().getResource(imagePaths.get(currentIndex[0])).toString()));
            }
        });

        HBox navButtons = new HBox(10, prevBtn, nextBtn);
        navButtons.setAlignment(Pos.CENTER);

        VBox content = new VBox(20, imageView, navButtons);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));

        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }


    private void applyFilters(String query, String criterion) {
        String lowerQuery = query.toLowerCase();
        List<Game> filtered = allGames.stream()
                .filter(game -> {
                    boolean matchesSearch;
                    switch (criterion) {
                        case "Genre":
                            matchesSearch = game.getGenre().toLowerCase().contains(lowerQuery);
                            break;
                        case "Developer":
                            matchesSearch = game.getDeveloper().toLowerCase().contains(lowerQuery);
                            break;
                        case "Publisher":
                            matchesSearch = game.getPublisher().toLowerCase().contains(lowerQuery);
                            break;
                        case "SteamID":
                            matchesSearch = game.getSteamid().toLowerCase().contains(lowerQuery);
                            break;
                        case "Release Year":
                            matchesSearch = game.getReleaseYear().toLowerCase().contains(lowerQuery);
                            break;
                        case "Play Time":
                            matchesSearch = game.getPlaytime().toLowerCase().contains(lowerQuery);
                            break;
                        case "Format":
                            matchesSearch = game.getFormat().toLowerCase().contains(lowerQuery);
                            break;
                        case "Rating":
                            matchesSearch = String.valueOf(game.getRating()).contains(lowerQuery);
                            break;
                        case "Platforms":
                            matchesSearch = game.getPlatforms().stream().anyMatch(p -> p.toLowerCase().contains(lowerQuery));
                            break;
                        case "Translators":
                            matchesSearch = game.getTranslators().stream().anyMatch(t -> t.toLowerCase().contains(lowerQuery));
                            break;
                        case "Languages":
                            matchesSearch = game.getLanguage().stream().anyMatch(l -> l.toLowerCase().contains(lowerQuery));
                            break;
                        case "Tags":
                            matchesSearch = game.getTags().stream().anyMatch(t -> t.toLowerCase().contains(lowerQuery));
                            break;
                        case "Title":
                        default:
                            matchesSearch = game.getTitle().toLowerCase().contains(lowerQuery);
                    }
                    ;

                    //its for get tag1 and tag2 seperately
                    // boolean matchesTags = selectedTags.isEmpty() || game.getTags().stream().anyMatch(selectedTags::contains);

                    //get tag1+tag2+tag3 etc
                    boolean matchesTags = selectedTags.isEmpty() || selectedTags.stream().allMatch(tag -> game.getTags().contains(tag));

                    return matchesSearch && matchesTags;
                })
                .collect(Collectors.toList());

        if (filtered.isEmpty() && !selectedTags.isEmpty()) {
            String tagsText = String.join(", ", selectedTags);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Results");
            alert.setHeaderText(null);
            alert.setContentText("There is no " + tagsText + " game in your library.");
            alert.showAndWait();

            selectedTags.clear();

            displayGames(allGames);
        } else {
            displayGames(filtered);
        }


    }

    private void displayGames(List<Game> gamesToShow) {
        Platform.runLater(() -> {
            gridLayout.getChildren().clear();

            double sceneWidth = gridLayout.getScene().getWidth();
            int columns = Math.max(1, (int) (sceneWidth / 300));

            gridLayout.getColumnConstraints().clear();
            for (int i = 0; i < columns; i++) {
                ColumnConstraints colConstraints = new ColumnConstraints();
                colConstraints.setPercentWidth(100.0 / columns);
                gridLayout.getColumnConstraints().add(colConstraints);
            }

            int row = 0, col = 0;

            displayedGameLabels.clear();
            for (Game game : gamesToShow) {
                GameLabel gameLabel = new GameLabel(game,this);
                displayedGameLabels.add(gameLabel);
                VBox itemBox = gameLabel.createItem();

                gridLayout.add(itemBox, col, row);

                col++;
                if (col == columns) {
                    col = 0;
                    row++;
                }
            }
        });
    }
    public void refreshCurrentView(){
        allGames.clear();
        //GameLoader gameLoader2 = new GameLoader();
        allGames = GameLoader.loadGames();
        System.out.println("Total games after refresh: " + allGames.size());
        displayGames(allGames);
    }

    private void openFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                //load current
                File gamesFile = new File("src/main/resources/json/games.json");
                List<Game> existingGames = new ArrayList<>();
                if (gamesFile.exists()) {
                    existingGames= objectMapper.readValue(gamesFile, new TypeReference<List<Game>>() {});
                }

                //load new
                List<Game> newGames= objectMapper.readValue(selectedFile, new TypeReference<List<Game>>() {});

                //append
                existingGames.addAll(newGames);

                //merge lists
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(gamesFile, existingGames);

                //update ui
                allGames.clear();
                allGames.addAll(existingGames);
                displayGames(allGames);

                //success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Adding game by JSON is successful!");
                successAlert.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
                //err message
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Adding JSON Failed");
                errorAlert.setContentText("This JSON file is not compatible or couldn't be added.");
                errorAlert.showAndWait();
            }
        }
    }

    private String toHexString(Color color) {
        int red= (int)(color.getRed() * 255);
        int green = (int)(color.getGreen() * 255);
        int blue = (int)(color.getBlue() * 255);

        return String.format("#%02X%02X%02X", red, green, blue);
    }

    private void toggleNightMode(Stage stage, VBox root) {
        Scene currentScene= stage.getScene();

        if (isNightMode) {
            //switch 2 light
            currentScene.getStylesheets().clear();
            currentScene.getStylesheets().add(getClass().getResource("/light.css").toExternalForm());
        } else {
            //switch 2 dark
            currentScene.getStylesheets().clear();
            currentScene.getStylesheets().add(getClass().getResource("/dark.css").toExternalForm());
        }

        isNightMode = !isNightMode;

        if (!isNightMode) {
            backgroundColor = Color.rgb(255, 255, 255);
            lineColor = Color.rgb(255, 255, 255);

        } else {
            backgroundColor = Color.rgb(34, 34, 34);
            lineColor = Color.rgb(255, 255, 255);
        }

        gridLayout.setStyle("-fx-background-color: " + toHexString(backgroundColor) + ";");

        separator.setStyle("-fx-border-color: " + (isNightMode ? "white" : "black") + "; -fx-border-width: 1px;");

        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        shadow.setRadius(5);
        shadow.setColor(isNightMode ? Color.WHITE : Color.rgb(0, 0, 0, 0.3));
        separator.setEffect(shadow);
    }

    //mehmetin code block
    public void addNewGame(Game newGame) {
        allGames.add(newGame);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            objectMapper.writeValue(new File("src/main/resources/json/games.json"), allGames);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save game to file.");
            alert.showAndWait();
        }
        displayGames(allGames);
    }

    public List getallGames(){
        //bugs sometimes with refresh
        return allGames;
    }

    private void exportSelectedGames() {
        //bug free dont touch
        List<Game> selectedGames = displayedGameLabels.stream()
                .filter(GameLabel::isSelected)
                .map(GameLabel::getGame)
                .collect(Collectors.toList());

        if (selectedGames.isEmpty()) {
            Alert alert= new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("No games were selected for export.");
            alert.showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Selected Games");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));
        File saveFile = fileChooser.showSaveDialog(null);

        if (saveFile != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(saveFile, selectedGames);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export Complete");
                alert.setHeaderText(null);
                alert.setContentText("Games exported successfully.");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) {
        launch();
    }
}
