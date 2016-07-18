package Observer;

public interface Observable {
	void addObserver(Observer obs);

	void notifyObserver();
}
