package com.rainbow6.siege.r6_app.service;

import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class UbiService {

    public static final String APP_ID = "39baebad-39e5-4552-8c25-2c9b919064e2";
    public static final String EXCEPTION_PATTERN = "EXCEPTION: ";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String REGION_EAMEA = "emea";
    public static final String REGION_NCSA = "ncsa";
    public static final String REGION_APAC = "apac";
    public static final int CURRENT_SEASON = -1;
    private static final String POST_METHOD = "POST";
    private static final String GET_METHOD = "GET";
    private static final String HEADER_UBI_APPID = "Ubi-AppId";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_BASIC = "Basic ";
    private static final String REMEMBER_BE = "{rememberMe: true}";
    private static final int RESPONSE_CODE_200 = 200;
    private static final int RESPONSE_CODE_299 = 299;
    private static final List<String> statsNames = Arrays.asList(
            "rankedpvp_timeplayed:infinite", "rankedpvp_matchplayed:infinite", "rankedpvp_matchwon:infinite", "rankedpvp_matchlost:infinite",
            "rankedpvp_death:infinite", "rankedpvp_kills:infinite",
            "casualpvp_timeplayed:infinite", "casualpvp_matchplayed:infinite", "casualpvp_matchwon:infinite", "casualpvp_matchlost:infinite",
            "casualpvp_kills:infinite", "casualpvp_death:infinite",
            "generalpvp_timeplayed:infinite", "generalpvp_matchplayed:infinite", "generalpvp_matchwon:infinite", "generalpvp_matchlost:infinite",
            "generalpvp_kills:infinite", "generalpvp_death:infinite", "generalpvp_killassists:infinite",
            "generalpvp_headshot:infinite", "generalpvp_meleekills:infinite", "generalpvp_penetrationkills:infinite",
            "generalpvp_bulletfired:infinite", "generalpvp_bullethit:infinite", "generalpvp_revive:infinite"
    );

    private static final String PLAYSTATION = "psn";

    public String callUbiConnectionService (String encodedKey){

        String response;
        String connectionUrl = "https://connect.ubi.com/ubiservices/v2/profiles/sessions";

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = prepareRequest(connectionUrl, AUTHORIZATION_BASIC + encodedKey, POST_METHOD);
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
                urlConnection = prepareRequest(serviceUrl, ticket, GET_METHOD);
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

    public String getProfileResponse(String ticket, String playerName, String plateformType){
        String profileUrl = "https://public-ubiservices.ubi.com/v2/profiles?nameOnPlatform=" + playerName + "&platformType=" + plateformType;
        Log.d("Debug---profileUrl", profileUrl);

        String profileResponse = callWebService(profileUrl, ticket);
        Log.d("Debug---profileResponse", profileResponse);
        return profileResponse;
    }

    public String getProgressionResponse(String ticket, String profileId, String plateformType){
        // plateformType is not yet implemented
        String progressionUrl = "https://public-ubiservices.ubi.com/v1/spaces/05bfb3f7-6c21-4c42-be1f-97a33fb5cf66/sandboxes/OSBOR_PS4_LNCH_A/r6playerprofile/playerprofile/progressions?profile_ids=" + profileId;
        Log.d("Debug---progressionUrl", progressionUrl);

        String progressionResponse = callWebService(progressionUrl, ticket);
        Log.d("Debug---progressionResp", progressionResponse);
        return progressionResponse;
    }

    public String getSeasonResponse(String ticket, String profileId, String region, int seasonNumber, String plateformType){
        String seasonUrl = "https://public-ubiservices.ubi.com/v1/spaces/05bfb3f7-6c21-4c42-be1f-97a33fb5cf66/sandboxes/OSBOR_PS4_LNCH_A/r6karma/players?board_id=pvp_ranked&profile_ids="
                + profileId + "&region_id=" + region + "&season_id=" + seasonNumber;
        Log.d("Debug---seasonUrl", seasonUrl);

        String seasonResponse = callWebService(seasonUrl, ticket);
        Log.d("Debug---seasonResponse", seasonResponse);
        return seasonResponse;
    }

    public String getStatsResponse(String ticket, String profileId, String plateformType){
        String statsUrl = "https://public-ubiservices.ubi.com/v1/spaces/05bfb3f7-6c21-4c42-be1f-97a33fb5cf66/sandboxes/OSBOR_PS4_LNCH_A/playerstats2/statistics?populations=" + profileId + "&statistics=";
        Log.d("Debug---statsUrl", statsUrl);

        String statsResponse = callWebService(statsUrl, ticket);
        Log.d("Debug---progressionResp", statsResponse);
        return statsResponse;
    }

    private HttpURLConnection prepareRequest(String connectionUrl, String authorization, String method) throws IOException {
        HttpURLConnection urlConnection = null;
        URL url;
        url = new URL(connectionUrl);
        urlConnection = (HttpURLConnection) url.openConnection();
        if(method.equals(POST_METHOD)) {
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(POST_METHOD);
        }
        urlConnection.setRequestProperty(HEADER_UBI_APPID, APP_ID);
        urlConnection.setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);
        urlConnection.setRequestProperty(HEADER_AUTHORIZATION, authorization);
        if(method.equals(POST_METHOD)){
            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_UTF8);
            osw.write(REMEMBER_BE);
            osw.flush();
            osw.close();
            os.close();
        }
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
