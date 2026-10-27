package sudoku.model;

import sudoku.difficulty.DifficultyStrategy;

import java.util.Random;

public class PuzzleGenerator {
    private final Random random;

    public PuzzleGenerator(Random random) {
        this.random = random;
    }

    public Board generate(DifficultyStrategy difficulty) {
        Board board = new Board();
        SudokuSolver.fillRandomly(board, random);
        difficulty.removeCells(board, random);
        board.lockFilledCells();
        return board;
    }
}
