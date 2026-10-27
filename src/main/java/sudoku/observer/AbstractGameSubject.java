package sudoku.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGameSubject implements GameSubject{
    private final List<GameObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers(GameEvent event) {
        for (GameObserver observer : observers) {
            observer.onGameEvent(event);
        }
    }

}
