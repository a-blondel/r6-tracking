package com.rainbow6.siege.r6_app.tools;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;

import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.db.entity.ProgressionEntity;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;
import com.rainbow6.siege.r6_app.db.entity.StatsEntity;
import com.rainbow6.siege.r6_app.db.entity.SyncEntity;
import com.rainbow6.siege.r6_app.repository.ConnectionRepository;
import com.rainbow6.siege.r6_app.repository.PlayerRepository;
import com.rainbow6.siege.r6_app.repository.ProgressionRepository;
import com.rainbow6.siege.r6_app.repository.SeasonRepository;
import com.rainbow6.siege.r6_app.repository.StatsRepository;
import com.rainbow6.siege.r6_app.repository.SyncRepository;
import com.rainbow6.siege.r6_app.service.UbiService;
import com.rainbow6.siege.r6_app.ui.MainActivity;
import com.rainbow6.siege.r6_app.ui.PlayerActivity;
import com.rainbow6.siege.r6_app.ui.TabSeasons;
import com.rainbow6.siege.r6_app.ui.TabStats;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;
import static com.rainbow6.siege.r6_app.service.UbiService.CURRENT_SEASON;
import static com.rainbow6.siege.r6_app.service.UbiService.REGION_EMEA;
import static com.rainbow6.siege.r6_app.service.UbiService.REGION_NCSA;
import static com.rainbow6.siege.r6_app.ui.TabAlarm.PLATEFORM_TYPE;
import static com.rainbow6.siege.r6_app.ui.TabAlarm.PROFILE_ID;
import static com.rainbow6.siege.r6_app.ui.TabAlarm.SYNC_APAC;
import static com.rainbow6.siege.r6_app.ui.TabAlarm.SYNC_EMEA;
import static com.rainbow6.siege.r6_app.ui.TabAlarm.SYNC_NCSA;
import static com.rainbow6.siege.r6_app.ui.TabAlarm.SYNC_PROGRESSION;
import static com.rainbow6.siege.r6_app.ui.TabAlarm.SYNC_STATS;
import static com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel.COUNT_1;
import static com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel.SKIP_0;

public class AlarmReceiver extends BroadcastReceiver {
    private PlayerRepository playerRepository;
    private ProgressionRepository progressionRepository;
    private SeasonRepository seasonRepository;
    private StatsRepository statsRepository;
    private ConnectionRepository connectionRepository;
    private AlarmServiceTask alarmServiceTask = null;
    private SyncRepository syncRepository;

    private ConnectionEntity connectionEntity;
    private UbiService ubiService;
    private ServiceHelper serviceHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO restart alarms on phone boot ?
        /*if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

        }*/

        ubiService = new UbiService();
        serviceHelper = new ServiceHelper();
        playerRepository = new PlayerRepository((Application) context.getApplicationContext());
        connectionRepository = new ConnectionRepository((Application) context.getApplicationContext());
        progressionRepository = new ProgressionRepository((Application) context.getApplicationContext());
        seasonRepository = new SeasonRepository((Application) context.getApplicationContext());
        statsRepository = new StatsRepository((Application) context.getApplicationContext());
        syncRepository = new SyncRepository((Application) context.getApplicationContext());

        String profileId = intent.getStringExtra(PROFILE_ID);

        String plateformType = intent.getStringExtra(PLATEFORM_TYPE);
        boolean syncProgression = intent.getBooleanExtra(SYNC_PROGRESSION, false);
        boolean syncEmeaSeason = intent.getBooleanExtra(SYNC_EMEA, false);
        boolean syncNcsaSeason = intent.getBooleanExtra(SYNC_NCSA, false);
        boolean syncApacSeason = intent.getBooleanExtra(SYNC_APAC, false);
        boolean syncStats = intent.getBooleanExtra(SYNC_STATS, false);


        if (alarmServiceTask != null) {
            return;
        }

        alarmServiceTask = new AlarmServiceTask(profileId, plateformType, syncProgression, syncEmeaSeason, syncNcsaSeason, syncApacSeason, syncStats, context);
        alarmServiceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean isTicketInvalid(){
        return System.currentTimeMillis() > connectionEntity.getExpiration().getTime();
    }

    public class AlarmServiceTask extends AsyncTask<Void, Void, Boolean> {
        private final String profileId;
        private final String plateformType;
        private final boolean syncProgression;
        private final boolean syncEmeaSeason;
        private final boolean syncNcsaSeason;
        private final boolean syncApacSeason;
        private final boolean syncStats;
        private final Context context;

