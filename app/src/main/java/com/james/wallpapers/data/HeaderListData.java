package com.james.wallpapers.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

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
    @Nullable public String name, content, url;

    public HeaderListData(@Nullable String name, @Nullable String content, boolean centered, @Nullable String url) {
        this.centered = centered;
        this.name = name;
        this.content = content;
        this.url = url;
    }

    public HeaderListData(Parcel in) {
        ReadFromParcel(in);
    }

    private void ReadFromParcel(Parcel in) {
        centered = in.readInt() == 1;
        name = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(centered ? 1 : 0);
        out.writeString(name);
        out.writeString(content);
        out.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
