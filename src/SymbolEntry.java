class SymbolEntry
{
    String name;
    String type;
    String value;
    String scope;
    int lineDeclared;

    public SymbolEntry(String name, String type, String value, String scope, int lineDeclared)
    {
        this.name = name;
        this.type = type;
        this.value = value;
        this.scope = scope;
        this.lineDeclared = lineDeclared;
    }

    public String toString()
    {
        return "Symbol{name='" + name + "', type='" + type + "', value='" + value + "', scope='" + scope + "', declared at line " + lineDeclared + "}";
    }
}