package com.example.main_screen;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;


public class HelloController {
    @FXML
    private ImageView logoImage;
    @FXML
    private ComboBox<String> dropdown_menu;
    @FXML
    private GridPane grid_layout;

    @FXML
    public void initialize() {
        Image image = new Image("/img/test.jpg");
        logoImage.setImage(image);

        //search by options
        dropdown_menu.setItems(FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));
        dropdown_menu.getSelectionModel().selectFirst();

        //grid

        int columns = 4;
        int row = 0, col = 0;
        for(int i=1; i<20; i++){ //20 is placeholder num for testing
            VBox itembox= new VBox();
            itembox.getStyleClass().add("grid-item");

            Label label =new Label("Item "+i);
            Button button = new Button("placeholder "+i);

            itembox.getChildren().addAll(label, button);
            grid_layout.add(itembox, col, row);

            col++;
            if(col==columns){
                col=0;
                row++;
            }
        }
    }
}