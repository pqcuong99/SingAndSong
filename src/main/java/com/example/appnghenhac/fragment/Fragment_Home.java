package com.example.appnghenhac.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appnghenhac.Bai_Hat;
import com.example.appnghenhac.MainActivity;
import com.example.appnghenhac.R;
import com.example.appnghenhac.adapter.Adapter_CapNhat;
import com.example.appnghenhac.service.Service_Nhac;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fragment_Home extends Fragment {
    Button mymusic, onlinemusic;
    EditText txtsearch ;
    ImageView imgsearch;
    ListView listsearch;
    String domain = "https://photo-zmp3.zadn.vn/";
    Context context;

    public Fragment_Home(Context context) {
        this.context = context;
    }

    public Fragment_Home() {
    }

    public Fragment_Home(int contentLayoutId) {
        super(contentLayoutId);
    }

    public Fragment_Home(int contentLayoutId, Context context) {
        super(contentLayoutId);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View anhxa = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_home,null);

        mymusic = anhxa.findViewById(R.id.mymusic);
        onlinemusic = anhxa.findViewById(R.id.onlinemusic);
        txtsearch = anhxa.findViewById(R.id.txtsearch);
        imgsearch = anhxa.findViewById(R.id.imgsearch);
        listsearch = anhxa.findViewById(R.id.listsearch);
        final Adapter_CapNhat adapter_capNhat = new Adapter_CapNhat(container.getContext(),MainActivity.arrayList);
        listsearch.setAdapter(adapter_capNhat);
        mymusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.ChuyenFragment(new Fragment_Person(context));
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtsearch.getText().toString().isEmpty()){
                    Toast.makeText(context, "Nhập tên bài hát đi bạn", Toast.LENGTH_SHORT).show();
                    return;
                }
                MainActivity.arrayList.clear();

                MainActivity.position = 0;
                RequestQueue requestQueue = Volley.newRequestQueue(container.getContext());
                StringRequest stringRequest = new StringRequest("http://ac.mp3.zing.vn/complete?type=artist,song,key,code&num=500&query="+txtsearch.getText().toString().replace(" ","%20") ,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("result")){
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                JSONArray jsonArray1 = jsonObject1.getJSONArray("song");

                                for (int i =0 ; i<jsonArray1.length();i++){
                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                                    Bai_Hat bai_hat = new Bai_Hat(jsonObject2.getString("name"),jsonObject2.getString("artist"),jsonObject2.getString("id"));
                                    bai_hat.setImage(domain+jsonObject2.getString("thumb"));
                                    MainActivity.arrayList.add(bai_hat);
                                }
                            }

                            adapter_capNhat.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(stringRequest);
            }
        });
        listsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bai_Hat bai_hat = MainActivity.arrayList.get(position);
                MainActivity.position = position;

                if (MainActivity.service_nhac == null || MainActivity.service_nhac.mediaPlayer == null)
                {
                    Intent intent = new Intent(container.getContext(), Service_Nhac.class);
                    intent.putExtra("ID",bai_hat.id);
                    container.getContext().bindService(intent,MainActivity.serviceConnection, Context.BIND_AUTO_CREATE);
                    MainActivity.ChuyenFragment(new Fragment_Playsong());
                    return;
                }
                if (MainActivity.service_nhac.mediaPlayer.isPlaying()){
                    MainActivity.ChuyenFragment(new Fragment_Playsong());
                    MainActivity.service_nhac.playNextPre(MainActivity.position);
                    return;
                }
            }
        });

        return anhxa;

    }
}
