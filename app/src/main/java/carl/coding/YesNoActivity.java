package carl.coding;

import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class YesNoActivity extends AppCompatActivity {

    private final Random random = new Random();
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper.applyNightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yes_no);

        resultText = findViewById(R.id.yes_no_result_text);
        Button askButton = findViewById(R.id.ask_yes_no_button);
        askButton.setOnClickListener(v -> pickAnswer());
    }

    private void pickAnswer() {
        boolean yes = random.nextBoolean();
        resultText.setText(yes ? "YES" : "NO");
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(140);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
