package com.example.todolist.entities;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class Task implements Parcelable {

    private static final int NULL_ELEMENT = 0;
    private static final int NONNULL_ELEMENT = 1;

    private long id;
    private long listId;
    private String title;

    private Optional<String> text;

    private Optional<LocalDate> endDate;

    private  Optional<LocalDateTime> dateWhenDone;

    private boolean isCompleted;
    private boolean isTagged;

    public Task(long id, long listId, String title,
                Optional<String> text,
                Optional<LocalDate> endDate,
                Optional<LocalDateTime> dateWhenDone,
                boolean isCompleted, boolean isTagged) {
        this.id = id;
        this.listId = listId;
        this.title = title;
        this.text = text;
        this.endDate = endDate;
        this.dateWhenDone = dateWhenDone;
        this.isCompleted = isCompleted;
        this.isTagged = isTagged;
    }

    public Task(long listId, String title,
                Optional<String> text,
                Optional<LocalDate> endDate,
                Optional<LocalDateTime> dateWhenDone,
                boolean isCompleted, boolean isTagged) {
        this.id = 0;
        this.listId = listId;
        this.title = title;
        this.text = text;
        this.endDate = endDate;
        this.isCompleted = isCompleted;
        this.isTagged = isTagged;
    }

    public Task() {
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Task(Parcel in) {
        id = in.readLong();
        listId = in.readLong();
        title = in.readString();
        isCompleted = in.readBoolean();
        isTagged = in.readBoolean();

        text = in.readInt() == NONNULL_ELEMENT ? Optional.of(in.readString()) : Optional.empty();
        endDate = in.readInt() == NONNULL_ELEMENT ? Optional.of((LocalDate) in.readSerializable()) : Optional.empty();
        dateWhenDone = in.readInt() == NONNULL_ELEMENT ? Optional.of((LocalDateTime) in.readSerializable()) : Optional.empty();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Optional<String> getText() {
        return text;
    }

    public void setText(Optional<String> text) {
        this.text = text;
    }

    public Optional<LocalDate> getEndDate() {
        return endDate;
    }

    public void setEndDate(Optional<LocalDate> endDate) {
        this.endDate = endDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isTagged() {
        return isTagged;
    }

    public void setTagged(boolean tagged) {
        isTagged = tagged;
    }

    public Optional<LocalDateTime> getDateWhenDone() {
        return dateWhenDone;
    }

    public void setDateWhenDone(Optional<LocalDateTime> dateWhenDone) {
        this.dateWhenDone = dateWhenDone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeLong(listId);
        parcel.writeString(title);
        parcel.writeBoolean(isCompleted);
        parcel.writeBoolean(isTagged);


        if (text.isPresent()){
            parcel.writeInt(NONNULL_ELEMENT);
            parcel.writeString(text.get());
        }
        else{
            parcel.writeInt(NULL_ELEMENT);
        }

        if (endDate.isPresent()){
            parcel.writeInt(NONNULL_ELEMENT);
            parcel.writeSerializable(endDate.get());
        }
        else{
            parcel.writeInt(NULL_ELEMENT);
        }

        if (dateWhenDone.isPresent()){
            parcel.writeInt(NONNULL_ELEMENT);
            parcel.writeSerializable(dateWhenDone.get());
        }
        else{
            parcel.writeInt(NULL_ELEMENT);
        }

    }
}