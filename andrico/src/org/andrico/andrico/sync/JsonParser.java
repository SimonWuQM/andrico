/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************
 *         Обработчик ошибок			*
 *										*
 ****************************************/



package org.andrico.andrico.sync;

import org.json.JSONException;
import org.json.JSONObject;


public class JsonParser {
    
    /**
     * Check the result for an error code.
     * 
     * @param result
     * @return -1 if no error, otherwise the integer error code, null if a
     *         problem occurred.
     */
    public static Integer parseForErrorCode(String result) {
        JSONObject error;
        try {
            error = new JSONObject(result);
            return error.getInt("error_code");
        } catch (JSONException e) {
            // This likely means that the result is not an error.
            return -1;
        } catch (NullPointerException e) {
            return null;
        }
    }
}
