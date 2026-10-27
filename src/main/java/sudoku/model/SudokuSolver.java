package sudoku.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static sudoku.model.SudokuGame.EMPTY;
import static sudoku.model.SudokuGame.SIZE;

public final class SudokuSolver {

    private SudokuSolver() {
    }

    //backtracking, digits shuffled so every grid is different
    public static boolean fillRandomly(Board board, Random random) {
        int[] cell = findEmptyCell(board);
        if (cell == null) {
            return true;
        }
        List<Integer> digits = new ArrayList<>();
        for (int d = 1; d <= SIZE; d++) {
            digits.add(d);
        }
        Collections.shuffle(digits, random);

        for (int digit : digits) {
            if (board.canPlace(cell[0], cell[1], digit)) {
                board.setValue(cell[0], cell[1], digit);
                if (fillRandomly(board, random)) {
                    return true;
                }
                board.clear(cell[0], cell[1]);
            }
        }
        return false;
    }

    //stops once limit is reached, leaves the board unchanged
    public static int countSolutions(Board board, int limit) {
        int[] cell = findEmptyCell(board);
        if (cell == null) {
            return 1;
        }
        int count = 0;
        for (int digit = 1; digit <= SIZE && count < limit; digit++) {
            if (board.canPlace(cell[0], cell[1], digit)) {
                board.setValue(cell[0], cell[1], digit);
                count += countSolutions(board, limit - count);
                board.clear(cell[0], cell[1]);
            }
        }
        return count;
    }

    private static int[] findEmptyCell(Board board) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board.getValue(r, c) == EMPTY) {
                    return new int[] {r, c};
                }
            }
        }
        return null;
    }
}
