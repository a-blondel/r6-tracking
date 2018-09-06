package com.rainbow6.siege.r6_app.ui;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
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
import android.os.SystemClock;
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
import com.rainbow6.siege.r6_app.tools.AlarmReceiver;
import com.rainbow6.siege.r6_app.tools.ServiceHelper;
import com.rainbow6.siege.r6_app.viewmodel.ConnectionViewModel;
import com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel;

import org.json.JSONException;

import java.text.ParseException;

import static android.content.Context.ALARM_SERVICE;
import static com.rainbow6.siege.r6_app.service.UbiService.CURRENT_SEASON;
import static com.rainbow6.siege.r6_app.service.UbiService.REGION_EMEA;
import static com.rainbow6.siege.r6_app.service.UbiService.REGION_NCSA;

public class TabAlarm extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String PROFILE_ID = "PROFILE";

    private AlarmPlayerTask alarmPlayerTask = null;

    private TextView playerNameItemView;
    private View rootView;
    private PlayerViewModel playerViewModel;
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

        alarmPlayerTask = new AlarmPlayerTask(profileId, plateformType, syncProgression, syncEmeaSeason, syncNcsaSeason, syncApacSeason, syncStats, syncTimer, getActivity().getApplicationContext());
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
        private final Context context;

        private AlarmManager alarmManager;

        AlarmPlayerTask(String profileId, String plateformType, boolean syncProgression, boolean syncEmeaSeason, boolean syncNcsaSeason, boolean syncApacSeason, boolean syncStats, int syncTimer, Context context) {
            this.profileId = profileId;
            this.plateformType = plateformType;
            this.syncProgression = syncProgression;
            this.syncEmeaSeason = syncEmeaSeason;
            this.syncNcsaSeason = syncNcsaSeason;
            this.syncApacSeason = syncApacSeason;
            this.syncStats = syncStats;
            this.syncTimer = syncTimer;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // Update sync settings
            SyncEntity syncEntity = new SyncEntity(profileId, syncProgression, syncEmeaSeason, syncNcsaSeason, syncApacSeason, syncStats, syncTimer);
            playerViewModel.updateSync(syncEntity);

            // Setting up the AlarmManager and pending intent
            alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(PROFILE_ID, profileId);

            // Must be a unique user identifier
            int broadcastId = (int) (playerEntity.getAddedDate().getTime() / 1000L);

            Log.d(profileId, "broadcastId :" + broadcastId);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, broadcastId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if(syncTimer == 0){

                if (alarmManager != null) {
                    alarmManager.cancel(pendingIntent);
                }

                sendMessage("Timer disabled");
                return true;

            }else {

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() +
                        syncTimer * 60 * 1000,syncTimer * 60 * 1000, pendingIntent);

                sendMessage("Timer set !");
                return true;
            }
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
