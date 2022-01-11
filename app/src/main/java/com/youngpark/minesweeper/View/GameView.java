package com.youngpark.minesweeper.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.youngpark.minesweeper.Activity.GameActivity;
import com.youngpark.minesweeper.GameEngine;
import com.youngpark.minesweeper.R;

/**
 * youngpark 2020.02.04
 * <p>
 * 扫雷 v0.1
 * <p>
 * 游戏绘图引擎：
 * 绘图与触控，素材管理
 */

public class GameView extends View {

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private int heightCellNum, widthCellNum;
    private int cellSize = 0, originSize = 0;
    private int margin = 15;

    private Paint paint;

    private Bitmap tileMask, tileBase, tileDown, tileNums[], tileBoom, tileFlag, tileBoomExp;

    private void init() {
        if (paint == null) {
            paint = new Paint();
        }
        tileMask = BitmapFactory.decodeResource(getResources(), R.drawable.tile_mask);
        tileBase = BitmapFactory.decodeResource(getResources(), R.drawable.tile_base);
        tileDown = BitmapFactory.decodeResource(getResources(), R.drawable.tile_down);
        tileBoom = BitmapFactory.decodeResource(getResources(), R.drawable.tile_b2);
        tileBoomExp = BitmapFactory.decodeResource(getResources(), R.drawable.tile_b);
        tileFlag = BitmapFactory.decodeResource(getResources(), R.drawable.tile_d);

        tileNums = new Bitmap[9];

        tileNums[1] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_1);
        tileNums[2] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_2);
        tileNums[3] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_3);
        tileNums[4] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_4);
        tileNums[5] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_5);
        tileNums[6] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_6);
        tileNums[7] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_7);
        tileNums[8] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_8);


        heightCellNum = GameEngine.mapSize;
        widthCellNum = GameEngine.mapSize;

        originSize = tileMask.getHeight();
    }

    private Bitmap tileMaskScale, tileBaseScale, tileDownScale, tileNumsScale[], tileBoomScale, tileBoomExpScale, tileFlagScale;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = getWidth();
        cellSize = (width - margin * 2) / widthCellNum;
        scaleBitmap();
        Log.d("youngpark_671", "layout was called!");
    }

    private void scaleBitmap() {
        float scale = (float) (cellSize * 1.0 / originSize * 1.0);
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.postScale(scale, scale);

        tileMaskScale = Bitmap.createBitmap(tileMask, 0, 0, tileMask.getWidth(), tileMask.getHeight(), scaleMatrix, true);
        tileBaseScale = Bitmap.createBitmap(tileBase, 0, 0, tileBase.getWidth(), tileBase.getHeight(), scaleMatrix, true);
        tileDownScale = Bitmap.createBitmap(tileDown, 0, 0, tileDown.getWidth(), tileDown.getHeight(), scaleMatrix, true);
        tileBoomScale = Bitmap.createBitmap(tileBoom, 0, 0, tileBoom.getWidth(), tileBoom.getHeight(), scaleMatrix, true);
        tileBoomExpScale = Bitmap.createBitmap(tileBoomExp, 0, 0, tileBoomExp.getWidth(), tileBoomExp.getHeight(), scaleMatrix, true);
        tileFlagScale = Bitmap.createBitmap(tileFlag, 0, 0, tileFlag.getWidth(), tileFlag.getHeight(), scaleMatrix, true);

        tileMask.recycle();
        tileBase.recycle();
        tileDown.recycle();
        tileBoom.recycle();
        ;
        tileBoomExp.recycle();
        tileFlag.recycle();

        tileNumsScale = new Bitmap[9];
        for (int i = 1; i <= 8; i++) {
            tileNumsScale[i] = Bitmap.createBitmap(tileNums[i], 0, 0, tileMask.getWidth(), tileFlag.getHeight(), scaleMatrix, true);
            tileNums[i].recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);//高宽相同
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < widthCellNum; i++) {
            for (int j = 0; j < heightCellNum; j++) {
                if (GameEngine.show[i][j] == 0) {
                    canvas.drawBitmap(tileMaskScale, margin + (cellSize * i), margin + (cellSize * j), paint);
                } else if (GameEngine.show[i][j] == -1 || GameEngine.show[i][j] == -2) {
                    canvas.drawBitmap(tileDownScale, margin + (cellSize * i), margin + (cellSize * j), paint);
                } else if (GameEngine.show[i][j] == 2) {
                    canvas.drawBitmap(tileFlagScale, margin + (cellSize * i), margin + (cellSize * j), paint);
                } else {
                    switch (GameEngine.array[i][j]) {
                        case 0:
                            canvas.drawBitmap(tileBaseScale, margin + (cellSize * i), margin + (cellSize * j), paint);
                            break;
                        case -1:
                            canvas.drawBitmap(tileBoomScale, margin + (cellSize * i), margin + (cellSize * j), paint);
                            break;
                        case -2:
                            canvas.drawBitmap(tileBoomExpScale, margin + (cellSize * i), margin + (cellSize * j), paint);
                            break;
                        default:
                            canvas.drawBitmap(tileNumsScale[GameEngine.array[i][j]], margin + (cellSize * i), margin + (cellSize * j), paint);
                            break;
                    }
                }
            }
        }
        super.onDraw(canvas);
    }

    int posX = -1, posY = -1;
    long pressTime = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX() - margin;
                float y = event.getY() - margin;
                posX = (int) x / cellSize;
                posY = (int) y / cellSize;
                if (posX >= 0 && posX < widthCellNum && posY >= 0 && posY < heightCellNum) {
                    if (GameEngine.show[posX][posY] == 0) {
                        GameEngine.show[posX][posY] = -1;
                    } else if (GameEngine.show[posX][posY] == 2) {
                        GameEngine.show[posX][posY] = -2;
                    }
                    pressTime = System.currentTimeMillis();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (posX >= 0 && posX < widthCellNum && posY >= 0 && posY < heightCellNum) {
                    if (GameEngine.show[posX][posY] == -1) {
                        //普通按下
                        if (System.currentTimeMillis() - pressTime > 350 || GameEngine.isFlag) {
                            //普通长按
                            if (!GameEngine.flag(posX, posY)) {
                                Toast.makeText(getContext(), "已经没有更多旗帜了！收回一些吧。", Toast.LENGTH_SHORT).show();
                            } else {
                                ((GameActivity) getContext()).playSound(((GameActivity) getContext()).markSoundID);
                            }
                        } else {
                            //普通点击
                            GameEngine.show[posX][posY] = 1;
                            ((GameActivity) getContext()).playSound(((GameActivity) getContext()).checkSoundID);
                            if (!GameEngine.touch(posX, posY)) {
                                Toast.makeText(getContext(), "Boom! 祝您下次走运！", Toast.LENGTH_SHORT).show();
                                ((GameActivity) getContext()).playSound(((GameActivity) getContext()).bombSoundID);
                                ((GameActivity) getContext()).stopGame(1000);
                            }
                        }
                    } else if (GameEngine.show[posX][posY] == -2) {
                        //标记按下
                        if (System.currentTimeMillis() - pressTime > 350 || GameEngine.isFlag) {
                            //标记长按
                            GameEngine.flag(posX, posY);
                            ((GameActivity) getContext()).playSound(((GameActivity) getContext()).markSoundID);
                        } else {
                            //标记点击
                            GameEngine.show[posX][posY] = 2;
                            ((GameActivity) getContext()).playSound(((GameActivity) getContext()).checkSoundID);
                        }
                    }
                }
                break;
            default:
                performClick();
                break;
        }
        ((GameActivity) getContext()).setBoomAndFlag(GameEngine.boomNum, GameEngine.flagNum);
        if (((GameActivity) getContext()).started && GameEngine.win()) {
            AlertDialog dialog = (new AlertDialog.Builder(getContext()))
                    .setTitle("恭喜您")
                    .setMessage("成功通关!🎉")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            ((GameActivity) getContext()).playSound(((GameActivity) getContext()).winSoundID);
            dialog.show();
            ((GameActivity) getContext()).stopGame((int) ((GameActivity) getContext()).time);
        }
        invalidate();
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
