package com.chiragagg5k.bu_news_android;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.time.LocalDate;

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

    public static String getDate(LocalDate localDate){
        String day = UtilityClass.convertToTitleCase(localDate.getDayOfWeek().toString());
        String date = localDate.getDayOfMonth() + "";
        String month = UtilityClass.convertToTitleCase(localDate.getMonth().toString());

        return day + ", " + date + " " + month;
    }

    public static String getCurrentDate() {
        LocalDate localDate = LocalDate.now();
        return getDate(localDate);
    }
}
