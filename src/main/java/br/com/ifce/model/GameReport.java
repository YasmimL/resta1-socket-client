package br.com.ifce.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

public record GameReport(@Getter String winner, @Getter List<Player> finalScore) implements Serializable {
}
