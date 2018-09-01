package com.rainbow6.siege.r6_app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.service.UbiService;
import com.rainbow6.siege.r6_app.viewmodel.ConnectionViewModel;
import com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel;

import java.util.Date;

public class NewPlayerActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "";

    private ConnectionViewModel connectionViewModel;
    private PlayerViewModel playerViewModel;

    private EditText mEditPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);

        connectionViewModel = ViewModelProviders.of(this).get(ConnectionViewModel.class);
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);

        mEditPlayerView = findViewById(R.id.edit_player);

        final Button button = findViewById(R.id.button_add_player);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditPlayerView.getText())) {
                    replyIntent.putExtra(EXTRA_REPLY, getResources().getString(R.string.error_empty));
                    setResult(RESULT_CANCELED, replyIntent);
                } else {

                    String playerName = mEditPlayerView.getText().toString();

                    ConnectionEntity connectionEntity = connectionViewModel.getConnection(UbiService.APP_ID);

                    boolean connectionNeeded = false;

                    if(connectionEntity != null){
                        // Ticket found
                        if(System.currentTimeMillis() > connectionEntity.getExpiration().getTime()){
                            // Invalid ticket
                            connectionNeeded = true;
                        }
                    }else{
                        connectionNeeded = true;
                    }

                    if(connectionNeeded){
                        // Call the Ubi Connection service to get a ticket
                    }

                    // Now let's call the Web Services

                    PlayerEntity playerEntity = new PlayerEntity(new Date() + "", new Date() + "", playerName, new Date() + "", new Date());
                    playerViewModel.insert(playerEntity);

                    // Success message
                    replyIntent.putExtra(EXTRA_REPLY, getResources().getString(R.string.player_added, playerName));
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
