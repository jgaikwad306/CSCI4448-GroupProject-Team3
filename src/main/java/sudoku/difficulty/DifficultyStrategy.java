package sudoku.difficulty;

import sudoku.model.Board;

import java.util.Random;

public interface DifficultyStrategy {
    String getName();
    void removeCells(Board board, Random random);
}
