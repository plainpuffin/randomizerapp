package carl.coding;

import android.os.Bundle;
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
    private EditText minInput;
    private EditText maxInput;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper.applyNightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        minInput = findViewById(R.id.min_input);
        maxInput = findViewById(R.id.max_input);
        resultText = findViewById(R.id.number_result_text);
        Button generateButton = findViewById(R.id.generate_number_button);

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

        int min = Integer.parseInt(minText);
        int max = Integer.parseInt(maxText);
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }

        int result = random.nextInt((max - min) + 1) + min;
        resultText.setText(String.valueOf(result));

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
