package com.rainbow6.siege.r6_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rainbow6.siege.r6_app.R;

public class NewPlayerActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "";

    private EditText mEditPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);

        mEditPlayerView = findViewById(R.id.edit_player);

        final Button button = findViewById(R.id.button_add_player);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditPlayerView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String playerName = mEditPlayerView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, playerName);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
