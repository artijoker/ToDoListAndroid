package com.example.todolist.observer;

import com.example.todolist.entities.Task;

public interface TaskObserver {
    void update(Task task);
}
