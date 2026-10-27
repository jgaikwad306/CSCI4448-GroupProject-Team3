package sudoku.model;

import sudoku.difficulty.DifficultyStrategy;
import sudoku.observer.GameSubject;

public interface SudokuGame extends GameSubject {
    int SIZE = 9;
    int BOX_SIZE = 3;
    int EMPTY = 0;

    int getValue(int row, int col);
    boolean isGiven(int row, int col);
    boolean hasConflict(int row, int col);
    void setValue(int row, int col, int value);
    void newGame();
    boolean isSolved();

    DifficultyStrategy getDifficulty();
    void setDifficulty(DifficultyStrategy difficulty);
}
