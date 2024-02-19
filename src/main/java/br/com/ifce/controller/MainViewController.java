package br.com.ifce.controller;

import br.com.ifce.model.GameState;
import br.com.ifce.model.Message;
import br.com.ifce.model.Movement;
import br.com.ifce.service.IntegrationService;
import br.com.ifce.service.MessageListener;
import br.com.ifce.view.MainView;

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
        }
    }

    void startGame(GameState state) {
        this.view = new MainView();
        view.renderBoardPanel(state.getBoard());
        view.show();
    }
}