        private AlarmManager alarmManager;

        private static final String ERROR = "Error";
        private static final boolean status = true;

        AlarmServiceTask(String profileId, String plateformType, boolean syncProgression, boolean syncEmeaSeason, boolean syncNcsaSeason, boolean syncApacSeason, boolean syncStats, Context context) {
            this.profileId = profileId;
            this.plateformType = plateformType;
            this.syncProgression = syncProgression;
            this.syncEmeaSeason = syncEmeaSeason;
            this.syncNcsaSeason = syncNcsaSeason;
            this.syncApacSeason = syncApacSeason;
            this.syncStats = syncStats;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            PlayerEntity playerEntity = playerRepository.getPlayerByProfileId(profileId);
            Log.d("Debug---Alarm triggered", playerEntity.getNameOnPlatform());

            List<String> errors = new ArrayList();

            WifiManager wifiManager = (WifiManager)this.context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(status);

            try {
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
                            Log.d(ERROR, serviceHelper.getErrorMessage(response));
                            errors.add(serviceHelper.getErrorMessage(response));
                        }
                    }
                } else {
                    // No ticket (the user has never signed in)
                    Log.d(ERROR, context.getResources().getString(R.string.errorNoConnection));
                }

                // Get progression
                ProgressionEntity progressionEntity = null;
                if (syncProgression) {
                    String progressionResponse = ubiService.getProgressionResponse(connectionEntity.getTicket(), profileId, plateformType);
                    if (serviceHelper.isValidResponse(progressionResponse)) {
                        progressionEntity = serviceHelper.generateProgressionEntity(progressionResponse);
                    } else {
                        Log.d(ERROR, serviceHelper.getErrorMessage(progressionResponse));
                        errors.add(serviceHelper.getErrorMessage(progressionResponse));
                    }
                }

                // Get season emea
                SeasonEntity seasonEmeaEntity = null;
                if (syncEmeaSeason) {
                    String seasonEmeaResponse = ubiService.getSeasonResponse(connectionEntity.getTicket(), profileId, REGION_EMEA, CURRENT_SEASON, plateformType);
                    if (serviceHelper.isValidResponse(seasonEmeaResponse)) {
                        seasonEmeaEntity = serviceHelper.generateSeasonEntity(seasonEmeaResponse, profileId);
                    } else {
                        Log.d(ERROR, serviceHelper.getErrorMessage(seasonEmeaResponse));
                        errors.add(serviceHelper.getErrorMessage(seasonEmeaResponse));
                    }
                }

                // Get season ncsa
                SeasonEntity seasonNcsaEntity = null;
                if (syncNcsaSeason) {
                    String seasonNcsaResponse = ubiService.getSeasonResponse(connectionEntity.getTicket(), profileId, REGION_NCSA, CURRENT_SEASON, plateformType);
                    if (serviceHelper.isValidResponse(seasonNcsaResponse)) {
                        seasonNcsaEntity = serviceHelper.generateSeasonEntity(seasonNcsaResponse, profileId);
                    } else {
                        Log.d(ERROR, serviceHelper.getErrorMessage(seasonNcsaResponse));
                        errors.add(serviceHelper.getErrorMessage(seasonNcsaResponse));
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
                        Log.d(ERROR, serviceHelper.getErrorMessage(statsResponse));
                        errors.add(serviceHelper.getErrorMessage(statsResponse));
                    }
                }

                if(syncProgression && progressionEntity != null) {
                    ProgressionEntity progressionEntityFromDB = progressionRepository.getLastProgressionEntityByProfileId(profileId);
                    if(progressionEntityFromDB == null || progressionEntityFromDB.getXp() != progressionEntity.getXp()) {
                        progressionRepository.insert(progressionEntity);
                        newStats = true;
                    }
                }

