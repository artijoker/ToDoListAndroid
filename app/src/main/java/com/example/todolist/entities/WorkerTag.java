package com.example.todolist.entities;

public class WorkerTag {
    private final long id;
    private final String tag;

    public WorkerTag(long id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public WorkerTag(String tag) {
        this.id = 0;
        this.tag = tag;
    }

    public long getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }
}
