package com.example.practice4;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/** Small offline demo whose APK is built by GitHub Actions. */
public class MainActivity extends Activity {
    private SharedPreferences preferences;
    private int count;
    private TextView counter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Respect status/navigation bars, including Android 15 edge-to-edge.
        View root = findViewById(R.id.root);
        int padding = root.getPaddingLeft();
        root.setOnApplyWindowInsetsListener((view, insets) -> {
            view.setPadding(padding + insets.getSystemWindowInsetLeft(),
                    padding + insets.getSystemWindowInsetTop(),
                    padding + insets.getSystemWindowInsetRight(),
                    padding + insets.getSystemWindowInsetBottom());
            return insets;
        });
        root.requestApplyInsets();
        preferences = getSharedPreferences("practice4", MODE_PRIVATE);
        count = preferences.getInt("count", 0);
        counter = findViewById(R.id.counter);
        findViewById(R.id.increment).setOnClickListener(view -> {
            if (count < Integer.MAX_VALUE) {
                count++;
            }
            saveAndRender();
        });
        findViewById(R.id.reset).setOnClickListener(view -> {
            count = 0;
            saveAndRender();
        });
        render();
    }

    private void saveAndRender() {
        preferences.edit().putInt("count", count).apply();
        render();
    }

    private void render() {
        counter.setText(getString(R.string.counter_value, count));
    }
}
