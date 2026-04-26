package carl.coding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class CoinActivity extends AppCompatActivity {

    Button flipButton;
    Drawable headsDrawable;
    Drawable tailsDrawable;
    Handler handler;
    ImageView coinImageView;
    int animationDuration = 500;
    ObjectAnimator animator1;
    ObjectAnimator animator2;
    Random randomizer;
    TextView coinFlipResult;
    Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper.applyNightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        randomizer = new Random();
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        handler = new Handler();

        coinImageView = findViewById(R.id.coinImageView);
        coinFlipResult = findViewById(R.id.coinFlipResult);
        flipButton = findViewById(R.id.flipButton);

        headsDrawable = ContextCompat.getDrawable(this, R.drawable.coin_heads);
        tailsDrawable = ContextCompat.getDrawable(this, R.drawable.coin_tails);
        coinImageView.setImageDrawable(headsDrawable);
        animator1 = ObjectAnimator.ofFloat(coinImageView, "rotationY", 0f, 360 * 4 + 90f);
        animator2 = ObjectAnimator.ofFloat(coinImageView, "rotationY", -90f, 0f);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void flip(View view) {
        flipButton.setEnabled(false);
        coinFlipResult.setText(" ");

        if (vib != null) {
            vib.vibrate(300);
        }

        int flipResult = randomizer.nextInt(2);

        handler.postDelayed(() -> {
            animator1.setDuration(animationDuration);
            animator1.start();
        }, 10);

        handler.postDelayed(() -> {
            animator2.setDuration(200);
            animator2.start();
            String flipResultString = resultToTextAndImage(flipResult);
            coinFlipResult.setText(flipResultString);
            flipButton.setEnabled(true);
        }, animationDuration);
    }

    private String resultToTextAndImage(int oneOrTwo) {
        String returnString;
        if (oneOrTwo == 1) {
            returnString = "HEADS";
            coinImageView.setImageDrawable(headsDrawable);
        } else {
            returnString = "TAILS";
            coinImageView.setImageDrawable(tailsDrawable);
        }
        return returnString;
    }
}
