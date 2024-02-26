package br.com.ifce.controller;

import br.com.ifce.model.*;
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
            case RESTART_GAME -> this.restartGame((GameState) message.getPayload());
            case HIT -> this.view.updateBoard(((GameState) message.getPayload()).getBoard());
            case INVALID_MOVEMENT -> this.view.undoMove((Movement) message.getPayload());
            case PLAYER_REGISTERED -> this.service.setPlayerKey((String) message.getPayload());
            case CHANGE_TURN -> {
                this.changeTurn((String) message.getPayload());
                this.view.updateActionButtons();
            }
            case GIVE_UP -> this.handleGiveUp((String) message.getPayload());
            case CHAT -> this.view.addChatMessage((ChatMessage) message.getPayload());
            case GAME_OVER -> this.handleGameOver();
            case GAME_COMPLETE -> this.handleGameComplete((GameReport) message.getPayload());
        }
    }

    private void handleGameComplete(GameReport report) {
        if (this.service.getPlayerKey().equals(report.getWinner())) {
            this.view.setStatusText("Você ganhou!!!");
        } else {
            this.view.setStatusText("Você perdeu!");
        }
        this.finishGame();
    }

    private void handleGameOver() {
        this.view.setStatusText("Fim de jogo!");
        this.finishGame();
    }

    private void handleGiveUp(String player) {
        if (this.service.getPlayerKey().equals(player)) {
            this.view.setStatusText("Você desistiu!");
        } else {
            this.view.setStatusText("O outro jogador desistiu!");
        }
        this.finishGame();
    }

    private void finishGame() {
        this.service.setGameFinished(true);
        this.service.setCurrentPlayer(null);
        this.view.updateActionButtons();
    }

    public void startGame(GameState state) {
        this.service.setCurrentPlayer(state.getCurrentPlayer());
        this.view = new MainView();
        this.view.setTitle(this.service.getPlayerKey());
        this.view.renderStatusPane();
        this.view.renderBoardPanel(state.getBoard());
        this.view.renderSideMenu();
        this.view.show();
        this.setCurrentPlayer(this.service.getCurrentPlayer());
    }

    private void restartGame(GameState state) {
        this.service.setGameFinished(false);
        this.service.setCurrentPlayer(state.getCurrentPlayer());
        this.setCurrentPlayer(this.service.getCurrentPlayer());
        this.view.updateBoard(state.getBoard());
        this.view.updateActionButtons();
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
