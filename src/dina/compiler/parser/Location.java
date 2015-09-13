package dina.compiler.parser;

public class Location {

    private int column;
    private int line;
    private int position;

    public Location(int col, int ln, int position) {
        this.column = col;
        this.line = ln;
        this.position = position;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }

    public int getPosition() {
        return position;
    }
}
