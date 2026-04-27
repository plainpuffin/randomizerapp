package carl.coding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class StartMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper.applyNightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        ImageButton themeToggleButton = findViewById(R.id.themeToggleButton);
        refreshThemeToggleButton(themeToggleButton);
        themeToggleButton.setOnClickListener(v -> {
            boolean enableDarkMode = !PreferencesHelper.isDarkModeEnabled(StartMenu.this);
            PreferencesHelper.setDarkModeEnabled(StartMenu.this, enableDarkMode);
            PreferencesHelper.applyNightMode(StartMenu.this);
            recreate();
        });
    }

    private void refreshThemeToggleButton(ImageButton themeToggleButton) {
        boolean darkModeEnabled = PreferencesHelper.isDarkModeEnabled(this);
        themeToggleButton.setImageResource(darkModeEnabled ? R.drawable.ic_theme_sun : R.drawable.ic_theme_moon);
        themeToggleButton.setContentDescription(darkModeEnabled ? "Switch to light mode" : "Switch to dark mode");
    }

    public void goCoin(View view) {
        startScreen(CoinActivity.class);
    }

    public void goDice(View view) {
        startScreen(DiceActivity.class);
    }

    public void goCategories(View view) {
        startScreen(CategoriesSetupActivity.class);
    }

    public void goTeams(View view) {
        startScreen(TeamSetupActivity.class);
    }

    public void goNumbers(View view) {
        startScreen(NumberActivity.class);
    }

    public void goYesNo(View view) {
        startScreen(YesNoActivity.class);
    }

    public void goYatzy(View view) {
        startScreen(YatzyActivity.class);
    }

    private void startScreen(Class<?> target) {
        Intent intent = new Intent(this, target);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
