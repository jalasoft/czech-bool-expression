package com.github.jalasoft.expression.czech.input;

import java.util.Optional;

/**
 * An abstraction of an input. It provides character after character until new ine is available.
 *
 * @author Jan "Honzales" Lastovkcka
 */
public interface InputSystem {

    Optional<Character> nextSymbol();
}
