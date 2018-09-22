package com.carsecurity.carsecurity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.carsecurity.carsecurity.api.Networking;
import com.carsecurity.carsecurity.model.DataModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    //    LocationManager locationManager;
    //    private ThingSpeakService mThingSpeakService;

    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    ImageView imageView_logo;
    DatabaseReference Xreference;
    String Image;
    TextView txName, txEmail;
    FloatingActionButton sendButton;
    LatLng mLatLng;
    LatLng gpsLatlng;
    FusedLocationProviderClient mFusedLocationProviderClient;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (this != null) {
                    mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        if (savedInstanceState!=null){
            requestDirection(mLatLng,gpsLatlng);

        }

        Xreference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        checkLocationPermission();
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        imageView_logo = header.findViewById(R.id.imageView_logo);
        txName = header.findViewById(R.id.txName);
        txEmail = header.findViewById(R.id.txEmail);
        Xreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Image = String.valueOf(dataSnapshot.child("imgs").getValue());
                String str_Name = String.valueOf(dataSnapshot.child("Name").getValue());
                String str_Email = String.valueOf(dataSnapshot.child("EmailAddress").getValue());
                txEmail.setText(str_Email);
                txName.setText(str_Name);

                if (!Image.equals("") | !Image.isEmpty()) {
                    Picasso.with(MapsActivity.this).load(Image).into(imageView_logo);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("LAT_KEY",String.valueOf(gpsLatlng.latitude));
        outState.putString("LNG_KEY",String.valueOf(gpsLatlng.longitude));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3 * 10000);
//        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                checkLocationPermission();
            }
        }
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        mMap.setMyLocationEnabled(true);
    }

//

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_menu_mylocation)
                        .setTitle("Allow Location")
                        .setMessage("Please  Allow Location To use this program Go TO settings/app/Name Project/permissions and Allow Location")
                        .setPositiveButton(" Ok ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }

    }

    private void loadData() {

        Retrofit mRetrofit = new Retrofit.Builder().baseUrl(Networking.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        Networking mNetworking = mRetrofit.create(Networking.class);
        mNetworking.getlocation().enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(Call<DataModel> call, Response<DataModel> response) {
                gpsLatlng = new LatLng(Double.parseDouble(response.body().getFeeds().get(0).getField1()), Double.parseDouble(response.body().getFeeds().get(0).getField2()));

                requestDirection(mLatLng,gpsLatlng);
            }

            @Override
            public void onFailure(Call<DataModel> call, Throwable t) {

            }
        });
    }

    public void requestDirection(final LatLng MyLocation, final LatLng ApiLocation) {
        if (MyLocation != null && ApiLocation != null) {
            GoogleDirection.withServerKey("")
                    .from(MyLocation)
                    .to(ApiLocation)
                    .transportMode(TransportMode.WALKING)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            if (direction.isOK()) {
                                Route route = direction.getRouteList().get(0);
                                mMap.addMarker(new MarkerOptions().position(MyLocation));
                                mMap.addMarker(new MarkerOptions().position(ApiLocation));
                                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                mMap.addPolyline(DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 5, Color.RED));
                                setCameraWithCoordinationBounds(route);
                            } else {
                                Toast.makeText(MapsActivity.this, direction.getStatus(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            Toast.makeText(MapsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        } else {
            Toast.makeText(this, R.string.locationNotSet, Toast.LENGTH_SHORT).show();
        }
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_help:
                startActivity(new Intent(MapsActivity.this, Help.class));
                drawerLayout.closeDrawers();
                break;

            case R.id.nav_edit_profile:
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_sign_out:
                mAuth.signOut();
                finish();
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
                drawerLayout.closeDrawers();
                break;
        }
        return true;
    }
}
