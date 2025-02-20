import java.util.*;

class Lexer
{
    private String source;
    private int index;
    private int line;
    private List<Token> tokens;

    private static final Set<String> keywords = new HashSet<>(Arrays.asList(
            "tni", "loob", "taolf", "rahc", "tuoc", "nic", "sey", "on"
    ));

    public Lexer(String source)
    {
        this.source = source;
        this.index = 0;
        this.line = 1;
        this.tokens = new ArrayList<>();
    }

    public List<Token> tokenize()
    {
        while (index < source.length())
        {
            char current = source.charAt(index);

            if (current == '/' && index + 1 < source.length() && source.charAt(index + 1) == '/')
            {
                int start = index;
                index += 2;

                while (index < source.length() && source.charAt(index) != '\n')
                {
                    index++;
                }

                String lexeme = source.substring(start, index).trim();
                tokens.add(new Token(TokenType.SINGLECOMMENT, lexeme, line));

                if (index < source.length() && source.charAt(index) == '\n')
                {
                    line++;
                    index++;
                }
                continue;
            }

            if (current == '/' && index + 1 < source.length() && source.charAt(index + 1) == '*')
            {
                int start = index;
                index += 2;

                while (index < source.length() && !(source.charAt(index - 1) == '*' && source.charAt(index) == '/')) {
                    if (source.charAt(index) == '\n')
                    {
                        line++;
                    }
                    index++;
                }

                if (index < source.length())
                {
                    index++;
                }
                else
                {
                    ErrorHandler.addError("Unterminated multi-line comment", line);
                }

                String lexeme = source.substring(start, index);
                tokens.add(new Token(TokenType.MULTICOMMENT, lexeme, line));
                continue;
            }

            if (Character.isWhitespace(current))
            {
                if (current == '\n') {
                    line++;
                }
                index++;
                continue;
            }

            if (Character.isLetter(current))
            {
                int start = index;
                while (index < source.length() && (Character.isLetterOrDigit(source.charAt(index)) || source.charAt(index) == '_'))
                {
                    index++;
                }
                String lexeme = source.substring(start, index);

                if (lexeme.equals("on") || lexeme.equals("sey"))
                {
                    tokens.add(new Token(TokenType.BOOLEAN, lexeme, line));
                }
                else
                {
                    TokenType type = keywords.contains(lexeme) ? TokenType.KEYWORD : TokenType.IDENTIFIER;

                    if (type == TokenType.IDENTIFIER && !lexeme.equals(lexeme.toLowerCase()))
                    {
                        ErrorHandler.addError("Invalid identifier (must be lowercase): " + lexeme, line);
                    }

                    tokens.add(new Token(type, lexeme, line));
                }
                continue;
            }

            if (Character.isDigit(current))
            {
                int start = index;
                boolean hasDecimal = false;
                int decimalCount = 0;

                while (index < source.length() &&
                        (Character.isDigit(source.charAt(index)) || source.charAt(index) == '.'))
                {
                    if (source.charAt(index) == '.')
                    {
                        if (hasDecimal)
                        {
                            ErrorHandler.addError("Invalid number format", line);
                            break;
                        }
                        hasDecimal = true;
                    }
                    else if (hasDecimal)
                    {
                        decimalCount++;
                    }
                    index++;
                }

                String lexeme = source.substring(start, index);

                if (hasDecimal)
                {
                    try
                    {
                        double num = Double.parseDouble(lexeme);
                        num = Math.round(num * 100000.0) / 100000.0;
                        lexeme = String.format("%.5f", num);
                    }
                    catch (NumberFormatException e)
                    {
                        ErrorHandler.addError("Invalid float format", line);
                    }
                    tokens.add(new Token(TokenType.FLOAT, lexeme, line));
                }
                else
                {
                    tokens.add(new Token(TokenType.NUMBER, lexeme, line));
                }
                continue;
            }

            if ("+-*/%^".indexOf(current) != -1)
            {
                tokens.add(new Token(TokenType.OPERATOR, Character.toString(current), line));
                index++;
            }
            else if (current == '=' && index + 1 < source.length() && source.charAt(index + 1) == '=')
            {
                tokens.add(new Token(TokenType.OPERATOR, "==", line));
                index += 2;
            }
            else if (current == '=' || current == '<' || current == '>')
            {
                tokens.add(new Token(TokenType.OPERATOR, Character.toString(current), line));
                index++;
            }
            else if (";,(){}".indexOf(current) != -1)
            {
                tokens.add(new Token(TokenType.PUNCTUATION, Character.toString(current), line));
                index++;
            }
            else if (current == '\'')
            {
                index++;
                if (index < source.length())
                {
                    char ch = source.charAt(index);
                    index++;
                    if (index < source.length() && source.charAt(index) == '\'') {
                        tokens.add(new Token(TokenType.CHARACTER, Character.toString(ch), line));
                        index++;
                    } else {
                        ErrorHandler.addError("Unclosed character literal", line);
                    }
                }
                else
                {
                    ErrorHandler.addError("Incomplete character literal", line);
                }
            }
            else
            {
                ErrorHandler.addError("Unknown token: " + current, line);
                index++;
            }
        }
        return tokens;
    }

}
