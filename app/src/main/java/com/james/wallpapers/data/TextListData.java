package com.james.wallpapers.data;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URISyntaxException;

public class TextListData implements Parcelable {

    public static final Creator<TextListData> CREATOR = new Creator<TextListData>() {
        public TextListData createFromParcel(Parcel in) {
            return new TextListData(in);
        }

        public TextListData[] newArray(int size) {
            return new TextListData[size];
        }
    };

    public String name;
    public String content;
    public Intent primary;

    public TextListData(String name, String content, Intent primary) {
        this.name = name;
        this.content = content;
        this.primary = primary;
    }

    public TextListData(Parcel in) {
        ReadFromParcel(in);
    }

    private void ReadFromParcel(Parcel in) {
        name = in.readString();
        content = in.readString();
        try {
            primary = Intent.parseUri(in.readString(), 0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(content);
        out.writeString(primary.toUri(0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
