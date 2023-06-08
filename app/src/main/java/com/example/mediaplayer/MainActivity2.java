package com.example.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    Button next;
    Button prev;
    Button play;
    TextView songname;
    SeekBar songbar;
    String s_name;
    static MediaPlayer mediaPlayer;
    int pos;
    ArrayList<File> mySongs;
    Thread updateseekbar;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        next = (Button)findViewById(R.id.next);
        prev = (Button) findViewById(R.id.previous);
        play = (Button) findViewById(R.id.pause);
        songbar = (SeekBar) findViewById(R.id.seekbar);
        songname = (TextView)findViewById(R.id.songname);
        songname.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        updateseekbar = new Thread(){
            @Override
            public void run() {
                int totalduration=mediaPlayer.getDuration();
                int currentpos= 0;
                while(currentpos<totalduration){
                    try{
                        sleep(500);
                        currentpos = mediaPlayer.getCurrentPosition();
                        songbar.setProgress(currentpos);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        };

        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        s_name = mySongs.get(pos).getName().toString();

        final String songsname = !intent.getStringExtra("songname").isEmpty() ? intent.getStringExtra("songname"): "adsd";

        songname.setText(songsname);
        songname.setSelected(true);

        pos = bundle.getInt("pos",0);
        Uri ur= Uri.parse(mySongs.get(pos).toString());

        mediaPlayer = MediaPlayer.create(getApplicationContext(),ur);
        mediaPlayer.start();
        songbar.setMax(mediaPlayer.getDuration());
        updateseekbar.start();

        songbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        songbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);

        songbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songbar.setMax(mediaPlayer.getDuration());
                if(mediaPlayer.isPlaying()){
                    play.setBackgroundResource(R.drawable.cover_art);
                    mediaPlayer.pause();
                }else{
                    play.setBackgroundResource(R.drawable.cover_art);
                    mediaPlayer.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                pos +=1;
                pos = (pos%mySongs.size());

                Uri ul= Uri.parse(mySongs.get(pos).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),ul);
                s_name = mySongs.get(pos).getName().toString();
                songname.setText(s_name);

                mediaPlayer.start();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                pos -=1;
                pos = (pos%mySongs.size());

                Uri ul= Uri.parse(mySongs.get(pos).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),ul);
                s_name = mySongs.get(pos).getName().toString();
                songname.setText(s_name);

                mediaPlayer.start();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
