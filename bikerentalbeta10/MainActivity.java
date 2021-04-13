package com.example.bikerentalbeta10;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.WindowInsetsAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ServData{
    public static int n = 0;
    public double lat, lang;
    public  String vehno, name;
    ServData(double lat,double lang ,String name , String vehno){
        this.lat = lat;
        n++;
        this.lang = lang;
        this.name = name;
        this.vehno = vehno;
    }

}
public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    ServData servData[] = new ServData[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
    }

    public void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MainActivity.this);
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.addMarker(markerOptions);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 40 degrees
                .build();                   // Creates a CameraPosition from the builderf
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        servData[0] = new ServData(mathFunction2(currentLocation.getLatitude()),mathFunction(currentLocation.getLongitude()),"Jason  ","KA53 HC 4141 ,Ph. 8784189328 ");
        servData[1] = new ServData(mathFunction2(currentLocation.getLatitude()),mathFunction(currentLocation.getLongitude()),"Alex  ","KA23 AD 3213 ,Ph. 6138852982 ");
        servData[2] = new ServData(mathFunction2(currentLocation.getLatitude()),mathFunction(currentLocation.getLongitude()),"Mav  ","KA23 AD 3223 ,Ph. 6146899087 ");
        servData[3] = new ServData(mathFunction2(currentLocation.getLatitude()),mathFunction(currentLocation.getLongitude()),"Derick  ","KA03 AD 5423 ,Ph. 7649299892");
        listeners(googleMap);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }
    public void listeners(GoogleMap googleMap){
        ImageView imageView = findViewById(R.id.searchButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                LatLng lng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                bounds.include(lng);
                for(i=0;i<4;i++){
                    LatLng latLng1 = new LatLng(servData[i].lat,servData[i].lang);
                    googleMap.addMarker(new MarkerOptions().position(latLng1).title(servData[i].name + servData[i].vehno));
                    bounds.include(latLng1);
                }
                LatLngBounds bounds1 = bounds.build();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds1,0));
                Toast.makeText(getApplicationContext(),"Found "+ i + " results.",Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Found "+ i +" results.");
                builder.setMessage("Click on marker to show Details.").setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        ImageView help = findViewById(R.id.helpButton);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Help");
                builder.setMessage("Please Call our Customer service helpline for issues:\n +91 8747041671 \n +91 7756428543").setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
    public double mathFunction(double ini){
        Random random = new Random();
        int perc = random.nextInt(5);
        double fin = ini-(((perc+0.5)/3000)*ini);
        return fin;
    }
    public double mathFunction2(double ini){
        Random random = new Random();
        int perc = random.nextInt(10);
        double fin = ini-(((perc+0.5)/650)*ini);
        return fin;
    }

}