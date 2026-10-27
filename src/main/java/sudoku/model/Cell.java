package sudoku.model;

public class Cell {
    private int value = SudokuGame.EMPTY;
    private boolean given = false;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (given) {
            throw new IllegalStateException("Cannot change a given cell");
        }
        if (value < SudokuGame.EMPTY || value > SudokuGame.SIZE) {
            throw new IllegalArgumentException("Value must be 0-9: " + value);
        }
        this.value = value;
    }

    public boolean isEmpty() {
        return value == SudokuGame.EMPTY;
    }

    public boolean isGiven() {
        return given;
    }

    public void lock() {
        given = true;
    }
}
