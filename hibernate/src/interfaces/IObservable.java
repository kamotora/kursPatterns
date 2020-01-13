package interfaces;

import model.categories.TypeCategory;

public interface IObservable {
    public void addObserver(IObserver observer, TypeCategory typeCategory);

    public void notifyObservers(TypeCategory typeCategory);
}
