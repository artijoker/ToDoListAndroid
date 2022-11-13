package com.example.todolist.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskList implements Parcelable {
    private long id;
    private String name;

    public TaskList(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TaskList(String name) {
        this.id = 0;
        this.name = name;
    }

    public TaskList() {
    }

    protected TaskList(Parcel in) {
        id = in.readLong();
        name = in.readString();
    }

    public static final Creator<TaskList> CREATOR = new Creator<TaskList>() {
        @Override
        public TaskList createFromParcel(Parcel in) {
            return new TaskList(in);
        }

        @Override
        public TaskList[] newArray(int size) {
            return new TaskList[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(name);

    }
}
