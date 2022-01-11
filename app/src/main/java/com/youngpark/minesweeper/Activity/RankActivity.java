package com.youngpark.minesweeper.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.youngpark.minesweeper.R;

/**
 * youngpark 2020.02.04
 *
 * 扫雷 v0.1
 *
 * 排行榜
 */

public class RankActivity extends AppCompatActivity {

    private ProgressBar top1ProgressBar, top2ProgressBar, top3ProgressBar;
    private TextView top1TextView, top2TextView, top3TextView, avgTextView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int top1, top2, top3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        findViews();
        initSP();
        showData();
    }

    private void initSP() {
        sharedPreferences = getSharedPreferences("tops", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        top1 = sharedPreferences.getInt("top1", 1000);
        top2 = sharedPreferences.getInt("top2", 1000);
        top3 = sharedPreferences.getInt("top3", 1000);
    }

    private void showData() {
        top3ProgressBar.setMax(top3);
        top2ProgressBar.setMax(top3);
        top1ProgressBar.setMax(top3);

        top1ProgressBar.setProgress(top1);
        top2ProgressBar.setProgress(top2);
        top3ProgressBar.setProgress(top3);

        top1TextView.setText(top1 + "s -- user");
        top2TextView.setText(top2 + "s -- user");
        top3TextView.setText(top3 + "s -- user");

        int count = 0;
        int sum = 0;
        if (top1 < 1000) {
            count++;
            sum += top1;
        }
        if (top2 < 1000) {
            count++;
            sum += top2;
        }
        if (top3 < 1000) {
            count++;
            sum += top3;
        }
        if (count > 0) {
            sum /= count;
            avgTextView.setText("平均成绩：" + sum + "s");
        }

    }

    private void findViews() {
        top1ProgressBar = findViewById(R.id.top1ProgressBar);
        top2ProgressBar = findViewById(R.id.top2ProgressBar);
        top3ProgressBar = findViewById(R.id.top3ProgressBar);

        top1TextView = findViewById(R.id.top1TextView);
        top2TextView = findViewById(R.id.top2TextView);
        top3TextView = findViewById(R.id.top3TextView);

        avgTextView = findViewById(R.id.avgTextView);
    }

    public void clearButton(View view) {
        editor.putInt("top1", 1000);
        editor.putInt("top2", 1000);
        editor.putInt("top3", 1000);
        top1 = top2 = top3 = 1000;
        editor.apply();
        editor.commit();
        showData();
    }
}
