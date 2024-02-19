package br.com.ifce.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Circle {
    @Getter
    private int row;

    @Getter
    private int column;

    @Getter
    @Setter
    private int value;
}
