package com.pip.phonexiaapi.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by filipsollar on 6.4.18.
 */

public class Speaker implements Serializable{
    private String file;
    @SerializedName("speaker_model")
    private String speakerModel;
    @SerializedName("channel_scores")
    private List<ChannelScore> channelScores;

    public String getFile() {
        return file;
    }

    public String getSpeakerModel() {
        return speakerModel;
    }

    public List<ChannelScore> getChannelScores() {
        return channelScores;
    }

    public static class ChannelScore {

        private int channel;
        @SerializedName("channel_scores")
        private List<Score> scores;

        public int getChannel() {
            return channel;
        }

        public List<Score> getScores() {
            return scores;
        }

        public static class Score {
            private double score;

            public double getScore() {
                return score;
            }
        }
    }
}
