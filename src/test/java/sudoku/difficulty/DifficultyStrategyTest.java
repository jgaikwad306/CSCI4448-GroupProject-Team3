package sudoku.difficulty;

import org.junit.jupiter.api.Test;
import sudoku.model.Board;
import sudoku.model.PuzzleGenerator;
import sudoku.model.SudokuGame;
import sudoku.model.SudokuGameBuilder;
import sudoku.model.SudokuSolver;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DifficultyStrategyTest {

    private int countEmpty(Board board) {
        int empty = 0;
        for (int r = 0; r < SudokuGame.SIZE; r++) {
            for (int c = 0; c < SudokuGame.SIZE; c++) {
                if (board.isEmpty(r, c)) empty++;
            }
        }
        return empty;
    }

    @Test
    void hardPuzzleHasMoreBlanksAndOneSolution() {
        Board medium = new PuzzleGenerator(new Random(3)).generate(new MediumStrategy());
        Board hard = new PuzzleGenerator(new Random(3)).generate(new HardStrategy());

        assertTrue(countEmpty(hard) > countEmpty(medium));
        assertEquals(1, SudokuSolver.countSolutions(hard, 2));
    }

    @Test
    void newGameUsesTheChosenDifficulty() {
        SudokuGame game = new SudokuGameBuilder().difficulty(new EasyStrategy()).seed(1).build();

        game.setDifficulty(new MediumStrategy());
        game.newGame();

        int empty = 0;
        for (int r = 0; r < SudokuGame.SIZE; r++) {
            for (int c = 0; c < SudokuGame.SIZE; c++) {
                if (game.getValue(r, c) == SudokuGame.EMPTY) empty++;
            }
        }
        assertEquals(46, empty);
    }
}
