package com.rainbow6.siege.r6_app.tools;

import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.db.entity.ProgressionEntity;
import com.rainbow6.siege.r6_app.db.entity.SkillEntity;
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
    private static final String UBI_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String UBI_DATE_DELIMITER = "\\.";
    private static final String UBI_EMPTY_RESPONSE = "Empty response";
    private static final String EMPTY_STRING = "";

    public ConnectionEntity generateConnectionEntity(String response, String encodedKey) throws JSONException, ParseException {

        JSONObject json = new JSONObject(response);
        SimpleDateFormat formatter = new SimpleDateFormat(UBI_DATE_FORMAT, Locale.getDefault());

        String ticket = UBI_TICKET_BEGIN + json.getString("ticket");
        Date expiration = formatter.parse(json.getString("expiration").split(UBI_DATE_DELIMITER)[0]);

        return new ConnectionEntity(UbiService.APP_ID, encodedKey, ticket, expiration);
    }

    public PlayerEntity generatePlayerEntity(String response) throws JSONException {
        PlayerEntity playerEntity = new PlayerEntity();

        if (response != "{\"profiles\":[]}" && response != ""){
            JSONObject json = new JSONObject(response);

            playerEntity.setProfileId(json.optJSONArray("profiles").getJSONObject(0).getString("profileId"));
            playerEntity.setNameOnPlatform(json.optJSONArray("profiles").getJSONObject(0).getString("nameOnPlatform"));
            playerEntity.setUserId(json.optJSONArray("profiles").getJSONObject(0).getString("userId"));
            playerEntity.setPlatformType(json.optJSONArray("profiles").getJSONObject(0).getString("platformType"));
            playerEntity.setAddedDate(new Date());
        }
        return playerEntity;
    }

    public ProgressionEntity generateProgressionEntity(String response) throws JSONException {
        ProgressionEntity progressionEntity = new ProgressionEntity();

        JSONObject json = new JSONObject(response);
        progressionEntity.setProfileId(json.optJSONArray("player_profiles").getJSONObject(0).getString("profile_id"));
        progressionEntity.setXp(Integer.parseInt(json.optJSONArray("player_profiles").getJSONObject(0).getString("xp")));
        progressionEntity.setLevel(Integer.parseInt(json.optJSONArray("player_profiles").getJSONObject(0).getString("level")));
        progressionEntity.setLootChance(Integer.parseInt(json.optJSONArray("player_profiles").getJSONObject(0).getString("lootbox_probability")));
        progressionEntity.setUpdateDate(new Date());
        return progressionEntity;
    }

    public SkillEntity generateSeasonEntity(String response) throws JSONException, ParseException {
        SkillEntity skillEntity = new SkillEntity();
        SimpleDateFormat formatter = new SimpleDateFormat(UBI_DATE_FORMAT, Locale.getDefault());

        JSONObject json = new JSONObject(response);
        skillEntity.setProfileId(json.getJSONObject("players").getString("profile_id"));
        skillEntity.setAbandons(Integer.parseInt(json.getJSONObject("players").getString("abandons")));
//        skillEntity.setBoardId(json.getJSONObject("players").getString("board_id"));
        skillEntity.setLosses(Integer.parseInt(json.getJSONObject("players").getString("losses")));
        skillEntity.setMaxMmr(Double.parseDouble(json.getJSONObject("players").getString("max_mmr")));
        skillEntity.setMaxRank(Integer.parseInt(json.getJSONObject("players").getString("max_rank")));
        skillEntity.setMmr(Double.parseDouble(json.getJSONObject("players").getString("mmr")));
        skillEntity.setNextRankMmr(Integer.parseInt(json.getJSONObject("players").getString("next_rank_mmr")));
        skillEntity.setPreviousRankMmr(Integer.parseInt(json.getJSONObject("players").getString("previous_rank_mmr")));
        skillEntity.setRank(Integer.parseInt(json.getJSONObject("players").getString("rank")));
        skillEntity.setRegion(json.getJSONObject("players").getString("region"));
        skillEntity.setSeason(Integer.parseInt(json.getJSONObject("players").getString("season")));
        skillEntity.setSkillMean(Double.parseDouble(json.getJSONObject("players").getString("skill_mean")));
//        skillEntity.setSkillStdev(Double.parseDouble(json.getJSONObject("players").getString("skill_stdev")));
        skillEntity.setWins(Integer.parseInt(json.getJSONObject("players").getString("wins")));

        if(Double.valueOf(2500) != skillEntity.getMmr()){
            skillEntity.setUpdateDate(formatter.parse(json.getJSONObject("players").getString("skill_mean").split(UBI_DATE_DELIMITER)[0]));
        }

        return skillEntity;
    }

    public boolean isValidResponse(String response){
        return response != null && !EMPTY_STRING.equals(response) && !response.contains(UBI_ERROR_CODE) &&!response.contains(EXCEPTION_PATTERN);
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
