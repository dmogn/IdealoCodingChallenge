package com.github.dmogn.idealo.model;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Robot position on the field.
 */
@Value
@AllArgsConstructor
public class Position {
    private int x;
    private int y;
}
