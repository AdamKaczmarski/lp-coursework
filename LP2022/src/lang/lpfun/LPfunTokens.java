package lang.lpfun;

public final class LPfunTokens {

    public static final String[][] DEFS = {
            {"LBR", "\\("},
            {"RBR", "\\)"},
            {"LCBR", "\\{"},
            {"RCBR", "\\}"},
            {"SEMIC", ";"},
            {"BEGIN", "begin"},
            {"END", "end"},
            {"PRINTINT", "printint"},
            {"PRINTCHAR", "printchar"},
            {"IF", "if"},
            {"ELSE", "else"},
            {"INT", "-?[0-9]+"},
            {"WHILE", "while"},
            {"DO", "do"},
            {"DONE", "done"},
            {"ADD", "\\+"},
            {"SUB", "\\-"},
            {"MUL", "\\*"},
            {"DIV", "\\/"},
            {"EQ", "=="},
            {"LT", "<"},
            {"LEQ", "<="},
            {"ENDIF", "endif"},
            {"THEN", "then"},
            {"ID", "_?+[a-zA-Z_$0-9]+"},
            {"EQUALS", "="},
            {"FUN", "fun"},
    };
}
