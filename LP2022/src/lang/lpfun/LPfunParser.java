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
        program();
        System.out.println("Parse succeeded.");
    }

    public void program() {
        eat("BEGIN");
        while (lex.tok().type != "EOF") {
            // System.out.println("Program: " + lex.tok().type);
            statement();
        }
        //lex.next();
        /*eat("END");
        while (lex.tok().type != "EOF") {
            statement();
        }*/
    }

    public void statement() {
        // System.out.println("Statement: " + lex.tok().type);
        switch (lex.tok().type) {
            case "END":
                eat("END");
                break;

            //statement -> PRINTINT exp SEMIC
            case "PRINTINT":
                lex.next();
                exp();
                break;
            //statement -> PRINTCHAR exp SEMIC
            case "PRINTCHAR":
                lex.next();
                exp();
                //eat("SEMIC");
                break;
            //statement -> IF LBR exp RBR THEN
            case "IF":
                lex.next();
                eat("LBR");
                exp();
                //statement();
                lex.next();
                while (!lex.tok().type.equals("ELSE")) {
                    statement();
                }
                lex.next();
                while (!lex.tok().type.equals("ENDIF")) {
                    statement();
                }
                lex.next();
                break;
            // statement-> WHILE LBR exp RBR DO
            case "WHILE":
                lex.next();
                eat("LBR");
                exp();
                //statement();
                eat("DO");
                while (lex.tok().type != "DONE") {
                    statement();
                }
                lex.next();
                break;
            //statement -> FUN ID* LBR exp RBR LCBR statement* RCBR
            case "FUN":
                lex.next();
                BasicExp();
                eat("LBR");
                while (lex.tok().type != "LCBR") {
                    exp();
                }
                //eat("RBR");
                eat("LCBR");
                while (lex.tok().type != "RCBR") {
                    statement();
                }
                eat("RCBR");
                break;
            //statement -> RETURN exp SEMIC
            case "RETURN":
                lex.next();
                exp();
                //eat("SEMIC");
                break;
            case "ID":
                exp();
                break;
            default:
                throw new ParseException(lex.tok(), "PRINTINT", "PRINTCHAR", "IF", "THEN", "ELSE", "WHILE", "DO", "ID", "FUN", "RETURN");
        }
    }

    //exp ->  BasicExp ExpRest
    public void exp() {
        //System.out.println("Exp: " + lex.tok().type);
        BasicExp();
        ExpRest();
    }

    public void BasicExp() {
        //System.out.println("BasicExp: " + lex.tok().type);
        switch (lex.tok().type) {
            //BasicExp -> INT
            case "INT":
                eat("INT");
                break;
            //BasicExp -> ID
            case "ID":
                eat("ID");
                break;
            //BasicExp -> LBR exp RBR
            case "LBR":
                eat("LBR");
                exp();
                //eat("RBR");
                break;
            //BasicExp -> EQ exp ExpRest
            case "EQ":
                eat("EQ");
                exp();
                break;
            case "RBR":
                eat("RBR");
                break;
            default:
                throw new ParseException(lex.tok(), "INT", "ID", "LBR", "EQ");
        }
    }

    public void ExpRest() {
        //System.out.println("ExpRest: " + lex.tok().type);
        switch (lex.tok().type) {
            //ExpRest  -> ADD Exp
            case "ADD":
                eat("ADD");
                /*BasicExp();
                ExpRest();*/
                exp();
                break;
            //ExpRest  -> SUB Exp
            case "SUB":
                eat("SUB");
                /*BasicExp();
                ExpRest();*/
                exp();
                break;
            //ExpRest  -> MUL Exp
            case "MUL":
                eat("MUL");
                BasicExp();
                ExpRest();
                break;
            //ExpRest  -> DIV Exp
            case "DIV":
                eat("DIV");
                BasicExp();
                ExpRest();
                break;
            //ExpRest  -> RBR
            case "RBR":
                eat("RBR");
                break;

            case "EQUALS":
                eat("EQUALS");
                exp();
                break;
            //ExpRest  -> EQ exp
            case "EQ":
                eat("EQ");
                exp();
                break;
            //ExpRest  -> LEQ exp
            case "LEQ":
                eat("LEQ");
                exp();
                break;
            //ExpRest -> LT exp
            case "LT":
                eat("LT");
                exp();
                break;
            //ExpRest -> SEMIC
            case "SEMIC":
                eat("SEMIC");
                break;
            //ExpRest -> COMMA
            case "COMMA":
                eat("COMMA");
                break;
            //ExpRest -> LBR exp* RBR
            case "LBR":
                eat("LBR");
                while (lex.tok().type != "RBR") {
                    BasicExp();
                    if (!lex.tok().type.equals("RBR")) {
                        ExpRest();
                    }

                }
                eat("RBR");
                ExpRest();
                break;
            case "LCBR":
                break;
            default:
                throw new ParseException(lex.tok(), "ADD", "SUB", "MUL", "DIV", "RBR", "EQUALS", "EQ", "LEQ", "LT", "SEMIC");
        }
    }

    /**
     * Check the head token and, if it matches, advance to the next token.
     *
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
