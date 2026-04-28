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

    private static final String KEY_NUMBER_MIN = "number_min";
    private static final String KEY_NUMBER_MAX = "number_max";
    private static final float RESULT_TEXT_SIZE_SP = 42f;

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

        minInput.setText(PreferencesHelper.getString(this, KEY_NUMBER_MIN, ""));
        maxInput.setText(PreferencesHelper.getString(this, KEY_NUMBER_MAX, ""));

        generateButton.setOnClickListener(v -> generateNumber());
    }

    private void generateNumber() {
        String minText = minInput.getText().toString().trim();
        String maxText = maxInput.getText().toString().trim();

        if (TextUtils.isEmpty(minText) || TextUtils.isEmpty(maxText)) {
            minInput.setError(TextUtils.isEmpty(minText) ? "Required" : null);
            maxInput.setError(TextUtils.isEmpty(maxText) ? "Required" : null);
            Toast.makeText(this, "Enter both min and max.", Toast.LENGTH_SHORT).show();
            return;
        }

        minInput.setError(null);
        maxInput.setError(null);

        long min;
        long max;
        try {
            min = Long.parseLong(minText);
            max = Long.parseLong(maxText);
        } catch (NumberFormatException exception) {
            minInput.setError("Invalid number");
            maxInput.setError("Invalid number");
            Toast.makeText(this, "Use valid whole numbers.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (min > max) {
            minInput.setError("Must be less than or equal to max");
            maxInput.setError("Must be greater than or equal to min");
            Toast.makeText(this, "Minimum must be less than or equal to maximum.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (min == max) {
            minInput.setError("Must be strictly less than max");
            maxInput.setError("Must be strictly greater than min");
            Toast.makeText(this, "Maximum must be greater than minimum.", Toast.LENGTH_SHORT).show();
            return;
        }

        PreferencesHelper.setString(this, KEY_NUMBER_MIN, minText);
        PreferencesHelper.setString(this, KEY_NUMBER_MAX, maxText);

        generateButton.setEnabled(false);
        generateButton.setText("Generating...");
        resultText.setTextColor(ContextCompat.getColor(this, R.color.text_feedback_muted));
        resultText.setTextSize(RESULT_TEXT_SIZE_SP);
        resultText.setText("...");

        vibrate(60);
        runRollingAnimation(min, max, 10, 70);
    }

    private void runRollingAnimation(long min, long max, int steps, long delayMs) {
        handler.postDelayed(new Runnable() {
            int remainingSteps = steps;

            @Override
            public void run() {
                resultText.setText(String.valueOf(nextRandomLong(min, max)));

                if (remainingSteps > 0) {
                    remainingSteps--;
                    handler.postDelayed(this, delayMs);
                } else {
                    finishNumberRoll(min, max);
                }
            }
        }, delayMs);
    }

    private void finishNumberRoll(long min, long max) {
        long result = nextRandomLong(min, max);
        resultText.setText(String.valueOf(result));
        resultText.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        resultText.setTextSize(RESULT_TEXT_SIZE_SP);
        resultText.animate().cancel();
        resultText.setScaleX(0.92f);
        resultText.setScaleY(0.92f);
        resultText.animate().scaleX(1f).scaleY(1f).setDuration(140).start();

        generateButton.setEnabled(true);
        generateButton.setText("Generate");

        vibrate(140);
    }

    private long nextRandomLong(long min, long max) {
        if (min == max) {
            return min;
        }

        double range = (double) max - (double) min + 1d;
        return min + (long) Math.floor(random.nextDouble() * range);
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
    protected void onPause() {
        PreferencesHelper.setString(this, KEY_NUMBER_MIN, minInput.getText().toString().trim());
        PreferencesHelper.setString(this, KEY_NUMBER_MAX, maxInput.getText().toString().trim());
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
