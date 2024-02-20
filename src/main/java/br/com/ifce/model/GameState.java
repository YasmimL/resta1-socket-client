package br.com.ifce.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

public record GameState(
        @Getter int[][] board,
        @Getter String currentPlayer,
        @Getter List<Player> gameScore) implements Serializable {
}
