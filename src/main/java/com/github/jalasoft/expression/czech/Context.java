package com.github.jalasoft.expression.czech;

import com.github.jalasoft.expression.czech.exception.IdentifierException;

/**
 * Instance of this class supplies values of identifiers that are required
 * by {@link BoolExpression}.
 */
public interface Context {

    /**
     * Gets an integer value of an identifier by its name
     * @param identName must not be null or blank
     * @return never null
     * @throws IllegalArgumentException if identName is null or blank
     * @throws IdentifierException if there is no identifier of given name or there is such identifier, but of distinct type
     */
    int number(String identName);

    /**
     * Gets a boolean value of an identifier by its name
     * @param identName must not be null or blank
     * @return never null
     * @throws IllegalArgumentException if identName is null or blank
     * @throws IdentifierException if there is no identifier of given name or there is such identifier, but of distinct type
     */
    boolean bool(String identName);
}
