package com.example.todolist.observer;

public interface TaskObservable {
    void addObserver(TaskObserver taskObserver);
    void removeObserver(TaskObserver taskObserver);
    void notifyObservers();
}
