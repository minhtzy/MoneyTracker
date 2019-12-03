package com.minhtzy.moneytracker.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import com.minhtzy.moneytracker.App;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class ResourceUtils {

    private static final String IMAGE_DIRECTORY = "/money_tracker_media";

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmap(byte[] datas) {
        return BitmapFactory.decodeByteArray(datas,0,datas.length);
    }

    public static String saveImage(Context context,Bitmap myBitmap) {
        byte[] datas = getBytes(myBitmap);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(datas);
            MediaScannerConnection.scanFile(context,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static Bitmap loadImageFromStorage(String uri)
    {
        if(uri == null ) return null;
        try {
            File f=new File(uri);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static final String CATEGORY_BASEPATH = "category";
    public static Drawable getCategoryIcon(String filename)
    {
        try {
            Drawable img = Drawable.createFromStream(App.self().getAssets().open(CATEGORY_BASEPATH + "/" + filename), null);
            return img;
        } catch (IOException e) {
            return null;
        }
    }

    public static final String CURRENCY_BASEPATH = "currency";
    public static Drawable getCurrencyIcon(String currencyCode)
    {
        try {
            Drawable img = Drawable.createFromStream(App.self().getAssets().open(String.format("%s/ic_currency_%s.png",CURRENCY_BASEPATH,currencyCode.toLowerCase())), null);
            return img;
        } catch (IOException e) {
            return null;
        }
    }

    public static final String WALLET_BASEPATH = "wallet";
    public static Drawable getWalletIcon(String fileName) {
        try {
            Drawable img = Drawable.createFromStream(App.self().getAssets().open(WALLET_BASEPATH + "/" + fileName), null);
            return img;
        } catch (IOException e) {
            return null;
        }
    }

    public static String getDefaultWalletIcon() {
        return "icon.png";
    }
}
