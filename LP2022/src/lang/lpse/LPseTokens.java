package lang.lpse;

public final class LPseTokens {

    public static final String[][] DEFS = {
            // define your LPse tokens here

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
            {"PLUS", "\\+"},
            {"MINUS", "\\-"},
            {"MULT", "\\*"},
            {"DIV", "\\/"},
            {"EQ", "=="},
            {"LT", "<"},
            {"LEQ", "<="},
            {"BREAK", "done"},
            {"BREAK", "endif"},
            {"CNTN", "then"},
    };


}
