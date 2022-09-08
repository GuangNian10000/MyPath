package com.my.path.app.util.ait;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Create by peng on 2020/11/26
 */
public class TopicBean implements Serializable, InsertData {
    private int topicId;
    private String topic;

    public TopicBean(int topicId, String topic) {
        this.topicId = topicId;
        this.topic = topic;
    }


    @Override
    public String showText() {
        return "#"+topic+"#";
    }

    @Override
    public String uploadFormatText() {
        final String TOPIC_FORMART = "{[%s, %s]}";
        return String.format(TOPIC_FORMART,"#"+topic+"#",topicId);
    }

    @Override
    public int color() {
        return Color.GREEN;
    }


    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
