package com.pip.phonexiaapi;

import rx.Observable;
import rx.Single;

/**
 * Created by filipsollar on 6.4.18.
 */

public interface IServerApi {


    /**
     * Using WebSockets or polling
     * @return
     */
    Single asynchronousRequest();

    /**
     * Synchronous request for short recordings
     * @return
     */
    Single synchronousRequest();

    /**
     * stream request real time translation
     * @return
     */
    Observable stream();


}
