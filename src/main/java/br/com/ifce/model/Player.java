package br.com.ifce.model;

import lombok.Getter;

import java.io.Serializable;

public record Player(@Getter String name, @Getter int score) implements Serializable {
}
