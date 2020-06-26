package com.github.jalasoft.expression.czech.lexan;

import com.github.jalasoft.expression.czech.input.InputSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.github.jalasoft.expression.czech.lexan.LexicalSymbol.Type.*;

public final class Lexan {

    private static final char DELIMITER = ' ';
    private static final String CHARS = "aábcčdďeěéfghiíjklmnňoópqrřsštťuúůvwxyýzž";
    private static final Map<String, LexicalSymbol> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("je", new LexicalSymbol(JE, "je"));
        KEYWORDS.put("není", new LexicalSymbol(NENI, "není"));
        KEYWORDS.put("jak", new LexicalSymbol(JAK, "jak"));
        KEYWORDS.put("než", new LexicalSymbol(NEZ, "než"));
        KEYWORDS.put("rovno", new LexicalSymbol(ROVNO, "rovno"));
        KEYWORDS.put("roven", new LexicalSymbol(ROVEN, "roven"));
        KEYWORDS.put("rovna", new LexicalSymbol(ROVNA, "rovna"));
        KEYWORDS.put("stejný", new LexicalSymbol(STEJNY, "stejný"));
        KEYWORDS.put("stejně", new LexicalSymbol(STEJNE, "stejně"));
        KEYWORDS.put("jako", new LexicalSymbol(JAKO, "jako"));
        KEYWORDS.put("větší", new LexicalSymbol(VETSI, "větší"));
        KEYWORDS.put("více", new LexicalSymbol(VICE, "více"));
        KEYWORDS.put("víc", new LexicalSymbol(VIC, "víc"));
        KEYWORDS.put("menší", new LexicalSymbol(MENSI, "menší"));
        KEYWORDS.put("méně", new LexicalSymbol(MENE, "menší"));
        KEYWORDS.put("míň", new LexicalSymbol(MIN, "menší"));
        KEYWORDS.put("nebo", new LexicalSymbol(NEBO, "nebo"));
        KEYWORDS.put("a", new LexicalSymbol(A, "a"));
        KEYWORDS.put("zároveň", new LexicalSymbol(ZAROVEN, "zároveň"));
    }

    private static final LexicalSymbol EPSILON = new LexicalSymbol(LexicalSymbol.Type.EPSILON, null);

    //---------------------------------------------------------------------------
    //INSTANCE SCOPE
    //---------------------------------------------------------------------------

    private final InputSystem input;
    private StringBuilder buffer;

    public Lexan(InputSystem input) {
        this.input = input;
    }

    public LexicalSymbol next() throws LexanException {
        this.buffer = new StringBuilder();

        Optional<Character> maybeSymbol;

        do {
            maybeSymbol = input.nextSymbol();
        } while(maybeSymbol.isPresent() && maybeSymbol.get() == DELIMITER);

        if (maybeSymbol.isEmpty()) {
            return EPSILON;
        }

        char symbol = maybeSymbol.get();
        buffer.append(symbol);

        if (isChar(symbol)) {
            return stringState();
        }

        if (isDigit(symbol)) {
            return numberState();
        }

        throw new LexanException(symbol);
    }

    private LexicalSymbol stringState() throws LexanException {
        var maybeSymbol = input.nextSymbol();

        if (maybeSymbol.isEmpty() || maybeSymbol.get() == DELIMITER) {
            return identifierOrKeyword();
        }

        char symbol = maybeSymbol.get();

        if (isChar(symbol) || isDigit(symbol)) {
            buffer.append(maybeSymbol.get());
            return stringState();
        }

        throw new LexanException(symbol);
    }

    private LexicalSymbol identifierOrKeyword() {
        String ident = buffer.toString();
        return KEYWORDS.getOrDefault(ident, new LexicalSymbol(IDENT, ident));
    }

    private LexicalSymbol numberState() throws LexanException {
        var maybeSymbol = input.nextSymbol();

        if (maybeSymbol.isEmpty() || maybeSymbol.get() == DELIMITER) {
            return number();
        }

        char symbol = maybeSymbol.get();
        buffer.append(symbol);

        if (isDigit(symbol)) {
            return numberState();
        }

        throw new LexanException(symbol);
    }

    private LexicalSymbol number() {
       return new LexicalSymbol(LexicalSymbol.Type.NUMBER, Integer.parseInt(buffer.toString()));
    }

    private boolean isChar(char symbol) {
        return CHARS.contains("" + symbol);
    }

    private boolean isDigit(char symbol) {
        return Character.isDigit(symbol) || symbol == '-';
    }
}
