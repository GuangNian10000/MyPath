package com.my.path.app.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;

import com.my.path.app.App;

import java.util.Arrays;

/**
 * @author GuangNian
 * @description:
 * @date : 2022/4/15 2:52 下午
 */
public class BitmapUtil {
    public static final String TAG = "BitmapUtil";

    /**
     * 将除了圈中的地方外的地方清空且删除
     *
     * @param bitmap
     * @param x
     * @param y
     * @param width
     * @param height
     * @param rot
     * @return
     */
    public static Bitmap clipBitMap(Bitmap bitmap, float x, float y, float width, float height, float rot) {
        float left = x;
        float top = y;
        float right = x + width;
        float bottom = y + height;
        if (bitmap == null) {
            Log.e(TAG, "输入的bitmap是空的！");
            return Bitmap.createBitmap(App.instance.getResources().getDisplayMetrics(),1, 1, Bitmap.Config.ARGB_8888);
        }
        /**
         * 初始画布和空白bitmap
         */
        Bitmap bitmap1 = Bitmap.createBitmap(App.instance.getResources().getDisplayMetrics(),bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(bitmap1);
        /**
         *创建bitmap画笔
         */
        Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 创建矩形
         */
        //创建好大小位置
        RectF rect = new RectF(left, top, right, bottom);
        //画布添加框
        targetCanvas.drawRect(rect, new Paint(Paint.ANTI_ALIAS_FLAG));
        /**
         * 将非选中区域删除
         */
        //旋转画布（注意要反方向旋转）
        targetCanvas.rotate(-rot, (left + right) / 2, (top + bottom) / 2);
        //画布添加bitmap
        targetCanvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        //裁剪bitmap
        Bitmap bitmap2 = crop(bitmap1);
        return bitmap2;
    }

    /**
     * 裁剪bitmap
     *
     * @param bitmap
     * @return
     */
    public static Bitmap crop(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int[] empty = new int[width];
        int[] buffer = new int[width];
        Arrays.fill(empty, 0);
        int top = 0;
        int left = 0;
        int bottom = height;
        int right = width;
        for (int y = 0; y < height; y++) {
            bitmap.getPixels(buffer, 0, width, 0, y, width, 1);
            if (!Arrays.equals(empty, buffer)) {
                top = y;
                break;
            }
        }
        for (int y = height - 1; y > top; y--) {
            bitmap.getPixels(buffer, 0, width, 0, y, width, 1);
            if (!Arrays.equals(empty, buffer)) {
                bottom = y;
                break;
            }
        }
        empty = new int[height];
        buffer = new int[height];
        Arrays.fill(empty, 0);
        for (int x = 0; x < width; x++) {
            bitmap.getPixels(buffer, 0, 1, x, 0, 1, height);
            if (!Arrays.equals(empty, buffer)) {
                left = x;
                break;
            }
        }
        for (int x = width - 1; x > left; x--) {
            bitmap.getPixels(buffer, 0, 1, x, 0, 1, height);
            if (!Arrays.equals(empty, buffer)) {
                right = x;
                break;
            }
        }
        return Bitmap.createBitmap(bitmap, left, top, right - left + 1, bottom - top + 1);
    }
}