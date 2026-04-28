package carl.coding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Random;

public class CategoriesActivity extends AppCompatActivity {

    private static final float RESULT_TEXT_SIZE_SP = 38f;

    private ArrayList<String> originalList;
    private ArrayList<String> activeList;
    private TextView resultText;
    private TextView poolStatusText;
    private Button pickButton;
    private Button resetButton;
    private Random random;
    private Handler handler;
    private Runnable rollingRunnable;
    private Vibrator vibrator;
    private boolean withdrawMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper.applyNightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        originalList = getIntent().getStringArrayListExtra("elements");
        if (originalList == null) {
            originalList = new ArrayList<>();
        }
        activeList = new ArrayList<>(originalList);
        withdrawMode = getIntent().getBooleanExtra("withdrawMode", false);

        resultText = findViewById(R.id.result_text);
        poolStatusText = findViewById(R.id.pool_status_text);
        pickButton = findViewById(R.id.pick_button);
        resetButton = findViewById(R.id.reset_button);

        random = new Random();
        handler = new Handler();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        pickButton.setOnClickListener(v -> pickNextElement());
        resetButton.setOnClickListener(v -> resetPool());

        resetButton.setVisibility(withdrawMode ? View.VISIBLE : View.GONE);
        updatePoolStatus();
    }

    private void pickNextElement() {
        if (activeList.isEmpty()) {
            resultText.setText("Pool empty");
            updatePoolStatus();
            pickButton.setEnabled(false);
            return;
        }

        pickButton.setEnabled(false);
        rollingRunnable = new Runnable() {
            @Override
            public void run() {
                int randomIndex = random.nextInt(activeList.size());
                String randomElement = activeList.get(randomIndex);
                resultText.setText(randomElement);
                resultText.setTextColor(ContextCompat.getColor(CategoriesActivity.this, R.color.text_primary));
                resultText.setTextSize(RESULT_TEXT_SIZE_SP);
                if (withdrawMode) {
                    activeList.remove(randomIndex);
                }
                updatePoolStatus();
                pickButton.setEnabled(!activeList.isEmpty());
            }
        };

        if (vibrator != null) {
            vibrator.vibrate(250);
        }
        rollThroughElements();
    }

    private void rollThroughElements() {
        int duration = 800;
        int interval = 100;
        int iterations = duration / interval;
        resultText.setTextSize(RESULT_TEXT_SIZE_SP);
        resultText.setTextColor(ContextCompat.getColor(this, R.color.text_feedback_muted));

        handler.postDelayed(new Runnable() {
            int iteration = 0;
            int currentIndex = 0;

            @Override
            public void run() {
                if (activeList.isEmpty()) {
                    handler.post(rollingRunnable);
                    return;
                }
                if (iteration < iterations) {
                    resultText.setText(activeList.get(currentIndex % activeList.size()));
                    currentIndex++;
                    iteration++;
                    handler.postDelayed(this, interval);
                } else {
                    handler.post(rollingRunnable);
                }
            }
        }, interval);
    }

    private void resetPool() {
        activeList.clear();
        activeList.addAll(originalList);
        resultText.setText("Ready");
        resultText.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        pickButton.setEnabled(!activeList.isEmpty());
        updatePoolStatus();
    }

    private void updatePoolStatus() {
        if (withdrawMode) {
            if (activeList.isEmpty()) {
                poolStatusText.setText("Withdraw mode on, all categories have been used.");
            } else {
                poolStatusText.setText("Withdraw mode on, " + activeList.size() + " left in the pool.");
            }
        } else {
            poolStatusText.setText("Standard mode, picks stay in the pool.");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
