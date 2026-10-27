package sudoku.observer;

//allows anyone to subcribe and unsubscribe
public interface GameSubject {
    void addObserver(GameObserver observer);
    void removeObserver(GameObserver observer);
}
