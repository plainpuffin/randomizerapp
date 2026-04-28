package carl.coding;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;

public class TeamSetupActivity extends AppCompatActivity {

    private static final String KEY_TEAM_PLAYERS = "team_players";
    private static final String KEY_TEAM_COUNT = "team_count";

    private final ArrayList<String> players = new ArrayList<>();
    private LinearLayout playerListLayout;
    private EditText teamCountInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper.applyNightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_setup);

        playerListLayout = findViewById(R.id.player_list_layout);
        teamCountInput = findViewById(R.id.team_count_input);

        Button addPlayerButton = findViewById(R.id.add_player_button);
        Button randomizeTeamsButton = findViewById(R.id.randomize_teams_button);

        addPlayerButton.setOnClickListener(v -> addPlayerField(null));
        randomizeTeamsButton.setOnClickListener(v -> startTeamResult());

        teamCountInput.setText(PreferencesHelper.getString(this, KEY_TEAM_COUNT, "2"));

        ArrayList<String> savedPlayers = PreferencesHelper.getStringList(this, KEY_TEAM_PLAYERS);
        if (!savedPlayers.isEmpty()) {
            for (String player : savedPlayers) {
                addPlayerField(player, false);
            }
        } else {
            addPlayerField(null, false);
            addPlayerField(null, false);
            addPlayerField(null, false);
            addPlayerField(null, false);
        }
    }

    private void addPlayerField(String prefill) {
        addPlayerField(prefill, true);
    }

    private void addPlayerField(String prefill, boolean requestFocus) {
        EditText editText = new EditText(this);
        editText.setHint("Player name");
        editText.setSingleLine(true);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editText.setText(prefill == null ? "" : prefill);
        editText.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        editText.setBackgroundResource(R.drawable.input_background);
        editText.setPadding(dp(16), dp(14), dp(16), dp(14));
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addPlayerField(null);
                    return true;
                }
                return false;
            }
        });

        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        editText.setLayoutParams(editParams);

        Button removeButton = new Button(this);
        removeButton.setText("Remove");
        removeButton.setTextColor(ContextCompat.getColor(this, R.color.button_text));
        removeButton.setBackgroundResource(R.drawable.button_selector);
        LinearLayout.LayoutParams removeParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        removeParams.setMarginStart(dp(12));
        removeButton.setLayoutParams(removeParams);
        removeButton.setPadding(dp(18), dp(10), dp(18), dp(10));
        removeButton.setOnClickListener(v -> removePlayer(v));

        LinearLayout row = new LinearLayout(this);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowParams.setMargins(0, 0, 0, dp(12));
        row.setLayoutParams(rowParams);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.addView(editText);
        row.addView(removeButton);

        playerListLayout.addView(row);
        if (requestFocus) {
            editText.requestFocus();
        }
    }

    private void removePlayer(View view) {
        playerListLayout.removeView((View) view.getParent());
    }

    private void startTeamResult() {
        collectPlayers();
        persistState();
        if (players.size() < 2) {
            Toast.makeText(this, "Add at least two players.", Toast.LENGTH_SHORT).show();
            return;
        }

        String teamCountText = teamCountInput.getText().toString().trim();
        int teamCount = TextUtils.isEmpty(teamCountText) ? 2 : Integer.parseInt(teamCountText);
        if (teamCount < 2) {
            teamCount = 2;
        }
        if (teamCount > players.size()) {
            teamCount = players.size();
        }

        Intent intent = new Intent(this, TeamResultActivity.class);
        intent.putStringArrayListExtra("players", players);
        intent.putExtra("teamCount", teamCount);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void collectPlayers() {
        players.clear();
        ArrayList<Integer> emptyIndexes = new ArrayList<>();
        for (int i = 0; i < playerListLayout.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) playerListLayout.getChildAt(i);
            EditText editText = (EditText) row.getChildAt(0);
            String name = editText.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                emptyIndexes.add(i);
            } else {
                players.add(name);
            }
        }
        emptyIndexes.sort(Collections.reverseOrder());
        for (Integer index : emptyIndexes) {
            playerListLayout.removeViewAt(index);
        }
    }

    private void persistState() {
        ArrayList<String> currentPlayers = new ArrayList<>();
        for (int i = 0; i < playerListLayout.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) playerListLayout.getChildAt(i);
            EditText editText = (EditText) row.getChildAt(0);
            String name = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(name)) {
                currentPlayers.add(name);
            }
        }

        PreferencesHelper.setStringList(this, KEY_TEAM_PLAYERS, currentPlayers);
        PreferencesHelper.setString(this, KEY_TEAM_COUNT, teamCountInput.getText().toString().trim());
    }

    @Override
    protected void onPause() {
        persistState();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
