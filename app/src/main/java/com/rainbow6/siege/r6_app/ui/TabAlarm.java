package com.rainbow6.siege.r6_app.ui;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.db.entity.SyncEntity;
import com.rainbow6.siege.r6_app.tools.AlarmReceiver;
import com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel;

import java.util.Date;

import static android.content.Context.ALARM_SERVICE;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class TabAlarm extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String PROFILE_ID = "PROFILE_ID";
    public static final String PLATEFORM_TYPE = "PLATEFORM_TYPE";
    public static final String SYNC_PROGRESSION = "SYNC_PROGRESSION";
    public static final String SYNC_EMEA = "SYNC_EMEA";
    public static final String SYNC_NCSA = "SYNC_NCSA";
    public static final String SYNC_APAC = "SYNC_APAC";
    public static final String SYNC_STATS = "SYNC_STATS";

    private AlarmPlayerTask alarmPlayerTask = null;

    private View rootView;
    private PlayerViewModel playerViewModel;
    private Handler mHandler;

    private PlayerEntity playerEntity;
    private EditText pickSyncTimer;
    private Spinner spinner;
    private Switch switchSyncProgression;
    private Switch switchEmeaSeason;
    private Switch switchNcsaSeason;
    private Switch switchApacSeason;
    private Switch switchStats;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_player_alarm, container, false);

        PlayerActivity activity = (PlayerActivity) getActivity();
        playerEntity = activity.getPlayerEntity();

        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);

        initComponents();

        spinner = rootView.findViewById(R.id.plateformType_spinner);
        ArrayAdapter<CharSequence> spinnerArrayAdapter =  ArrayAdapter.createFromResource(
                getActivity(), R.array.platformType_array, R.layout.spinner_item
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

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
        String profileId = playerEntity.getProfileId();
        String plateformType = spinner.getSelectedItem().toString();
        boolean syncProgression = switchSyncProgression.isChecked();
        boolean syncEmeaSeason = switchEmeaSeason.isChecked();
        boolean syncNcsaSeason = switchNcsaSeason.isChecked();
        boolean syncApacSeason = switchApacSeason.isChecked();
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
        alarmPlayerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initComponents(){
        SyncEntity syncEntity = playerViewModel.getSyncByProfileId(playerEntity.getProfileId());
        switchSyncProgression = rootView.findViewById(R.id.switchSyncProgression);
        switchEmeaSeason = rootView.findViewById(R.id.switchEmeaSeason);
        switchNcsaSeason = rootView.findViewById(R.id.switchNcsaSeason);
        switchApacSeason = rootView.findViewById(R.id.switchApacSeason);
        switchStats = rootView.findViewById(R.id.switchStats);
        pickSyncTimer = rootView.findViewById(R.id.pickRefreshTimer);

        switchSyncProgression.setChecked(syncEntity.isSyncProgression());
        switchEmeaSeason.setChecked(syncEntity.isSyncEmea());
        switchNcsaSeason.setChecked(syncEntity.isSyncNcsa());
        switchApacSeason.setChecked(syncEntity.isSyncApac());
        switchStats.setChecked(syncEntity.isSyncStats());
        long minutes = syncEntity.getSyncDelay() % 60;
        long hours = syncEntity.getSyncDelay() / 60;
        pickSyncTimer.setText(String.format("%02d:%02d", hours, minutes));
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
            intent.putExtra(PLATEFORM_TYPE, plateformType);
            intent.putExtra(SYNC_PROGRESSION, syncProgression);
            intent.putExtra(SYNC_EMEA, syncEmeaSeason);
            intent.putExtra(SYNC_NCSA, syncNcsaSeason);
            intent.putExtra(SYNC_APAC, syncApacSeason);
            intent.putExtra(SYNC_STATS, syncStats);

            // Must be a unique user identifier
            int broadcastId = (int) (playerEntity.getAddedDate().getTime() / 1000L);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, broadcastId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            SharedPreferences pref = getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = pref.edit();

            if(syncTimer == 0){

                // Remove the alarm
                if (alarmManager != null) {
                    alarmManager.cancel(pendingIntent);
                }
                // Remove the last refresh time
                editor.remove(playerEntity.getProfileId());
                editor.commit();

                if(PlayerActivity.getInstance()!=null) {
                    PlayerActivity.getInstance().updateUI();
                }
                sendMessage(getString(R.string.timer_disabled));
                return true;
            }else {
                editor.putLong(playerEntity.getProfileId(), new Date().getTime());
                editor.commit();

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                        (long)syncTimer * 60L * 1000L,(long)syncTimer * 60L * 1000L, pendingIntent);

                sendMessage(getString(R.string.timer_set));
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
            if(PlayerActivity.getInstance()!=null) {
                PlayerActivity.getInstance().updateUI();
            }
        }

        @Override
        protected void onCancelled() {
            alarmPlayerTask = null;
        }
    }
}
