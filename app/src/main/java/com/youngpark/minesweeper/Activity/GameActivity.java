package com.youngpark.minesweeper.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.youngpark.minesweeper.GameEngine;
import com.youngpark.minesweeper.R;
import com.youngpark.minesweeper.View.GameView;

import java.text.DecimalFormat;

/**
 * youngpark 2020.02.04
 * <p>
 * 扫雷 v0.1
 * <p>
 * 游戏界面：
 * 负责游戏相关活动，初始化各类素材
 */

public class GameActivity extends AppCompatActivity {

    public volatile boolean started = false;

    GameView gameView;
    ImageView BoomImageView1, BoomImageView2, FlagImageView1, FlagImageView2;
    TextView TimeTextView;
    Button setFlagButton;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int top1, top2, top3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViews();
        initSP();
        init();
        initSound();
        startGame();
    }

    private void initSP() {
        sharedPreferences = getSharedPreferences("tops", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        top1 = sharedPreferences.getInt("top1", 1000);
        top2 = sharedPreferences.getInt("top2", 1000);
        top3 = sharedPreferences.getInt("top3", 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseSound();

        stopGame(1000);
    }

    private int[] redNums, greenNums;

    void init() {
        if (redNums == null) {
            redNums = new int[12];
        }
        if (greenNums == null) {
            greenNums = new int[12];
        }

        redNums[0] = R.drawable.number_0;
        redNums[1] = R.drawable.number_1;
        redNums[2] = R.drawable.number_2;
        redNums[3] = R.drawable.number_3;
        redNums[4] = R.drawable.number_4;
        redNums[5] = R.drawable.number_5;
        redNums[6] = R.drawable.number_6;
        redNums[7] = R.drawable.number_7;
        redNums[8] = R.drawable.number_8;
        redNums[9] = R.drawable.number_9;
        redNums[10] = R.drawable.number_dash;


        greenNums[0] = R.drawable.gnumber_0;
        greenNums[1] = R.drawable.gnumber_1;
        greenNums[2] = R.drawable.gnumber_2;
        greenNums[3] = R.drawable.gnumber_3;
        greenNums[4] = R.drawable.gnumber_4;
        greenNums[5] = R.drawable.gnumber_5;
        greenNums[6] = R.drawable.gnumber_6;
        greenNums[7] = R.drawable.gnumber_7;
        greenNums[8] = R.drawable.gnumber_8;
        greenNums[9] = R.drawable.gnumber_9;
        greenNums[10] = R.drawable.gnumber_dash;
    }

    private void findViews() {
        gameView = findViewById(R.id.GameView);
        BoomImageView1 = findViewById(R.id.BoomImageView1);
        BoomImageView2 = findViewById(R.id.BoomImageView2);
        FlagImageView1 = findViewById(R.id.FlagImageView1);
        FlagImageView2 = findViewById(R.id.FlagImageView2);

        TimeTextView = findViewById(R.id.TimeTextView);
        setFlagButton = findViewById(R.id.SetFlagButton);
    }

    public void setBoomAndFlag(int boom, int flag) {
        int boom2 = boom % 10;
        int boom1 = boom / 10;

        int flag2 = flag % 10;
        int flag1 = flag / 10;

        BoomImageView1.setImageResource(redNums[boom1]);
        BoomImageView2.setImageResource(redNums[boom2]);
        FlagImageView1.setImageResource(greenNums[flag1]);
        FlagImageView2.setImageResource(greenNums[flag2]);
    }

    public void newGame(View view) {
        setFlagButton.setText("非标记");
        startGame();
    }

    public void setFlag(View view) {
        GameEngine.isFlag = !GameEngine.isFlag;
        playSound(GameEngine.isFlag ? markSoundID : checkSoundID);
        ((Button) view).setText(GameEngine.isFlag ? "标记" : "非标记");
        ((Button) view).setTextColor(GameEngine.isFlag ? Color.RED : Color.BLUE);
    }

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    public double time = 0.;
    private DecimalFormat df = new DecimalFormat("0.0");

    private SoundPool soundPool;

    private void startGame() {
        started = true;
        GameEngine.init();
        gameView.invalidate();

        playSound(startSoundID);
        time = 0;

        Thread timeThread = new Thread() {
            @Override
            public void run() {
                while (started) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    time += 0.1;
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TimeTextView.setText("时间：" + df.format(time) + "s");
                        }
                    });
                }
            }
        };
        timeThread.start();
    }

    public void stopGame(int socre) {
        started = false;
        if (socre < top1) {
            top1 = socre;
            editor.putInt("top1", top1);
        } else if (socre < top2) {
            top2 = socre;
            editor.putInt("top2", top2);
        } else if (socre < top3) {
            top3 = socre;
            editor.putInt("top3", top3);
        }
        editor.apply();
        editor.commit();
    }

    public int bombSoundID, checkSoundID, startSoundID, winSoundID, markSoundID;

    public void playSound(int soundID) {
        soundPool.play(soundID, 1, 1, 1, 0, 1);
    }

    private void initSound() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder().setMaxStreams(16)
                .setAudioAttributes(audioAttributes)
                .build();
        bombSoundID = soundPool.load(GameActivity.this, R.raw.bomb, 1);
        checkSoundID = soundPool.load(GameActivity.this, R.raw.check, 1);
        startSoundID = soundPool.load(GameActivity.this, R.raw.start, 1);
        winSoundID = soundPool.load(GameActivity.this, R.raw.win, 1);
        markSoundID = soundPool.load(GameActivity.this, R.raw.marking, 1);
    }

    private void releaseSound() {
        soundPool.release();
        soundPool = null;
    }

    private void exit() {
        if (started) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameActivity.this);
            AlertDialog dialog = null;
            dialogBuilder.setTitle("退出确认");
            dialogBuilder.setMessage(getString(R.string.exit_warning));
            dialogBuilder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stopGame(1000);
                    finish();
                }
            });

            dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog = dialogBuilder.create();
            dialog.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exit();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
