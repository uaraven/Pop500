package net.ninjacat.pop500.logger;

import android.util.Log;
import net.ninjacat.pop500.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: raven
 * Date: 08.10.12
 */
public class Logger {
    private static final String TAG = "Pop500";
    private static final String MSG_FORMAT = "[%s][%s]%s";
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("hh:MM:ss");


    private static String date() {
        return FORMAT.format(new Date());
    }

    private static String formatMessage(String userText) {
        return String.format(MSG_FORMAT, date(), Thread.currentThread().getName(), userText);
    }

    public static void debug(String text, Object... params) {
        if (BuildConfig.DEBUG) {
            String userText = String.format(text, params);
            Log.d(TAG, formatMessage(userText));
        }
    }

    public static void error(String text, Throwable e) {
        Log.e(TAG, formatMessage(text), e);
    }

    public static void info(String text, Object... params) {
        Log.d(TAG, formatMessage(String.format(text, params)));
    }
}
