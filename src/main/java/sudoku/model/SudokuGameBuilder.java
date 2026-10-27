package sudoku.model;

import sudoku.difficulty.DifficultyStrategy;
import sudoku.difficulty.EasyStrategy;

import java.util.Random;

public class SudokuGameBuilder {
    private DifficultyStrategy difficulty = new EasyStrategy();
    private Long seed;
    private String puzzle;

    public SudokuGameBuilder difficulty(DifficultyStrategy difficulty) {
        if (difficulty == null) {
            throw new IllegalArgumentException("difficulty cannot be null");
        }
        this.difficulty = difficulty;
        return this;
    }

    //same seed gives the same puzzles, useful for tests
    public SudokuGameBuilder seed(long seed) {
        this.seed = seed;
        return this;
    }

    //uses this exact puzzle every game instead of generating one
    public SudokuGameBuilder puzzle(String puzzle) {
        Board.fromString(puzzle); //throws now if the string is malformed
        this.puzzle = puzzle;
        return this;
    }

    public SudokuGame build() {
        Random random = (seed == null) ? new Random() : new Random(seed);
        return new SudokuGameImpl(new PuzzleGenerator(random), difficulty, puzzle);
    }
}
