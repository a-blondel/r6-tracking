package com.rainbow6.siege.r6_app.ui;

import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.db.entity.SyncEntity;
import com.rainbow6.siege.r6_app.service.UbiService;
import com.rainbow6.siege.r6_app.tools.ServiceHelper;
import com.rainbow6.siege.r6_app.viewmodel.ConnectionViewModel;
import com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel;

import org.json.JSONException;

import java.text.ParseException;

import static com.rainbow6.siege.r6_app.service.UbiService.EXCEPTION_PATTERN;
import static com.rainbow6.siege.r6_app.tools.ServiceHelper.UBI_ERROR_CODE;

public class NewPlayerActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "";
    private static final String VIDE = "";

    private ConnectionViewModel connectionViewModel;
    private PlayerViewModel playerViewModel;
    private UbiService ubiService;
    private ServiceHelper serviceHelper;
    private ConnectionEntity connectionEntity;
    private Intent replyIntent;

    private String playerName;
    private String plateformType;
    private EditText mEditPlayerView;
    private Spinner spinner;
    private EditText pickSyncTimer;
    private boolean syncProgression;
    private boolean syncEmeaSeason;
    private boolean syncNcsaSeason;
    private boolean syncApacSeason;
    private boolean syncStats;
    private boolean syncGeneral;
    private int syncTimer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);

        connectionViewModel = ViewModelProviders.of(this).get(ConnectionViewModel.class);
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
        ubiService = new UbiService();
        serviceHelper = new ServiceHelper();

        mEditPlayerView = findViewById(R.id.edit_player);

        spinner = findViewById(R.id.plateformType_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.platformType_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        pickSyncTimer = findViewById(R.id.pickRefreshRate);
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
                replyIntent = new Intent();
                String response;
                if (TextUtils.isEmpty(mEditPlayerView.getText())) {
                    replyIntent.putExtra(EXTRA_REPLY, getResources().getString(R.string.error_empty));
                    setResult(RESULT_CANCELED, replyIntent);
                } else {

                    try {
                        connectionEntity = connectionViewModel.getConnection(UbiService.APP_ID);

                        if (connectionEntity != null) {
                            // Ticket found
                            if (isTicketInvalid()) {
                                // Invalid ticket, new connection is needed
                                response = ubiService.callUbiConnectionService(connectionEntity.getEncodedKey());

                                if (isValidResponse(response)) {
                                    connectionViewModel.insert(serviceHelper.generateConnectionEntity(response, connectionEntity.getEncodedKey()));
                                } else {
                                    sendError(serviceHelper.getErrorMessage(response));
                                }
                            }
                        } else {
                            // No ticket (the user has never signed in)
                            sendError(getResources().getString(R.string.errorNoConnection));
                        }

                        // Synchronisation settings
                        populateSynchronisationSettings();

                        // Don't do anything if there is nothing
                        if (VIDE.equals(playerName)) {
                            sendError(getResources().getString(R.string.error_empty));
                        }

                        // Now let's call the Web Services
                        // First, get the profile
                        String profileUrl = "https://public-ubiservices.ubi.com/v2/profiles?nameOnPlatform=" + playerName + "&platformType=" + plateformType;
                        String profileResponse = ubiService.callWebService(profileUrl, connectionEntity.getTicket());

                        PlayerEntity playerEntity = new PlayerEntity();

                        if (isValidResponse(profileResponse)) {
                            playerEntity = serviceHelper.generatePlayerEntity(profileResponse);
                        } else {
                            sendError(serviceHelper.getErrorMessage(profileResponse));
                        }

                        if(playerEntity.getProfileId() == null){
                            sendError(getResources().getString(R.string.playerDoesNotExist, playerName));
                        }

                        // Get Progression (only the first time) if wanted

                        // Get Skill emea
                        // Get Skill cnsa
                        // Get Skill apac

                        // Get Stats (Casual / Ranked)

                        // Get General (accuracy)


//                        playerViewModel.insert(playerEntity);
                        SyncEntity syncEntity = new SyncEntity(playerEntity.getProfileId(), syncProgression, syncEmeaSeason, syncNcsaSeason, syncApacSeason, syncGeneral, syncStats, syncTimer);

                        replyIntent.putExtra(EXTRA_REPLY, getResources().getString(R.string.player_added, playerName));
                        setResult(RESULT_OK, replyIntent);
                    } catch (JSONException e) {
                        sendError(e.getMessage());
                    } catch (ParseException e) {
                        sendError(e.getMessage());
                    }
                }
                finish();
            }
        });
    }

    private void sendError(String msg){
        replyIntent.putExtra(EXTRA_REPLY, msg);
        setResult(RESULT_CANCELED, replyIntent);
    }

    private boolean isTicketInvalid(){
        return System.currentTimeMillis() > connectionEntity.getExpiration().getTime();
    }

    private void populateSynchronisationSettings(){
        playerName = mEditPlayerView.getText().toString();

        plateformType = spinner.getSelectedItem().toString();

        Switch switchSyncProgression = findViewById(R.id.switchSyncProgression);
        syncProgression = switchSyncProgression.isChecked();

        Switch switchEmeaSeason = findViewById(R.id.switchEmeaSeason);
        syncEmeaSeason = switchEmeaSeason.isChecked();

        Switch switchNcsaSeason = findViewById(R.id.switchNcsaSeason);
        syncNcsaSeason = switchNcsaSeason.isChecked();

        Switch switchApacSeason = findViewById(R.id.switchApacSeason);
        syncApacSeason = switchApacSeason.isChecked();

        Switch switchStats = findViewById(R.id.switchStats);
        syncStats = switchStats.isChecked();

        Switch switchGeneral = findViewById(R.id.switchGeneral);
        syncGeneral = switchGeneral.isChecked();

        String syncTimerString = pickSyncTimer.getText().toString();
        if(!syncTimerString.equals("") && !syncTimerString.equals("0:0") ){
            String[] separated = syncTimerString.split(":");
            int hours = Integer.parseInt(separated[0]);
            int minutes = Integer.parseInt(separated[1]);
            syncTimer = hours * 60 + minutes;
        }
    }

    private boolean isValidResponse(String response){
        return response != null && !VIDE.equals(response) && !response.contains(UBI_ERROR_CODE) &&!response.contains(EXCEPTION_PATTERN);
    }
}
