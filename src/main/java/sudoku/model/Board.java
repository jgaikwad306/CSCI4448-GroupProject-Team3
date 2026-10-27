package sudoku.model;

import static sudoku.model.SudokuGame.BOX_SIZE;
import static sudoku.model.SudokuGame.EMPTY;
import static sudoku.model.SudokuGame.SIZE;

public class Board {
    private final Cell[][] cells = new Cell[SIZE][SIZE];

    public Board() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                cells[r][c] = new Cell();
            }
        }
    }

    //81 chars read left to right, '0' or '.' for blanks
    public static Board fromString(String puzzle) {
        if (puzzle == null || puzzle.length() != SIZE * SIZE) {
            throw new IllegalArgumentException("Puzzle must be exactly 81 characters");
        }
        Board board = new Board();
        for (int i = 0; i < SIZE * SIZE; i++) {
            char ch = puzzle.charAt(i);
            int value = (ch == '.') ? EMPTY : ch - '0';
            if (value < EMPTY || value > SIZE) {
                throw new IllegalArgumentException("Invalid character in puzzle: " + ch);
            }
            board.setValue(i / SIZE, i % SIZE, value);
        }
        board.lockFilledCells();
        return board;
    }

    public int getValue(int row, int col) {
        return cells[row][col].getValue();
    }

    public void setValue(int row, int col, int value) {
        cells[row][col].setValue(value);
    }

    public void clear(int row, int col) {
        setValue(row, col, EMPTY);
    }

    public boolean isEmpty(int row, int col) {
        return cells[row][col].isEmpty();
    }

    public boolean isGiven(int row, int col) {
        return cells[row][col].isGiven();
    }

    public void lockFilledCells() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (!cells[r][c].isEmpty()) {
                    cells[r][c].lock();
                }
            }
        }
    }

    public boolean canPlace(int row, int col, int value) {
        for (int i = 0; i < SIZE; i++) {
            if (i != col && getValue(row, i) == value) return false;
            if (i != row && getValue(i, col) == value) return false;
        }
        int boxRow = (row / BOX_SIZE) * BOX_SIZE;
        int boxCol = (col / BOX_SIZE) * BOX_SIZE;
        for (int r = boxRow; r < boxRow + BOX_SIZE; r++) {
            for (int c = boxCol; c < boxCol + BOX_SIZE; c++) {
                if ((r != row || c != col) && getValue(r, c) == value) return false;
            }
        }
        return true;
    }

    public boolean hasConflict(int row, int col) {
        int value = getValue(row, col);
        return value != EMPTY && !canPlace(row, col, value);
    }

    public boolean isSolved() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (isEmpty(r, c) || hasConflict(r, c)) return false;
            }
        }
        return true;
    }
}
