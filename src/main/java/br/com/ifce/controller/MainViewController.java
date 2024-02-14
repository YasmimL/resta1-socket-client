package br.com.ifce.controller;

import br.com.ifce.model.GameState;
import br.com.ifce.model.Message;
import br.com.ifce.service.IntegrationService;
import br.com.ifce.service.MessageListener;

public class MainViewController implements MessageListener {

    private IntegrationService service;

    public MainViewController(IntegrationService service) {
        this.service = service;
        this.service.setMessageListener(this);
    }

    @Override
    public void onMessage(Message<?> message) {
        switch (message.getType()) {
            case START_GAME -> {
                this.startGame((GameState) message.getPayload());
                break;
            }
        }

//        for (int[] row : (int[][]) state.getBoard())
//            System.out.println(Arrays.toString(row));
//        System.out.println(state.getCurrentPlayer());
    }

    void startGame(GameState state) {

    }
}
