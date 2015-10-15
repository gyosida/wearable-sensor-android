package com.belatrix.wearablegyroscope.realtime;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gianfranco on 10/6/15.
 */
public class GyroscopeStat implements Parcelable {

    public float targetRotationX;
    public float targetRotationY;

    public GyroscopeStat() {}

    protected GyroscopeStat(Parcel in) {
        targetRotationX = in.readFloat();
        targetRotationY = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(targetRotationX);
        dest.writeFloat(targetRotationY);
    }

    @SuppressWarnings("unused")
    public transient static final Parcelable.Creator<GyroscopeStat> CREATOR = new Parcelable.Creator<GyroscopeStat>() {
        @Override
        public GyroscopeStat createFromParcel(Parcel in) {
            return new GyroscopeStat(in);
        }

        @Override
        public GyroscopeStat[] newArray(int size) {
            return new GyroscopeStat[size];
        }
    };
}
