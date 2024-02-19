package br.com.ifce.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class Movement implements Serializable {
    @Getter
    private int[] source;

    @Getter
    private int[] target;
}