                SeasonEntity seasonEmeaEntityFromDB = seasonRepository.getLastSeasonEntityByProfileIdAndRegionId(profileId,REGION_EMEA, SKIP_0, COUNT_1);
                if(syncEmeaSeason && seasonEmeaEntity != null) {
                    if(seasonEmeaEntityFromDB == null || Double.compare(seasonEmeaEntityFromDB.getMmr(), seasonEmeaEntity.getMmr()) != 0) {
                        seasonRepository.insert(seasonEmeaEntity);
                        newStats = true;
                    }
                }
                SeasonEntity seasonNcsaEntityFromDB = seasonRepository.getLastSeasonEntityByProfileIdAndRegionId(profileId,REGION_NCSA, SKIP_0, COUNT_1);
                if(syncNcsaSeason && seasonNcsaEntity != null) {
                    if(seasonNcsaEntityFromDB == null || Double.compare(seasonNcsaEntityFromDB.getMmr(), seasonNcsaEntity.getMmr()) != 0) {
                        seasonRepository.insert(seasonNcsaEntity);
                        newStats = true;
                    }
                }
                StatsEntity statsEntityFromDB = statsRepository.getLastStatsEntityByProfileId(profileId);
                if(syncStats && statsEntity != null) {
                    if(statsEntityFromDB == null || statsEntityFromDB.getGeneralTimePlayed() != statsEntity.getGeneralTimePlayed()) {
                        statsRepository.insert(statsEntity);
                        newStats = true;
                    }
                }

