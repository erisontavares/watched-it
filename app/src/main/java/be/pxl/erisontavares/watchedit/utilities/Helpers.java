package be.pxl.erisontavares.watchedit.utilities;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Helpers {
    private Helpers() {}

    private static final String TAG = Helpers.class.getSimpleName();

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e(TAG, "ParseException - dateFormat");
        }

        return outputDate;
    }

    // TODO: Refactor this method to arrays values
    public static String getListSortTypeBySetting(String value) {
        switch (value) {
            case "1":
                return "title ASC";
            case "2":
                return "title DESC";
            case "3":
                return "added_date DESC";
            case "4":
                return "added_date ASC";
            case "5":
                return "release_date DESC";
            case "6":
                return "release_date ASC";
        }
        return "";
    }
}
