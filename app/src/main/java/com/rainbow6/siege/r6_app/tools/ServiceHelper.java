package com.rainbow6.siege.r6_app.tools;

import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;
import com.rainbow6.siege.r6_app.service.UbiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.rainbow6.siege.r6_app.service.UbiService.EXCEPTION_PATTERN;

public class ServiceHelper {

    public static final String UBI_ERROR_CODE = "errorCode";
    private static final String UBI_ERROR_BEGIN = "\"message\":\"";
    private static final String UBI_ERROR_END = "\",\"";
    private static final String UBI_TICKET_BEGIN = "Ubi_v1 t=";
    private static final String UBI_TICKET_KEY = "ticket";
    private static final String UBI_EXPIRATION_KEY = "expiration";
    private static final String UBI_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String UBI_DATE_DELIMITER = "\\.";
    private static final String UBI_EMPTY_RESPONSE = "Empty response";

    public ConnectionEntity generateConnectionEntity(String response, String encodedKey) throws JSONException, ParseException {

        JSONObject json = new JSONObject(response);
        SimpleDateFormat formatter = new SimpleDateFormat(UBI_DATE_FORMAT, Locale.getDefault());

        String ticket = UBI_TICKET_BEGIN + json.getString(UBI_TICKET_KEY);
        Date expiration = formatter.parse(json.getString(UBI_EXPIRATION_KEY).split(UBI_DATE_DELIMITER)[0]);

        return new ConnectionEntity(UbiService.APP_ID, encodedKey, ticket, expiration);
    }

    public String getErrorMessage(String response){
        String message;
        if (response.contains(UBI_ERROR_CODE)) {
            String errorMessageBegin = UBI_ERROR_BEGIN;
            String errorMessageEnd = UBI_ERROR_END;
            int pFrom = response.indexOf(errorMessageBegin) + errorMessageBegin.length();
            int pTo = response.indexOf(errorMessageEnd, pFrom);

            message = response.substring(pFrom, pTo);
        } else if(response.contains(EXCEPTION_PATTERN)) {
            message = response;
        } else {
            message = UBI_EMPTY_RESPONSE;
        }
        return message;
    }

}
