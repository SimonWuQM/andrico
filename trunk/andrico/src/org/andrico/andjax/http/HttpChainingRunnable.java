/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/
package org.andrico.andjax.http;

import org.apache.http.HttpResponse;

import android.util.Log;

import java.util.concurrent.Callable;

/**
 * These runnables are used to take a pair of runnables and call the first and
 * setting its HttpResponse on the second, which is then called. This runnable
 * currently only supports successful requests. Failed requests will be lost in
 * the aether. There is a TODO to handle this.
 * 
 * 
 */
class HttpChainingRunnable implements Runnable {
    private static final String LOG = "HttpChainingRunnable";

    private final Callable<HttpResponse> mRequest;
    private final IHttpResponseRunnable mResponseRunnable;

    /**
     * @param request The http request to make.
     * @param responseRunnable The runnable to call after the request completes.
     */
    public HttpChainingRunnable(Callable<HttpResponse> request,
            IHttpResponseRunnable responseRunnable) {
        this.mRequest = request;
        this.mResponseRunnable = responseRunnable;
    }

    public void run() {
        try {
            final HttpResponse response = this.mRequest.call();
            if (this.mResponseRunnable != null) {
                this.mResponseRunnable.setResponse(response);
                this.mResponseRunnable.run();
            }
        } catch (final Exception e) {
            Log.d(LOG, "Request failed, calling fail() after exception:", e);
            this.mResponseRunnable.fail(e);
        }
    }
}
