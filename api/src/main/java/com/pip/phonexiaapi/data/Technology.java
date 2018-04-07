package com.pip.phonexiaapi.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by filipsollar on 6.4.18.
 */

public class Technology implements Serializable {
    private String name;
    private String abbreviation;
    private List<Model> models;


    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public List<Model> getModels() {
        return models;
    }

    public static class Model implements Serializable{
        private String name;
        private String version;
        @SerializedName("n_total_instances")
        private int nTotalInstances;
        @SerializedName("n_busy_instancess")
        private int nBusyInstances;

        public String getName() {
            return name;
        }

        public String getVersion() {
            return version;
        }

        public int getnTotalInstances() {
            return nTotalInstances;
        }

        public int getnBusyInstances() {
            return nBusyInstances;
        }
    }
}
