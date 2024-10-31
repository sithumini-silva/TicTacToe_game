package com.assignment.tictactoe.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button btnplay;

    @FXML
    private AnchorPane rootNode;

    @FXML
    void actplay(ActionEvent event) {
        navigatTo();
    }

    private void navigatTo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BoardView.fxml"));
            AnchorPane gameScreen = loader.load();

            // Create a new Scene with the loaded game screen
            Scene gameScene = new Scene(gameScreen);

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) rootNode.getScene().getWindow();
            stage.setScene(gameScene);
            stage.setTitle("Tic Tac Toe");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
