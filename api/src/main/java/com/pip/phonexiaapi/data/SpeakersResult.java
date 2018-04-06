package com.pip.phonexiaapi.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by filipsollar on 6.4.18.
 */

public class SpeakersResult extends BaseResult {
    private String model;
    @SerializedName("is_last")
    private boolean isLast;
    @SerializedName("task_id")
    private String taskId;
    @SerializedName("task_execution_time")
    private double taskExecutionTime;
    @SerializedName("stream_id")
    private String streamId;
    @SerializedName("stream_port_is_open")
    private boolean streamPortIsOpen;
    @SerializedName("speaker_group")
    private String speakerGroup;
    @SerializedName("speaker_model")
    private String speakerModel;
    @SerializedName("utterance_length")
    private double utteranceLength;
    @SerializedName("min_utterance_limit")
    private double minUtteranceLimit;
    @SerializedName("min_utterance_limit_reached")
    private boolean minUtteranceLimitReached;
    @SerializedName("calibration_set")
    private String calibrationSet;
    @SerializedName("max_fa_rate")
    private double maxFaRate;

    private List<Speaker> results;

    public String getModel() {
        return model;
    }

    public boolean isLast() {
        return isLast;
    }

    public String getTaskId() {
        return taskId;
    }

    public double getTaskExecutionTime() {
        return taskExecutionTime;
    }

    public String getStreamId() {
        return streamId;
    }

    public boolean isStreamPortIsOpen() {
        return streamPortIsOpen;
    }

    public String getSpeakerGroup() {
        return speakerGroup;
    }

    public String getSpeakerModel() {
        return speakerModel;
    }

    public double getUtteranceLength() {
        return utteranceLength;
    }

    public double getMinUtteranceLimit() {
        return minUtteranceLimit;
    }

    public boolean isMinUtteranceLimitReached() {
        return minUtteranceLimitReached;
    }

    public String getCalibrationSet() {
        return calibrationSet;
    }

    public double getMaxFaRate() {
        return maxFaRate;
    }

    public List<Speaker> getResults() {
        return results;
    }
}
