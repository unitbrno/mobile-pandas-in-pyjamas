package com.pip.phonexiaapi.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by filipsollar on 6.4.18.
 */

public class StreamTaskInfo implements Serializable {
    private String id;
    private String state;
    @SerializedName("stream_id")
    private String streamId;

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getStreamId() {
        return streamId;
    }
}
