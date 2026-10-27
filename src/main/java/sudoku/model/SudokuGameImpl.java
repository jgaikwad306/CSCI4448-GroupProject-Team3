package sudoku.model;

import sudoku.difficulty.DifficultyStrategy;
import sudoku.observer.AbstractGameSubject;
import sudoku.observer.GameEvent;

public class SudokuGameImpl extends AbstractGameSubject implements SudokuGame {
    private final PuzzleGenerator generator;
    private final String fixedPuzzle; //null means generate a new puzzle each game
    private DifficultyStrategy difficulty;
    private Board board;

    //package-private so games are only created through SudokuGameBuilder
    SudokuGameImpl(PuzzleGenerator generator, DifficultyStrategy difficulty, String fixedPuzzle) {
        this.generator = generator;
        this.difficulty = difficulty;
        this.fixedPuzzle = fixedPuzzle;
        this.board = createBoard();
    }

    private Board createBoard() {
        if (fixedPuzzle != null) {
            return Board.fromString(fixedPuzzle);
        }
        return generator.generate(difficulty);
    }

    @Override
    public int getValue(int row, int col) {
        return board.getValue(row, col);
    }

    @Override
    public boolean isGiven(int row, int col) {
        return board.isGiven(row, col);
    }

    @Override
    public boolean hasConflict(int row, int col) {
        return board.hasConflict(row, col);
    }

    @Override
    public void setValue(int row, int col, int value) {
        if (board.isGiven(row, col) || value < EMPTY || value > SIZE) {
            notifyObservers(GameEvent.invalidMove(row, col));
            return;
        }
        board.setValue(row, col, value);
        notifyObservers(GameEvent.cellChanged(row, col));
        if (board.isSolved()) {
            notifyObservers(GameEvent.gameWon());
        }
    }

    @Override
    public void newGame() {
        board = createBoard();
        notifyObservers(GameEvent.boardReset());
    }

    @Override
    public boolean isSolved() {
        return board.isSolved();
    }

    @Override
    public DifficultyStrategy getDifficulty() {
        return difficulty;
    }

    @Override
    public void setDifficulty(DifficultyStrategy difficulty) {
        this.difficulty = difficulty;
    }
}
