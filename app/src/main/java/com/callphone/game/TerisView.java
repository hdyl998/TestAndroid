package com.callphone.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.callphone.myapplication.MyLogUitls;
import com.callphone.myapplication.R;

import java.util.Random;

public class TerisView extends View {

    Context context;
    MinSound mSound;
    public int SCREEN_W = 320;
    public int SCREEN_H = 455;
    Paint paint;

    public boolean enableSound = false;

    public Random rand;

    public final int STATE_LOADING = 0;// 载入图片
    public final int STATE_MENU = 2;// 主菜单
    public final int STATE_OPTIONS = 3;// 选项
    public final int STATE_CREDITS = 4;// 开发人员
    public final int STATE_PLAY = 5;// 正在玩
    public final int STATE_GAMEOVER = 7;// 游戏结束
    public final int STATE_REMOVE = 8;// 消去
    public final int STATE_PAUSE = 9;// 暂停
    public final int STATE_ENSOUND = 10;// 是否开启声音

    public int gamestate = STATE_LOADING;

    // 图片

    private Bitmap bufferMap;// buffer
    private Canvas canvas;
    private Bitmap title;
    private Bitmap menubg;
    private Bitmap menuword1;
    private Bitmap menuword2;
    private Bitmap options1;
    private Bitmap options2;
    private Bitmap credits;
    private Bitmap pause1;
    private Bitmap pause2;
    private Bitmap gauge1;
    private Bitmap gauge2;
    private Bitmap gameover;
    private Bitmap[] mBlock = new Bitmap[8]; // 小格子图片

    // 菜单变量
    int menuAt = 0;
    int menuSpace = 10;
    int titleDown = 10;
    int menuDown = titleDown * 3;
    int optionsAt;
    int pauseAt;
    int pauseBack;

    int loadFrame = 20;
    // END菜单变量

    // 主要游戏变量
    int beginDrawX; // 开始画主体X
    int beginDrawY; // 开始画主体Y

    public int caseWidth = 20; // 小格子边长
    public final int case_L = 4; // 块格子边长
    public int body_W = 11;
    public int body_H = SCREEN_H / caseWidth + 5;

    public int[][] body = new int[body_W][body_H]; // 游戏池
    public boolean[] isReCase = new boolean[body_H]; // 消去的行

    public int[][] currentCase = new int[case_L][case_L]; // 当前活动图形
    public int[][] tempCase = new int[case_L][case_L]; // 中间活动图形
    public int[][] nextCase = new int[case_L][case_L]; // 下一个活动图形

    public int currentX; // 当前块坐标
    public int currentY; // 当前块坐标

    public int currentColor; // 当前块颜色
    public int nextColor; // 下一块颜色

    public int index; // 生成块的下标
    public int nowIndex; // 当前块的下标

    {
        MyLogUitls.print("body_H", body_H + "");
    }

    public int[][][] store = new int[][][]{{// I
            {0, 0, 0, 1}, {0, 0, 0, 1}, {0, 0, 0, 1}, {0, 0, 0, 1}}, {// I
            {0, 0, 0, 0}, {0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}}, {// I
            {0, 0, 0, 1}, {0, 0, 0, 1}, {0, 0, 0, 1}, {0, 0, 0, 1}}, {// I
            {0, 0, 0, 0}, {0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}}, {// O
            {0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}}, {// O
            {0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}}, {// O
            {0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}}, {// O
            {0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}}, {// L
            {0, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}}, {// L
            {0, 0, 0, 0}, {1, 1, 1, 0}, {1, 0, 0, 0}, {0, 0, 0, 0}}, {// L
            {0, 1, 1, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 0}}, {// L
            {0, 0, 0, 0}, {0, 0, 1, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}}, {// J
            {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}}, {// J
            {0, 0, 0, 0}, {1, 0, 0, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}}, {// J
            {0, 1, 1, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}}, {// J
            {0, 0, 0, 0}, {1, 1, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 0}}, {// T
            {0, 0, 0, 0}, {0, 1, 0, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}}, {// T
            {0, 1, 0, 0}, {0, 1, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}}, {// T
            {0, 0, 0, 0}, {1, 1, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}}, {// T
            {0, 0, 1, 0}, {0, 1, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 0}}, {// S
            {0, 0, 0, 0}, {0, 1, 1, 0}, {1, 1, 0, 0}, {0, 0, 0, 0}}, {// S
            {0, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 0}}, {// S
            {0, 0, 0, 0}, {0, 1, 1, 0}, {1, 1, 0, 0}, {0, 0, 0, 0}}, {// S
            {0, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 0}}, {// Z
            {0, 0, 0, 0}, {1, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}}, {// Z
            {0, 0, 1, 0}, {0, 1, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}}, {// Z
            {0, 0, 0, 0}, {1, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}}, {// Z
            {0, 0, 1, 0}, {0, 1, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}}};

