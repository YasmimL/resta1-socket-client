package br.com.ifce.model;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalTime;

public record ChatMessage(
        @Getter String sender,
        @Getter LocalTime time,
        @Getter String text
) implements Serializable {
}
