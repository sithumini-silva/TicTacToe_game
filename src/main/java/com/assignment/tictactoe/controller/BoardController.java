package com.assignment.tictactoe.controller;

import com.assignment.tictactoe.service.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;

public class BoardController implements BoardUI {

    @FXML
    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;

    @FXML
    private GridPane Grideplay;

    private BoardImpl board;  // Represents the game board
    private HumanPlayer humanPlayer; // Represents the human player
    private AIPlayer aiPlayer; // Represents the AI player

    public BoardController() {
        // Initialize game objects
        board = new BoardImpl();
        aiPlayer = new AIPlayer(board);
        humanPlayer = new HumanPlayer(board);
    }

    // Handles button click events on the game
    @FXML
    void clickOnAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String id = clickedButton.getId();

        // Extract row and column indices from the button ID
        int row = Character.getNumericValue(id.charAt(3));
        int col = Character.getNumericValue(id.charAt(4));

        // Human player
        humanPlayer.move(row, col);
        updateUI(); // Update the UI to reflect the move

        // Check for a winner or a tie
        if (board.checkWinner() != null) {
            notifyWinner(board.checkWinner().getWinningPiece());
        } else if (board.isFull()) {
            showAlert("It's a draw! Both players gave it their all.");
        } else {
            // AI player
            aiPlayer.findBestMove();
            updateUI(); //after AI

            // Check for a winner or a tie again
            if (board.checkWinner() != null) {
                notifyWinner(board.checkWinner().getWinningPiece());
            } else if (board.isFull()) {
                showAlert("It's a draw! Both players gave it their all.");
            }
        }
    }

    // Updates the UI to reflect the current state of the game board
    private void updateUI() {
        for (int i = 0; i < board.getPieces().length; i++) {
            for (int j = 0; j < board.getPieces()[i].length; j++) {
                update(i, j, board.getPieces()[i][j]);
            }
        }
    }

    // Displays an alert message - game is over
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game is Over!");
        alert.setContentText(msg);

        // Define buttons for the alert
        ButtonType playAgain = new ButtonType("Play Again");
        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(playAgain, close);

        // Handle button actions
        alert.showAndWait().ifPresent(response -> {
            if (response == playAgain) {
                resetGame();
            } else if (response == close) {
                Platform.exit();
            }
        });
    }

    // Resets the game board for new game
    private void resetGame() {
        board.initializeBoard();

        // Clear all button
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = (Button) Grideplay.lookup("#btn" + i + j);
                button.setText(""); // Clear button text
            }
        }
    }

    // Displays an error alert
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setContentText(msg);

        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(ok);

        alert.showAndWait();
    }

    @Override
    public void update(int row, int col, Piece piece) {
        String buttonId = "#btn" + row + col;
        Button button = (Button) Grideplay.lookup(buttonId);
        if (button != null) {
            if (piece == Piece.X) {
                button.setText("X");
            } else if (piece == Piece.O) {
                button.setText("O");
            } else {
                button.setText("");
            }
        }
    }

    @Override
    public void notifyWinner(Piece winner) {
        if (winner != Piece.EMPTY) {
            // Construct the winner
            String winnerMsg = (winner == Piece.X) ? "Player X wins!" : "Player O wins!";
            showAlert(winnerMsg);
        }
    }
}
