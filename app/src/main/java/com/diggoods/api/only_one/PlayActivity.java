package com.diggoods.api.only_one;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gyf.immersionbar.ImmersionBar;

import java.io.IOException;
import java.security.Key;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener{

    private MediaPlayer player;
    private ImageView start;
    private ImageView pause;
    private SnowView snow;
    private RainView rain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init();

        ImageView icon = findViewById(R.id.play_icon);
        SimpleDraweeView icons = findViewById(R.id.play_icons);
        TextView title = findViewById(R.id.play_title);
        TextView name = findViewById(R.id.play_name);
        rain = findViewById(R.id.play_rain);
        snow = findViewById(R.id.play_snow);
        start = findViewById(R.id.play_start);
        pause = findViewById(R.id.play_pause);
        pause.setVisibility(View.VISIBLE);
        start.setVisibility(View.GONE);
        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        Intent intent = getIntent();
        String author = intent.getStringExtra("author");
        String pic = intent.getStringExtra("pic");
        String url = intent.getStringExtra("url");
        String title1 = intent.getStringExtra("title");
        name.setText(author);
        title.setText(title1);
        icons.setImageURI(pic);
        player = new MediaPlayer();
        try {
            player.setDataSource(url);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.stop();
                player.release();
                player = null;
            }
        });

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_start:
                snow.setVisibility(View.VISIBLE);
                rain.setVisibility(View.VISIBLE);
                player.start();
                pause.setVisibility(View.VISIBLE);
                start.setVisibility(View.GONE);
                break;
            case R.id.play_pause:
                snow.setVisibility(View.GONE);
                rain.setVisibility(View.GONE);
                player.pause();
                start.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                break;
        }
    }




}
