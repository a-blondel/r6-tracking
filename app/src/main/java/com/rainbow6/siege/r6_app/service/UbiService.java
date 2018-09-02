package com.rainbow6.siege.r6_app.service;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UbiService {

    public static final String APP_ID = "39baebad-39e5-4552-8c25-2c9b919064e2";
    public static final String EXCEPTION_PATTERN = "ERROR: ";
    public static final String CHARSET_UTF8 = "UTF-8";
    private static final String POST_METHOD = "POST";
    private static final String HEADER_UBI_APPID = "Ubi-AppId";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_BASIC = "Basic ";
    private static final String REMEMBER_BE = "{rememberMe: true}";
    private static final int RESPONSE_CODE_200 = 200;
    private static final int RESPONSE_CODE_299 = 299;

    private static final String PLAYSTATION = "psn";

    public String callUbiConnectionService (String encodedKey){

        String response;
        String connectionUrl = "https://connect.ubi.com/ubiservices/v2/profiles/sessions";

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = prepareRequest(connectionUrl, AUTHORIZATION_BASIC + encodedKey);

            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_UTF8);
            osw.write(REMEMBER_BE);
            osw.flush();
            osw.close();
            os.close();
            urlConnection.connect();

            response = getResponse(urlConnection);

        } catch (MalformedURLException e) {
            response = EXCEPTION_PATTERN + e.getMessage();
        } catch (IOException e) {
            response = EXCEPTION_PATTERN + e.getMessage();
        } finally {
            urlConnection.disconnect();
        }
        return response;
    }

    public String callWebService(String serviceUrl, String ticket) {
        String response;

            HttpURLConnection urlConnection = null;
            try {
                urlConnection = prepareRequest(serviceUrl, ticket);

                OutputStream os = urlConnection.getOutputStream();
                os.close();
                urlConnection.connect();

                response = getResponse(urlConnection);

            } catch (MalformedURLException e) {
                response = EXCEPTION_PATTERN + e.getMessage();
            } catch (ProtocolException e) {
                response = EXCEPTION_PATTERN + e.getMessage();
            }catch (IOException e) {
                response = EXCEPTION_PATTERN + e.getMessage();
            } finally {
                urlConnection.disconnect();
            }
        return response;
    }

    private HttpURLConnection prepareRequest(String connectionUrl, String authorization) throws IOException {
        HttpURLConnection urlConnection = null;
        URL url;
        url = new URL(connectionUrl);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod(POST_METHOD);
        urlConnection.setRequestProperty(HEADER_UBI_APPID, APP_ID);
        urlConnection.setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);
        urlConnection.setRequestProperty(HEADER_AUTHORIZATION, authorization);
        return urlConnection;
    }

    private String getResponse(HttpURLConnection urlConnection) throws IOException {
        if (RESPONSE_CODE_200 <= urlConnection.getResponseCode() && urlConnection.getResponseCode() <= RESPONSE_CODE_299) {
            return IOUtils.toString(urlConnection.getInputStream(), StandardCharsets.UTF_8);
        } else {
            return IOUtils.toString(urlConnection.getErrorStream(), StandardCharsets.UTF_8);
        }
    }

}
