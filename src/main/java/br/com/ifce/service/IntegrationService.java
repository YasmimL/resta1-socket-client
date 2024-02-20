package br.com.ifce.service;

import br.com.ifce.model.Message;

public interface IntegrationService {
    String getPlayerKey();

    void setPlayerKey(String playerKey);

    String getCurrentPlayer();

    void setCurrentPlayer(String currentPlayer);

    boolean isPlayerTurn();

    void send(Message<?> message);

    void setMessageListener(MessageListener listener);

    void close();
}
