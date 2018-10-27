package com.rainbow6.siege.r6_app.tools;

import android.util.Log;

import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.db.entity.ProgressionEntity;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;
import com.rainbow6.siege.r6_app.db.entity.StatsEntity;
import com.rainbow6.siege.r6_app.service.UbiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.rainbow6.siege.r6_app.service.UbiService.EXCEPTION_PATTERN;

public class ServiceHelper {

    public static final String UBI_ERROR_CODE = "errorCode";
    private static final String UBI_ERROR_BEGIN = "\"message\":\"";
    private static final String UBI_ERROR_END = "\",";
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

    public SeasonEntity generateSeasonEntity(String response, String profileId) throws JSONException, ParseException {
        SeasonEntity seasonEntity = new SeasonEntity();
        SimpleDateFormat formatter = new SimpleDateFormat(UBI_DATE_FORMAT, Locale.getDefault());

        JSONObject json = new JSONObject(response);
        seasonEntity.setProfileId(profileId);
        seasonEntity.setAbandons(Integer.parseInt(json.getJSONObject("players").getJSONObject(profileId).getString("abandons")));
        seasonEntity.setBoardId(json.getJSONObject("players").getJSONObject(profileId).getString("board_id"));
        seasonEntity.setLosses(Integer.parseInt(json.getJSONObject("players").getJSONObject(profileId).getString("losses")));
        seasonEntity.setMaxMmr(Double.parseDouble(json.getJSONObject("players").getJSONObject(profileId).getString("max_mmr")));
        seasonEntity.setMaxRank(Integer.parseInt(json.getJSONObject("players").getJSONObject(profileId).getString("max_rank")));
        seasonEntity.setMmr(Double.parseDouble(json.getJSONObject("players").getJSONObject(profileId).getString("mmr")));
        seasonEntity.setNextRankMmr(Double.parseDouble(json.getJSONObject("players").getJSONObject(profileId).getString("next_rank_mmr")));
        seasonEntity.setPreviousRankMmr(Double.parseDouble(json.getJSONObject("players").getJSONObject(profileId).getString("previous_rank_mmr")));
        seasonEntity.setRank(Integer.parseInt(json.getJSONObject("players").getJSONObject(profileId).getString("rank")));
        seasonEntity.setRegion(json.getJSONObject("players").getJSONObject(profileId).getString("region"));
        seasonEntity.setSeason(Integer.parseInt(json.getJSONObject("players").getJSONObject(profileId).getString("season")));
        seasonEntity.setSkillMean(Double.parseDouble(json.getJSONObject("players").getJSONObject(profileId).getString("skill_mean")));
        seasonEntity.setSkillStdev(Double.parseDouble(json.getJSONObject("players").getJSONObject(profileId).getString("skill_stdev")));
        seasonEntity.setWins(Integer.parseInt(json.getJSONObject("players").getJSONObject(profileId).getString("wins")));

        if(!json.getJSONObject("players").getJSONObject(profileId).getString("update_time").equals("1970-01-01T00:00:00+00:00")){

            String dateJson = json.getJSONObject("players").getJSONObject(profileId).getString("update_time");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"+ dateJson.substring(dateJson.length() - 6, dateJson.length() - 3) + dateJson.substring(dateJson.length() -3 , dateJson.length())));
            seasonEntity.setUpdateDate(formatter.parse(dateJson.split(UBI_DATE_DELIMITER)[0]));
            Log.d("Debug---UpdateDate", String.valueOf(seasonEntity.getUpdateDate()));
        }
        //TODO else - current datetime

        return seasonEntity;
    }

