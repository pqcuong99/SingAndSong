package com.example.appnghenhac.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appnghenhac.MainActivity;
import com.example.appnghenhac.R;

public class Fragment_Person extends Fragment {
    ImageView imgbacklai;
    LinearLayout baihat;
    Context context;

    public Fragment_Person(Context context) {
        this.context = context;
    }

    public Fragment_Person() {
    }

    public Fragment_Person(int contentLayoutId) {
        super(contentLayoutId);
    }

    public Fragment_Person(int contentLayoutId, Context context) {
        super(contentLayoutId);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View person = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_person,null);
        imgbacklai = person.findViewById(R.id.imgbacklai);
        baihat = person.findViewById(R.id.baihat);

        imgbacklai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.popBackStack();
            }
        });
        baihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.ChuyenFragment(new Fragment_Songabum(context));
            }
        });
        return person;
    }
}
