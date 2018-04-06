package com.pip.phonexiaapi;

/**
 * Created by filipsollar on 6.4.18.
 */

interface RealTimeCallback<T>{
    void onError(Throwable t);
    void onResult(T result);
    void finished();


}