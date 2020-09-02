package com.example.appnghenhac.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.appnghenhac.Bai_Hat;
import com.example.appnghenhac.EventChangeMusic;
import com.example.appnghenhac.MainActivity;
import com.example.appnghenhac.R;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class Fragment_Playsong extends Fragment {
    ImageView quaylai, threeDot,download,random,pre,play,next,repeat_all;
    public SeekBar seekBar;
    public static TextView tvtimechay,tvthoiluong , musicname , casi;
    boolean cuongabcut=false;
    public static ImageView imagesong;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View Playsong = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_playsong,null);
        imagesong = Playsong.findViewById(R.id.imagesong);
        quaylai = Playsong.findViewById(R.id.quaylai);

        random = Playsong.findViewById(R.id.random);
        pre = Playsong.findViewById(R.id.pre);
        next = Playsong.findViewById(R.id.next);
        repeat_all = Playsong.findViewById(R.id.repeat_all);
        play =  Playsong.findViewById(R.id.play);
        seekBar = Playsong.findViewById(R.id.seekbar);
        tvtimechay = Playsong.findViewById(R.id.timechay);
        tvthoiluong = Playsong.findViewById(R.id.thoiluong);
        musicname = Playsong.findViewById(R.id.musicname);
        casi = Playsong.findViewById(R.id.casi);
        //seekBar.setProgress(MainActivity.service_nhac.mediaPlayer.getCurrentPosition());
        MainActivity.ln.setVisibility(View.GONE);
        quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.popBackStack();
                MainActivity.ln.setVisibility(View.VISIBLE);
            }
        });

        tvthoiluong.setText(String.format("%02d:%02d",MainActivity.dua/60/1000,(MainActivity.dua/1000)%60));
        demgiay();
        musicname.setText(MainActivity.arrayList.get(MainActivity.position).TenBH);
        casi.setText(MainActivity.arrayList.get(MainActivity.position).Casi);
        if(MainActivity.arrayList.get(MainActivity.position).image != null && !MainActivity.arrayList.get(MainActivity.position).image.isEmpty() ){
            Picasso.get().load(MainActivity.arrayList.get(MainActivity.position).image).error(R.drawable.music4u).into(imagesong);
        }else {
            imagesong.setImageResource(R.drawable.music4u);
        }


        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.position = new Random().nextInt(MainActivity.arrayList.size());
                Bai_Hat bai_hat = MainActivity.arrayList.get(MainActivity.position);
                MainActivity.service_nhac.mediaPlayer.reset();
                if (bai_hat.uri != null && !bai_hat.uri.isEmpty()){
                    MainActivity.service_nhac.playoffline(bai_hat.uri);
                }else {
                    MainActivity.service_nhac.play(bai_hat.id);
                }
                seekBar.setProgress(0);
                seekBar.setMax(MainActivity.dua);
                tvthoiluong.setText(String.format("%02d:%02d",MainActivity.dua/60/1000,(MainActivity.dua/1000)%60));
                musicname.setText(bai_hat.TenBH);
                Picasso.get().load(MainActivity.arrayList.get(MainActivity.position).image).error(R.drawable.music4u).into(imagesong);
                casi.setText(bai_hat.Casi);
            }
        });
// Lùi bài ở vị trí posiontion
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.position ==0){
                    MainActivity.position = MainActivity.arrayList.size()-1;
                }else
                    MainActivity.position--;
                MainActivity.service_nhac.playNextPre(MainActivity.position);
                seekBar.setProgress(0);
                seekBar.setMax(MainActivity.dua);
                Bai_Hat bai_hat = MainActivity.arrayList.get(MainActivity.position);
                if(bai_hat.image == null || bai_hat.image.isEmpty()){
                    Fragment_Playsong.imagesong.setImageResource(R.drawable.music4u);
                }else {
                    Picasso.get().load(bai_hat.image).error(R.drawable.music4u).into(Fragment_Playsong.imagesong);
                }
                tvthoiluong.setText(String.format("%02d:%02d",MainActivity.dua/60/1000,(MainActivity.dua/1000)%60));
            }
        });
        // next bài
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (MainActivity.position == MainActivity.arrayList.size()-1){
                    MainActivity.position =0;
                }else
                MainActivity.position++;
                Bai_Hat bai_hat = MainActivity.arrayList.get(MainActivity.position);
                MainActivity.service_nhac.playNextPre(MainActivity.position);
                seekBar.setProgress(0);
                seekBar.setMax(MainActivity.dua);
                if(bai_hat.image == null || bai_hat.image.isEmpty()){
                    Fragment_Playsong.imagesong.setImageResource(R.drawable.music4u);
                }else {
                    Picasso.get().load(bai_hat.image).error(R.drawable.music4u).into(Fragment_Playsong.imagesong);
                }
                tvthoiluong.setText(String.format("%02d:%02d",MainActivity.dua/60/1000,(MainActivity.dua/1000)%60));
                musicname.setText(bai_hat.TenBH);
                casi.setText(bai_hat.Casi);
            }
        });
        repeat_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.service_nhac.mediaPlayer.isLooping()){
                    MainActivity.service_nhac.mediaPlayer.setLooping(false);
                    repeat_all.setImageResource(R.drawable.repeat);
                }else{
                    MainActivity.service_nhac.mediaPlayer.setLooping(true);
                    repeat_all.setImageResource(R.drawable.ic_repeat_one_black_24dp);
                }



            }
        });

        //  media có đang chạy hay không
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.service_nhac.mediaPlayer.isPlaying()){
                    MainActivity.service_nhac.mediaPlayer.pause();
                    play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }else{
                    MainActivity.service_nhac.mediaPlayer.start();
                    play.setImageResource(R.drawable.pause_64);
                }


            }
        });

        seekBar.setMax(MainActivity.dua);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("TAG", "onStartTrackingTouch: ");
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                if(seekBar.getProgress() >= seekBar.getMax()){
                    MainActivity.position++;
                    MainActivity.service_nhac.playNextPre(MainActivity.position);
                }
                MainActivity.service_nhac.setOnChange(new EventChangeMusic() {
                    @Override
                    public void onChange() {
                        seekBar.setProgress(0);
                        seekBar.setMax(MainActivity.dua);
                        tvthoiluong.setText(String.format("%02d:%02d",MainActivity.dua/60/1000,(MainActivity.dua/1000)%60));
                    }
                });
                MainActivity.service_nhac.mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        if(MainActivity.service_nhac != null){
            MainActivity.service_nhac.setOnChange(new EventChangeMusic() {
                @Override
                public void onChange() {
                    System.out.println("ok");
                    seekBar.setProgress(0);
                    seekBar.setMax(MainActivity.dua);
                    tvthoiluong.setText(String.format("%02d:%02d",MainActivity.dua/60/1000,(MainActivity.dua/1000)%60));
                }
            });
        }
        return Playsong;
    }
    public void demgiay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(cuongabcut)
                    return;
                int dua = MainActivity.service_nhac.mediaPlayer.getCurrentPosition();
                seekBar.setProgress(dua);
                tvtimechay.setText(String.format("%02d:%02d",dua/60/1000,(dua/1000)%60));
                demgiay();
            }
        },1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cuongabcut=true;
    }

}
