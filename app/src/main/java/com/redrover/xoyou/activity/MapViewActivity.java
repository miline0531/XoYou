package com.redrover.xoyou.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.request.ActionRequestStoreMongStoryGetLocationColor;
import com.redrover.xoyou.network.response.DefaultResult;


public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback {
    private String Adr;
    private String Dou;
    private String Lat;
    private String Lng;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
//        binding = ActivityMapviewBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        initView();
        googleMapInit();
    }

    private void initView() {
        Adr = getIntent().getStringExtra("Adr");
        Dou = getIntent().getStringExtra("Dou");
        Dou = getIntent().getStringExtra("Dou");
        Lat = getIntent().getStringExtra("Lat");
        Lng = getIntent().getStringExtra("Lng");

        getLocationColor();

    }


    private void googleMapInit(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setEnabled(false);
        mapFragment.getView().setClickable(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Float fLng = Float.parseFloat(Lng);
        Float fLat = Float.parseFloat(Lat);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(fLat, fLng);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMaxZoomPreference(19);
        mMap.setMinZoomPreference(19);
    }

    ArrayList<String> locationColorList;
    public void getLocationColor(){
        ActionRuler.getInstance().addAction(new ActionRequestStoreMongStoryGetLocationColor(this, Lat,Lng,"1", new ActionResultListener<DefaultResult>() {
            @Override
            public void onResponseResult(DefaultResult response) {
                try {
                    String result = response.getData();

//                    JSONArray job = new JSONArray(result);
//                    JSONObject jsonObject = job.getJSONObject(0);
                    locationColorList = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(result);
//                    Log.v("ifeelbluu","jsonObject : " + result);
//                    String btnM = jsonObject.getString("btnM");
//                    String btnNW = jsonObject.getString("btnNW");
//                    String btnN = jsonObject.getString("btnN");
//                    String btnNE = jsonObject.getString("btnNE");
//                    String btnE = jsonObject.getString("btnE");
//                    String btnSE = jsonObject.getString("btnSE");
//                    String btnS = jsonObject.getString("btnS");
//                    String btnSW = jsonObject.getString("btnSW");
//                    String btnW = jsonObject.getString("btnW");

                    locationColorList.add(jsonObject.getString("btnM"));
                    locationColorList.add(jsonObject.getString("btnNW"));
                    locationColorList.add(jsonObject.getString("btnN"));
                    locationColorList.add(jsonObject.getString("btnNE"));
                    locationColorList.add(jsonObject.getString("btnE"));
                    locationColorList.add(jsonObject.getString("btnSE"));
                    locationColorList.add(jsonObject.getString("btnS"));
                    locationColorList.add(jsonObject.getString("btnSW"));
                    locationColorList.add(jsonObject.getString("btnW"));

                    setColor();
                } catch (Exception e) {
                    Log.e("TEST","에러" + e.getMessage());
                    e.printStackTrace();
                }
            }
            @Override
            public void onResponseError(String message) {
                Log.d("TEST","에러 : " + message);
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    public void setColor(){
        findViewById(R.id.area0).setBackgroundColor(Color.parseColor(locationColorList.get(0)));
        findViewById(R.id.area1).setBackgroundColor(Color.parseColor(locationColorList.get(1)));
        findViewById(R.id.area2).setBackgroundColor(Color.parseColor(locationColorList.get(2)));
        findViewById(R.id.area3).setBackgroundColor(Color.parseColor(locationColorList.get(3)));
        findViewById(R.id.area4).setBackgroundColor(Color.parseColor(locationColorList.get(4)));
        findViewById(R.id.area5).setBackgroundColor(Color.parseColor(locationColorList.get(5)));
        findViewById(R.id.area6).setBackgroundColor(Color.parseColor(locationColorList.get(6)));
        findViewById(R.id.area7).setBackgroundColor(Color.parseColor(locationColorList.get(7)));
        findViewById(R.id.area8).setBackgroundColor(Color.parseColor(locationColorList.get(8)));
        ((TextView)findViewById(R.id.txt_addr)).setText(Adr);
    }

    public int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.argb(90,r, g, b);
    }

}