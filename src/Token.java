enum TokenType {
    KEYWORD,
    IDENTIFIER,
    NUMBER,
    FLOAT,
    OPERATOR,
    PUNCTUATION,
    CHARACTER,
    SINGLECOMMENT,
    MULTICOMMENT,
    BOOLEAN
}

class Token
{
    private TokenType type;
    private String lexeme;
    private int line;

    public Token(TokenType type, String lexeme, int line)
    {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLine() {
        return line;
    }

    public String toString() {
        return "Token{" + "type=" + type + ", lexeme=" + lexeme + ", " + "line= " + line + '}';
    }
}