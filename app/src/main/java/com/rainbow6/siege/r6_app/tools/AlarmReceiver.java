package com.rainbow6.siege.r6_app.tools;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.db.entity.ProgressionEntity;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;
import com.rainbow6.siege.r6_app.db.entity.StatsEntity;
import com.rainbow6.siege.r6_app.repository.ConnectionRepository;
import com.rainbow6.siege.r6_app.repository.PlayerRepository;
import com.rainbow6.siege.r6_app.service.UbiService;
import com.rainbow6.siege.r6_app.ui.TabAlarm;
import com.rainbow6.siege.r6_app.viewmodel.ConnectionViewModel;
import com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel;

import org.json.JSONException;

import java.text.ParseException;
import java.util.Date;

import static com.rainbow6.siege.r6_app.service.UbiService.CURRENT_SEASON;
import static com.rainbow6.siege.r6_app.service.UbiService.REGION_EMEA;
import static com.rainbow6.siege.r6_app.service.UbiService.REGION_NCSA;
import static com.rainbow6.siege.r6_app.ui.TabAlarm.PROFILE_ID;

public class AlarmReceiver extends BroadcastReceiver {
    private PlayerRepository playerRepository;
    private ConnectionRepository connectionRepository;

    private ConnectionEntity connectionEntity;
    private UbiService ubiService;
    private ServiceHelper serviceHelper;

    private static String SUCCESS = "SUCCESS";

