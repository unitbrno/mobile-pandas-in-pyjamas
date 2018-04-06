package com.pip.phonexiaapi.data;

import java.util.List;

/**
 * Created by filipsollar on 6.4.18.
 */

public class SpeakerModelsResponse extends BaseResult {

    private List<String> models;
    private List<String> groups;

    public List<String> getModels() {
        return models;
    }

    public List<String> getGroups() {
        return groups;
    }
}
