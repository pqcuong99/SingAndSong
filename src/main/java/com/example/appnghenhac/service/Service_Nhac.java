package com.example.appnghenhac.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.appnghenhac.Bai_Hat;
import com.example.appnghenhac.BroadcastReceiverCustom;
import com.example.appnghenhac.EventChangeMusic;
import com.example.appnghenhac.MainActivity;
import com.example.appnghenhac.R;
import com.example.appnghenhac.fragment.Fragment_Playsong;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class Service_Nhac extends Service {
    private static final int NOTIFICATION_ID =1 ;
    public static final String ACTION_PLAY = "2" ;
    public static final String ACTION_PAUSE = "3";
    public static final String ACTION_NEXT = "4";
    public static final String ACTION_PREVIOUS = "5";
    public static final String ACTION_STOP ="6" ;
    public MediaPlayer mediaPlayer;
    private final IBinder iBinder = new MaiBinder();

    public EventChangeMusic onChange;

    public EventChangeMusic getOnChange() {
        return onChange;
    }

    public void setOnChange(EventChangeMusic onChange) {
        this.onChange = onChange;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (MainActivity.service_nhac.mediaPlayer.isLooping()){
                    return;
                }
                if (MainActivity.position == MainActivity.arrayList.size()-1){
                    MainActivity.position =0;
                }else {
                    MainActivity.position++;
                }
                MainActivity.service_nhac.playNextPre(MainActivity.position);
            }

        });
        if (intent.getStringExtra("uri") != null && !intent.getStringExtra("uri").isEmpty() ){
            playoffline(intent.getStringExtra("uri"));
        }else{
            play(intent.getStringExtra("ID"));
        }
        Bai_Hat bai_hat = MainActivity.arrayList.get(MainActivity.position);
        MainActivity.msname.setText(bai_hat.getNameShort());
        MainActivity.cs.setText(bai_hat.getCaSiShort());
        if(bai_hat.image == null || bai_hat.image.isEmpty()){
            MainActivity.imagesong.setImageResource(R.drawable.music4u);
        }else {
            Picasso.get().load(bai_hat.image).error(R.drawable.music4u).into(MainActivity.imagesong);
        }
        createNotification(mediaPlayer.isPlaying(),MainActivity.arrayList.get(MainActivity.position));
        return iBinder;
    }
    public void playNextPre(int position){
        if (position >= MainActivity.arrayList.size()-1){
            position = 0;
        }else if(position<0){
            position = MainActivity.arrayList.size()-1;
        }
        Bai_Hat bai_hat = MainActivity.arrayList.get(position);
        MainActivity.msname.setText(bai_hat.getNameShort());
        MainActivity.cs.setText(bai_hat.getCaSiShort());
        if(bai_hat.image == null || bai_hat.image.isEmpty()){
            MainActivity.imagesong.setImageResource(R.drawable.music4u);
            Fragment_Playsong.imagesong.setImageResource(R.drawable.music4u);
        }else {
            Picasso.get().load(bai_hat.image).error(R.drawable.music4u).into(MainActivity.imagesong);
            Picasso.get().load(bai_hat.image).error(R.drawable.music4u).into(Fragment_Playsong.imagesong);
        }

        Fragment_Playsong.musicname.setText(bai_hat.getTenBH());
        Fragment_Playsong.casi.setText(bai_hat.getCasi());
        Log.d("TAG", "ggggggggggggggggggggggg: ");
        mediaPlayer.reset();

        if (bai_hat.uri != null && !bai_hat.uri.isEmpty() ){
            playoffline(bai_hat.uri);
        }else{
            play(bai_hat.id);
        }
        MainActivity.position = position;
        if(onChange != null)
            onChange.onChange();
        createNotification(mediaPlayer.isPlaying(),bai_hat);
    }
    public void playoffline(String uri){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, Uri.parse(uri));
            mediaPlayer.prepare();
            mediaPlayer.start();
            MainActivity.dua = mediaPlayer.getDuration();
        }catch (Exception ex){
            Log.d("TAG", "playoffline: "+ex.getMessage());
        }
    }
    public void play(String ID){
        try {
            mediaPlayer.reset();
            String url ="http://api.mp3.zing.vn/api/streaming/audio/"+ID+"/128";
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
            MainActivity.dua = mediaPlayer.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }
    public class MaiBinder extends Binder{

        public Service_Nhac getService(){
            return  Service_Nhac.this;
        }
    }
    public void createNotification(boolean isPlay,Bai_Hat bai_hat) {
        PendingIntent playIntent;
        int icon_play;
        if (isPlay) {
            playIntent = playbackAction(1);
            icon_play = R.drawable.pause_64;
        } else {
            playIntent = playbackAction(0);
            icon_play = R.drawable.ic_play_arrow_black_24dp;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID123456")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setShowWhen(false)
                .setOngoing(isPlay)
                .addAction(R.drawable.back_64, "Previous", playbackAction(3))
                .addAction(icon_play, "Pause", playIntent)
                .addAction(R.drawable.next_64, "next", playbackAction(2))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                )
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(bai_hat.getTenBH())
                .setContentText(bai_hat.getCasi());

        if(!isPlay){
            builder.setDeleteIntent(playbackAction(4));
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, BroadcastReceiverCustom.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getBroadcast(this, actionNumber, playbackAction, PendingIntent.FLAG_UPDATE_CURRENT);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getBroadcast(this, actionNumber, playbackAction, PendingIntent.FLAG_UPDATE_CURRENT);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getBroadcast(this, actionNumber, playbackAction, PendingIntent.FLAG_UPDATE_CURRENT);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getBroadcast(this, actionNumber, playbackAction, PendingIntent.FLAG_UPDATE_CURRENT);
            case 4:
                // Previous track
                playbackAction.setAction(ACTION_STOP);
                return PendingIntent.getBroadcast(this, actionNumber, playbackAction, PendingIntent.FLAG_UPDATE_CURRENT);
            default:
                break;
        }
        return null;
    }


}
