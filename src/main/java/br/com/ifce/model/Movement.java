package br.com.ifce.model;

import lombok.Getter;

import java.io.Serializable;

public record Movement(@Getter int[] source, @Getter int[] target) implements Serializable {
}
