package com.cs360.williambingham.bingham_william_c360_final_project.locator;

public class GalleryItem {
    private String mCaption;
    private String mId;
    private double mLat;
    private double mLon;


    public void setCaption(String caption) {
        mCaption = caption;
    }
    public String getId() {
        return mId;
    }
    public void setId(String id) {
        mId = id;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(double lon) {
        mLon = lon;
    }

    @Override
    public String toString() {
        return mCaption;
    }
}
