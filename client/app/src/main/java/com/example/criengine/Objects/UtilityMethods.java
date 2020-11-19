package com.example.criengine.Objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Contains static utility methods that are used by multiple other classes.
 */
public class UtilityMethods {
    /**
     * Retrieved from:
     * URL: https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
     * Date: 2020-11-18
     * @param encodedString The encoded string which can be converted to a bitmap.
     * @return The bitmap.
     */
    public static Bitmap stringToBitMap(String encodedString){
        try {
            byte[] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    /**
     * Retrieved from:
     * URL: https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
     * Date: 2020-11-18
     * @param bitmap The bitmap to be converted to a string.
     * @return The string.
     */
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
