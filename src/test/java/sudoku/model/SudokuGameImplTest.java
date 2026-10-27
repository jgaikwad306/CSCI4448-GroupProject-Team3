package sudoku.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sudoku.observer.GameEvent;
import sudoku.observer.RecordingObserver;

import static org.junit.jupiter.api.Assertions.*;

class SudokuGameImplTest {
    private static final String PUZZLE =
            "530070000" +
            "600195000" +
            "098000060" +
            "800060003" +
            "400803001" +
            "700020006" +
            "060000280" +
            "000419005" +
            "000080079";

    private SudokuGame game;
    private RecordingObserver observer;

    @BeforeEach
    void setUp() {
        game = new SudokuGameBuilder().puzzle(PUZZLE).build();
        observer = new RecordingObserver();
        game.addObserver(observer);
    }

    @Test
    void givenCellCannotBeChanged() {
        game.setValue(0, 0, 9);

        assertEquals(5, game.getValue(0, 0));
        assertEquals(GameEvent.Type.INVALID_MOVE, observer.lastEvent().getType());
    }

    @Test
    void duplicateInRowIsAConflict() {
        game.setValue(0, 2, 5);

        assertTrue(game.hasConflict(0, 2));
        assertEquals(GameEvent.Type.CELL_CHANGED, observer.lastEvent().getType());
    }
}
