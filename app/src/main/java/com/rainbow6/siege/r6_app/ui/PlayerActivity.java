package com.rainbow6.siege.r6_app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;
import com.rainbow6.siege.r6_app.db.entity.SyncEntity;
import com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel;

import java.util.Date;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PlayerActivity extends AppCompatActivity implements TabSeasons.OnListFragmentInteractionListener {

    public static String PLAYER = "player";

    private PlayerViewModel playerViewModel;
    private PlayerEntity playerEntity;
    private CountDownTimer cTimer = null;
    private Toolbar toolbar;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static PlayerActivity playerActivityRunningInstance;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        playerActivityRunningInstance = this;

        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        playerEntity = (PlayerEntity) getIntent().getSerializableExtra(PLAYER);
        toolbar.setTitle(playerEntity.getNameOnPlatform());
        showPlayerName();

        int i = getIntent().getIntExtra("tabSeasons", -1);
        if (i != -1) {
            mViewPager.setCurrentItem(i);
        }

    }

    private void showPlayerName(){
        SharedPreferences pref = getDefaultSharedPreferences(getApplicationContext());

        long dateLastRefresh = pref.getLong(playerEntity.getProfileId(), 0);
        SyncEntity syncEntity = playerViewModel.getSyncByProfileId(playerEntity.getProfileId());

        if(syncEntity.getSyncDelay() !=0 && dateLastRefresh != 0){
            long timeLeft = (syncEntity.getSyncDelay() * 60 * 1000 + dateLastRefresh) - (new Date().getTime());
            startTimer(timeLeft);
        }else{
            cancelTimer();
            toolbar.setTitle(playerEntity.getNameOnPlatform());
        }
    }

    public void updateUI() {
        PlayerActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                showPlayerName();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        cancelTimer();
    }

    public void startTimer(long timeLeft) {
        cancelTimer();
        cTimer = new CountDownTimer(timeLeft, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                toolbar.setTitle(playerEntity.getNameOnPlatform() + " (" + String.format("%02d:%02d:%02d", hours % 24, minutes % 60, seconds % 60) + ")");
            }
            public void onFinish() {
            }
        };
        cTimer.start();
    }

    public void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    public static PlayerActivity getInstance() { return playerActivityRunningInstance; }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    @Override
    public void onListFragmentInteraction(SeasonEntity item) {
//        Toast.makeText(getBaseContext(), String.valueOf(item.getRank()), Toast.LENGTH_SHORT).show();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TabStats tabStats = new TabStats();
                    return tabStats;
                case 1:
                    TabSeasons tabSeasons = new TabSeasons();
                    return tabSeasons;
                case 2:
                    TabSettings tabSettings = new TabSettings();
                    return tabSettings;
                case 3:
                    TabAlarm tabAlarm = new TabAlarm();
                    return tabAlarm;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.tab_stats);
                case 1:
                    return getResources().getString(R.string.tab_seasons);
                case 2:
                    return getResources().getString(R.string.tab_settings);
                case 3:
                    return getResources().getString(R.string.tab_timer);
                default:
                    return null;
            }
        }

    }
}
