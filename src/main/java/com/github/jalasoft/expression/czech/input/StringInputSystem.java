package com.github.jalasoft.expression.czech.input;

import java.util.Optional;

public final class StringInputSystem implements InputSystem {

    private final String input;
    private int index;

    public StringInputSystem(String input) {
        this.input = input;
    }

    @Override
    public Optional<Character> nextSymbol() {
        if (index >= input.length()) {
            return Optional.empty();
        }

        return Optional.of(input.charAt(index++));
    }
}
