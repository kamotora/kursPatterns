package controllers;

import model.categories.TypeCategory;

import java.util.ArrayList;
import java.util.List;

public interface IObservable {
    public void addObserver(IObserver observer, TypeCategory typeCategory);

    public void notifyObservers(TypeCategory typeCategory);
}
