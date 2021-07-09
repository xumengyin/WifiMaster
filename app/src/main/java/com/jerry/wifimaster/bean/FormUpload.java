package com.jerry.wifimaster.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FormUpload implements Parcelable {
    @SerializedName("name")
    private String mName;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("file1")
    private String mFile1;
    @SerializedName("file2")
    private String mFile2;
    @SerializedName("file3")
    private String mFile3;


    protected FormUpload(Parcel in) {
        mName = in.readString();
        mPassword = in.readString();
        mFile1 = in.readString();
        mFile2 = in.readString();
        mFile3 = in.readString();
    }

    public static final Creator<FormUpload> CREATOR = new Creator<FormUpload>() {
        @Override
        public FormUpload createFromParcel(Parcel in) {
            return new FormUpload(in);
        }

        @Override
        public FormUpload[] newArray(int size) {
            return new FormUpload[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mPassword);
        dest.writeString(mFile1);
        dest.writeString(mFile2);
        dest.writeString(mFile3);
    }
}
