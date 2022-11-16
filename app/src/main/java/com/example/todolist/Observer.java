package com.example.todolist;

import com.example.todolist.entities.Task;

public interface Observer {
    void update(Task task);
}
