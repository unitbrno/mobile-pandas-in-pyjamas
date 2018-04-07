package com.pip.phonexiaapi;

/**
 * Created by filipsollar on 6.4.18.
 */

public interface ApiCallback<T> {

    void onSuccess(T result);
    void onFailure(Throwable t);

}
