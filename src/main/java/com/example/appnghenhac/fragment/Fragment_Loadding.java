package com.example.appnghenhac.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appnghenhac.MainActivity;
import com.example.appnghenhac.R;

public class Fragment_Loadding extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View loadding = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_loadding,null);

        MainActivity.fragmentManager.popBackStack();
        MainActivity.ln.setVisibility(View.GONE);
        return loadding;
    }
}
