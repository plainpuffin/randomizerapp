package carl.coding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class StartMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper.applyNightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        SwitchCompat darkModeSwitch = findViewById(R.id.darkModeSwitch);
        darkModeSwitch.setChecked(PreferencesHelper.isDarkModeEnabled(this));
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            PreferencesHelper.setDarkModeEnabled(StartMenu.this, isChecked);
            PreferencesHelper.applyNightMode(StartMenu.this);
            recreate();
        });
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
