package sudoku.observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractGameSubjectTest {
    private static class TestSubject extends AbstractGameSubject {
        void fire(GameEvent event) {
            notifyObservers(event);
        }
    }

    private TestSubject subject;
    private RecordingObserver first;
    private RecordingObserver second;

    @BeforeEach
    void setUp() {
        subject = new TestSubject();
        first = new RecordingObserver();
        second = new RecordingObserver();
    }

    @Test
    void notifiesEveryObserver() {
        subject.addObserver(first);
        subject.addObserver(second);

        GameEvent event = GameEvent.boardReset();
        subject.fire(event);

        assertSame(event, first.lastEvent());
        assertSame(event, second.lastEvent());
    }

    @Test
    void removedObserverIsNotNotified() {
        subject.addObserver(first);
        subject.addObserver(second);
        subject.removeObserver(first);

        subject.fire(GameEvent.gameWon());

        assertTrue(first.getEvents().isEmpty());
        assertEquals(1, second.getEvents().size());
    }
}
