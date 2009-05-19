/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/

package org.andrico.andrico;

import org.andrico.andjax.http.HttpResponseRunnable;
import org.andrico.andjax.http.ByteArrayBody.WriteToProgressHandler;
import org.andrico.andrico.facebook.FB;
import org.andrico.andrico.facebook.FBMethod;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

class UiHandler extends Handler {
    private static final String LOG = "UiHandler";

    private Context mContext;
    private FB mFacebook;

    /**
     * @param context
     * @param statusBar
     */
    UiHandler(Context context, FB facebook) {
        super();
        mContext = context;
        mFacebook = facebook;
    }

    void executeMethodForMessage(FBMethod m, int messageCode, WriteToProgressHandler progressHandler) {
        HttpResponse response = null;
        try {
            response = mFacebook.execute(m, progressHandler);
        } catch (NullPointerException e) {
            // We don't care if we get a npe. the npe will be handled by the handler.
        } finally {
            Bundle bundle = new Bundle();
            bundle.putString("result", HttpResponseRunnable.httpResponseToString(response));
            Message msg = obtainMessage(messageCode);
            msg.setData(bundle);
            sendMessage(msg);
        }
    }
    
    void executeMethodForMessage(FBMethod m, int messageCode) {
        executeMethodForMessage(m, messageCode, null);
    }

    @Override
    protected void finalize() throws Throwable {
        mContext = null;
        mFacebook = null;
        super.finalize();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Log.d(LOG, "what:" + msg.what);
        Log.d(LOG, "result: " + msg.getData().getString("result"));
    }

    void notifyUser(final String statusBarMessage) {
        notifyUser(statusBarMessage, true);
    }

    void notifyUser(final String statusBarMessage, final Boolean toast) {
        post(new Runnable() {
            public void run() {
                if (toast) {
                    Toast.makeText(mContext, statusBarMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
        return;
    }

    /**
     * Notify user and update logged in setting if there was an error.
     * @param errorCode
     */
    void handleErrorCode(Integer errorCode) {
        if (errorCode == null) {
            Log.d(LOG, "handleErrorCode was handed null");
            notifyUser("A network error occured.");
        } else if (errorCode == 102) {
            notifyUser("Please login again, your session is invalid.");
            Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
            editor.putBoolean(Preferences.FACEBOOK_CRED_LOGGED_IN, false);
            editor.commit();
        } else if (errorCode == 104) {
            // This means the signature was incorrect... The app will
            // probably want to try this request again.
        } else if (errorCode == 250) {
            notifyUser("Please authorize 'Status Updates' and 'Photo Uploads' from preferences.");
        } else {
            notifyUser("An unknown error occured.");
        }
    }

    /**
     * @param msg
     * @return The error code if there was a facebook problem, null if it was
     *         OK, -1 if there was an internal error.
     */
    Integer parseErrorCodeInMessage(Message msg) {
        return JsonParser.parseForErrorCode(msg.getData().getString("result"));
    }
}
