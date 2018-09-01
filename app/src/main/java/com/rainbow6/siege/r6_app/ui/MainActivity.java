package com.rainbow6.siege.r6_app.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.service.UbiService;
import com.rainbow6.siege.r6_app.viewmodel.ConnectionViewModel;
import com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_PLAYER_ACTIVITY_REQUEST_CODE = 1;

    private PlayerViewModel playerViewModel;
    private ConnectionViewModel connectionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final PlayerListAdapter adapter = new PlayerListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);

        playerViewModel.getAllPlayers().observe(this, new Observer<List<PlayerEntity>>() {
            @Override
            public void onChanged(@Nullable final List<PlayerEntity> playerEntities) {
                // Update the cached copy of the playerEntities in the adapter.
                adapter.setPlayers(playerEntities);
            }
        });

        connectionViewModel = ViewModelProviders.of(this).get(ConnectionViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewPlayerActivity.class);
                startActivityForResult(intent, NEW_PLAYER_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_PLAYER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            ConnectionEntity connectionEntity = connectionViewModel.getConnect(UbiService.APP_ID);

            boolean connectionNeed = false;

            if(connectionEntity != null){
                // Ticket found
                if(System.currentTimeMillis() > connectionEntity.getExpiration().getTime()){
                    // Invalid ticket
                    connectionNeed = true;
                }
            }else{
                connectionNeed = true;
            }

            if(connectionNeed){
                // Call the Ubi Connection service to get a ticket
            }

            // Now let's call the Web Services

            PlayerEntity playerEntity = new PlayerEntity(new Date() + "", new Date() + "", data.getStringExtra(NewPlayerActivity.EXTRA_REPLY), new Date() + "", new Date());
            playerViewModel.insert(playerEntity);
        } else {

            if(data != null) {
                Toast.makeText(
                        getApplicationContext(),
                        data.getStringExtra(NewPlayerActivity.EXTRA_REPLY),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
//            return true;
              Intent intent = new Intent(MainActivity.this, LoginActivity.class);
              startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
