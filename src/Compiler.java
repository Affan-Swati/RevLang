import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Compiler
{

    private static String readFile(String filePath)
    {
        String code = "";
        try
        {
            code = new String(Files.readAllBytes(Paths.get(filePath)));
        }
        catch (IOException e)
        {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return code;
    }

    public static void main(String[] args)
    {

            String path = args[0];
//            String path = "src/sourceFile.rl";
            String code = readFile(path);


            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.tokenize();
            System.out.println("Total Tokens: " + tokens.size());
            System.out.println("Tokens:");
            for (Token token : tokens)
            {
                System.out.println(token);
            }

            Set<String> typeKeywords = new HashSet<>(Arrays.asList("tni", "loob", "taolf", "rahc"));
            SymbolTable symTable = new SymbolTable(tokens,typeKeywords);
            System.out.println("\nSymbol Table:");
            symTable.printSymbols();

            if (ErrorHandler.hasErrors())
            {
                System.out.println("\nErrors Found:");
                ErrorHandler.printErrors();
            }
            else
            {
                System.out.println("\nNo lexical errors found.");
            }

            String preprocessedSource = PreProcessor.preprocess(code);
            System.out.println("Preprocessed Source:\n" + preprocessedSource + "\n");

            String regex1 = "(a|b)*";
            NFAFragment nfa1  = NFA.buildNFA(regex1);
            System.out.println("NFA constructed for regex: " + regex1);
            NFA.displayNFAFragment(nfa1);

            DFA.DFAState dfaStart = DFA.convertFromNFA(nfa1);
            DFA.displayDFA(dfaStart);

    }
}
