package com.my.path.app.util.ait;


/**
 * Create by peng on 2020/11/13
 */
public class FormatRangBean extends RangBean {
    private String uploadFormatText;

    public FormatRangBean(int from, int to) {
        super(from, to);
    }

    public String getUploadFormatText() {
        return uploadFormatText;
    }

    public void setUploadFormatText(String uploadFormatText) {
        this.uploadFormatText = uploadFormatText;
    }
}
