import java.util.*;

class SymbolTable
{
    private Map<String, SymbolEntry> symbols;

    public SymbolTable(List<Token> tokens , Set<String> typeKeywords)
    {
        symbols = new HashMap<>();

        int braceCount = 0;

        for (int i = 0; i < tokens.size(); i++)
        {
            Token token = tokens.get(i);
            String lex = token.getLexeme();

            if (lex.equals("{"))
            {
                braceCount++;
            }
            else if (lex.equals("}"))
            {
                braceCount--;
            }

            if (typeKeywords.contains(lex))
            {
                String varType = lex;
                if (varType.equals("tni"))
                {
                    varType = "INTEGER";
                }
                else if (varType.equals("taolf"))
                {
                    varType = "DECIMAL";
                }
                else if (varType.equals("loob"))
                {
                    varType = "BOOLEAN";
                }
                else if (varType.equals("rahc"))
                {
                    varType = "CHARACTER";
                }

                if (i + 1 < tokens.size())
                {
                    Token varToken = tokens.get(i + 1);
                    if (varToken.getType() == TokenType.IDENTIFIER)
                    {
                        String varName = varToken.getLexeme();
                        String scope = (braceCount == 0) ? "GLOBAL" : "LOCAL";
                        String value = "N/A";
                        if (i + 2 < tokens.size() && tokens.get(i + 2).getLexeme().equals("="))
                        {
                            if (i + 3 < tokens.size())
                            {
                                value = tokens.get(i + 3).getLexeme();
                            }
                        }
                        this.addSymbol(varName, varType, varToken.getLine(), value, scope);
                    }
                }
            }

            if (token.getType() == TokenType.SINGLECOMMENT)
            {
                this.addSymbol("SINGLE_COMMENT", "COMMENT", token.getLine(), lex, "N/A");
            }
            else if (token.getType() == TokenType.MULTICOMMENT)
            {
                this.addSymbol("MULTI_COMMENT", "COMMENT", token.getLine(), lex, "N/A");
            }
        }

    }

    public void addSymbol(String name, String type, int lineDeclared, String value, String scope)
    {
        if (!symbols.containsKey(name)) {
            symbols.put(name, new SymbolEntry(name, type, value, scope, lineDeclared));
        }
    }

    public void printSymbols()
    {
        System.out.println("+----------------+-----------+------+-----------------+--------+");
        System.out.println("| Name           | Type      | Line | Value           | Scope  |");
        System.out.println("+----------------+-----------+------+-----------------+--------+");

        for (SymbolEntry entry : symbols.values())
        {
            System.out.printf("| %-14s | %-9s | %-4d | %-15s | %-6s |\n",
                    entry.name,
                    entry.type,
                    entry.lineDeclared,
                    entry.value,
                    entry.scope);
        }

        System.out.println("+----------------+-----------+------+-----------------+--------+");
    }
}