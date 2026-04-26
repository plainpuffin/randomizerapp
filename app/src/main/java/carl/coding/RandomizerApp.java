package carl.coding;

import android.app.Application;

public class RandomizerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesHelper.applyNightMode(this);
    }
}
