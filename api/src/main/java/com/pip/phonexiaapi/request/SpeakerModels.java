package com.pip.phonexiaapi.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by filipsollar on 6.4.18.
 */

public class SpeakerModels implements Serializable {

    @SerializedName("speaker_models")
    private List<String> speakerNames;

    public void setSpeakerNames(List<String> speakerNames) {
        this.speakerNames = speakerNames;
    }
}
