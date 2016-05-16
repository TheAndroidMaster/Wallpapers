package com.james.wallpapers.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;

public class WallData implements Parcelable {
    public String name, url, authorName;
    public int authorId;
    public boolean favorite, credit;

    public WallData(Context context, String name, String url, String authorName, int authorId, boolean credit) {
        this.name = name;
        this.url = url;
        this.authorName = authorName;
        this.authorId = authorId;
        favorite = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(url, false);
        this.credit = credit;
    }

    public void setFavorite(Context context, boolean favorite) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(url, favorite).apply();
        this.favorite = favorite;
    }

    protected WallData(Parcel in) {
        name = in.readString();
        url = in.readString();
        authorName = in.readString();
        authorId = in.readInt();
        favorite = in.readInt() == 1;
        credit = in.readInt() == 1;
    }

    public static final Creator<WallData> CREATOR = new Creator<WallData>() {
        @Override
        public WallData createFromParcel(Parcel in) {
            return new WallData(in);
        }

        @Override
        public WallData[] newArray(int size) {
            return new WallData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(authorName);
        dest.writeInt(authorId);
        dest.writeInt(favorite ? 1 : 0);
        dest.writeInt(credit ? 1 : 0);
    }
}
