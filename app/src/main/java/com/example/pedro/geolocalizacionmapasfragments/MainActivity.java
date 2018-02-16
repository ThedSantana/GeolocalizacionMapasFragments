package com.example.pedro.geolocalizacionmapasfragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback, LocationListener{

   //GEOLOCALIZACION
    private LocationManager locationManager;
    private Criteria criteria;
    private String mejorProveedor;
    double latitud;
    double longitud;
    MarkerOptions marcador;

    //GOOGLE MAPS
    GoogleMap mapa;
    SupportMapFragment fragmentMapa;
    LatLng posicion;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentMapa= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragMapa);
        fragmentMapa.getMapAsync(this);
       // getLocalizacion();
    }



    //METODO DESARROLLADO EN CLASE PARA CALCULAR GEOLOCALIZACION ASOCIADO A UN BOTON
    public void getLocalizacion(View v) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        mejorProveedor = String.valueOf(locationManager.getBestProvider(criteria, true));

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = locationManager.getLastKnownLocation(mejorProveedor);

        if(location!=null){
           latitud= location.getLatitude();
           longitud=location.getLongitude();
            posicion=new LatLng(latitud, longitud);
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15));
            marcador=new MarkerOptions().title("Mi geolocalizacion (lat: "+latitud+" long:"+longitud+")").position(posicion);
            mapa.addMarker(marcador);

        }
        else{
            Toast.makeText(getApplicationContext(), "Determinando nueva ubicación", Toast.LENGTH_LONG).show();
            locationManager.requestLocationUpdates(mejorProveedor, 1000, 0, this);
        }
    }

//metodos de INTERFACE geocalizacion
    @Override
    public void onLocationChanged(Location location) {

        latitud= location.getLatitude();
        longitud=location.getLongitude();
        posicion=new LatLng(latitud, longitud);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

//METODO DE GOOGLE MAPS
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa=googleMap;
        mapa.setMapType(googleMap.MAP_TYPE_SATELLITE); //Establecemos tipo de mapa. Podemos poner tb MAP_TYPE_SATELLITE, o MAP_TYPE_HYBRID  o MAP_TYPE_NORMAL
        mapa.getUiSettings().setZoomControlsEnabled(true); //Para botones de zoom el el mapa
        posicion=new LatLng(latitud, longitud);

    }
}