    @Override
    public void onReceive(final Context context, final Intent intent) {

        final PendingResult pendingResult = goAsync();
        AsyncTask<String, Integer, String> asyncTask = new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {

                // We can start services on phone boot
                /*if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

                }*/

                ubiService = new UbiService();
                serviceHelper = new ServiceHelper();
                playerRepository = new PlayerRepository((Application) context.getApplicationContext());


                String profileId = intent.getStringExtra(PROFILE_ID);

                Log.d("profile : ", profileId);

                PlayerEntity playerEntity = playerRepository.getPlayerByProfileId(profileId);

                // Logic here

                /*try {

                    // EVERYTHING IN THE ALARM MANAGER
                    boolean newStats = false;

                    connectionEntity = connectionRepository.getConnection(UbiService.APP_ID);
                    if (connectionEntity != null) {
                        Log.d("Debug---Connectivity", "Valid ticket : " + !isTicketInvalid());
                        // Ticket found
                        if (isTicketInvalid()) {
                            // Invalid ticket, new connection is needed
                            String response = ubiService.callUbiConnectionService(connectionEntity.getEncodedKey());
                            if (serviceHelper.isValidResponse(response)) {
                                connectionEntity = serviceHelper.generateConnectionEntity(response, connectionEntity.getEncodedKey());
                                connectionRepository.insert(connectionEntity);
                                Log.d("Debug---Connectivity", "New ticket generated!");
                            } else {
                                sendMessage(serviceHelper.getErrorMessage(response));
                            }
                        }
                    } else {
                        // No ticket (the user has never signed in)
                        sendMessage(getResources().getString(R.string.errorNoConnection));
                    }

                    // Get progression
                    ProgressionEntity progressionEntity = null;
                    if (syncProgression) {
                        String progressionResponse = ubiService.getProgressionResponse(connectionEntity.getTicket(), profileId, plateformType);
                        if (serviceHelper.isValidResponse(progressionResponse)) {
                            progressionEntity = serviceHelper.generateProgressionEntity(progressionResponse);
                        } else {
                            sendMessage(serviceHelper.getErrorMessage(progressionResponse));
                        }
                    }

                    // Get season emea
                    SeasonEntity seasonEmeaEntity = null;
                    if (syncEmeaSeason) {
                        String seasonEmeaResponse = ubiService.getSeasonResponse(connectionEntity.getTicket(), profileId, REGION_EMEA, CURRENT_SEASON, plateformType);
                        if (serviceHelper.isValidResponse(seasonEmeaResponse)) {
                            seasonEmeaEntity = serviceHelper.generateSeasonEntity(seasonEmeaResponse, profileId);
                        } else {
                            sendMessage(serviceHelper.getErrorMessage(seasonEmeaResponse));
                        }
                    }

                    // Get season ncsa
                    SeasonEntity seasonNcsaEntity = null;
                    if (syncNcsaSeason) {
                        String seasonNcsaResponse = ubiService.getSeasonResponse(connectionEntity.getTicket(), profileId, REGION_NCSA, CURRENT_SEASON, plateformType);
                        if (serviceHelper.isValidResponse(seasonNcsaResponse)) {
                            seasonNcsaEntity = serviceHelper.generateSeasonEntity(seasonNcsaResponse, profileId);
                        } else {
                            sendMessage(serviceHelper.getErrorMessage(seasonNcsaResponse));
                        }
                    }

                    // Get season apac - Disabled here

                    // Get stats
                    StatsEntity statsEntity = null;
                    if (syncStats) {
                        String statsResponse = ubiService.getStatsResponse(connectionEntity.getTicket(), playerEntity.getProfileId(), plateformType);
                        if (serviceHelper.isValidResponse(statsResponse)) {
                            statsEntity = serviceHelper.generateStatsEntity(statsResponse, profileId);
                        } else {
                            sendMessage(serviceHelper.getErrorMessage(statsResponse));
                        }
                    }

                    if(syncProgression && progressionEntity != null) {
                        ProgressionEntity progressionEntityFromDB = playerViewModel.getLastProgressionEntityByProfileId(profileId);
                        if(progressionEntityFromDB == null || progressionEntityFromDB.getXp() != progressionEntity.getXp()) {
                            playerViewModel.insertProgression(progressionEntity);
                            newStats = true;
                        }
                    }
                    if(syncEmeaSeason && seasonEmeaEntity != null) {
                        SeasonEntity seasonEmeaEntityFromDB = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(profileId,REGION_EMEA);
                        if(seasonEmeaEntityFromDB == null || Double.compare(seasonEmeaEntityFromDB.getMmr(), seasonEmeaEntity.getMmr()) != 0) {
                            playerViewModel.insertSeason(seasonEmeaEntity);
                            newStats = true;
                        }
                    }
                    if(syncNcsaSeason && seasonNcsaEntity != null) {
                        SeasonEntity seasonNcsaEntityFromDB = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(profileId,REGION_NCSA);
                        if(seasonNcsaEntityFromDB == null || Double.compare(seasonNcsaEntityFromDB.getMmr(), seasonNcsaEntity.getMmr()) != 0) {
                            playerViewModel.insertSeason(seasonNcsaEntity);
                            newStats = true;
                        }
                    }
                    if(syncStats && statsEntity != null) {
                        StatsEntity statsEntityFromDB = playerViewModel.getLastStatsByProfileId(profileId);
                        if(statsEntityFromDB == null || statsEntityFromDB.getGeneralTimePlayed() != statsEntity.getGeneralTimePlayed()) {
                            playerViewModel.insertStats(statsEntity);
                            newStats = true;
                        }
                    }



                    if(newStats){
                        // SendNotification
                        sendMessage(getResources().getString(R.string.player_updated));

                    }else{
                        // Do nothing
                        sendMessage(getResources().getString(R.string.nothing_new));

                    }

                } catch (JSONException e) {
                    Log.d("Debug---JSONException", e.getMessage());
                    sendMessage(e.getMessage());
                } catch (ParseException e) {
                    Log.d("Debug---ParseException", e.getMessage());
                    sendMessage(e.getMessage());
                }*/

                // End logic


                // If there is any change, send a notification
                setUpChannel(context, profileId);

                // Put rank icon, Player name, MMR (+ difference mmr, + new rank)
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, profileId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(playerEntity.getNameOnPlatform())
                        .setContentText("New MMR : " + playerEntity.getProfileId())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


                // We need a unique id for each notification, we could store it to cancel notification, ...
                Integer notificationId = (int) (new Date().getTime() / 1000L);

                notificationManager.notify(notificationId++, mBuilder.build());

                // Must call finish() so the BroadcastReceiver can be recycled.
                pendingResult.finish();
                return SUCCESS;
            }
        };
        asyncTask.execute();

    }

    private boolean isTicketInvalid(){
        return System.currentTimeMillis() > connectionEntity.getExpiration().getTime();
    }

    private void setUpChannel(Context context, String profileId){
//        Only for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Name";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(profileId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}