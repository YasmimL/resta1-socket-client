package br.com.ifce.model;

import br.com.ifce.model.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class Message<T> implements Serializable {
    @Getter
    private MessageType type;

    @Getter
    private T payload;
}