    public int score = 0;
    public int showScore = 0;
    public int reLine = 0;

    public int iStep = body_H; // 快速落下 步数
    public int frame = 0;
    public int playFrame = 0;
    public int comboFrame = 0;
    public int addFrame = 0;
    public int frame5 = 5;
    public int frame10 = 10;
    public int frame20 = 20;
    public int removeFrame = 5;
    public int fastFrame = 1;
    public int overFrame = 10;
    public int createdFrame = 280;

    public int initSpeed = 0;
    public int addSpeed;

    // 开关
    boolean isNewCase = false;
    boolean isMoveDown = false;
    boolean isCombo = false;
    boolean isRemove = false;
    boolean isGameOver = false;

    // END 主要游戏变量

    private final int SID_ADD = 0;
    private final int SID_MOVE = 1;

    // TODO 构造函数
    public TerisView(Context ctx) {
        super(ctx);
        initTerisme(ctx);
    }

    public TerisView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        initTerisme(ctx);
    }

    public TerisView(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        initTerisme(ctx);
    }

    public void initTerisme(Context ctx) {
        context = ctx;
        setFocusable(true);
        paint = new Paint();
        rand = new Random();
        mSound = new MinSound(context);
        mSound.initSounds();
        mSound.loadSfx(R.raw.bomb, SID_ADD);
        mSound.loadSfx(R.raw.move, SID_MOVE);

        paint.setTextSize(24);

        beginDrawX = 0;
        beginDrawY = SCREEN_H - caseWidth * body_H;
        Log.v("beginDrawY", "beginDrawY"+beginDrawY);
        Log.v("beginDrawY", "beginDrawY"+caseWidth * body_H);



        bufferMap = Bitmap.createBitmap(SCREEN_W, SCREEN_H, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bufferMap);

        loadFP();
        loadAllImage();
        gamestate = STATE_LOADING;

        startGame();
        Log.v("teris", "initialized");
    }

    public Bitmap createImage(Drawable tile, int w, int h) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, w, h);
        tile.draw(canvas);
        return bitmap;
    }

    public void loadFP() {
        Resources r = this.getContext().getResources();
        title = createImage(r.getDrawable(R.drawable.title), 200, 71);
        menubg = createImage(r.getDrawable(R.drawable.menubg), 320, 455);
        gauge1 = createImage(r.getDrawable(R.drawable.gauge1), 200, 17);
        gauge2 = createImage(r.getDrawable(R.drawable.gauge2), 200, 17);
    }

    public void freeFP() {
        gauge1 = null;
        gauge2 = null;
    }

    public void loadAllImage() {
        Resources r = this.getContext().getResources();
        mBlock[0] = createImage(r.getDrawable(R.drawable.block0), caseWidth, caseWidth);
        mBlock[1] = createImage(r.getDrawable(R.drawable.block1), caseWidth, caseWidth);
        mBlock[2] = createImage(r.getDrawable(R.drawable.block2), caseWidth, caseWidth);
        mBlock[3] = createImage(r.getDrawable(R.drawable.block3), caseWidth, caseWidth);
        mBlock[4] = createImage(r.getDrawable(R.drawable.block4), caseWidth, caseWidth);
        mBlock[5] = createImage(r.getDrawable(R.drawable.block5), caseWidth, caseWidth);
        mBlock[6] = createImage(r.getDrawable(R.drawable.block6), caseWidth, caseWidth);
        mBlock[7] = createImage(r.getDrawable(R.drawable.block7), caseWidth, caseWidth);
        //
        // menuword1 = createImage(r.getDrawable(R.drawable.menuword1), 200,
        // 100);
        // menuword2 = createImage(r.getDrawable(R.drawable.menuword2), 200,
        // 100);
        // options1 = createImage(r.getDrawable(R.drawable.options1), 177, 80);
        // options2 = createImage(r.getDrawable(R.drawable.options2), 177, 80);
        // credits = createImage(r.getDrawable(R.drawable.credits), 145, 127);
        // pause1 = createImage(r.getDrawable(R.drawable.pause1), 177, 60);
        // pause2 = createImage(r.getDrawable(R.drawable.pause2), 177, 60);
        // gameover = createImage(r.getDrawable(R.drawable.gameover), 173, 21);
    }

    public void startGame() {
        isGameOver = false;
        // 清空游戏池
        for (int i = 0; i < body.length; i++) {
            for (int j = 0; j < body[i].length; j++) {
                if (body[i][j] != 0) {
                    body[i][j] = 0;
                }
            }
        }
        // 分数归零
        score = 0;
        showScore = 0;
        // 速度归零
        addSpeed = 0;
        // 消去行数归零
        reLine = 0;

        // 要连续产生2块
        newCase();
        isNewCase = true;
        gamestate = STATE_PLAY;
    }

    // TODO 逻辑
    public void logic() {
        switch (gamestate) {
            case STATE_ENSOUND:
                break;
            case STATE_LOADING:
                if (frame <= loadFrame) {
                    frame++;
                    if (frame == 15) {
                        // loadAllImage();
                    } else if (frame == loadFrame) {
                    }
                }
                break;
            case STATE_PLAY:
                if (showScore < score) {
                    showScore += (score - showScore) >> 1;
                }
                if (showScore < score) {
                    showScore++;
                }
                playFrame++;
                if (playFrame > frame5) {
                    playFrame = 0;
                    isMoveDown = true;
                }

                if (isGameOver) {
                    startGame();
                } else if (isNewCase) {
                    newCase();
                } else if (isCombo) {
                    comboFrame++;
                    if (comboFrame > frame5) {
                        comboFrame = 0;
                        doCombo();
                    }
                } else if (isRemove) {
                    gamestate = STATE_REMOVE;
                } else if (isMoveDown) {
                    moveDown();
                }
                break;
            case STATE_REMOVE:
                doRemove();
                gamestate = STATE_PLAY;

                break;
        }
    }

    public void onDraw(Canvas mainCanvas) {
        switch (gamestate) {
            case STATE_ENSOUND:
                paintEnSound(canvas);
                break;
            case STATE_LOADING:
                paintLoading(canvas);
                break;
            case STATE_MENU:
                paintMenu(canvas);
                break;
            case STATE_OPTIONS:
                paintOptions(canvas);
                break;
            case STATE_CREDITS:
                paintCredits(canvas);
                break;
            case STATE_PLAY:
            case STATE_REMOVE:
                paintGame(canvas);
                break;
            case STATE_GAMEOVER:
                if (frame <= overFrame) {
                    frame++;
                }
                paintBody(canvas);
                paintCurrent(canvas);
                paintNextCase(canvas);
                canvas.drawColor(Color.BLACK);
                canvas.drawRect(0, SCREEN_H / 2 - gameover.getHeight(), SCREEN_W, gameover.getHeight() * 2, paint);
                canvas.drawBitmap(gameover, (SCREEN_W - gameover.getWidth()) / 2, (SCREEN_H - gameover.getHeight()) / 2, paint);
                break;
            case STATE_PAUSE:
                paintPause(canvas);
                break;
        }
        mainCanvas.drawBitmap(bufferMap, 0, 0, paint);
    }

    private void paintGame(Canvas canvas2) {
        // TODO paintGame
        fillColor(canvas, Color.BLACK);
        paintBody(canvas);
        paintCurrent(canvas);
        paintNextCase(canvas);

        paint.setColor(Color.WHITE);

        canvas.drawText("Speed", caseWidth * body_W, 8 * caseWidth, paint);
        canvas.drawText("Line", caseWidth * body_W, 12 * caseWidth, paint);
        canvas.drawText("Score", caseWidth * body_W, 16 * caseWidth, paint);
        paint.setColor(Color.GREEN);
        canvas.drawText("" + initSpeed, caseWidth * body_W, 10 * caseWidth, paint);
        canvas.drawText("" + reLine, caseWidth * body_W, 14 * caseWidth, paint);
        canvas.drawText("" + showScore, caseWidth * body_W, 18 * caseWidth, paint);
    }

    public void paintLoading(Canvas canvas) {
        canvas.save();
        canvas.clipRect(0, 0, SCREEN_W, SCREEN_H);
        canvas.drawBitmap(menubg, 0, 0, paint);
        canvas.drawBitmap(title, (SCREEN_W - title.getWidth()) / 2, titleDown, paint);
        canvas.drawBitmap(gauge2, (SCREEN_W - gauge2.getWidth()) / 2, SCREEN_H - 100, paint);

        int len = SCREEN_W - gauge2.getWidth();

        canvas.clipRect(len / 2, 0, len / 2 + 10 * frame, SCREEN_H);
        canvas.drawBitmap(gauge1, len / 2, SCREEN_H - 100, paint);

        canvas.restore();
    }

    public void paintEnSound(Canvas canvas) {
        fillColor(canvas, 0x000000);
        canvas.drawColor(Color.WHITE);
        canvas.clipRect(0, 0, SCREEN_W, SCREEN_H);
        canvas.drawTextOnPath("Enable Sound?", new Path(), SCREEN_W / 2, SCREEN_H / 2, paint);
        canvas.drawText("Yes", 0, SCREEN_H - 10, paint);
        canvas.drawText("No", SCREEN_W - 10, SCREEN_H - 10, paint);
    }

    public void paintMenu(Canvas canvas) {
        canvas.clipRect(0, 0, SCREEN_W, SCREEN_H);
        canvas.drawBitmap(menubg, 0, 0, paint);
        canvas.drawBitmap(title, (SCREEN_W - title.getWidth()) / 2, titleDown, paint);

        for (int i = 0; i < 5; i++) {
            canvas.save();
            canvas.clipRect(0, title.getHeight() + menuDown + menuword1.getHeight() / 5 * i + menuSpace * i, SCREEN_W, title.getHeight() + menuDown + menuword1.getHeight() / 5 * i + menuSpace * i + menuword1.getHeight() / 5);
            if (i == menuAt) {
                canvas.drawBitmap(menuword1, (SCREEN_W - menuword1.getWidth()) / 2, title.getHeight() + menuDown + menuSpace * i, paint);
            } else {
                canvas.drawBitmap(menuword2, (SCREEN_W - menuword1.getWidth()) / 2, title.getHeight() + menuDown + menuSpace * i, paint);
            }
            canvas.restore();
        }
        canvas.clipRect(0, 0, SCREEN_W, SCREEN_H);
    }

    public void paintOptions(Canvas canvas) {
        canvas.clipRect(0, 0, SCREEN_W, SCREEN_H);
        canvas.drawBitmap(menubg, 0, 0, paint);
        canvas.drawBitmap(title, (SCREEN_W - title.getWidth()) / 2, titleDown, paint);
        // music
        canvas.clipRect(0, SCREEN_H / 2 - options2.getHeight() / 4 - menuSpace, SCREEN_W, options2.getHeight() / 4);
        if (enableSound) {
            canvas.drawBitmap(options2, (SCREEN_W - options2.getWidth()) / 2, SCREEN_H / 2 - options2.getHeight() / 2 - menuSpace, paint);
        } else {
            canvas.drawBitmap(options2, (SCREEN_W - options2.getWidth()) / 2, SCREEN_H / 2 - options2.getHeight() / 4 - menuSpace, paint);
        }
        // level
        canvas.clipRect(0, SCREEN_H / 2, SCREEN_W, options1.getHeight() / 4);
        canvas.drawBitmap(options2, (SCREEN_W - options2.getWidth()) / 2, SCREEN_H / 2 - options2.getHeight() / 2, paint);

        canvas.clipRect(0, SCREEN_H / 2 + options1.getHeight() / 4 + menuSpace, SCREEN_W, options1.getHeight() / 4);
        // done
        canvas.drawBitmap(options2, (SCREEN_W - options2.getWidth()) / 2, SCREEN_H / 2 - options2.getHeight() / 2 + menuSpace, paint);

        switch (optionsAt) {
            case 0:
                canvas.clipRect(0, SCREEN_H / 2 - options2.getHeight() / 4 - menuSpace, SCREEN_W, options2.getHeight() / 4);
                if (enableSound) {
                    canvas.drawBitmap(options1, (SCREEN_W - options1.getWidth()) / 2, SCREEN_H / 2 - options2.getHeight() / 2 - menuSpace, paint);
                } else {
                    canvas.drawBitmap(options1, (SCREEN_W - options1.getWidth()) / 2, SCREEN_H / 2 - options2.getHeight() / 4 - menuSpace, paint);
                }
                break;
            case 1:
                canvas.clipRect(0, SCREEN_H / 2, SCREEN_W, options1.getHeight() / 4);
                canvas.drawBitmap(options1, (SCREEN_W - options1.getWidth()) / 2, SCREEN_H / 2 - options1.getHeight() / 2, paint);
                break;
            case 2:
                canvas.clipRect(0, SCREEN_H / 2 + options1.getHeight() / 4 + menuSpace, SCREEN_W, options1.getHeight() / 4);
                canvas.drawBitmap(options1, (SCREEN_W - options1.getWidth()) / 2, SCREEN_H / 2 - options1.getHeight() / 2 + menuSpace, paint);
                break;
        }

        canvas.clipRect(0, 0, SCREEN_W, SCREEN_H);
    }

    public void paintCredits(Canvas canvas) {
        canvas.clipRect(0, 0, SCREEN_W, SCREEN_H);
        canvas.drawBitmap(menubg, 0, 0, paint);
        canvas.drawBitmap(title, (SCREEN_W - title.getWidth()) / 2, titleDown, paint);
        canvas.drawBitmap(credits, (SCREEN_W - credits.getWidth()) / 2, SCREEN_H / 2, paint);
    }

    public void paintPause(Canvas canvas) {
        paintBody(canvas);
        paintCurrent(canvas);
        paintNextCase(canvas);
        canvas.drawColor(Color.BLACK);
        canvas.drawRect(0, SCREEN_H / 2 - pause2.getHeight() * 2 / 3 - menuSpace * 3, SCREEN_W, pause2.getHeight() / 3 * 4 + menuSpace * 5, paint);
        // resume
        canvas.clipRect(0, SCREEN_H / 2 - pause2.getHeight() * 2 / 3 - menuSpace * 2, SCREEN_W, pause2.getHeight() / 3);
        canvas.drawBitmap(pause2, (SCREEN_W - pause2.getWidth()) / 2, SCREEN_H / 2 - pause2.getHeight() * 2 / 3 - menuSpace * 2, paint);
        // music
        canvas.clipRect(0, SCREEN_H / 2 - pause2.getHeight() / 3 - menuSpace, SCREEN_W, pause2.getHeight() / 3);
        if (enableSound) {
            canvas.drawBitmap(options2, (SCREEN_W - options2.getWidth()) / 2, SCREEN_H / 2 - options2.getHeight() / 2 - menuSpace, paint);
        } else {
            canvas.drawBitmap(options2, (SCREEN_W - options2.getWidth()) / 2, SCREEN_H / 2 - options2.getHeight() / 4 - menuSpace, paint);
        }

        canvas.clipRect(0, SCREEN_H / 2, SCREEN_W, pause2.getHeight() / 3);
        canvas.drawBitmap(pause2, (SCREEN_W - pause2.getWidth()) / 2, SCREEN_H / 2 - pause2.getHeight() / 3, paint);

        canvas.clipRect(0, SCREEN_H / 2 + pause2.getHeight() / 3 + menuSpace, SCREEN_W, pause2.getHeight() / 3);
        canvas.drawBitmap(pause2, (SCREEN_W - pause2.getWidth()) / 2, SCREEN_H / 2 - pause2.getHeight() / 3 + menuSpace, paint);

        switch (pauseAt) {
            case 0:// resume
                canvas.clipRect(0, SCREEN_H / 2 - pause2.getHeight() * 2 / 3 - menuSpace * 2, SCREEN_W, pause2.getHeight() / 3);
                canvas.drawBitmap(pause1, (SCREEN_W - pause1.getWidth()) / 2, SCREEN_H / 2 - pause2.getHeight() * 2 / 3 - menuSpace * 2, paint);
                break;
            case 1: // music
                canvas.clipRect(0, SCREEN_H / 2 - pause2.getHeight() / 3 - menuSpace, SCREEN_W, pause2.getHeight() / 3);
                if (enableSound) {
                    canvas.drawBitmap(options1, (SCREEN_W - options1.getWidth()) / 2, SCREEN_H / 2 - options2.getHeight() / 2 - menuSpace, paint);
                } else {
                    canvas.drawBitmap(options1, (SCREEN_W - options1.getWidth()) / 2, SCREEN_H / 2 - options2.getHeight() / 4 - menuSpace, paint);
                }
                break;
            case 2:
                canvas.clipRect(0, SCREEN_H / 2, SCREEN_W, pause2.getHeight() / 3);
                canvas.drawBitmap(pause1, (SCREEN_W - pause1.getWidth()) / 2, SCREEN_H / 2 - pause2.getHeight() / 3, paint);
                break;
            case 3:
                canvas.clipRect(0, SCREEN_H / 2 + pause2.getHeight() / 3 + menuSpace, SCREEN_W, pause2.getHeight() / 3);
                canvas.drawBitmap(pause1, (SCREEN_W - pause1.getWidth()) / 2, SCREEN_H / 2 - pause2.getHeight() / 3 + menuSpace, paint);
                break;
        }
    }

    // 游戏池
    public void paintBody(Canvas canvas) {
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, caseWidth * body_W, SCREEN_H, paint);
        for (int i = 0; i < body_W; i++) {
            for (int j = 0; j < body_H; j++) {
                if (body[i][j] != 0) {
                    canvas.drawBitmap(mBlock[body[i][j] - 1], beginDrawX + i * caseWidth, beginDrawY + j * caseWidth, paint);
                }
            }
        }
    }

    // 活动块
    public void paintCurrent(Canvas canvas) {
        for (int i = 0; i < currentCase.length; i++) {
            for (int j = 0; j < currentCase[i].length; j++) {
                if (currentCase[i][j] != 0) {
                    canvas.drawBitmap(mBlock[currentColor], beginDrawX + (i + currentX) * caseWidth, beginDrawY + (j + currentY) * caseWidth, paint);
                }
            }
        }
    }

    // 下一块
    public void paintNextCase(Canvas canvas) {
        paint.setColor(Color.GRAY);
        canvas.drawRect(caseWidth * body_W, 0, SCREEN_W, (currentCase.length + 1) * caseWidth, paint);
        for (int i = 0; i < nextCase.length; i++) {
            for (int j = 0; j < nextCase[i].length; j++) {
                if (nextCase[i][j] != 0) {
                    canvas.drawBitmap(mBlock[nextColor], caseWidth * body_W + i * caseWidth + caseWidth / 2, j * caseWidth + caseWidth / 2, paint);
                }
            }
        }
    }

    // 涂全屏
    public void fillColor(Canvas canvas, int color) {
        paint.setColor(color);
        canvas.drawRect(0, 0, SCREEN_W, SCREEN_H, paint);
    }

    public void onKeyDown(int keyCode) {
        switch (gamestate) {
            case STATE_ENSOUND:
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    gamestate = STATE_LOADING;

                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    gamestate = STATE_LOADING;
                }
                break;
            case STATE_LOADING:
                gamestate = STATE_MENU;
                frame = 0;
                break;
            case STATE_MENU:
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (menuAt == 0) {
                        startGame();
                    } else if (menuAt == 1) {
                        gamestate = STATE_OPTIONS;
                    } else if (menuAt == 2) {

                    } else if (menuAt == 3) {
                        gamestate = STATE_CREDITS;
                    } else if (menuAt == 4) {
                        ((TerisMe) context).finish();
                    }
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    menuAt = (--menuAt + 5) % 5;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    menuAt = ++menuAt % 5;
                }
                break;
            case STATE_OPTIONS:
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (optionsAt == 0) {
                        if (enableSound) {

                        } else {

                        }
                    } else if (optionsAt == 1) {
                        initSpeed = ++initSpeed % 10;
                    } else if (optionsAt == 2) {
                        optionsAt = 0;
                        gamestate = STATE_MENU;

                    }
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    if (optionsAt == 0) {
                        if (enableSound) {

                        } else {

                        }
                    } else if (optionsAt == 1) {
                        initSpeed = (--initSpeed + 10) % 10;
                    }
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (optionsAt == 0) {
                        if (enableSound) {

                        } else {

                        }
                    } else if (optionsAt == 1) {
                        initSpeed = ++initSpeed % 10;
                    }
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    optionsAt = (--optionsAt + 3) % 3;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    optionsAt = ++optionsAt % 3;
                }
                break;
            case STATE_CREDITS:
                frame = 0;
                gamestate = STATE_MENU;
                break;
            case STATE_PLAY:
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                    fastDrop();
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    turn();
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    moveLeft();
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    moveRight();
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    isMoveDown = true;
                }
                break;
            case STATE_GAMEOVER:
                if (frame >= overFrame) {
                    frame = 0;
                    toOver();
                }
                break;
            case STATE_PAUSE:
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                    switch (pauseAt) {
                        case 0:// 恢复
                            if (pauseBack != 0) {
                                gamestate = pauseBack;
                                pauseBack = 0;
                            } else {
                                gamestate = STATE_PLAY;
                            }
                            break;
                        case 1:// 声音
                            if (enableSound) {

                            } else {

                            }
                            break;
                        case 2:// 菜单
                            pauseAt = 0;
                            toOver();
                            break;
                        case 3:// 退出
                            // Quit

                            break;
                    }
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    pauseAt = (--pauseAt + 4) % 4;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    pauseAt = ++pauseAt % 4;
                }
                break;
        }

    }

    // /////////////////////////////////////////////////////////////////////////////

    public boolean isSpace(int x, int y) {
        if (x < 0 || x > body.length - 1) {
            return false;
        }
        if (y < 0 || y > body[0].length - 1) {
            return false;
        }

        if (body[x][y] == 0) {
            return true;
        } else {
            return false;
        }
    }

    // 左移
    public void moveLeft() {
        for (int i = 0; i < currentCase.length; i++) {
            for (int j = 0; j < currentCase[i].length; j++) {
                if (currentCase[i][j] != 0) {
                    if (!isSpace(currentX + i - 1, currentY + j)) {
                        return;
                    }
                }
            }
        }
        currentX--;
        isCombo = false;
        comboFrame = 0;

    }

    // 右移
    public void moveRight() {
        for (int i = 0; i < currentCase.length; i++) {
            for (int j = 0; j < currentCase[i].length; j++) {
                if (currentCase[i][j] != 0) {
                    if (!isSpace(currentX + i + 1, currentY + j)) {
                        return;
                    }
                }
            }
        }
        currentX++;
        isCombo = false;
        comboFrame = 0;
    }

    // 下移
    public void moveDown() {
        playFrame = 0;
        isMoveDown = false;
        for (int i = 0; i < currentCase.length; i++) {
            for (int j = 0; j < currentCase[i].length; j++) {
                if (currentCase[i][j] != 0) {
                    if (!isSpace(currentX + i, currentY + j + 1) || currentY + j + 1 == body[0].length) {
                        isCombo = true;
                        return;
                    }
                }
            }
        }
        currentY++;
    }

    // 快速落下
    public void fastDrop() {
        for (int i = 0; i < currentCase.length; i++) {
            for (int j = 0; j < currentCase[i].length; j++) {
                if (currentCase[i][j] != 0) {
                    for (int k = currentY + j; k < body[0].length; k++) {
                        if (!isSpace(currentX + i, k + 1) || k == body[0].length - 1) {
                            if (iStep > k - currentY - j) {
                                iStep = k - currentY - j;
                            }
                        }
                    }
                }
            }
        }
        currentY += iStep;
        if (iStep > 0) {
            comboFrame = frame5 >> 1;
            isCombo = true;
        }
        iStep = body_H;
    }

    // 把活动块加到游戏池
    public void doCombo() {
        isCombo = false;

        // 加入
        for (int i = 0; i < currentCase.length; i++) {
            for (int j = 0; j < currentCase[i].length; j++) {
                if (currentCase[i][j] != 0) {
                    body[currentX + i][currentY + j] = currentColor + 1;
                }
            }
        }

        // 处理能消去的行
        boolean remove = false;
        for (int i = 0; i < body_H; i++) {
            remove = true;
            for (int j = 0; j < body_W; j++) {
                if (isSpace(j, i)) {
                    remove = false;
                    break;
                }
            }
            isReCase[i] = remove;
            if (isReCase[i]) {
                reLine++;
            }
        }

        for (int i = 0; i < body_H; i++) {
            if (isReCase[i]) {
                isRemove = true;
                break;
            }
        }

        isNewCase = !isRemove; // 不能消去 就生产新块
    }

    public void doRemove() {
        int removeLine = 0;
        for (int i = 0; i < body_H; i++) {
            if (isReCase[i]) {
                removeLine++;
                for (int j = i; j > 0; j--) {
                    for (int k = 0; k < body_W; k++) {
                        body[k][j] = body[k][j - 1];
                    }
                }
            }
        }
        // 记分
        switch (removeLine) {
            case 1:
                score += 100;
                break;
            case 2:
                score += 300;
                break;
            case 3:
                score += 700;
                break;
            case 4:
                score += 1500;
                break;
        }
        isRemove = false;
        isNewCase = true; // 不能消去 就生产新块
    }

    // 旋转
    public void turn() {
        boolean canTurn = false;
        int tempX = 0, tempY = 0;
        int tempIndex;

        // 向右转
        tempIndex = nowIndex;
        if (tempIndex % 4 > 0) {
            tempIndex--;
        } else {
            tempIndex += 3;
        }
        for (int i = 0; i < tempCase.length; i++) {
            for (int j = 0; j < tempCase[i].length; j++) {
                tempCase[i][j] = store[tempIndex][i][j];
            }
        }

        tempX = currentX;
        tempY = currentY;

        canTurn = check(tempX, tempY);
        // 加强判断
        while (!canTurn) {
            // 下移1
            canTurn = check(tempX, tempY + 1);
            if (canTurn) {
                tempY++;
                break;
            }
            // 下左移1
            canTurn = check(tempX - 1, tempY + 1);
            if (canTurn) {
                tempX--;
                tempY++;
                break;
            }
            // 下右移1
            canTurn = check(tempX + 1, tempY + 1);
            if (canTurn) {
                tempX++;
                tempY++;
                break;
            }
            // 下移2
            canTurn = check(tempX, tempY + 2);
            if (canTurn) {
                tempY += 2;
                break;
            }
            // 左移1
            canTurn = check(tempX - 1, tempY);
            if (canTurn) {
                tempX--;
                break;
            }
            // 右移1
            canTurn = check(tempX + 1, tempY);
            if (canTurn) {
                tempX++;
                break;
            }
            // 上移1
            canTurn = check(tempX, tempY - 1);
            if (canTurn) {
                tempY--;
                break;
            }
            // 上左移1
            canTurn = check(tempX - 1, tempY - 1);
            if (canTurn) {
                tempX--;
                tempY--;
                break;
            }
            // 上右移1
            canTurn = check(tempX + 1, tempY - 1);
            if (canTurn) {
                tempX++;
                tempY--;
                break;
            }
            // 左移2
            canTurn = check(tempX - 2, tempY);
            if (canTurn) {
                tempX -= 2;
                break;
            }
            // 右移2
            canTurn = check(tempX + 2, tempY);
            if (canTurn) {
                tempX += 2;
                break;
            }
            // 上移2
            canTurn = check(tempX, tempY - 2);
            if (canTurn) {
                tempY -= 2;
                break;
            }
            break;
        }
        // 转换后
        if (canTurn) {
            nowIndex = tempIndex;
            currentX = tempX;
            currentY = tempY;
            for (int i = 0; i < currentCase.length; i++) {
                for (int j = 0; j < currentCase[i].length; j++) {
                    currentCase[i][j] = tempCase[i][j];
                }
            }

            isCombo = false;
            comboFrame = 0;
        }
    }

    public boolean check(int tempX, int tempY) {
        for (int i = 0; i < tempCase.length; i++) {
            for (int j = 0; j < tempCase[i].length; j++) {
                if (tempCase[i][j] != 0) {
                    if (!isSpace(tempX + i, tempY + j)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // 产生随机块
    public void newCase() {

        nowIndex = index;
        index = Math.abs(rand.nextInt() % 28);

        currentColor = nextColor;
        nextColor = index / 4 % 7; // 0 to 6

        for (int i = 0; i < nextCase.length; i++) {
            for (int j = 0; j < nextCase[i].length; j++) {
                // 把下一块提上来
                currentCase[i][j] = nextCase[i][j];

                // 下一块刷新
                nextCase[i][j] = store[index][i][j];
            }
        }

        for (int j = 0; j < currentCase.length; j++) {
            for (int i = 0; i < currentCase[j].length; i++) {
                if (currentCase[i][j] != 0) {
                    currentY = currentCase.length - 1 - j;
                }
            }
        }

        int width = 0;
        int start = 0;
        int end = 0;
        boolean isBreak;

        isBreak = false;
        for (int i = 0; i < currentCase.length; i++) {
            for (int j = 0; j < currentCase.length; j++) {
                if (currentCase[i][j] != 0) {
                    start = i;
                    isBreak = true;
                    break;
                }
            }
            if (isBreak) {
                break;
            }
        }

        isBreak = false;
        for (int i = currentCase.length - 1; i >= 0; i--) {
            for (int j = 0; j < currentCase.length; j++) {

                if (currentCase[i][j] != 0) {
                    end = i + 1;
                    isBreak = true;
                    break;
                }

            }

            if (isBreak) {
                break;
            }
        }

        width = end - start;

        currentX = (body_W - width) / 2 - start;

        isNewCase = false;
        isGameOver();
    }

    // 是否结束
    public void isGameOver() {
        for (int i = 0; i < currentCase.length; i++) {
            for (int j = 0; j < currentCase[i].length; j++) {
                if (currentCase[i][j] != 0) {
                    if (!isSpace(currentX + i, currentY + j)) {
                        // 游戏结束
                        // gamestate = STATE_GAMEOVER;
                        isGameOver = true;
                        // startGame();
                        return;
                    }
                }
            }
        }
    }

    public void toOver() {
        // for(int i=0;i<midlet.scores.length;i++){
        // if(score>midlet.scores[i]){
        // midlet.inputName(i,score);
        // gamestate = STATE_MENU;
        // return;
        // }
        // }
        gamestate = STATE_MENU;
    }

    public void setMode(int newMode) {
        gamestate = newMode;
    }

    public Bundle saveState() {
        Bundle map = new Bundle();

        return map;
    }

    public void restoreState(Bundle icicle) {

    }

}
