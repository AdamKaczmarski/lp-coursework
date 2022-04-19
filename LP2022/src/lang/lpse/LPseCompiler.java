package lang.lpse;

import lang.ParseException;
import lex.Lexer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import stackmachine.machine.SysCall;


/*
The language is untyped.

Some remarks on the semantics:

    all variables are implicitly global

    global variables are implicitly initialised to 0

    if and while treat 0 as false and everything else as true

    the comparison operators evaluate to either 0 (false) or 1 (true)

    there are no logical operators (but they can be simulated with arithmetic)
 */

public class LPseCompiler {

    private Lexer lex;
    private int freshNameCounter;
    private PrintStream out;
    private ArrayList<String> vars = new ArrayList<String>();

    public LPseCompiler(PrintStream out) {
        this.out = out;
    }

    private String freshName(String prefix) {
        return "$" + prefix + "_" + (freshNameCounter++);
    }

    private void emit(String s) {
        out.println(s);
    }

    public void compile(String filePath) throws IOException {
        freshNameCounter = 0;
        lex = new Lexer(LPseTokens.DEFS);
        lex.readFile(filePath);
        lex.next();
        program();
    }

    public void program() {
        //vars.add("total");
        eat("BEGIN");
        while (lex.tok().type != "END") {
            // System.out.println("Program: " + lex.tok().type);
            statement();
        }
        eat("END");
        emit("halt");
        emit(".data");
        for (String s : vars) {
            emit(s + ": 0");
        }
        eof();
    }

    public void statement() {
        //System.out.println("Statement: " + lex.tok().type);
        switch (lex.tok().type) {
            //statement -> PRINTINT exp SEMIC
            case "PRINTINT":
                lex.next();
                exp();
                emit("push " + SysCall.OUT_DEC);
                emit("sysc");
                break;
            //statement -> PRINTCHAR exp SEMIC
            case "PRINTCHAR":
                lex.next();
                exp();
                emit("push " + SysCall.OUT_CHAR);
                emit("sysc");
                //eat("SEMIC");
                break;
            //Statement -> IF LBR Exp RBR THEN Statement* ELSE Statement* ENDIF
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
            case "ID":
                exp();
                break;
            default:
                throw new ParseException(lex.tok(), "PRINTINT", "PRINTCHAR", "IF", "THEN", "ELSE", "WHILE", "DO", "ID");
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

        String t;
        int i;
        switch (lex.tok().type) {
            //BasicExp -> INT
            case "INT":
                i = Integer.parseInt(eat("INT"));
                emit("push " + i);
                //emit("push total");
                //emit("store");
                break;
            //BasicExp -> ID
            case "ID":
                t = eat("ID");
                if (!vars.contains(t)){
                    vars.add(t);
                }
                emit("push " + t);
                break;
            //BasicExp -> LBR exp RBR
            case "LBR":
                eat("LBR");
                exp();
                break;
            //BasicExp -> EQ exp ExpRest
            case "EQ":
                eat("EQ");
                exp();
                break;
            default:
                throw new ParseException(lex.tok(), "INT", "ID", "LBR", "EQ");
        }
    }

    public void ExpRest() {
        //System.out.println("ExpRest: " + lex.tok().type);
        //emit("push total\nload");
        int i;
        switch (lex.tok().type) {
            //ExpRest  -> ADD Exp
            case "ADD":
                eat("ADD");
                i = Integer.parseInt(eat("INT"));
                emit("push " + i);
                emit("add");
                //emit("push total\nstore");
                ExpRest();
                break;
            //ExpRest  -> SUB Exp
            case "SUB":
                eat("SUB");
                i = Integer.parseInt(eat("INT"));
                emit("push " + i);
                emit("sub");
                //emit("push total\nstore");
                ExpRest();
                break;
            //ExpRest  -> MUL Exp
            case "MUL":
                eat("MUL");
                i = Integer.parseInt(eat("INT"));
                emit("push " + i);
                emit("mul");
                //emit("push total\nstore");
                ExpRest();
                break;
            //ExpRest  -> DIV Exp
            case "DIV":
                eat("DIV");
                i = Integer.parseInt(eat("INT"));
                emit("push " + i);
                emit("div");
                //emit("push total\nstore");
                ExpRest();
                break;
            //ExpRest  -> RBR
            case "RBR":
                eat("RBR");
                break;

            case "EQUALS":
                eat("EQUALS");
                emit("push 3\nsysc\npush 2\nsysc");
                BasicExp();
                ExpRest();
                emit("store");

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

    private void eof() {
        if (lex.tok().type != "EOF") {
            throw new ParseException(lex.tok(), "EOF");
        }
    }

    public static void main(String[] args) throws IOException {
        String inFilePath = args[0];
        if (args.length > 1) {
            try (PrintStream out = new PrintStream(new FileOutputStream(args[1]))) {
                LPseCompiler compiler = new LPseCompiler(out);
                compiler.compile(inFilePath);
            }
        } else {
            LPseCompiler compiler = new LPseCompiler(System.out);
            compiler.compile(inFilePath);
        }
    }
}
