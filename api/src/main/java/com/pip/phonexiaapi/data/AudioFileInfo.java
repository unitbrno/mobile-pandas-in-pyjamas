package com.pip.phonexiaapi.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by filipsollar on 6.4.18.
 */

public class AudioFileInfo extends DirectoryItem{

    private long frequency;
    private double length;
    @SerializedName("n_channels")
    private int nChannels;
    private String format;

    public long getFrequency() {
        return frequency;
    }

    public double getLength() {
        return length;
    }

    public int getnChannels() {
        return nChannels;
    }

    public String getFormat() {
        return format;
    }
}
