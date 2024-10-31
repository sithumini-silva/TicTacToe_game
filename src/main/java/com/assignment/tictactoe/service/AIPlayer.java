package com.assignment.tictactoe.service;

import java.util.Random;

public class AIPlayer extends Player {

    public AIPlayer(BoardImpl board) {
        super(board);
    }

    // Makes a move for the AI player
    @Override
    public void move(int row, int col) {
        if (board.isLegalMove(row, col)) {
            board.updateMove(row, col, Piece.O); // update after AI
        }
    }

    //AI player using the minimax algorithm
    public void findBestMove() {
        int bestValue = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestColumn = -1;
        Piece[][] pieces = board.getPieces(); // Get the current state

        // Iterate through all cells of the board
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                // Check for empty cells to make a move
                if (pieces[i][j] == Piece.EMPTY) {
                    pieces[i][j] = Piece.O; //AI is making a move
                    int moveValue = minimax(pieces, 0, false);
                    pieces[i][j] = Piece.EMPTY; // Undo the move

                    // Update best move if the current move has a better value
                    if (moveValue > bestValue) {
                        bestRow = i;
                        bestColumn = j;
                        bestValue = moveValue;
                    }
                }
            }
        }

        // If a best move was found, make that move
        if (bestRow != -1 && bestColumn != -1) {
            move(bestRow, bestColumn);
        }
    }

    // Minimax algorithm implementation to evaluate the board state
    private int minimax(Piece[][] pieces, int depth, boolean isMaximize) {
        Winner winner = board.checkWinner();
        // If there is a winner, return the score based on the winning piece
        if (winner != null) {
            if (winner.getWinningPiece() == Piece.O) {
                return 10 - depth;
            } else if (winner.getWinningPiece() == Piece.X) {
                return depth - 10;
            }
        }
        // If the board is full, return a score of 0 (tie)
        if (board.isFull()) {
            return 0;
        }

        if (isMaximize) {
            int bestValue = Integer.MIN_VALUE;

            // Iterate through the board to find the best possible move
            for (int i = 0; i < pieces.length; i++) {
                for (int j = 0; j < pieces[i].length; j++) {
                    if (pieces[i][j] == Piece.EMPTY) {
                        pieces[i][j] = Piece.O;
                        bestValue = Math.max(bestValue, minimax(pieces, depth + 1, false));
                        pieces[i][j] = Piece.EMPTY;
                    }
                }
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;

            // Iterate through the board to minimize the AI's score
            for (int i = 0; i < pieces.length; i++) {
                for (int j = 0; j < pieces[i].length; j++) {
                    if (pieces[i][j] == Piece.EMPTY) {
                        pieces[i][j] = Piece.X;
                        bestValue = Math.min(bestValue, minimax(pieces, depth + 1, true));
                        pieces[i][j] = Piece.EMPTY;
                    }
                }
            }
            return bestValue;
        }
    }
}
