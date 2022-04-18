package lang.lpse;

import lang.ParseException;
import lex.Lexer;

import java.io.IOException;

public class LPseParser {

    private Lexer lex;

    public LPseParser() {
    }

    public void parse(String filePath) throws IOException {
        lex = new Lexer(LPseTokens.DEFS);
        lex.readFile(filePath);
        lex.next();
        // call your start-symbol parsing method here
        System.out.println("Parse succeeded.");
    }
    public void program(){
        eat("BEGIN");
        while(lex.tok().type!="END"){
            statement();
        }
    }
    public void statement(){
        switch  (lex.tok().type){
            case "PRINTINT":
                lex.next();
                exp();
                eat("SEMIC");
                break;
            case "PRINTCHAR":
                lex.next();
                exp();
                eat("SEMIC");
                break;
            case "IF":
                lex.next();
                eat("LBR");
                exp();
                eat("RBR");
                statement();
                eat("ELSE");
                statement();
                break;
            case "LCBR":
                lex.next();
                while (lex.tok().type != "RCBR") {
                    statement();
                }
                lex.next();
                break;
            // statement-> WHILE LBR exp LT exp RBR statement DONE
            case "WHILE":
                lex.next();
                eat("LBR");
                exp();
                eat("LT");
                exp();
                eat("RBR");
                while (lex.tok().type != "DONE") {

                }

            default:
                throw new ParseException(lex.tok(), "PRINTINT", "PRINTCHAR", "IF", "LCBR");
        }
    }
    public void exp(){
        eat("INT");
    }
    private void eof() {
        if (lex.tok().type != "EOF") {
            throw new ParseException(lex.tok(), "EOF");
        }
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
        LPseParser parser = new LPseParser();
        parser.parse(args[0]);
    }
}
