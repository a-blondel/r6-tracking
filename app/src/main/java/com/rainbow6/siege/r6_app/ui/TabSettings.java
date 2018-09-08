package com.rainbow6.siege.r6_app.ui;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.db.entity.ProgressionEntity;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;
import com.rainbow6.siege.r6_app.db.entity.StatsEntity;
import com.rainbow6.siege.r6_app.db.entity.SyncEntity;
import com.rainbow6.siege.r6_app.service.UbiService;
import com.rainbow6.siege.r6_app.tools.AlarmReceiver;
import com.rainbow6.siege.r6_app.tools.ServiceHelper;
import com.rainbow6.siege.r6_app.viewmodel.ConnectionViewModel;
import com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel;

import org.json.JSONException;

import java.text.ParseException;

import static android.content.Context.ALARM_SERVICE;
import static com.rainbow6.siege.r6_app.service.UbiService.CURRENT_SEASON;
import static com.rainbow6.siege.r6_app.service.UbiService.PLAYSTATION;
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

public class TabSettings extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private UpdatePlayerTask updatePlayerTask = null;
    private DeletePlayerTask deletePlayerTask = null;

    private View rootView;
    private ConnectionViewModel connectionViewModel;
    private PlayerViewModel playerViewModel;
    private UbiService ubiService;
    private ServiceHelper serviceHelper;
    private ConnectionEntity connectionEntity;
    private Handler mHandler;

    private PlayerEntity playerEntity;
    private Spinner spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_player_settings, container, false);

        // Find a way to bypass that
        PlayerActivity activity = (PlayerActivity) getActivity();
        playerEntity = activity.getPlayerEntity();

        ubiService = new UbiService();
        serviceHelper = new ServiceHelper();

        connectionViewModel = ViewModelProviders.of(this).get(ConnectionViewModel.class);
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);

        spinner = rootView.findViewById(R.id.plateformType_spinner);
        ArrayAdapter<CharSequence> spinnerArrayAdapter =  ArrayAdapter.createFromResource(
                getActivity(), R.array.platformType_array, R.layout.spinner_item
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        final Button button = rootView.findViewById(R.id.button_update_player);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                attemptUpdatePlayer();
            }
        });


        final Button buttonDelete = rootView.findViewById(R.id.button_delete_player);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                attemptDeletePlayer();
            }
        });

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                Toast.makeText(getActivity(),
                        (String) message.obj, Toast.LENGTH_LONG).show();
            }
        };

        return rootView;
    }

    private void attemptDeletePlayer(){

        deletePlayerTask = new DeletePlayerTask(playerEntity, getActivity().getApplicationContext());
        deletePlayerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void attemptUpdatePlayer(){

        if (updatePlayerTask != null) {
            return;
        }
        // Get ui values
        String profileId = playerEntity.getProfileId();

        String plateformType = spinner.getSelectedItem().toString();

        Switch switchSyncProgression = rootView.findViewById(R.id.switchSyncProgression);
        boolean syncProgression = switchSyncProgression.isChecked();

        Switch switchEmeaSeason = rootView.findViewById(R.id.switchEmeaSeason);
        boolean syncEmeaSeason = switchEmeaSeason.isChecked();

        Switch switchNcsaSeason = rootView.findViewById(R.id.switchNcsaSeason);
        boolean syncNcsaSeason = switchNcsaSeason.isChecked();

        Switch switchApacSeason = rootView.findViewById(R.id.switchApacSeason);
        boolean syncApacSeason = switchApacSeason.isChecked();

        Switch switchStats = rootView.findViewById(R.id.switchStats);
        boolean syncStats = switchStats.isChecked();

        updatePlayerTask = new UpdatePlayerTask(profileId, plateformType, syncProgression, syncEmeaSeason, syncNcsaSeason, syncApacSeason, syncStats);
        updatePlayerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    public class DeletePlayerTask extends AsyncTask<Void, Void, Boolean> {
        private final PlayerEntity playerEntity;
        private final Context context;

        private AlarmManager alarmManager;

        DeletePlayerTask(PlayerEntity playerEntity, Context context) {
            this.playerEntity = playerEntity;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(PROFILE_ID, playerEntity.getProfileId());
            // TODO search platformType in Bd based on the profileId
            intent.putExtra(PLATEFORM_TYPE, PLAYSTATION);

            SyncEntity syncEntity = playerViewModel.getSyncByProfileId(playerEntity.getProfileId());

            intent.putExtra(SYNC_PROGRESSION, syncEntity.isSyncProgression());
            intent.putExtra(SYNC_EMEA, syncEntity.isSyncEmea());
            intent.putExtra(SYNC_NCSA, syncEntity.isSyncNcsa());
            intent.putExtra(SYNC_APAC, syncEntity.isSyncApac());
            intent.putExtra(SYNC_STATS, syncEntity.isSyncStats());

            int broadcastId = (int) (playerEntity.getAddedDate().getTime() / 1000L);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, broadcastId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }

            playerViewModel.delete(playerEntity);

            sendMessage(getString(R.string.player_deleted));
            return true;
        }

        private void sendMessage(String message){
            Message msg = Message.obtain();
            msg.obj = message;
            msg.setTarget(mHandler);
            msg.sendToTarget();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            updatePlayerTask = null;
            getActivity().finish();
        }

        @Override
        protected void onCancelled() {
            updatePlayerTask = null;
        }
    }

    public class UpdatePlayerTask extends AsyncTask<Void, Void, Boolean> {
        private final String profileId;
        private final String plateformType;
        private final boolean syncProgression;
        private final boolean syncEmeaSeason;
        private final boolean syncNcsaSeason;
        private final boolean syncApacSeason;
        private final boolean syncStats;


        UpdatePlayerTask(String profileId, String plateformType, boolean syncProgression, boolean syncEmeaSeason, boolean syncNcsaSeason, boolean syncApacSeason, boolean syncStats) {
            this.profileId = profileId;
            this.plateformType = plateformType;
            this.syncProgression = syncProgression;
            this.syncEmeaSeason = syncEmeaSeason;
            this.syncNcsaSeason = syncNcsaSeason;
            this.syncApacSeason = syncApacSeason;
            this.syncStats = syncStats;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                connectionEntity = connectionViewModel.getConnection(UbiService.APP_ID);
                if (connectionEntity != null) {
                    Log.d("Debug---Connectivity", "Valid ticket : " + !isTicketInvalid());
                    // Ticket found
                    if (isTicketInvalid()) {
                        // Invalid ticket, new connection is needed
                        String response = ubiService.callUbiConnectionService(connectionEntity.getEncodedKey());
                        if (serviceHelper.isValidResponse(response)) {
                            connectionEntity = serviceHelper.generateConnectionEntity(response, connectionEntity.getEncodedKey());
                            connectionViewModel.insert(connectionEntity);
                            Log.d("Debug---Connectivity", "New ticket generated!");
                        } else {
                            sendMessage(serviceHelper.getErrorMessage(response));
                            return false;
                        }
                    }
                } else {
                    // No ticket (the user has never signed in)
                    sendMessage(getResources().getString(R.string.errorNoConnection));
                    return false;
                }

                // Get progression
                ProgressionEntity progressionEntity = null;
                if(syncProgression) {
                    String progressionResponse = ubiService.getProgressionResponse(connectionEntity.getTicket(), profileId, plateformType);
                    if (serviceHelper.isValidResponse(progressionResponse)) {
                        progressionEntity = serviceHelper.generateProgressionEntity(progressionResponse);
                    } else {
                        sendMessage(serviceHelper.getErrorMessage(progressionResponse));
                        return false;
                    }
                }

                // Get season emea
                SeasonEntity seasonEmeaEntity = null;
                if(syncEmeaSeason) {
                    String seasonEmeaResponse = ubiService.getSeasonResponse(connectionEntity.getTicket(), profileId, REGION_EMEA, CURRENT_SEASON, plateformType);
                    if (serviceHelper.isValidResponse(seasonEmeaResponse)) {
                        seasonEmeaEntity = serviceHelper.generateSeasonEntity(seasonEmeaResponse, profileId);
                    } else {
                        sendMessage(serviceHelper.getErrorMessage(seasonEmeaResponse));
                        return false;
                    }
                }

                // Get season ncsa
                SeasonEntity seasonNcsaEntity = null;
                if(syncNcsaSeason) {
                    String seasonNcsaResponse = ubiService.getSeasonResponse(connectionEntity.getTicket(), profileId, REGION_NCSA, CURRENT_SEASON, plateformType);
                    if (serviceHelper.isValidResponse(seasonNcsaResponse)) {
                        seasonNcsaEntity = serviceHelper.generateSeasonEntity(seasonNcsaResponse, profileId);
                    } else {
                        sendMessage(serviceHelper.getErrorMessage(seasonNcsaResponse));
                        return false;
                    }
                }

                // Get season apac - Disabled here

                // Get stats
                StatsEntity statsEntity = null;
                if(syncStats) {
                    String statsResponse = ubiService.getStatsResponse(connectionEntity.getTicket(), playerEntity.getProfileId(), plateformType);
                    if (serviceHelper.isValidResponse(statsResponse)) {
                        statsEntity = serviceHelper.generateStatsEntity(statsResponse, profileId);
                    } else {
                        sendMessage(serviceHelper.getErrorMessage(statsResponse));
                        return false;
                    }
                }

                boolean newStats = false;

                if(syncProgression && progressionEntity != null) {
                    ProgressionEntity progressionEntityFromDB = playerViewModel.getLastProgressionEntityByProfileId(profileId);
                    if(progressionEntityFromDB == null || progressionEntityFromDB.getXp() != progressionEntity.getXp()) {
                        playerViewModel.insertProgression(progressionEntity);
                        newStats = true;
                    }
                }
                if(syncEmeaSeason && seasonEmeaEntity != null) {
                    SeasonEntity seasonEmeaEntityFromDB = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(profileId,REGION_EMEA, SKIP_0, COUNT_1);
                    if(seasonEmeaEntityFromDB == null || Double.compare(seasonEmeaEntityFromDB.getMmr(), seasonEmeaEntity.getMmr()) != 0) {
                        playerViewModel.insertSeason(seasonEmeaEntity);
                        newStats = true;
                    }
                }
                if(syncNcsaSeason && seasonNcsaEntity != null) {
                    SeasonEntity seasonNcsaEntityFromDB = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(profileId,REGION_NCSA, SKIP_0, COUNT_1);
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
                    sendMessage(getResources().getString(R.string.player_updated));
                }else{
                    sendMessage(getResources().getString(R.string.nothing_new));
                }

            } catch (JSONException e) {
                Log.d("Debug---JSONException", e.getMessage());
                sendMessage(e.getMessage());
                return false;
            } catch (ParseException e) {
                Log.d("Debug---ParseException", e.getMessage());
                sendMessage(e.getMessage());
                return false;
            }
            return true;
        }

        private boolean isTicketInvalid(){
            return System.currentTimeMillis() > connectionEntity.getExpiration().getTime();
        }

        private void sendMessage(String message){
            Message msg = Message.obtain();
            msg.obj = message;
            msg.setTarget(mHandler);
            msg.sendToTarget();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            updatePlayerTask = null;
            if (success) {
            } else {
            }
        }

        @Override
        protected void onCancelled() {
            updatePlayerTask = null;
        }
    }
}
