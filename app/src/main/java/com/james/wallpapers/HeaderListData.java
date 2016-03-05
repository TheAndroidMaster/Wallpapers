package com.james.wallpapers;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URISyntaxException;

public class HeaderListData implements Parcelable {

    public static final Creator<HeaderListData> CREATOR = new Creator<HeaderListData>() {
        public HeaderListData createFromParcel(Parcel in) {
            return new HeaderListData(in);
        }

        public HeaderListData[] newArray(int size) {
            return new HeaderListData[size];
        }
    };

    public boolean centered;
    public String name;
    public String content;
    public Intent primary;

    public HeaderListData(String name, String content, boolean centered, Intent primary) {
        this.centered = centered;
        this.name = name;
        this.content = content;
        this.primary = primary;
    }

    public HeaderListData(Parcel in) {
        ReadFromParcel(in);
    }

    private void ReadFromParcel(Parcel in) {
        centered = in.readInt() == 1;
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
        out.writeInt(centered ? 1 : 0);
        out.writeString(name);
        out.writeString(content);
        out.writeString(primary.toUri(0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
