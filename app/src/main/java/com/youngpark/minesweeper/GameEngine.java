package com.youngpark.minesweeper;

import com.youngpark.minesweeper.Util.RandUtil;

/**
 * youngpark 2020.02.04
 *
 * 扫雷 v0.1
 *
 * 游戏逻辑引擎：
 * 地图生成管理，游戏逻辑管理
 */


public class GameEngine {

    public static int mapSize = 9, boomNum = 10;
    public static boolean isFlag = false;

    public static volatile int[][] array = null;
    public static volatile byte[][] show = null;

    private static final int[]
            directX = {-1, 0, 1, -1, 1, -1, 0, 1},
            directY = {1, 1, 1, 0, 0, -1, -1, -1};


    /**
     * array
     *
     * 0      base
     * 1 - 8  boom_num
     * -1     boom
     * -2     boom_exp
     *
     **/


    /**
     * show
     *
     * -2   down_flag
     * -1   down
     * 0    mask
     * 1    show
     * 2    flag
     */

    public static void init() {
        isFlag = false;
        flagNum = 0;
        array = new int[mapSize][mapSize];
        show = new byte[mapSize][mapSize];
        visit = new boolean[mapSize][mapSize];

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                array[i][j] = 0;
                show[i][j] = 0;
            }
        }

        /**
         * 随机放雷
         */
        for (int i = 0; i < boomNum; i++) {
            int x = RandUtil.getBoom(mapSize);
            int y = RandUtil.getBoom(mapSize);
            while (array[x][y] == -1) {
                x = RandUtil.getBoom(mapSize);
                y = RandUtil.getBoom(mapSize);
            }
            array[x][y] = -1;
        }

        /**
         * 地图数字生成
         */
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                if (array[x][y] == -1) {
                    for (int j = 0; j < directX.length; j++) {
                        int nowX = x + directX[j];
                        int nowY = y + directY[j];
                        if (nowX >= 0 && nowY >= 0 && nowX < mapSize && nowY < mapSize && array[nowX][nowY] != -1) {
                            array[nowX][nowY]++;
                        }
                    }
                }
            }
        }
    }

    private static void showAll() {
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                show[i][j] = 1;
            }
        }
    }

    private static boolean visit[][];

    private static void initBFS() {
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                visit[i][j] = false;
            }
        }
    }

    /**
     * youngpark 2020。02。04
     *
     * 对地图进行广度搜索，遍历出所有空地
     * @param x 坐标x
     * @param y 坐标y
     */

    public static void bfs(int x, int y) {
        if (show[x][y] == 2) {
            flagNum--;
        }
        show[x][y] = 1;
        visit[x][y] = true;
        for (int j = 0; j < directX.length; j++) {
            int nowX = x + directX[j];
            int nowY = y + directY[j];
            if (nowX >= 0 && nowY >= 0 && nowX < mapSize && nowY < mapSize && !visit[nowX][nowY]) {
                if (array[nowX][nowY] == 0) {
                    bfs(nowX, nowY);
                } else if (array[nowX][nowY] != -1) {
                    if (show[nowX][nowY] == 2) {
                        flagNum--;
                    }
                    show[nowX][nowY] = 1;
                }

            }
        }
    }

    public static boolean touch(int x, int y) {
        if (array[x][y] == -1) {
            array[x][y] = -2;
            showAll();
            return false;
        } else if (array[x][y] == 0) {
            initBFS();
            bfs(x, y);
            return true;
        } else {
            show[x][y] = 1;
            return true;
        }
    }

    public static int flagNum = 0;

    /**
     * 插旗
     *
     * @param x 坐标x
     * @param y 坐标y
     * @return 是否插旗成功（旗帜是有数量限制的）
     */

    public static boolean flag(int x, int y) {

        if (flagNum < boomNum + 1 && show[x][y] == -1) {
            show[x][y] = 2;
            flagNum++;
            return true;
        } else if (show[x][y] == -2) {
            show[x][y] = 0;
            flagNum--;
            return true;
        } else {
            if (show[x][y] == -1) {
                show[x][y] = 0;
            }
            return false;
        }
    }

    public static boolean win() {
        boolean success = true;
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                if (show[i][j] != 1 && array[i][j] != -1) {
                    success = false;
                    break;
                }
            }
        }
        if (success) {
            showAll();
        }
        return success;
    }


}
