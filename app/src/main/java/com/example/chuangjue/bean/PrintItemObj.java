package com.example.chuangjue.bean;

/**
 * @author kuan
 * Created on 2018/4/9.
 * @description
 */
public class PrintItemObj {

    private String content;
    private float textSize = 16f;
    private boolean isBold = false;

    /**
     * 默认小号字体 加粗
     * @param content
     */
    public PrintItemObj(String content) {
        this.content = content;
    }

    /**
     * 默认小号字体
     * @param content
     * @param isBold
     */
    public PrintItemObj(String content, boolean isBold) {
        this.content = content;
        this.isBold = isBold;
    }

    /**
     * 默认不加粗
     * @param content
     * @param textSize
     */
    public PrintItemObj(String content, float textSize) {
        this.content = content;
        this.textSize = textSize;
    }

    public PrintItemObj(String content, float textSize, boolean isBold) {
        this.content = content;
        this.textSize = textSize;
        this.isBold = isBold;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public boolean getBold() {
        return isBold;
    }


    public void setBold(boolean bold) {
        isBold = bold;
    }


    @Override
    public String toString() {
        return "PrintItemObj{" +
                "content='" + content + '\'' +
                ", textSize=" + textSize +
                ", getBold=" + isBold +
                '}';
    }
}