    public StatsEntity generateStatsEntity(String response, String profileId) throws JSONException {
        StatsEntity statsEntity = new StatsEntity();

        JSONObject json = new JSONObject(response);
        statsEntity.setProfileId(profileId);
        statsEntity.setUpdateDate(new Date());
        statsEntity.setTimePlayedRanked(getValue(json, profileId,"rankedpvp_timeplayed:infinite"));
        statsEntity.setMatchPlayedRanked(getValue(json, profileId,"rankedpvp_matchplayed:infinite"));
        statsEntity.setMatchWonRanked(getValue(json, profileId,"rankedpvp_matchwon:infinite"));
        statsEntity.setMatchLostRanked(getValue(json, profileId,"rankedpvp_matchlost:infinite"));
        statsEntity.setKillsRanked(getValue(json, profileId,"rankedpvp_kills:infinite"));
        statsEntity.setDeathRanked(getValue(json, profileId,"rankedpvp_death:infinite"));
        statsEntity.setTimePlayedCasual(getValue(json, profileId,"casualpvp_timeplayed:infinite"));
        statsEntity.setMatchPlayedCasual(getValue(json, profileId,"casualpvp_matchplayed:infinite"));
        statsEntity.setMatchWonCasual(getValue(json, profileId,"casualpvp_matchwon:infinite"));
        statsEntity.setMatchLostCasual(getValue(json, profileId,"casualpvp_matchlost:infinite"));
        statsEntity.setKillsCasual(getValue(json, profileId,"casualpvp_kills:infinite"));
        statsEntity.setDeathCasual(getValue(json, profileId,"casualpvp_death:infinite"));
        statsEntity.setGeneralTimePlayed(getValue(json, profileId,"generalpvp_timeplayed:infinite"));
        statsEntity.setGeneralMatchPlayed(getValue(json, profileId,"generalpvp_matchplayed:infinite"));
        statsEntity.setGeneralMatchWon(getValue(json, profileId,"generalpvp_matchwon:infinite"));
        statsEntity.setGeneralMatchLost(getValue(json, profileId,"generalpvp_matchlost:infinite"));
        statsEntity.setGeneralKills(getValue(json, profileId,"generalpvp_kills:infinite"));
        statsEntity.setGeneralDeath(getValue(json, profileId,"generalpvp_death:infinite"));
        statsEntity.setGeneralHeadshots(getValue(json, profileId,"generalpvp_headshot:infinite"));
        statsEntity.setGeneralBulletFired(getValue(json, profileId,"generalpvp_bulletfired:infinite"));
        statsEntity.setGeneralBulletHit(getValue(json, profileId,"generalpvp_bullethit:infinite"));
        statsEntity.setGeneralKillAssists(getValue(json, profileId,"generalpvp_killassists:infinite"));
        statsEntity.setGeneralPenetrationKills(getValue(json, profileId,"generalpvp_penetrationkills:infinite"));
        statsEntity.setGeneralMeleeKills(getValue(json, profileId,"generalpvp_meleekills:infinite"));
        statsEntity.setGeneralRevive(getValue(json, profileId,"generalpvp_revive:infinite"));
        statsEntity.setGeneralPvpBlindKills(getValue(json, profileId, "generalpvp_blindkills:infinite"));
        statsEntity.setGeneralPvpRappelBreach(getValue(json, profileId, "generalpvp_rappelbreach:infinite"));
        statsEntity.setGeneralPvpDbno(getValue(json, profileId, "generalpvp_dbno:infinite"));
        statsEntity.setGeneralPvpDbnoAssists(getValue(json, profileId, "generalpvp_dbnoassists:infinite"));
        statsEntity.setGeneralPvpSuicide(getValue(json, profileId, "generalpvp_suicide:infinite"));
        statsEntity.setGeneralPvpBarricadeDeployed(getValue(json, profileId, "generalpvp_barricadedeployed:infinite"));
        statsEntity.setGeneralPvpReinforcementDeploy(getValue(json, profileId, "generalpvp_reinforcementdeploy:infinite"));
//        statsEntity.setGeneralPvpDistanceTravelled(getValue(json, profileId,"generalpvp_distancetravelled:infinite"));
        statsEntity.setGeneralPvpReviveDenied(getValue(json, profileId, "generalpvp_revivedenied:infinite"));
        statsEntity.setGeneralPvpGadgetDestroy(getValue(json, profileId, "generalpvp_gadgetdestroy:infinite"));
        statsEntity.setGeneralPvpTotalXp(getValue(json, profileId, "generalpvp_totalxp:infinite"));
        statsEntity.setSecureareapvpTimeplayed(getValue(json, profileId, "secureareapvp_timeplayed:infinite"));
        statsEntity.setRescuehostagepvpTimeplayed(getValue(json, profileId, "rescuehostagepvp_timeplayed:infinite"));
        statsEntity.setPlantbombpvpTimeplayed(getValue(json, profileId, "plantbombpvp_timeplayed:infinite"));
        statsEntity.setSecureareapvpMatchwon(getValue(json, profileId, "secureareapvp_matchwon:infinite"));
        statsEntity.setSecureareapvpMatchlost(getValue(json, profileId, "secureareapvp_matchlost:infinite"));
        statsEntity.setSecureareapvpMatchplayed(getValue(json, profileId, "secureareapvp_matchplayed:infinite"));
        statsEntity.setSecureareapvpBestscore(getValue(json, profileId, "secureareapvp_bestscore:infinite"));
        statsEntity.setRescuehostagepvpMatchwon(getValue(json, profileId, "rescuehostagepvp_matchwon:infinite"));
        statsEntity.setRescuehostagepvpMatchlost(getValue(json, profileId, "rescuehostagepvp_matchlost:infinite"));
        statsEntity.setRescuehostagepvpMatchplayed(getValue(json, profileId, "rescuehostagepvp_matchplayed:infinite"));
        statsEntity.setRescuehostagepvpBestscore(getValue(json, profileId, "rescuehostagepvp_bestscore:infinite"));
        statsEntity.setPlantbombpvpMatchwon(getValue(json, profileId, "plantbombpvp_matchwon:infinite"));
        statsEntity.setPlantbombpvpMatchlost(getValue(json, profileId, "plantbombpvp_matchlost:infinite"));
        statsEntity.setPlantbombpvpMatchplayed(getValue(json, profileId, "plantbombpvp_matchplayed:infinite"));
        statsEntity.setPlantbombpvpBestscore(getValue(json, profileId, "plantbombpvp_bestscore:infinite"));
        statsEntity.setGeneralpvpServershacked(getValue(json, profileId, "generalpvp_servershacked:infinite"));
        statsEntity.setGeneralpvpServerdefender(getValue(json, profileId, "generalpvp_serverdefender:infinite"));
        statsEntity.setGeneralpvpServeraggression(getValue(json, profileId, "generalpvp_serveraggression:infinite"));
        statsEntity.setGeneralpvpHostagerescue(getValue(json, profileId, "generalpvp_hostagerescue:infinite"));
        statsEntity.setGeneralpvpHostagedefense(getValue(json, profileId, "generalpvp_hostagedefense:infinite"));
        

        return statsEntity;
    }

    private Integer getValue(JSONObject json, String profileId, String key) throws JSONException {
        if(json.getJSONObject("results").getJSONObject(profileId).has(key)){
            return Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString(key));
        }else return 0;
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
