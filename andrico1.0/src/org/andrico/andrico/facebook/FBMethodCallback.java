/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/

package org.andrico.andrico.facebook;

import org.andrico.andjax.http.HttpResponseRunnable;
import org.andrico.andjax.http.IHttpResponseRunnable;

/**
 * Extend this to enable a callback when a FacebookMethod completes.
 */
abstract public class FBMethodCallback extends HttpResponseRunnable implements
        IHttpResponseRunnable {
    private String mResponse;

    protected String getResult() {
        if (mResponse == null) {
            mResponse = HttpResponseRunnable.httpResponseToString(this.getResponse());
        }
        return mResponse;
    }
}
