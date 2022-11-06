package com.example.todolist.entities;

import java.time.LocalDate;

public class Task {
    private final long id;
    private final long listId;
    private final String title;
    private final String text;
    private final LocalDate endDate;
    private final boolean isCompleted;
    private final boolean isNotifyEnd;

    public Task(long id, long listId, String title, String text, LocalDate endDate, boolean isCompleted, boolean isNotifyEnd) {
        this.id = id;
        this.listId = listId;
        this.title = title;
        this.text = text;
        this.endDate = endDate;
        this.isCompleted = isCompleted;
        this.isNotifyEnd = isNotifyEnd;
    }

    public Task(long listId, String title, String text, LocalDate endDate, boolean isCompleted, boolean isTagged, boolean isNotifyEnd) {
        this.id = 0;
        this.listId = listId;
        this.title = title;
        this.text = text;
        this.endDate = endDate;
        this.isCompleted = isCompleted;
        this.isNotifyEnd = isNotifyEnd;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }


    public boolean isNotifyEnd() {
        return isNotifyEnd;
    }


    public long getListId() {
        return listId;
    }
}
