package com.james.wallpapers.data;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.net.URISyntaxException;

public class PersonListData implements Parcelable {

    public static final Creator<PersonListData> CREATOR = new Creator<PersonListData>() {
        public PersonListData createFromParcel(Parcel in) {
            return new PersonListData(in);
        }

        public PersonListData[] newArray(int size) {
            return new PersonListData[size];
        }
    };

    public String icon, name, content;
    @Nullable public String url;

    public PersonListData(String icon, String name, String content, @Nullable String url) {
        this.icon = icon;
        this.name = name;
        this.content = content;
        this.url = url;
    }

    public PersonListData(Parcel in) {
        ReadFromParcel(in);
    }

    private void ReadFromParcel(Parcel in) {
        icon = in.readString();
        name = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(icon);
        out.writeString(name);
        out.writeString(content);
        out.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
