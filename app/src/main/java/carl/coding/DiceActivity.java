package carl.coding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Vibrator;

import java.util.Random;

public class DiceActivity extends AppCompatActivity {

    private int currentRoll = 1;

    Button rollButton;
    Drawable dice1Drawable;
    Drawable dice2Drawable;
    Drawable dice3Drawable;
    Drawable dice4Drawable;
    Drawable dice5Drawable;
    Drawable dice6Drawable;
    Handler handler;
    ImageView diceImageView1;
    Random randomizer;
    TextView rollResult;
    Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper.applyNightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        randomizer = new Random();
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        handler = new Handler();

        dice1Drawable = ContextCompat.getDrawable(this, R.drawable.dice_1_512);
        dice2Drawable = ContextCompat.getDrawable(this, R.drawable.dice_2_512);
        dice3Drawable = ContextCompat.getDrawable(this, R.drawable.dice_3_512);
        dice4Drawable = ContextCompat.getDrawable(this, R.drawable.dice_4_512);
        dice5Drawable = ContextCompat.getDrawable(this, R.drawable.dice_5_512);
        dice6Drawable = ContextCompat.getDrawable(this, R.drawable.dice_6_512);

        rollResult = findViewById(R.id.rollResult);
        rollButton = findViewById(R.id.rollButton);
        diceImageView1 = findViewById(R.id.ImageView1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void roll(View view) {
        rollButton.setEnabled(false);
        rollResult.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));

        if (vib != null) {
            vib.vibrate(300);
        }

        int[] resultInt = {1, 2};

        for (int i = 0; i <= 6; i++) {
            int delay = 70 * i + (i + 10) * (i + 10);
            handler.postDelayed(() -> {
                diceImageView1.setScaleX(0.8f);
                diceImageView1.setScaleY(0.8f);
                resultInt[1] = resultInt[0];
                resultInt[0] = randomizer.nextInt(6) + 1;
                if (resultInt[0] == resultInt[1]) {
                    if (resultInt[0] == 6) {
                        resultInt[0] = 1;
                    } else {
                        resultInt[0] += 1;
                    }
                }
                resultToImage(resultInt[0]);
                rollResult.setText(String.valueOf(resultInt[0]));
                currentRoll = resultInt[0];
            }, delay);
        }

        handler.postDelayed(() -> {
            diceImageView1.setScaleX(1f);
            diceImageView1.setScaleY(1f);
            resultToImage(currentRoll);
            rollResult.setText(String.valueOf(currentRoll));
            rollResult.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            rollButton.setEnabled(true);
        }, 6 * 70 + (6 + 10) * (6 + 10));
    }

    private void resultToImage(int rollResult) {
        if (rollResult == 1) {
            diceImageView1.setImageDrawable(dice1Drawable);
        } else if (rollResult == 2) {
            diceImageView1.setImageDrawable(dice2Drawable);
        } else if (rollResult == 3) {
            diceImageView1.setImageDrawable(dice3Drawable);
        } else if (rollResult == 4) {
            diceImageView1.setImageDrawable(dice4Drawable);
        } else if (rollResult == 5) {
            diceImageView1.setImageDrawable(dice5Drawable);
        } else if (rollResult == 6) {
            diceImageView1.setImageDrawable(dice6Drawable);
        }
    }
}
