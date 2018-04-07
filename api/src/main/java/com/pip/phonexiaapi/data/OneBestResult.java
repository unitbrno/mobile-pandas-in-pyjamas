package com.pip.phonexiaapi.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by filipsollar on 6.4.18.
 */

public class OneBestResult implements Serializable {

    @SerializedName("segmentation")
    private List<Segment> segments;

    public List<Segment> getSegments() {
        return segments;
    }
}
