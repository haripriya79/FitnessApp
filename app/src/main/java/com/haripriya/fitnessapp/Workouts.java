package com.haripriya.fitnessapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Workouts implements Parcelable {
    private String Description;
    private String name ;
    private String title;
    private String videoId;

    public Workouts() {
    }

    public Workouts(String description, String name, String title, String videoId) {
        Description = description;
        this.name = name;
        this.title = title;
        this.videoId = videoId;
    }

    protected Workouts(Parcel in) {
        Description = in.readString();
        name = in.readString();
        title = in.readString();
        videoId = in.readString();
    }

    public static final Creator<Workouts> CREATOR = new Creator<Workouts>() {
        @Override
        public Workouts createFromParcel(Parcel in) {
            return new Workouts(in);
        }

        @Override
        public Workouts[] newArray(int size) {
            return new Workouts[size];
        }
    };

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Description);
        parcel.writeString(name);
        parcel.writeString(title);
        parcel.writeString(videoId);
    }
}
