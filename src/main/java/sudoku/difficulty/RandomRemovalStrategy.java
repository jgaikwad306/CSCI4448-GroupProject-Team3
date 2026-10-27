package sudoku.difficulty;

import sudoku.model.Board;
import sudoku.model.SudokuGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class RandomRemovalStrategy implements DifficultyStrategy {
    private final String name;
    private final int cellsToRemove;

    protected RandomRemovalStrategy(String name, int cellsToRemove) {
        this.name = name;
        this.cellsToRemove = cellsToRemove;
    }

    @Override
    public String getName() {
        return name;
    }

    protected int getCellsToRemove() {
        return cellsToRemove;
    }

    @Override
    public void removeCells(Board board, Random random) {
        List<int[]> positions = shuffledPositions(random);
        for (int i = 0; i < cellsToRemove; i++) {
            int[] cell = positions.get(i);
            board.clear(cell[0], cell[1]);
        }
    }

    protected static List<int[]> shuffledPositions(Random random) {
        List<int[]> positions = new ArrayList<>();
        for (int r = 0; r < SudokuGame.SIZE; r++) {
            for (int c = 0; c < SudokuGame.SIZE; c++) {
                positions.add(new int[] {r, c});
            }
        }
        Collections.shuffle(positions, random);
        return positions;
    }

    //JComboBox displays options using toString
    @Override
    public String toString() {
        return name;
    }
}
