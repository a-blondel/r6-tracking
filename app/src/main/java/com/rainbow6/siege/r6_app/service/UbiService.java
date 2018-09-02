package com.rainbow6.siege.r6_app.service;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UbiService {

    public static final String APP_ID = "39baebad-39e5-4552-8c25-2c9b919064e2";


    public String callUbiConnectionService (String encodedKey){

        String response;
        String connectionUrl = "https://connect.ubi.com/ubiservices/v2/profiles/sessions";

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(connectionUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Ubi-AppId", APP_ID);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization", "Basic " + encodedKey);

            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write("{rememberMe: true}");

            osw.flush();
            osw.close();
            os.close();
            urlConnection.connect();

//            System.out.println(urlConnection.getResponseCode());
//            System.out.println(urlConnection.getResponseMessage());

            if (200 <= urlConnection.getResponseCode() && urlConnection.getResponseCode() <= 299) {
                response = IOUtils.toString(urlConnection.getInputStream(), StandardCharsets.UTF_8);
            } else {
                response = IOUtils.toString(urlConnection.getErrorStream(), StandardCharsets.UTF_8);
            }

        } catch (MalformedURLException e) {
            response = "ERROR:" + e.getMessage();
        } catch (IOException e) {
            response = "ERROR:" + e.getMessage();
        } finally {
            urlConnection.disconnect();
        }
        return response;
    }


    //get stats... (String ticket, String plateformType) psn ... not implemented yet

}
