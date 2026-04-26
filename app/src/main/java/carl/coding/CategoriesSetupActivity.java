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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;

public class CategoriesSetupActivity extends AppCompatActivity {

    private final ArrayList<String> elementList = new ArrayList<>();
    private LinearLayout elementListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper.applyNightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_setup);

        elementListLayout = findViewById(R.id.element_list_layout);
        Button addButton = findViewById(R.id.add_button);
        Button startButton = findViewById(R.id.start_button);

        addButton.setOnClickListener(v -> addElement(null));
        startButton.setOnClickListener(v -> startCategoriesActivity());

        if (elementListLayout.getChildCount() == 0) {
            addElement(null);
            addElement(null);
            addElement(null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void addElement(String prefill) {
        EditText editText = new EditText(this);
        editText.setHint("Category name");
        editText.setSingleLine(true);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
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
                    addElement(null);
                    return true;
                }
                return false;
            }
        });

        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
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
        removeButton.setOnClickListener(v -> removeElement(v));

        LinearLayout elementLayout = new LinearLayout(this);
        LinearLayout.LayoutParams elementParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        elementParams.setMargins(0, 0, 0, dp(12));
        elementLayout.setLayoutParams(elementParams);
        elementLayout.setOrientation(LinearLayout.HORIZONTAL);
        elementLayout.addView(editText);
        elementLayout.addView(removeButton);

        elementListLayout.addView(elementLayout);
        editText.requestFocus();
    }

    private void removeElement(View view) {
        LinearLayout layout = (LinearLayout) view.getParent();
        elementListLayout.removeView(layout);
    }

    private void startCategoriesActivity() {
        removeEmptyElements();

        if (elementList.size() == 0) {
            addElement(null);
            return;
        }

        boolean withdrawMode = ((android.widget.Switch) findViewById(R.id.withdraw_switch)).isChecked();
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putStringArrayListExtra("elements", elementList);
        intent.putExtra("withdrawMode", withdrawMode);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void removeEmptyElements() {
        elementList.clear();
        ArrayList<Integer> emptyIndexes = new ArrayList<>();

        for (int i = 0; i < elementListLayout.getChildCount(); i++) {
            LinearLayout layout = (LinearLayout) elementListLayout.getChildAt(i);
            EditText editText = (EditText) layout.getChildAt(0);
            String value = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(value)) {
                elementList.add(value);
            } else {
                emptyIndexes.add(i);
            }
        }

        if (!emptyIndexes.isEmpty()) {
            emptyIndexes.sort(Collections.reverseOrder());
            for (Integer index : emptyIndexes) {
                View childToRemove = elementListLayout.getChildAt(index);
                elementListLayout.removeView(childToRemove);
            }
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
