package carl.coding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class NumberActivity extends AppCompatActivity {

    private final Random random = new Random();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private EditText minInput;
    private EditText maxInput;
    private TextView resultText;
    private Button generateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper.applyNightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        minInput = findViewById(R.id.min_input);
        maxInput = findViewById(R.id.max_input);
        resultText = findViewById(R.id.number_result_text);
        generateButton = findViewById(R.id.generate_number_button);

        minInput.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        minInput.setHintTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        maxInput.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        maxInput.setHintTextColor(ContextCompat.getColor(this, R.color.text_secondary));

        generateButton.setOnClickListener(v -> generateNumber());
    }

    private void generateNumber() {
        String minText = minInput.getText().toString().trim();
        String maxText = maxInput.getText().toString().trim();

        if (TextUtils.isEmpty(minText) || TextUtils.isEmpty(maxText)) {
            Toast.makeText(this, "Enter both min and max.", Toast.LENGTH_SHORT).show();
            return;
        }

        int min;
        int max;
        try {
            min = Integer.parseInt(minText);
            max = Integer.parseInt(maxText);
        } catch (NumberFormatException exception) {
            Toast.makeText(this, "Use valid whole numbers.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }

        generateButton.setEnabled(false);
        generateButton.setText("Generating...");
        resultText.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        resultText.setTextSize(30);
        resultText.setText("...");

        vibrate(60);
        runRollingAnimation(min, max, 10, 70);
    }

    private void runRollingAnimation(int min, int max, int steps, long delayMs) {
        handler.postDelayed(new Runnable() {
            int remainingSteps = steps;

            @Override
            public void run() {
                int preview = random.nextInt((max - min) + 1) + min;
                resultText.setText(String.valueOf(preview));

                if (remainingSteps > 0) {
                    remainingSteps--;
                    handler.postDelayed(this, delayMs);
                } else {
                    finishNumberRoll(min, max);
                }
            }
        }, delayMs);
    }

    private void finishNumberRoll(int min, int max) {
        int result = random.nextInt((max - min) + 1) + min;
        resultText.setText(String.valueOf(result));
        resultText.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        resultText.setTextSize(42);
        resultText.animate().cancel();
        resultText.setScaleX(0.92f);
        resultText.setScaleY(0.92f);
        resultText.animate().scaleX(1f).scaleY(1f).setDuration(140).start();

        generateButton.setEnabled(true);
        generateButton.setText("Generate");

        vibrate(140);
    }

    private void vibrate(long durationMs) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(durationMs);
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
