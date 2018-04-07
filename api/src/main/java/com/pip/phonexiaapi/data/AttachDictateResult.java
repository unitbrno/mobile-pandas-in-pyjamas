package com.pip.phonexiaapi.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by filipsollar on 6.4.18.
 */

public class AttachDictateResult extends BaseResult implements Serializable {

    @SerializedName("stream_task_info")
    private StreamTaskInfo streamTaskInfo;

    public StreamTaskInfo getStreamTaskInfo() {
        return streamTaskInfo;
    }
}
