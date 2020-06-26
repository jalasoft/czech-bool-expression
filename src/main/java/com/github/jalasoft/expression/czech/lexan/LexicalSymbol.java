package com.github.jalasoft.expression.czech.lexan;

public final class LexicalSymbol {

    public enum Type {
        IDENT, NUMBER, JE, NENI, ROVNO, ROVEN, ROVNA, STEJNY, STEJNE, VETSI, VICE, VIC, MENSI, MENE, MIN, NEZ, JAK, JAKO, NEBO, A, ZAROVEN, EPSILON
    }

    private final Type type;
    private final Object value;

    public LexicalSymbol(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public boolean is(Type type) {
        return this.type == type;
    }

    public Object value() {
        return value;
    }

    public Type type() {
        return type;
    }

    @Override
    public String toString() {
        return "LexicalSymbol[" + type + ",value=" + value + "]";
    }
}
