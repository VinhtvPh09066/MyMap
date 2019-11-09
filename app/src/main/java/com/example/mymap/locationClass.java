package com.example.mymap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class locationClass {
    @PrimaryKey
    public double id;

    @ColumnInfo(name = "lat")
    public double lat;

    @ColumnInfo(name = "lng")
    public double lng;

    @ColumnInfo(name = "tagName")
    public String tagName;

    public locationClass(double id, double lat, double lng, String tagName) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.tagName = tagName;
    }

    public locationClass() {
    }

    @Override
    public String toString() {
        return tagName + " - " + lat + " - " + lng;

    }
}
