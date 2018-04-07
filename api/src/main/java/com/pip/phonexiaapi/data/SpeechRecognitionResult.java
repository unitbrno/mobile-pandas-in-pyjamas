package com.pip.phonexiaapi.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by filipsollar on 6.4.18.
 */

public class SpeechRecognitionResult extends BaseResult {

    private Language model;
    @SerializedName("is_last")
    private boolean isLast;
    @SerializedName("delete_n_words")
    private int deleteNWords;
    @SerializedName("one_best_result")
    private OneBestResult recognitionResult;

    public Language getModel() {
        return model;
    }

    public boolean isLast() {
        return isLast;
    }

    public int getDeleteNWords() {
        return deleteNWords;
    }

    public OneBestResult getRecognitionResult() {
        return recognitionResult;
    }
}
