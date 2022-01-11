package com.youngpark.minesweeper.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.youngpark.minesweeper.GameEngine;
import com.youngpark.minesweeper.R;

/**
 * youngpark 2020.02.04
 *
 * 扫雷 v0.1
 *
 * 主界面
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void setGame() {
        final Button easyButton, midButton, hardButton, customButton;
        final EditText mapSizeEditText, boomNumEditText;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

        View dialogView = View.inflate(MainActivity.this, R.layout.dialog_choose, null);

        easyButton = dialogView.findViewById(R.id.easyButton);
        midButton = dialogView.findViewById(R.id.midButton);
        hardButton = dialogView.findViewById(R.id.hardButton);
        customButton = dialogView.findViewById(R.id.customButton);

        mapSizeEditText = dialogView.findViewById(R.id.mapSizeEditText);
        boomNumEditText = dialogView.findViewById(R.id.boomNumEditText);

        Button.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.easyButton:
                        GameEngine.mapSize = 9;
                        GameEngine.boomNum = 10;
                        Toast.makeText(MainActivity.this, "已选简单", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.midButton:
                        GameEngine.mapSize = 16;
                        GameEngine.boomNum = 40;
                        Toast.makeText(MainActivity.this, "已选中等", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.hardButton:
                        GameEngine.mapSize = 20;
                        GameEngine.boomNum = 80;
                        Toast.makeText(MainActivity.this, "已选困难", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.customButton:
                        GameEngine.boomNum = Integer.parseInt(boomNumEditText.getText().toString());
                        GameEngine.mapSize = Integer.parseInt(mapSizeEditText.getText().toString());
                        Toast.makeText(MainActivity.this, "已选:" + GameEngine.mapSize + ", " + GameEngine.boomNum, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        easyButton.setOnClickListener(clickListener);
        midButton.setOnClickListener(clickListener);
        hardButton.setOnClickListener(clickListener);
        customButton.setOnClickListener(clickListener);

        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("设置游戏难度");
        dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
        dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.create().show();
    }

    public void startGame(View view) {
        setGame();
    }

    public void showRank(View view) {
        Intent intent = new Intent(MainActivity.this, RankActivity.class);
        startActivity(intent);
    }
}
