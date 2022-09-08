package com.my.path.app.weight.textview;

/**
 * @author GuangNian
 * @description:
 * @date : 2022/8/24 11:44 上午
 */
public final class WidthAndHeight {
    private float width;
    private float height;

    public WidthAndHeight() {
    }

    public WidthAndHeight(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setWidthAndHeight(float width, float height){
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "WidthAndHeight{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}