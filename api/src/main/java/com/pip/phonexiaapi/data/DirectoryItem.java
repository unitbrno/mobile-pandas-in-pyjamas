package com.pip.phonexiaapi.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by filipsollar on 6.4.18.
 */

public class DirectoryItem implements Serializable {
    private String name;
    @SerializedName("last_modified")
    private Date lastModified;

    private Date created;
    private long size;
    @SerializedName("is_directory")
    private boolean directory;
    @SerializedName("is_registered")
    private boolean registered;

    public String getName() {
        return name;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public Date getCreated() {
        return created;
    }

    public long getSize() {
        return size;
    }

    public boolean isDirectory() {
        return directory;
    }

    public boolean isRegistered() {
        return registered;
    }
}
