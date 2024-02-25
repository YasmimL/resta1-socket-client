package br.com.ifce.service;

import br.com.ifce.model.GameState;
import br.com.ifce.model.Message;
import br.com.ifce.model.enums.MessageType;

import java.util.Collections;
import java.util.stream.IntStream;

public class TestIntegrationService implements IntegrationService {

    @Override
    public String getPlayerKey() {
        return "PLAYER 1";
    }

    @Override
    public void setPlayerKey(String playerKey) {

    }

    @Override
    public String getCurrentPlayer() {
        return "PLAYER 1";
    }

    @Override
    public void setCurrentPlayer(String currentPlayer) {

    }

    @Override
    public boolean isPlayerTurn() {
        return true;
    }

    @Override
    public void send(Message<?> message) {

    }

    @Override
    public void setMessageListener(MessageListener listener) {
        listener.onMessage(new Message<>(
                MessageType.START_GAME,
                this.getGameState()
        ));
    }

    @Override
    public void close() {

    }

    private GameState getGameState() {
        return new GameState(
                this.getBoard(),
                "PLAYER 1",
                Collections.emptyList()
        );
    }

    private int[][] getBoard() {
        final int rows = 7;
        final int columns = 7;
        final int[][] board = new int[rows][columns];
        final var middleSpot = 3;
        final var invalidSpots = new int[]{0, 1, 5, 6};

        IntStream.range(0, rows).forEach(row ->
                IntStream.range(0, columns).forEach(column -> board[row][column] = 1)
        );

        board[middleSpot][middleSpot] = 0;

        for (int row : invalidSpots) {
            for (int column : invalidSpots) {
                board[row][column] = -1;
            }
        }

        return board;
    }
}
