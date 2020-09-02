package com.example.appnghenhac.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appnghenhac.Bai_Hat;
import com.example.appnghenhac.MainActivity;
import com.example.appnghenhac.R;
import com.example.appnghenhac.adapter.Adapter_CapNhat;
import com.example.appnghenhac.service.Service_Nhac;

public class Fragment_Songabum extends Fragment {
    ImageView ql, threeDot;
    ListView lissongabum;
    Adapter_CapNhat adapter_capNhat ;
    Context context;

    public Fragment_Songabum(Context context) {
        this.context = context;
    }

    public Fragment_Songabum(int contentLayoutId, Context context) {
        super(contentLayoutId);
        this.context = context;
    }

    public Fragment_Songabum() {
    }

    public Fragment_Songabum(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View Songabum = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_song_abum, null);
        ql = Songabum.findViewById(R.id.ql);
        threeDot = Songabum.findViewById(R.id.threeDot);
        lissongabum = Songabum.findViewById(R.id.listsongalbum);
        ql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.popBackStack();
            }
        });
        adapter_capNhat= new Adapter_CapNhat(context,MainActivity.arrayList);
        lissongabum.setAdapter(adapter_capNhat);
        MainActivity.arrayList.clear();
        MainActivity.arrayList.addAll( MainActivity.getSongList(context));
        adapter_capNhat.notifyDataSetChanged();
        threeDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(container.getContext());
                dialog.setContentView(R.layout.activity_capnhat);
                Button btncapnhat = dialog.findViewById(R.id.btnCapnhat);

                btncapnhat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.arrayList.clear();
                        MainActivity.arrayList.addAll( MainActivity.getSongList(context));
                        adapter_capNhat.notifyDataSetChanged();
                        dialog.dismiss();
                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

            }
        });
        lissongabum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bai_Hat baiHat = MainActivity.arrayList.get(position);
                MainActivity.msname.setText(baiHat.TenBH);
                MainActivity.cs.setText(baiHat.Casi);
                if (MainActivity.service_nhac == null || MainActivity.service_nhac.mediaPlayer == null)
                {
                    Intent intent = new Intent(container.getContext(), Service_Nhac.class);
                    intent.putExtra("ID",baiHat.id);
                    if (baiHat.uri != null && !baiHat.uri.isEmpty())
                        intent.putExtra("uri",baiHat.uri);
                    else
                        intent.putExtra("uri","");
                    MainActivity.position = position;
                    container.getContext().bindService(intent,MainActivity.serviceConnection, Context.BIND_AUTO_CREATE);

                    MainActivity.ChuyenFragment(new Fragment_Playsong());
                    return;
                }
                if (MainActivity.service_nhac.mediaPlayer.isPlaying()){
                    MainActivity.service_nhac.playNextPre(position);
                    MainActivity.position = position;
                    MainActivity.ChuyenFragment(new Fragment_Playsong());
                    return;
                }
//                container.getContext().bindService(intent,MainActivity.serviceConnection,Context.BIND_AUTO_CREATE);
//
//                MainActivity.ChuyenFragment(new Fragment_Playsong());

            }
        });
        event();

        return Songabum;
    }
    public void event(){
        lissongabum.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    popupMenu();
                return true;
            }
        });

    }
    private void popupMenu(){
        PopupMenu pop = new PopupMenu(context, lissongabum);

        pop.getMenuInflater().inflate(R.menu.context, pop.getMenu());

        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Xoa:
                        break;
                }
                return false;
            }
        });

        pop.show();
    }

}
