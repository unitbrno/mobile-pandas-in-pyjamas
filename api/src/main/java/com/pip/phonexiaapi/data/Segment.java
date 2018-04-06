package com.pip.phonexiaapi.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by filipsollar on 6.4.18.
 */

public class Segment {
    @SerializedName("channel_id")
    private long channelId;
    private double score;
    private double confidence;
    private long start;
    private long end;
    private String word;

    public long getChannelId() {
        return channelId;
    }

    public double getScore() {
        return score;
    }

    public double getConfidence() {
        return confidence;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public String getWord() {
        return word;
    }
}
