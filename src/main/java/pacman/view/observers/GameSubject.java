package pacman.view.observers;



public interface GameSubject {
    void addObserver(GameObserver observer);

    void removeObserver(GameObserver observer);

    void notifyUpdateScore();
}
