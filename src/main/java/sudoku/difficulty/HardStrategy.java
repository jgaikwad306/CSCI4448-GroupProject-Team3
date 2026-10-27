package sudoku.difficulty;

import sudoku.model.Board;
import sudoku.model.SudokuSolver;

import java.util.List;
import java.util.Random;

//only removes a cell if the puzzle still has exactly one solution
public class HardStrategy extends RandomRemovalStrategy {
    public HardStrategy() {
        super("Hard", 54);
    }

    @Override
    public void removeCells(Board board, Random random) {
        List<int[]> positions = shuffledPositions(random);
        int removed = 0;
        for (int[] cell : positions) {
            if (removed == getCellsToRemove()) {
                break;
            }
            int value = board.getValue(cell[0], cell[1]);
            board.clear(cell[0], cell[1]);
            if (SudokuSolver.countSolutions(board, 2) == 1) {
                removed++;
            } else {
                board.setValue(cell[0], cell[1], value);
            }
        }
    }
}
