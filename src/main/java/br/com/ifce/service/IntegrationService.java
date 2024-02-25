package br.com.ifce.service;

import br.com.ifce.model.Message;
import br.com.ifce.network.SocketIntegrationService;

public interface IntegrationService {
    static IntegrationService getInstance() {
        return SocketIntegrationService.getInstance();
//        return new TestIntegrationService();
    }

    String getPlayerKey();

    void setPlayerKey(String playerKey);

    String getCurrentPlayer();

    void setCurrentPlayer(String currentPlayer);

    boolean isPlayerTurn();

    void send(Message<?> message);

    void setMessageListener(MessageListener listener);

    void close();
}
