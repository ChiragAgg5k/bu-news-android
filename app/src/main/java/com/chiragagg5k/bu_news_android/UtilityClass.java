package com.chiragagg5k.bu_news_android;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Utility class to hold the methods which are used in multiple classes
 */
public class UtilityClass {

    /**
     * Method to convert the string to title case
     *
     * @param text String to be converted
     * @return Converted string
     */
    public static String convertToTitleCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

    /**
     * Method to get the name of the file from the uri
     *
     * @param resolver ContentResolver
     * @param uri      Uri of the file
     * @return Name of the file
     */
    public static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    public static String getDate(long dateInMilliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMilliSeconds);
        int date = calendar.get(Calendar.DATE);
        String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        return day + ", " + date + " " + month;
    }


    public static long getCurrentDateInMilliSeconds() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String arrayListToString(ArrayList<String> arrayList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (i != arrayList.size() - 1) {
                stringBuilder.append(", ");
            } else {
                stringBuilder.append(".");
            }
        }
        return stringBuilder.toString();
    }
}
