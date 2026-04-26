package carl.coding;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class PreferencesHelper {
    private static final String PREFS = "randomizer_prefs";
    private static final String KEY_DARK_MODE = "dark_mode";

    public static boolean isDarkModeEnabled(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getBoolean(KEY_DARK_MODE, false);
    }

    public static void setDarkModeEnabled(Context context, boolean enabled) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public static void applyNightMode(Context context) {
        AppCompatDelegate.setDefaultNightMode(
                isDarkModeEnabled(context)
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}
