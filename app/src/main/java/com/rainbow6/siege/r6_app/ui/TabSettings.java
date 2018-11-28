package com.rainbow6.siege.r6_app.ui;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
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
    private AlertDialog alertDialog;
    private AlertDialog seasonsDialog;
    private List<Integer> mSelectedSeasons = new ArrayList();
    private String[] seasons = new String[]{"WIND BASTION", "GRIM SKY", "PARA BELLUM", "CHIMERA", "WHITE NOISE", "BLOOD ORCHID", "HEALTH"};
    private List<Integer> seasonsIds = new ArrayList<>(Arrays.asList(12, 11, 10, 9, 8, 7, 6));

    private PlayerEntity playerEntity;
    private Spinner spinner;
    private Switch switchSyncProgression;
    private Switch switchEmeaSeason;
    private Switch switchNcsaSeason;
    private Switch switchApacSeason;
    private Switch switchStats;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_player_settings, container, false);

        PlayerActivity activity = (PlayerActivity) getActivity();
        playerEntity = activity.getPlayerEntity();

        ubiService = new UbiService();
        serviceHelper = new ServiceHelper();

        connectionViewModel = ViewModelProviders.of(this).get(ConnectionViewModel.class);
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);

        initComponents();

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
                alertDialog.show();
            }
        });

        alertDialog = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.confirm_delete_player)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        attemptDeletePlayer();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();

        final Button buttonSelectSeasons = rootView.findViewById(R.id.button_select_seasons);
        buttonSelectSeasons.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                seasonsDialog.show();
            }
        });

        //seasonsDialog = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert)
        seasonsDialog = new AlertDialog.Builder(getActivity())
                .setMultiChoiceItems(seasons, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    mSelectedSeasons.add(seasonsIds.get(which));
                                } else if (mSelectedSeasons.contains(seasonsIds.get(which))) {
                                    mSelectedSeasons.remove(mSelectedSeasons.indexOf(seasonsIds.get(which)));
                                }
                            }
                        })
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        buttonSelectSeasons.setText(getString(R.string.past_seasons_selected, mSelectedSeasons.size()));
                    }
                }).create();

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
        String profileId = playerEntity.getProfileId();
        String plateformType = spinner.getSelectedItem().toString();
        boolean syncProgression = switchSyncProgression.isChecked();
        boolean syncEmeaSeason = switchEmeaSeason.isChecked();
        boolean syncNcsaSeason = switchNcsaSeason.isChecked();
        boolean syncApacSeason = switchApacSeason.isChecked();
        boolean syncStats = switchStats.isChecked();

        updatePlayerTask = new UpdatePlayerTask(profileId, plateformType, syncProgression, syncEmeaSeason, syncNcsaSeason, syncApacSeason, syncStats, mSelectedSeasons);
        updatePlayerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initComponents(){
        SyncEntity syncEntity = playerViewModel.getSyncByProfileId(playerEntity.getProfileId());
        switchSyncProgression = rootView.findViewById(R.id.switchSyncProgression);
        switchEmeaSeason = rootView.findViewById(R.id.switchEmeaSeason);
        switchNcsaSeason = rootView.findViewById(R.id.switchNcsaSeason);
        switchApacSeason = rootView.findViewById(R.id.switchApacSeason);
        switchStats = rootView.findViewById(R.id.switchStats);

        switchSyncProgression.setChecked(syncEntity.isSyncProgression());
        switchEmeaSeason.setChecked(syncEntity.isSyncEmea());
        switchNcsaSeason.setChecked(syncEntity.isSyncNcsa());
        switchApacSeason.setChecked(syncEntity.isSyncApac());
        switchStats.setChecked(syncEntity.isSyncStats());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        alertDialog.dismiss();
        alertDialog=null;

        seasonsDialog.dismiss();
        seasonsDialog = null;
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

            // Remove the alarm
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }

            // Remove the last refresh time
            SharedPreferences pref = getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(playerEntity.getProfileId());
            editor.commit();


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
        private final List<Integer> mSelectedSeasons;


        UpdatePlayerTask(String profileId, String plateformType, boolean syncProgression, boolean syncEmeaSeason, boolean syncNcsaSeason, boolean syncApacSeason, boolean syncStats, List<Integer> mSelectedSeasons) {
            this.profileId = profileId;
            this.plateformType = plateformType;
            this.syncProgression = syncProgression;
            this.syncEmeaSeason = syncEmeaSeason;
            this.syncNcsaSeason = syncNcsaSeason;
            this.syncApacSeason = syncApacSeason;
            this.syncStats = syncStats;
            this.mSelectedSeasons = mSelectedSeasons;
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
                    SeasonEntity seasonEmeaEntityFromDB = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(profileId, REGION_EMEA, SKIP_0, COUNT_1);
                    if(seasonEmeaEntityFromDB == null || Double.compare(seasonEmeaEntityFromDB.getMmr(), seasonEmeaEntity.getMmr()) != 0) {
                        playerViewModel.insertSeason(seasonEmeaEntity);
                        newStats = true;
                    }
                }
                if(syncNcsaSeason && seasonNcsaEntity != null) {
                    SeasonEntity seasonNcsaEntityFromDB = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(profileId, REGION_NCSA, SKIP_0, COUNT_1);
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

                if (mSelectedSeasons != null && !mSelectedSeasons.isEmpty()) {

                    for (Integer seasonId : mSelectedSeasons) {

                        if (syncEmeaSeason) {
                            String seasonEmeaResponse = ubiService.getSeasonResponse(connectionEntity.getTicket(), profileId, REGION_EMEA, seasonId, plateformType);
                            if (serviceHelper.isValidResponse(seasonEmeaResponse)) {
                                seasonEmeaEntity = serviceHelper.generateSeasonEntity(seasonEmeaResponse, profileId);

                                if (seasonEmeaEntity != null) {
                                    SeasonEntity seasonEmeaEntityFromDB = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(profileId, REGION_EMEA, SKIP_0, COUNT_1);
                                    if (seasonEmeaEntityFromDB == null || Double.compare(seasonEmeaEntityFromDB.getMmr(), seasonEmeaEntity.getMmr()) != 0) {
                                        playerViewModel.insertSeason(seasonEmeaEntity);
                                        newStats = true;
                                    }
                                }
                            } else {
                                sendMessage(serviceHelper.getErrorMessage(seasonEmeaResponse));
                                return false;
                            }
                        }

                        if (syncNcsaSeason) {
                            String seasonNcsaResponse = ubiService.getSeasonResponse(connectionEntity.getTicket(), profileId, REGION_NCSA, seasonId, plateformType);
                            if (serviceHelper.isValidResponse(seasonNcsaResponse)) {
                                seasonNcsaEntity = serviceHelper.generateSeasonEntity(seasonNcsaResponse, profileId);

                                if (seasonNcsaEntity != null) {
                                    SeasonEntity seasonNcsaEntityFromDB = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(profileId, REGION_NCSA, SKIP_0, COUNT_1);
                                    if (seasonNcsaEntityFromDB == null || Double.compare(seasonNcsaEntityFromDB.getMmr(), seasonNcsaEntity.getMmr()) != 0) {
                                        playerViewModel.insertSeason(seasonNcsaEntity);
                                        newStats = true;
                                    }
                                }
                            } else {
                                sendMessage(serviceHelper.getErrorMessage(seasonNcsaResponse));
                                return false;
                            }
                        }
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
            if(MainActivity.getInstance()!=null) {
                MainActivity.getInstance().updateUI();
            }
            if(TabStats.getInstance()!=null) {
                TabStats.getInstance().updateUI();
            }
            if(TabSeasons.getInstance()!=null) {
                TabSeasons.getInstance().updateUI();
            }
        }

        @Override
        protected void onCancelled() {
            updatePlayerTask = null;
        }
    }
}
