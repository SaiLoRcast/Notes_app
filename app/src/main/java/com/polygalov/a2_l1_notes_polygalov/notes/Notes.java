package com.polygalov.a2_l1_notes_polygalov.notes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Константин on 22.09.2017.
 */

public class Notes implements Parcelable {

    private int id;
    private String title;
    private String message;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.message);
        parcel.writeString(this.date);
    }

    public Notes() {
        //Пустрой конструктор
    }

    protected Notes(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.message = in.readString();
        this.date = in.readString();
    }

    public static final Parcelable.Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel parcel) {
            return new Notes(parcel);
        }

        @Override
        public Notes[] newArray(int arraySize) {
            return new Notes[arraySize];
        }
    };
}