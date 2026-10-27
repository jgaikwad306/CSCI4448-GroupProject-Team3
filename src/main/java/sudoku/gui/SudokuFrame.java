package sudoku.gui;

import sudoku.model.SudokuGame;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/** Main window: wires the model to its views. */
public class SudokuFrame extends JFrame {

    public SudokuFrame(SudokuGame game) {
        super("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SudokuBoardPanel board = new SudokuBoardPanel(game);

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> {
            game.newGame();
            board.requestFocusInWindow();
        });

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.add(newGameButton);

        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }
}