package com.james.wallpapers;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

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

    public int drawableRes;
    public String name;
    public String content;
    public Intent primary;

    public PersonListData(int drawableRes, String name, String content, Intent primary) {
        this.drawableRes = drawableRes;
        this.name = name;
        this.content = content;
        this.primary = primary;
    }

    public PersonListData(Parcel in) {
        ReadFromParcel(in);
    }

    private void ReadFromParcel(Parcel in) {
        drawableRes = in.readInt();
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
        out.writeInt(drawableRes);
        out.writeString(name);
        out.writeString(content);
        out.writeString(primary.toUri(0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
