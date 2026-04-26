package carl.coding;

import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class TeamResultActivity extends AppCompatActivity {

    private ArrayList<String> players;
    private int teamCount;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper.applyNightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_result);

        players = getIntent().getStringArrayListExtra("players");
        teamCount = getIntent().getIntExtra("teamCount", 2);
        resultText = findViewById(R.id.team_result_text);
        Button rerollButton = findViewById(R.id.reroll_teams_button);

        rerollButton.setOnClickListener(v -> renderTeams());
        renderTeams();
    }

    private void renderTeams() {
        if (players == null || players.isEmpty()) {
            resultText.setText("No players available.");
            return;
        }

        ArrayList<String> shuffled = new ArrayList<>(players);
        Collections.shuffle(shuffled);

        ArrayList<ArrayList<String>> teams = new ArrayList<>();
        for (int i = 0; i < teamCount; i++) {
            teams.add(new ArrayList<>());
        }

        for (int i = 0; i < shuffled.size(); i++) {
            teams.get(i % teamCount).add(shuffled.get(i));
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < teams.size(); i++) {
            builder.append("Team ").append(i + 1).append("\n");
            for (String player : teams.get(i)) {
                builder.append("• ").append(player).append("\n");
            }
            if (i < teams.size() - 1) {
                builder.append("\n");
            }
        }
        resultText.setText(builder.toString().trim());

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(180);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
