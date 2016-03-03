package com.james.wallpapers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StrictMode;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class Cache {

    private static String codename = "f0naxwa115";

    public static void saveDrawable(String folder, String name, Drawable image, Context context) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

        File file = new File(extStorageDirectory + "/" + codename, codename + folder + name);

        if (!file.exists()) {
            File dir = new File(extStorageDirectory + "/" + codename);
            dir.mkdir();
        }

        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "error creating file", Toast.LENGTH_SHORT).show();
        }

        ((BitmapDrawable) image).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, outStream);
        try {
            outStream.flush();
        } catch(IOException e) {
            Toast.makeText(context, "error flushing output stream", Toast.LENGTH_SHORT).show();
        }
        try {
            outStream.close();
        } catch(IOException e) {
            Toast.makeText(context, "error closing output stream", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean dir(String folder, Context context) {

        File storage = new File(Environment.getExternalStorageDirectory().getPath() + "/" + codename);

        File[] files = storage.listFiles();

        if (files != null) {
            for (File file : files) {
                if(file.getName().contains(codename + folder) && !file.getName().toLowerCase().contains(".png")) return true;
            }
        }
        return false;
    }

    public static Drawable getDrawable(String folder, String name, String src, Context context) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        File f = new File(Environment.getExternalStorageDirectory() + "/" + codename + "/" + codename + folder + name);
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            if (bmp.getHeight() * bmp.getWidth() > 4500000) {
                bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/2, bmp.getHeight()/2, true);
            }
            return new BitmapDrawable(context.getResources(), bmp);
        } catch (Exception e) {
            e.printStackTrace();
            return downloadDrawable(src, context);
        }
    }

    public static Drawable getCompressedDrawable(String folder, String name, String src, Context context) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        File f = new File(Environment.getExternalStorageDirectory() + "/" + codename + "/" + codename + folder + name);
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            o.inSampleSize = 4;

            return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(BitmapFactory.decodeStream(new FileInputStream(f), null, o), o.outWidth/2, o.outHeight/2, true));
        } catch (Exception e) {
            e.printStackTrace();
            return downloadCompressedDrawable(src, context);
        }
    }

    public static Drawable downloadDrawable (String src, Context context) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bmp = BitmapFactory.decodeStream(input);
            connection.disconnect();
            if (bmp.getHeight() * bmp.getWidth() > 4500000) {
                bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/2, bmp.getHeight()/2, true);
            }
            return new BitmapDrawable(context.getResources(), bmp);
        } catch (Exception e) {
            e.printStackTrace();
            return context.getResources().getDrawable(R.mipmap.wifioff);
        }
    }

    public static Drawable downloadCompressedDrawable(String src, Context context) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = 4;

            Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(input, null, o2), o2.outWidth / 2, o2.outHeight / 2, true);

            connection.disconnect();
            //bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 6, bmp.getHeight() / 6, false);
            return new BitmapDrawable(context.getResources(), bmp);
        } catch (Exception e) {
            e.printStackTrace();
            return context.getResources().getDrawable(R.mipmap.wifioff);
        }
    }

    public static void delete(String folder, Context context) {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + codename);

        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if(file.getName().contains(codename + folder)) file.delete();
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
}
