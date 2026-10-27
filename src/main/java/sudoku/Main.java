package sudoku;

import sudoku.difficulty.EasyStrategy;
import sudoku.gui.SudokuFrame;
import sudoku.model.SudokuGame;
import sudoku.model.SudokuGameBuilder;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SudokuGame game = new SudokuGameBuilder()
                    .difficulty(new EasyStrategy())
                    .build();
            new SudokuFrame(game).setVisible(true);
        });
    }
}
