package com.rainbow6.siege.r6_app.tools;

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
            seasonEntity.setUpdateDate(formatter.parse(json.getJSONObject("players").getJSONObject(profileId).getString("update_time").split(UBI_DATE_DELIMITER)[0]));
        }

        return seasonEntity;
    }

    public StatsEntity generateStatsEntity(String response, String profileId) throws JSONException {
        StatsEntity statsEntity = new StatsEntity();

        JSONObject json = new JSONObject(response);
        statsEntity.setProfileId(profileId);
        statsEntity.setUpdateDate(new Date());
        statsEntity.setTimePlayedRanked(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("rankedpvp_timeplayed:infinite")));
        statsEntity.setMatchPlayedRanked(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("rankedpvp_matchplayed:infinite")));
        statsEntity.setMatchWonRanked(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("rankedpvp_matchwon:infinite")));
        statsEntity.setMatchLostRanked(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("rankedpvp_matchlost:infinite")));
        statsEntity.setKillsRanked(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("rankedpvp_kills:infinite")));
        statsEntity.setDeathRanked(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("rankedpvp_death:infinite")));
        statsEntity.setTimePlayedCasual(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("casualpvp_timeplayed:infinite")));
        statsEntity.setMatchPlayedCasual(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("casualpvp_matchplayed:infinite")));
        statsEntity.setMatchWonCasual(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("casualpvp_matchwon:infinite")));
        statsEntity.setMatchLostCasual(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("casualpvp_matchlost:infinite")));
        statsEntity.setKillsCasual(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("casualpvp_kills:infinite")));
        statsEntity.setDeathCasual(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("casualpvp_death:infinite")));
        statsEntity.setGeneralTimePlayed(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("generalpvp_timeplayed:infinite")));
        statsEntity.setGeneralMatchPlayed(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("generalpvp_matchplayed:infinite")));
        statsEntity.setGeneralMatchWon(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("generalpvp_matchwon:infinite")));
        statsEntity.setGeneralMatchLost(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("generalpvp_matchlost:infinite")));
        statsEntity.setGeneralKills(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("generalpvp_kills:infinite")));
        statsEntity.setGeneralDeath(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("generalpvp_death:infinite")));
        statsEntity.setGeneralHeadshots(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("generalpvp_headshot:infinite")));
        statsEntity.setGeneralBulletFired(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("generalpvp_bulletfired:infinite")));
        statsEntity.setGeneralBulletHit(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("generalpvp_bullethit:infinite")));
        statsEntity.setGeneralKillAssists(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("generalpvp_killassists:infinite")));
        statsEntity.setGeneralPenetrationKills(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("generalpvp_penetrationkills:infinite")));
        statsEntity.setGeneralMeleeKills(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("generalpvp_meleekills:infinite")));
        statsEntity.setGeneralRevive(Integer.parseInt(json.getJSONObject("results").getJSONObject(profileId).getString("generalpvp_revive:infinite")));

        return statsEntity;
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
