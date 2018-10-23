package com.cortado.geolocator;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class BlankFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    Polyline line;
    private static final String TEXT =  "Data";
    private ArrayList<String> SuperData = new ArrayList<> ();


    public BlankFragment(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        SuperData = getArguments().getStringArrayList(TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_blank, container, false);
        //SupportMapFragment mapFragment = (SupportMapFragment)getFragmentManager ().findFragmentById (R.id.map);
        //mapFragment.getMapAsync (this);
        return mView;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        if( isGooglePlayServicesAvailable(getActivity ())) {
            mMapView = (MapView) mView.findViewById (R.id.map);
            if (mMapView != null) {
                mMapView.onCreate (null);
                mMapView.onResume ();
                mMapView.getMapAsync (this);
            }
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        List<LatLng> markers = new ArrayList<LatLng>();
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        int ent =1;
        for(int i = 0; i<SuperData.size ();i++){
            LatLng temp = new LatLng(Double.parseDouble(SuperData.get(i + 2)),Double.parseDouble(SuperData.get(i + 1)));
            googleMap.addMarker(new MarkerOptions()
                    .position(temp)
                    .title("Entry: " + String.valueOf(ent) + " at Time= " + SuperData.get(i)));
            ent++;
            i = i + 2;
            markers.add(temp);
        }
        PolylineOptions options = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
        for (int i = 0; i < markers.size(); i++) {
            LatLng point = markers.get(i);
            options.add(point);
        }
        line = googleMap.addPolyline(options);
    }
    public static BlankFragment newInstance(ArrayList<String> data) {
        BlankFragment fragment = new BlankFragment ();
        Bundle args = new Bundle();
        args.putStringArrayList(TEXT, data);
        fragment.setArguments (args);
        return fragment;
  }
    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }
}
