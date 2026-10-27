package sudoku.observer;

public final class GameEvent {
    public enum Type{
        CELL_CHANGED,
        BOARD_RESET,
        INVALID_MOVE,
        GAME_WON
    }
    private final Type type;
    private final int row;
    private final int col;

    private GameEvent(Type type, int row, int col){
        this.type = type;
        this.row = row;
        this.col = col;
    }
    public static GameEvent cellChanged(int row, int col) {
        return new GameEvent(Type.CELL_CHANGED, row, col);
    }

    public static GameEvent invalidMove(int row, int col) {
        return new GameEvent(Type.INVALID_MOVE, row, col);
    }

    public static GameEvent boardReset() {
        return new GameEvent(Type.BOARD_RESET, -1, -1);
    }

    public static GameEvent gameWon() {
        return new GameEvent(Type.GAME_WON, -1, -1);
    }

    public Type getType() { return type; }
    public int getRow()   { return row; }
    public int getCol()   { return col; }

    //debugging code, was printing gameEventID earlier
    @Override
    public String toString() {
        return "GameEvent[" + type + ", row=" + row + ", col=" + col + "]";
    }
}
