package carl.coding;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatDelegate;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PreferencesHelper {
    private static final String PREFS = "randomizer_prefs";
    private static final String KEY_DARK_MODE = "dark_mode";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static boolean isDarkModeEnabled(Context context) {
        return getPreferences(context).getBoolean(KEY_DARK_MODE, false);
    }

    public static void setDarkModeEnabled(Context context, boolean enabled) {
        getPreferences(context).edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public static void setString(Context context, String key, String value) {
        getPreferences(context).edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getPreferences(context).getString(key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        getPreferences(context).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getPreferences(context).getBoolean(key, defaultValue);
    }

    public static void setStringList(Context context, String key, List<String> values) {
        JSONArray jsonArray = new JSONArray();
        for (String value : values) {
            if (!TextUtils.isEmpty(value)) {
                jsonArray.put(value);
            }
        }
        setString(context, key, jsonArray.toString());
    }

    public static ArrayList<String> getStringList(Context context, String key) {
        String raw = getString(context, key, "[]");
        ArrayList<String> values = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(raw);
            for (int index = 0; index < jsonArray.length(); index++) {
                String value = jsonArray.optString(index).trim();
                if (!TextUtils.isEmpty(value)) {
                    values.add(value);
                }
            }
        } catch (Exception ignored) {
            return values;
        }

        return values;
    }

    public static void applyNightMode(Context context) {
        AppCompatDelegate.setDefaultNightMode(
                isDarkModeEnabled(context)
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}
