package com.example.todolist.entities;

public class TaskList {
    private final long id;
    private final String name;

    public TaskList(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TaskList(String name) {
        this.id = 0;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
