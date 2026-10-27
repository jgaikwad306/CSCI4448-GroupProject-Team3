package sudoku.observer;

import java.util.ArrayList;
import java.util.List;

public class RecordingObserver implements GameObserver{
    private final List<GameEvent> events = new ArrayList<>();

    @Override
    public void onGameEvent(GameEvent event) {
        events.add(event);
    }

    public List<GameEvent> getEvents() {
        return events;
    }

    public GameEvent lastEvent() {
        return events.isEmpty() ? null : events.get(events.size() - 1);
    }
}
