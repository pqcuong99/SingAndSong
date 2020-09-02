package com.example.appnghenhac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appnghenhac.service.Service_Nhac;
import com.example.appnghenhac.fragment.Fragment_Home;
import com.example.appnghenhac.fragment.Fragment_Loadding;
import com.example.appnghenhac.fragment.Fragment_Playsong;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView musicname,sn;
    ImageButton pre,play,next;

    LinearLayout linearLayout;
    public static FragmentManager fragmentManager;
    public static Service_Nhac service_nhac;
    public  static ArrayList<Bai_Hat> arrayList;
    public static int position;
    public static  int dua;
    private static final String TAG = "MainActivity";
    private int MY_PERMISSIONS_REQUEST_READ_STORAGE = 501;
    public static  LinearLayout ln;
    public static TextView msname,cs;
    public static ImageButton luibai,playbai,nextbai;
    public static LinearLayout lnquaylai;
    public static ImageView imagesong;
    public static boolean isPlay = false;
    public BroadcastReceiver broadcastReceiverCustom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msname = findViewById(R.id.msname);
        cs = findViewById(R.id.cs);
        luibai = findViewById(R.id.luibai);
        playbai = findViewById(R.id.playbai);
        nextbai = findViewById(R.id.nextbai);
        lnquaylai = findViewById(R.id.lnquaylai);
        imagesong = findViewById(R.id.imagesong);
        fragmentManager = getSupportFragmentManager();
        arrayList = new ArrayList<>();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frgmain,new Fragment_Loadding());
        if (isReadStoragePermissionGranted()){

        }
        fragmentTransaction.commit();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ChuyenFragment(new Fragment_Home(MainActivity.this));
                ln.setVisibility(View.VISIBLE);
            }

        },2500);

        MainActivity.ln = findViewById(R.id.footer);
        luibai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.position == 0){
                    MainActivity.position = MainActivity.arrayList.size()-1;
                }else {
                    MainActivity.position--;
                }
                MainActivity.service_nhac.mediaPlayer.reset();
                MainActivity.service_nhac.playNextPre(position);
            }
        });
        playbai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.service_nhac.mediaPlayer.isPlaying()){
                    MainActivity.service_nhac.mediaPlayer.pause();
                    playbai.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }else {
                    MainActivity.service_nhac.mediaPlayer.start();
                    playbai.setImageResource(R.drawable.pause_64);
                }
            }
        });
        nextbai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.position == MainActivity.arrayList.size()-1){
                    MainActivity.position = 0;
                }else {
                    MainActivity.position++;
                }
                Bai_Hat bai_hat = MainActivity.arrayList.get(position);
                MainActivity.service_nhac.mediaPlayer.reset();
                MainActivity.service_nhac.playNextPre(position);

            }
        });
        lnquaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.ChuyenFragment(new Fragment_Playsong());
            }
        });
        createNotificationChannel();

    }

    public  static void ChuyenFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frgmain,fragment);
        fragmentTransaction.addToBackStack(fragment.getTag());
        fragmentTransaction.commit();
    }
    public static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Service_Nhac.MaiBinder maiBinder = (Service_Nhac.MaiBinder) service;
            service_nhac = maiBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    public static ArrayList<Bai_Hat> getSongList(Context context){
        ArrayList list = new ArrayList<Bai_Hat>();
        //query external audio
        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);

            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                Uri uri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        thisId);
                list.add(new Bai_Hat(thisTitle, thisArtist,thisId+"" ,uri.toString()));
            }while (musicCursor.moveToNext());
        }
        return list;
    }
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"Permission is granted1");
                return true;
            } else {

                Log.d(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.d(TAG,"Permission is granted1");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 3:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                }else{
                }
                break;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CHANNEL_NAME";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID123456", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager.getNotificationChannel("CHANNEL_ID123456") == null) {
                Log.d("LQH_NOTIMN", "Chưa tạo channel");
                notificationManager.createNotificationChannel(channel);
            } else {
                Log.d("LQH_NOTIMN", "Đã tạo channel");
            }
        }
        broadcastReceiverCustom = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getStringExtra("action")){
                    case Service_Nhac.ACTION_NEXT:
                        position++;
                        service_nhac.playNextPre(position);
                        break;
                    case Service_Nhac.ACTION_PREVIOUS:
                        position--;
                        service_nhac.playNextPre(position);
                        break;
                    case Service_Nhac.ACTION_PLAY:
                        service_nhac.mediaPlayer.start();
                        service_nhac.createNotification(true,arrayList.get(position));
                        break;
                    case Service_Nhac.ACTION_PAUSE:
                        service_nhac.mediaPlayer.pause();
                        service_nhac.createNotification(false,arrayList.get(position));
                        break;

                }
            }
        };
        registerReceiver(broadcastReceiverCustom,new IntentFilter("track"));
    }
}
