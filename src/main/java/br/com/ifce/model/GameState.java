package br.com.ifce.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class GameState implements Serializable {
    @Getter
    private int[][] board;

    @Getter
    private String currentPlayer;
}
