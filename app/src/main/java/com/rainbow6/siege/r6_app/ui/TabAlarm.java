package com.rainbow6.siege.r6_app.ui;

import android.app.LoaderManager;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.db.entity.ProgressionEntity;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;
import com.rainbow6.siege.r6_app.db.entity.StatsEntity;
import com.rainbow6.siege.r6_app.db.entity.SyncEntity;
import com.rainbow6.siege.r6_app.service.UbiService;
import com.rainbow6.siege.r6_app.tools.ServiceHelper;
import com.rainbow6.siege.r6_app.viewmodel.ConnectionViewModel;
import com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel;

import org.json.JSONException;

import java.text.ParseException;

import static com.rainbow6.siege.r6_app.service.UbiService.CURRENT_SEASON;
import static com.rainbow6.siege.r6_app.service.UbiService.REGION_EMEA;
import static com.rainbow6.siege.r6_app.service.UbiService.REGION_NCSA;

public class TabAlarm extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private AlarmPlayerTask alarmPlayerTask = null;

    private TextView playerNameItemView;
    private View rootView;
    private ConnectionViewModel connectionViewModel;
    private PlayerViewModel playerViewModel;
    private UbiService ubiService;
    private ServiceHelper serviceHelper;
    private ConnectionEntity connectionEntity;
    private Handler mHandler;

    private PlayerEntity playerEntity;
    private EditText pickSyncTimer;
    private Spinner spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_player_alarm, container, false);

        // Find a way to bypass that
        PlayerActivity activity = (PlayerActivity) getActivity();
        playerEntity = activity.getPlayerEntity();

        ubiService = new UbiService();
        serviceHelper = new ServiceHelper();

        connectionViewModel = ViewModelProviders.of(this).get(ConnectionViewModel.class);
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);

        pickSyncTimer = rootView.findViewById(R.id.pickRefreshTimer);

        playerNameItemView = rootView.findViewById(R.id.playerName);
        playerNameItemView.setText(playerEntity.getNameOnPlatform());

        spinner = rootView.findViewById(R.id.plateformType_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.platformType_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        pickSyncTimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String syncTimerString = pickSyncTimer.getText().toString();
                int hour = 0;
                int minute = 0;
                if(!syncTimerString.equals("")){
                    String[] separated = syncTimerString.split(":");
                    hour = Integer.parseInt(separated[0]);
                    minute = Integer.parseInt(separated[1]);
                }
                TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        pickSyncTimer.setText( getString( R.string.timePattern, selectedHour, selectedMinute ));
                    }
                }, hour, minute, true);
                mTimePicker.setTitle(getString(R.string.syncTimerTitle));
                mTimePicker.show();
            }
        });

        final Button button = rootView.findViewById(R.id.button_set_timer);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                attemptUpdatePlayer();
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

    private void attemptUpdatePlayer(){

        if (alarmPlayerTask != null) {
            return;
        }
        boolean cancel = false;
        View focusView = null;

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

        int syncTimer = 0;

        String syncTimerString = pickSyncTimer.getText().toString();
        if(!syncTimerString.equals("") && !syncTimerString.equals("0:0") ){
            String[] separated = syncTimerString.split(":");
            int hours = Integer.parseInt(separated[0]);
            int minutes = Integer.parseInt(separated[1]);
            syncTimer = hours * 60 + minutes;
        }

        alarmPlayerTask = new AlarmPlayerTask(profileId, plateformType, syncProgression, syncEmeaSeason, syncNcsaSeason, syncApacSeason, syncStats, syncTimer);
//            newPlayerTask.execute((Void) null);
        alarmPlayerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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

    public class AlarmPlayerTask extends AsyncTask<Void, Void, Boolean> {
        private final String profileId;
        private final String plateformType;
        private final boolean syncProgression;
        private final boolean syncEmeaSeason;
        private final boolean syncNcsaSeason;
        private final boolean syncApacSeason;
        private final boolean syncStats;
        private final int syncTimer;

        private static final boolean SKIPPED_FOR_DEBUGGUER = true;


        AlarmPlayerTask(String profileId, String plateformType, boolean syncProgression, boolean syncEmeaSeason, boolean syncNcsaSeason, boolean syncApacSeason, boolean syncStats, int syncTimer) {
            this.profileId = profileId;
            this.plateformType = plateformType;
            this.syncProgression = syncProgression;
            this.syncEmeaSeason = syncEmeaSeason;
            this.syncNcsaSeason = syncNcsaSeason;
            this.syncApacSeason = syncApacSeason;
            this.syncStats = syncStats;
            this.syncTimer = syncTimer;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if(syncTimer == 0){
                // VERIFIER SI ALARM MANAGER EXISTANT
                // SI OUI, LE SUPPRIMER
                // ENVOYER MESSAGE (AUCUN PRESENT, SUPPRIME)
//                sendMessage(getResources().getString(R.string.player_updated));
                sendMessage("Timer set to 0");
                return true;

            }else {

                try {

                    // EVERYTHING IN THE ALARM MANAGER
                    boolean newStats = false;

                    if(!SKIPPED_FOR_DEBUGGUER) {

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
                        if (syncProgression) {
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
                        if (syncEmeaSeason) {
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
                        if (syncNcsaSeason) {
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
                        if (syncStats) {
                            String statsResponse = ubiService.getStatsResponse(connectionEntity.getTicket(), playerEntity.getProfileId(), plateformType);
                            if (serviceHelper.isValidResponse(statsResponse)) {
                                statsEntity = serviceHelper.generateStatsEntity(statsResponse, profileId);
                            } else {
                                sendMessage(serviceHelper.getErrorMessage(statsResponse));
                                return false;
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

                    }

                    // UPDATE THE ALARM MANAGER WITH THE NEW SYNC ENTITY BY CHANNEL ( = PROFILE)
                    // DON'T FORGET TO SHOW THE LAST VALUES IN THE VIEW

                    SyncEntity syncEntity = new SyncEntity(profileId, syncProgression, syncEmeaSeason, syncNcsaSeason, syncApacSeason, syncStats, syncTimer);
                    playerViewModel.updateSync(syncEntity);

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
                    return false;
                } catch (ParseException e) {
                    Log.d("Debug---ParseException", e.getMessage());
                    sendMessage(e.getMessage());
                    return false;
                }
                return true;
            }
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
            alarmPlayerTask = null;
            if (success) {
//                finish();
            } else {
//                playerNameText.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            alarmPlayerTask = null;
        }
    }
}