                if(newStats) {

                    Log.d("Debug---New Stats/Notif", playerEntity.getNameOnPlatform());

                    // Send notification
                    setUpChannel(context, profileId);

                    String imageRank = "";
                    String title = playerEntity.getNameOnPlatform();
//                    if(title.length() > 12){
//                        title = title.substring(0, 11) + ".";
//                    }
                    String message = context.getString(R.string.notif_casual);
                    Spannable sb = new SpannableString(message);
                    Date dateRefresh = new Date();

                    if (seasonEmeaEntity != null && seasonEmeaEntityFromDB != null && Double.compare(seasonEmeaEntityFromDB.getMmr(), seasonEmeaEntity.getMmr()) != 0) {
//                      if (seasonEmeaEntityFromDB.getRank() != seasonEmeaEntity.getRank()) {
//                        title += " - new rank " + REGION_EMEA;
//                      }
                        int actualMmr = (int) Math.floor(seasonEmeaEntity.getMmr());
                        String charMessage = "EU: " + actualMmr + " (" + (int) (Math.floor(seasonEmeaEntity.getMmr()) - Math.floor(seasonEmeaEntityFromDB.getMmr())) + ")";
                        int pos = charMessage.indexOf(String.valueOf(actualMmr));
                        sb = new SpannableString(charMessage);
                        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), pos, pos + String.valueOf(actualMmr).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        imageRank = "rank_" + seasonEmeaEntity.getRank();
                        dateRefresh = seasonEmeaEntity.getUpdateDate();
                    } else if (seasonNcsaEntity != null && seasonNcsaEntityFromDB != null && Double.compare(seasonNcsaEntityFromDB.getMmr(), seasonNcsaEntity.getMmr()) != 0) {
//                      if (seasonNcsaEntityFromDB.getRank() != seasonNcsaEntity.getRank()) {
//                          title += " - new rank " + REGION_NCSA;
//                      }
                        int actualMmr = (int) Math.floor(seasonNcsaEntity.getMmr());
                        String charMessage = "US: " + actualMmr + " (" + (int) (Math.floor(seasonNcsaEntity.getMmr()) - Math.floor(seasonNcsaEntityFromDB.getMmr())) + ")";
                        int pos = charMessage.indexOf(String.valueOf(actualMmr));
                        sb = new SpannableString(charMessage);
                        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), pos, pos + String.valueOf(actualMmr).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        imageRank = "rank_" + seasonNcsaEntity.getRank();
                        dateRefresh = seasonNcsaEntity.getUpdateDate();
                    }

                    int newKills = 0, newDeaths = 0;
                    if (statsEntity != null && statsEntityFromDB != null){
                        boolean validEmea = seasonEmeaEntity != null && seasonEmeaEntityFromDB != null && Double.compare(seasonEmeaEntityFromDB.getMmr(), seasonEmeaEntity.getMmr()) != 0;
                        boolean validNcsa = seasonNcsaEntity != null && seasonNcsaEntityFromDB != null && Double.compare(seasonNcsaEntityFromDB.getMmr(), seasonNcsaEntity.getMmr()) != 0;
                        if(validEmea || validNcsa){
                            newKills = statsEntity.getKillsRanked() - statsEntityFromDB.getKillsRanked();
                            newDeaths = statsEntity.getDeathRanked() - statsEntityFromDB.getDeathRanked();
                        }else{
                            newKills = statsEntity.getKillsCasual() - statsEntityFromDB.getKillsCasual();
                            newDeaths = statsEntity.getDeathCasual() - statsEntityFromDB.getDeathCasual();
                            dateRefresh = statsEntity.getUpdateDate();
                        }
                    }

                    // TODO get best rank from any region
                    if("".equals(imageRank)){
                        if (seasonEmeaEntity != null){
                            imageRank = "rank_" + seasonEmeaEntity.getRank();
                        }else if(seasonEmeaEntityFromDB != null){
                            imageRank = "rank_" + seasonEmeaEntityFromDB.getRank();
                        }else {
                            imageRank = "rank_0";
                        }
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());

                    /*
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                    inboxStyle.setBigContentTitle(title);
                    inboxStyle.addLine(sb);
                    inboxStyle.addLine(sdf.format(dateRefresh) + " - Score: " + newKills + " / " + newDeaths);

                    .setStyle(inboxStyle)
                    */

                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("tabSeasons", 1);
                    intent.putExtra("player", playerEntity);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, profileId)
                            .setSmallIcon(R.drawable.ic_r6_default_white)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier(imageRank, "drawable", context.getPackageName())))
                            .setColor(context.getColor(R.color.colorPrimary))
                            .setContentTitle(title + " - " + sdf.format(dateRefresh))
                            .setContentText(sb + " - Score: " + newKills + " / " + newDeaths)
                            .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)) // needed to allow AutoCancel
                            .setAutoCancel(true)
                            .setVibrate(new long[] { 0, 400, 200, 400 })
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                    // We need a unique id for each notification, we could store it to cancel the notification
                    Integer notificationId = (int) (new Date().getTime() / 1000L) + (int) (playerEntity.getAddedDate().getTime() / 1000L);

                    notificationManager.notify(notificationId, mBuilder.build());

                }

            } catch (JSONException e) {
                Log.d("Debug---JSONException", e.getMessage());
            } catch (ParseException e) {
                Log.d("Debug---ParseException", e.getMessage());
            }

            /*if(!errors.isEmpty()){
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("tabSeasons", 1);
                intent.putExtra("player", playerEntity);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, profileId)
                        .setSmallIcon(R.drawable.ic_r6_default_white)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier("", "drawable", context.getPackageName())))
                        .setColor(context.getColor(R.color.colorPrimary))
                        .setContentTitle("ERROR - " + playerEntity.getNameOnPlatform())
                        .setContentText(TextUtils.join(",",errors))
                        .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(TextUtils.join(",",errors)))
                        .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)) // needed to allow AutoCancel
                        .setAutoCancel(true)
                        .setVibrate(new long[] { 0, 400, 200, 400 })
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                // We need a unique id for each notification, we could store it to cancel the notification
                Integer notificationId = (int) (new Date().getTime() / 1000L) + (int) (playerEntity.getAddedDate().getTime() / 1000L);

                notificationManager.notify(notificationId, mBuilder.build());
            }*/

            SyncEntity syncEntity = syncRepository.getSyncByProfileId(playerEntity.getProfileId());
            syncEntity.setLastSync(new Date().getTime());
            syncRepository.update(syncEntity);

            // Setting up the AlarmManager and pending intent
            alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(PROFILE_ID, profileId);
            intent.putExtra(PLATEFORM_TYPE, plateformType);
            intent.putExtra(SYNC_PROGRESSION, syncProgression);
            intent.putExtra(SYNC_EMEA, syncEmeaSeason);
            intent.putExtra(SYNC_NCSA, syncNcsaSeason);
            intent.putExtra(SYNC_APAC, syncApacSeason);
            intent.putExtra(SYNC_STATS, syncStats);

            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

            // Must be a unique user identifier
            int broadcastId = (int) (playerEntity.getAddedDate().getTime() / 1000L);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, broadcastId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            int syncTimer = syncEntity.getSyncDelay();

            /*AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(System.currentTimeMillis() +
                    (long)syncTimer * 60L * 1000L,
                    pendingIntent);

            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);*/

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                    (long)syncTimer * 60L * 1000L, pendingIntent);

            return true;
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

        @Override
        protected void onPostExecute(final Boolean success) {
            alarmServiceTask = null;
            if(MainActivity.getInstance()!=null) {
                MainActivity.getInstance().updateUI();
            }
            if(TabStats.getInstance()!=null) {
                TabStats.getInstance().updateUI();
            }
            if(TabSeasons.getInstance()!=null) {
                TabSeasons.getInstance().updateUI();
            }
            if(PlayerActivity.getInstance()!=null) {
                PlayerActivity.getInstance().updateUI();
            }
        }

        @Override
        protected void onCancelled() {
            alarmServiceTask = null;
        }
    }
}