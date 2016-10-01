package com.james.wallpapers;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

import com.james.wallpapers.data.AuthorData;
import com.james.wallpapers.data.HeaderListData;
import com.james.wallpapers.data.WallData;

import java.util.ArrayList;

public class Supplier extends Application {

    public boolean getNetworkResources() {
        //download any resources needed for the voids below while the splash screen is showing
        //yes, this is thread-safe
        //no, it is not needed for the current setup since all the resources are in res/values/strings.xml
        return true;
    }

    //get a list of the different sections
    public ArrayList<AuthorData> getAuthors(Context context) {
        ArrayList<AuthorData> authors = new ArrayList<>();
        String[] authorNames = context.getResources().getStringArray(R.array.people_names);
        String[] authorIcons = context.getResources().getStringArray(R.array.people_icons);
        String[] authorDescs = context.getResources().getStringArray(R.array.people_desc);
        String[] authorUrls = null;
        try {
            authorUrls = context.getResources().getStringArray(R.array.people_urls);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < authorNames.length; i++) {
            //specify section name, icon url, description, id, and url
            authors.add(new AuthorData(authorNames[i], authorIcons[i], authorDescs[i], i, authorUrls == null ? null : authorUrls[i]));
        }

        return authors;
    }

    //get a list of the different wallpapers
    public ArrayList<WallData> getWallpapers(Context context) {
        ArrayList<WallData> walls = new ArrayList<>();

        String[] authorNames = context.getResources().getStringArray(R.array.people_names);
        TypedArray wallNames = context.getResources().obtainTypedArray(R.array.wp_names);
        TypedArray wallUrls = context.getResources().obtainTypedArray(R.array.wp_urls);

        for (int i = 0; i < wallNames.length(); i++) {
            String[] names = context.getResources().getStringArray(wallNames.getResourceId(i, -1));
            String[] urls = context.getResources().getStringArray(wallUrls.getResourceId(i, -1));

            for (int i2 = 0; i2 < names.length; i2++) {
                //specify context, wallpaper name, wallpaper url, author name, author id, and whether credit is required (see values/strings.xml for more info)
                walls.add(new WallData(context, names[i2].replace("*", ""), urls[i2], authorNames[i], i, names[i].endsWith("*")));
            }
        }

        wallNames.recycle();
        wallUrls.recycle();

        return walls;
    }

    public ArrayList<WallData> getWallpapers(Context context, int authorId) {
        ArrayList<WallData> walls = new ArrayList<>();

        TypedArray wallNames = context.getResources().obtainTypedArray(R.array.wp_names);
        TypedArray wallUrls = context.getResources().obtainTypedArray(R.array.wp_urls);

        String[] names = context.getResources().getStringArray(wallNames.getResourceId(authorId, -1));
        String[] urls = context.getResources().getStringArray(wallUrls.getResourceId(authorId, -1));
        String[] authors = context.getResources().getStringArray(R.array.people_names);

        for (int i = 0; i < names.length; i++) {
            //specify context, wallpaper name, wallpaper url, author name, author id, and whether credit is required (see values/strings.xml for more info)
            walls.add(new WallData(context, names[i].replace("*", ""), urls[i], authors[authorId], authorId, names[i].endsWith("*")));
        }

        wallNames.recycle();
        wallUrls.recycle();

        return walls;
    }

    //additional info to put in the about section
    public ArrayList<HeaderListData> getAdditionalInfo(Context context) {
        ArrayList<HeaderListData> headers = new ArrayList<>();
        //specify title (optional), description (optional), whether to center the content, and a url (optional)

        return headers;
    }

    public AlertDialog getCreditDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        //dialog to be shown when credit is required
        return new AlertDialog.Builder(context)
                .setTitle(R.string.credit_required)
                .setMessage(R.string.credit_required_msg)
                .setPositiveButton("OK", onClickListener)
                .create();
    }

    public void downloadWallpaper(Context context, WallData data) {
        //start a download
        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(data.url));
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, data.name + ".png");
        r.allowScanningByMediaScanner();
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        dm.enqueue(r);
    }

    public AlertDialog getDownloadedDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        //dialog to be shown upon completion of a download
        return new AlertDialog.Builder(context).setTitle(R.string.download_complete).setMessage(R.string.download_complete_msg).setPositiveButton("View", onClickListener).create();
    }

    //share a wallpaper
    public void shareWallpaper(Context context, WallData data) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, String.valueOf(Uri.parse(data.url)));
        context.startActivity(intent);
    }
}
