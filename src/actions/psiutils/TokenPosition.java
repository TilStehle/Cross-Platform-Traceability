package actions.psiutils;

/**
 * Created by tilmannstehle on 08.12.16.
 */
public class TokenPosition {
    TokenPosition(int lineNumber, int columnNumber)
        {
            line= lineNumber;
            column=columnNumber;

        }
        private int line;
        private int column;

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
