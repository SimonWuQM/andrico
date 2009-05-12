/************************************
 * Andrico Team Copyright 2009      *
 * http://code.google.com/p/andrico *
 ************************************/
package org.andrico.andrico.facebook;

import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

/**
 * Represents a facebook method to remotely execute by way of REST.
 */
public class FacebookMethod {
    // Non-static because we customize it.
    private final static String LOG = "FacebookMethod";

    /**
     * Create an md5sum of all a method's parameters per @link XXX
     * 
     * @param md5 A MessageDigest set to generate md5s
     * @param parameters The parameters used for the method that will be
     *            signaturized.
     * @param secret A session's secret token.
     * @return
     */
    protected static String signature(MessageDigest md5, HashMap<String, String> parameters,
            String secret) {
        Log.d(LOG, "Using secret: " + secret);
        Vector<String> keys = new Vector<String>(parameters.keySet());
        Collections.sort(keys);
        StringBuffer concat = new StringBuffer();
        for (Object key : keys) {
            concat.append(key + "=" + parameters.get(key));
        }
        concat.append(secret);

        byte[] sig = md5.digest(concat.toString().getBytes());
        return md5HexDigest(sig);
    }

    private static String md5HexDigest(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    /**
     * Given the parameters return a string applicable as the query parameters
     * of an http GET.
     * 
     * @param parameters The key,value pairs to turn into a query.
     * @return
     */
    protected static String urlParameters(HashMap<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        Log.d(LOG, "urlParameters: " + parameters);
        for (Object key : parameters.keySet()) {
            sb.append((String)key + "=" + java.net.URLEncoder.encode(parameters.get(key)) + "&");
        }

        int sbLength = sb.length();
        if (sbLength > 0) {
            sb.deleteCharAt(sbLength - 1);
        }
        return sb.toString();
    }

    private String LOG_SPECIFIC = "FacebookMethod";

    protected String mApiKey;
    protected HashMap<String, String> mParameters;
    protected String mMethod;
    protected String mSecret;
    protected String mSession;
    protected byte[] mData;
    protected String mDataContentType = "image/jpg";
    protected String mDataFilename;
    private MessageDigest mMd5;

    FacebookMethod(String method, String apiKey, String secret, String session,
            HashMap<String, String> parameters) {
        // Help keep track of messages when there are multiple at once.
        LOG_SPECIFIC += "." + method;

        mMethod = method;
        mApiKey = apiKey;
        mSecret = secret;
        mSession = session;

        Log.d(LOG_SPECIFIC, mMethod);
        Log.d(LOG_SPECIFIC, mApiKey);
        Log.d(LOG_SPECIFIC, mSecret);
        if (mSession != null) {
            Log.d(LOG_SPECIFIC, mSession);
        } else {
            Log.d(LOG_SPECIFIC, "null");
        }

        mParameters = getRequestParameters(method);
        if (parameters != null) {
            mParameters.putAll(parameters);
        }

        // Build the md5 builder
        try {
            mMd5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // NO ERROR HANDLING!
        }
    }

    /**
     * Get the default set of parameters for a facebook method.
     * 
     * @param method
     * @return
     */
    private HashMap<String, String> getRequestParameters(String method) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("api_key", mApiKey);
        parameters.put("format", "JSON");
        parameters.put("v", "1.0");
        parameters.put("method", method);
        if (mSession != null) {
            Log.d(LOG_SPECIFIC, "Using session_key for getRequestParameters");
            parameters.put("session_key", mSession);
        }

        return parameters;
    }

    /**
     * Generate the full URL to be used to execute this method.
     * 
     * @return
     */
    public String getRequestUrl() {
        String stringParams = (mParameters.isEmpty()) ? "" : (urlParameters(mParameters) + "&");
        Log.d(LOG_SPECIFIC, "stringParams: " + stringParams);
        String requestUrl = Facebook.REST_URI + stringParams + "sig="
                + FacebookMethod.signature(mMd5, mParameters, mSecret);
        Log.d(LOG_SPECIFIC, "getRequestUrl: " + requestUrl);
        return requestUrl;
    }

    /**
     * Test if a method has data that needs to be be posted.
     * 
     * @return
     */
    protected boolean hasData() {
        return (mData != null);
    }

    /**
     * If data is to be posted as part of the method execution, set it with
     * this.
     * 
     * @param data The bytes to be transmitted to facebook.
     * @param filename A name associated with the data.
     * @param contentType The type this data composes, eg "image/jpg" for a jpg.
     */
    protected void setData(byte[] data, String filename, String contentType) {
        mData = data;
        mDataContentType = contentType;
        mDataFilename = filename;
    }

}
