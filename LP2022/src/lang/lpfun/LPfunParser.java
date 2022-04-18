package lang.lpfun;

import lang.ParseException;
import lex.Lexer;

import java.io.IOException;

public class LPfunParser {

    private Lexer lex;

    public LPfunParser() {
    }

    public void parse(String filePath) throws IOException {
        lex = new Lexer(LPfunTokens.DEFS);
        lex.readFile(filePath);
        lex.next();
        // call your start-symbol parsing method here
        System.out.println("Parse succeeded.");
    }

    /**
     * Check the head token and, if it matches, advance to the next token.
     * @param type the token type that we expect
     * @return the text of the head token that was matched
     * @throws ParseException if the head token does not match.
     */
    public String eat(String type) {
        if (type.equals(lex.tok().type)) {
            String image = lex.tok().image;
            lex.next();
            return image;
        } else {
            throw new ParseException(lex.tok(), type);
        }
    }

    public static void main(String[] args) throws IOException {
        LPfunParser parser = new LPfunParser();
        parser.parse(args[0]);
    }
}
