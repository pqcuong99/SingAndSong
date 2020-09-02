package com.example.appnghenhac.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appnghenhac.Bai_Hat;
import com.example.appnghenhac.MainActivity;
import com.example.appnghenhac.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_CapNhat extends BaseAdapter {
    Context context;
    ArrayList<Bai_Hat> arrayList;

    public Adapter_CapNhat(Context context, ArrayList<Bai_Hat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Bai_Hat> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Bai_Hat> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder viewHoder;
        if (convertView == null){
            viewHoder = new ViewHoder();
            convertView = LayoutInflater.from(context).inflate(R.layout.danhsachbaihat,null);
            viewHoder.tvBaiHat = convertView.findViewById(R.id.tvBaiHat);
            viewHoder.tvCasi = convertView.findViewById(R.id.tvCasi);
            viewHoder.tvID = convertView.findViewById(R.id.tvID);
//            LinearLayout dsplay = convertView.findViewById(R.id.dsplay);
            viewHoder.imgnhac = convertView.findViewById(R.id.imgnhac);
            convertView.setTag(viewHoder);
        }else{
            viewHoder = (ViewHoder) convertView.getTag();
        }


        Bai_Hat bai_hat = arrayList.get(position);
        viewHoder.tvBaiHat.setText(bai_hat.TenBH);
        viewHoder.tvCasi.setText(bai_hat.Casi);
        viewHoder.tvID.setText(bai_hat.id);
        if(bai_hat.getImage() != null){
            Picasso.get().load(bai_hat.image).into(viewHoder.imgnhac);
        }
        return convertView;
    }

    public class ViewHoder{
        TextView tvBaiHat ;
        TextView tvCasi;
        TextView tvID;
        ImageView imgnhac;

    }
}
