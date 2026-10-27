package sudoku.gui;

import sudoku.difficulty.DifficultyStrategy;
import sudoku.difficulty.EasyStrategy;
import sudoku.difficulty.HardStrategy;
import sudoku.difficulty.MediumStrategy;
import sudoku.model.SudokuGame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class SudokuFrame extends JFrame {

    public SudokuFrame(SudokuGame game) {
        super("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SudokuBoardPanel board = new SudokuBoardPanel(game);

        JComboBox<DifficultyStrategy> difficultyBox = new JComboBox<>(new DifficultyStrategy[] {
                new EasyStrategy(), new MediumStrategy(), new HardStrategy()
        });
        selectCurrentDifficulty(difficultyBox, game.getDifficulty());

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> {
            game.setDifficulty((DifficultyStrategy) difficultyBox.getSelectedItem());
            game.newGame();
            board.requestFocusInWindow();
        });

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.add(new JLabel("Difficulty:"));
        toolbar.add(difficultyBox);
        toolbar.add(newGameButton);

        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private static void selectCurrentDifficulty(JComboBox<DifficultyStrategy> box, DifficultyStrategy current) {
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).getName().equals(current.getName())) {
                box.setSelectedIndex(i);
                return;
            }
        }
    }
}
