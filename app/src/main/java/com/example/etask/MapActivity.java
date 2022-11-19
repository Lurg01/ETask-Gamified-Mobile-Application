package com.example.etask;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback  {
    GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        // GET LATITUDE AND LONGITUDE FROM INTENT
        String location = getIntent().getStringExtra("Val");
        Toast.makeText(getApplicationContext(), ""+ location, Toast.LENGTH_SHORT).show();
        String[] text = location.split(", ");
        double lat = Double.parseDouble(text[0]);
        double lng = Double.parseDouble(text[1]);
        LatLng ph = new LatLng(13.5824117,121.5673412);
        map.moveCamera(CameraUpdateFactory.newLatLng(ph));
        float zoomLevel = 5.0f;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ph, zoomLevel));
        LatLng loc = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions().position(loc).title("Philippines"));
    }
}