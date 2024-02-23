package br.com.ifce.controller;

import br.com.ifce.model.ChatMessage;
import br.com.ifce.model.GameState;
import br.com.ifce.model.Message;
import br.com.ifce.model.Movement;
import br.com.ifce.service.IntegrationService;
import br.com.ifce.service.MessageListener;
import br.com.ifce.view.MainView;

import java.time.LocalTime;

public class MainViewController implements MessageListener {

    private final IntegrationService service;

    private MainView view;

    public MainViewController(IntegrationService service) {
        this.service = service;
        this.service.setMessageListener(this);
    }

    @Override
    public void onMessage(Message<?> message) {
        switch (message.getType()) {
            case START_GAME -> this.startGame((GameState) message.getPayload());
            case HIT -> this.view.updateBoard(((GameState) message.getPayload()).getBoard());
            case INVALID_MOVEMENT -> this.view.undoMove((Movement) message.getPayload());
            case PLAYER_REGISTERED -> this.service.setPlayerKey((String) message.getPayload());
            case CHANGE_TURN -> this.changeTurn((String) message.getPayload());
            case GAME_OVER, GAME_COMPLETE -> this.setCurrentPlayer(null);
        }
    }

    void startGame(GameState state) {
        this.view = new MainView();
        this.view.setTitle(this.service.getPlayerKey());
        this.view.renderStatusPane();
        this.view.renderBoardPanel(state.getBoard());
        this.view.renderChatPanel();
        this.view.show();
        this.setCurrentPlayer(state.getCurrentPlayer());

        this.view.addChatMessage(new ChatMessage("PLAYER 1", LocalTime.now(), "Hello!"));
        this.view.addChatMessage(new ChatMessage("PLAYER 2", LocalTime.now(), "How are you doing?"));
        this.view.addChatMessage(new ChatMessage("PLAYER 1", LocalTime.now(), "I'm doing fine, what about you?"));
        this.view.addChatMessage(new ChatMessage("PLAYER 1", LocalTime.now(), "Hello!"));
        this.view.addChatMessage(new ChatMessage("PLAYER 2", LocalTime.now(), "How are you doing?"));
        this.view.addChatMessage(new ChatMessage("PLAYER 1", LocalTime.now(), "I'm doing fine, what about you?"));
        this.view.addChatMessage(new ChatMessage("PLAYER 1", LocalTime.now(), "Hello!"));
        this.view.addChatMessage(new ChatMessage("PLAYER 2", LocalTime.now(), "How are you doing?"));
        this.view.addChatMessage(new ChatMessage("PLAYER 1", LocalTime.now(), "I'm doing fine, what about you?"));
    }

    void changeTurn(String player) {
        this.setCurrentPlayer(player);
    }

    void setCurrentPlayer(String player) {
        this.service.setCurrentPlayer(player);
        if (this.service.isPlayerTurn()) {
            this.view.setStatusText("Sua vez!");
        } else {
            this.view.setStatusText("Aguarde sua vez...");
        }
    }
}
