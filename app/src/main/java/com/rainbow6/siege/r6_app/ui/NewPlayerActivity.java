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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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

public class NewPlayerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private NewPlayerTask newPlayerTask = null;
    private ConnectionViewModel connectionViewModel;
    private PlayerViewModel playerViewModel;
    private UbiService ubiService;
    private ServiceHelper serviceHelper;
    private ConnectionEntity connectionEntity;
    private Handler mHandler;

    private EditText playerNameText;
    private EditText pickSyncTimer;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ubiService = new UbiService();
        serviceHelper = new ServiceHelper();

        playerNameText = findViewById(R.id.edit_player);
        pickSyncTimer = findViewById(R.id.pickRefreshTimer);

        spinner = findViewById(R.id.plateformType_spinner);

        ArrayAdapter<CharSequence> spinnerArrayAdapter =  ArrayAdapter.createFromResource(
                this, R.array.platformType_array, R.layout.spinner_item
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        connectionViewModel = ViewModelProviders.of(this).get(ConnectionViewModel.class);
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);


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
                TimePickerDialog mTimePicker = new TimePickerDialog(NewPlayerActivity.this, android.R.style.Theme_Holo_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        pickSyncTimer.setText( getString( R.string.timePattern, selectedHour, selectedMinute ));
                    }
                }, hour, minute, true);
                mTimePicker.setTitle(getString(R.string.syncTimerTitle));
                mTimePicker.show();
            }
        });


        final Button button = findViewById(R.id.button_add_player);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(playerNameText.getText())) {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_empty),
                            Toast.LENGTH_LONG).show();
                    playerNameText.requestFocus();
                } else {
                    attemptAddPlayer();
                }
            }
        });

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                Toast.makeText(getApplicationContext(),
                        (String) message.obj, Toast.LENGTH_LONG).show();
            }
        };
    }

    private void attemptAddPlayer(){
        if (newPlayerTask != null) {
            return;
        }
        boolean cancel = false;
        View focusView = null;

        // Get ui values
        String playerName = playerNameText.getText().toString().replaceAll("\\s", "");

        String plateformType = spinner.getSelectedItem().toString();

        Switch switchSyncProgression = findViewById(R.id.switchSyncProgression);
        boolean syncProgression = switchSyncProgression.isChecked();

        Switch switchEmeaSeason = findViewById(R.id.switchEmeaSeason);
        boolean syncEmeaSeason = switchEmeaSeason.isChecked();

        Switch switchNcsaSeason = findViewById(R.id.switchNcsaSeason);
        boolean syncNcsaSeason = switchNcsaSeason.isChecked();

        Switch switchApacSeason = findViewById(R.id.switchApacSeason);
        boolean syncApacSeason = switchApacSeason.isChecked();

        Switch switchStats = findViewById(R.id.switchStats);
        boolean syncStats = switchStats.isChecked();

        int syncTimer = 0;

        String syncTimerString = pickSyncTimer.getText().toString();
        if(!syncTimerString.equals("") && !syncTimerString.equals("0:0") ){
            String[] separated = syncTimerString.split(":");
            int hours = Integer.parseInt(separated[0]);
            int minutes = Integer.parseInt(separated[1]);
            syncTimer = hours * 60 + minutes;
        }
        // all ui values set !

        if (TextUtils.isEmpty(playerName)) {
            focusView = playerNameText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            newPlayerTask = new NewPlayerTask(playerName, plateformType, syncProgression, syncEmeaSeason, syncNcsaSeason, syncApacSeason, syncStats, syncTimer);
//            newPlayerTask.execute((Void) null);
            newPlayerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

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

    public class NewPlayerTask extends AsyncTask<Void, Void, Boolean> {
        private final String playerName;
        private final String plateformType;
        private final boolean syncProgression;
        private final boolean syncEmeaSeason;
        private final boolean syncNcsaSeason;
        private final boolean syncApacSeason;
        private final boolean syncStats;
        private final int syncTimer;


        NewPlayerTask(String playerName, String plateformType, boolean syncProgression, boolean syncEmeaSeason, boolean syncNcsaSeason, boolean syncApacSeason, boolean syncStats, int syncTimer) {
            this.playerName = playerName;
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

                // Get profile
                String profileResponse = ubiService.getProfileResponse(connectionEntity.getTicket(), playerName, plateformType);
                PlayerEntity playerEntity;
                if (serviceHelper.isValidResponse(profileResponse)) {
                    playerEntity = serviceHelper.generatePlayerEntity(profileResponse);
                } else {
                    sendMessage(serviceHelper.getErrorMessage(profileResponse));
                    return false;
                }

                String profileId = playerEntity.getProfileId();

                if(profileId == null){
                    sendMessage(getResources().getString(R.string.playerDoesNotExist, playerName));
                    return false;
                }

                // Get progression
                String progressionResponse = ubiService.getProgressionResponse(connectionEntity.getTicket(), profileId, plateformType);
                ProgressionEntity progressionEntity;
                if (serviceHelper.isValidResponse(progressionResponse)) {
                    progressionEntity = serviceHelper.generateProgressionEntity(progressionResponse);
                } else {
                    sendMessage(serviceHelper.getErrorMessage(progressionResponse));
                    return false;
                }

                // Get season emea
                String seasonEmeaResponse = ubiService.getSeasonResponse(connectionEntity.getTicket(), profileId, REGION_EMEA, CURRENT_SEASON, plateformType);
                SeasonEntity seasonEntity;
                if (serviceHelper.isValidResponse(seasonEmeaResponse)) {
                    seasonEntity = serviceHelper.generateSeasonEntity(seasonEmeaResponse, profileId);
                } else {
                    sendMessage(serviceHelper.getErrorMessage(seasonEmeaResponse));
                    return false;
                }
                // Get season ncsa - Disabled here
                // Get season apac - Disabled here

                // Get stats
                String statsResponse = ubiService.getStatsResponse(connectionEntity.getTicket(), playerEntity.getProfileId(), plateformType);
                StatsEntity statsEntity;
                if (serviceHelper.isValidResponse(statsResponse)) {
                    statsEntity = serviceHelper.generateStatsEntity(statsResponse, profileId);
                } else {
                    sendMessage(serviceHelper.getErrorMessage(statsResponse));
                    return false;
                }

                SyncEntity syncEntity = new SyncEntity(playerEntity.getProfileId(), syncProgression, syncEmeaSeason, syncNcsaSeason, syncApacSeason, syncStats, syncTimer);

                playerViewModel.insertPlayer(playerEntity);
                playerViewModel.insertProgression(progressionEntity);
                playerViewModel.insertSeason(seasonEntity);
                playerViewModel.insertStats(statsEntity);
                playerViewModel.insertSync(syncEntity);

                sendMessage(getResources().getString(R.string.player_added, playerName));
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
            newPlayerTask = null;
            if (success) {
                finish();
            } else {
                playerNameText.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            newPlayerTask = null;
        }

    }

}
