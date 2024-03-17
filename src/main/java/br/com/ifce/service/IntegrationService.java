package br.com.ifce.service;

import br.com.ifce.model.Message;
import br.com.ifce.network.rmi.RMIIntegrationService;

public interface IntegrationService {
    static IntegrationService getInstance() {
        return RMIIntegrationService.getInstance();
//        return SocketIntegrationService.getInstance();
//        return new TestIntegrationService();
    }

    default boolean isPlayerTurn() {
        return this.getPlayerKey().equals(this.getCurrentPlayer());
    }

    String getPlayerKey();

    void setPlayerKey(String playerKey);

    String getCurrentPlayer();

    void setCurrentPlayer(String currentPlayer);

    void send(Message<?> message);

    void setListener(MessageListener listener);

    void close();

    boolean isGameFinished();

    void setGameFinished(boolean gameFinished);
}
