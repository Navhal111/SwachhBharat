package com.india.swachhbharat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Printer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.india.dataset.Complain;

public class ComplainRegister extends AppCompatActivity implements LocationListener {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button submit;
    EditText description;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    TextView tyextmain;
    Boolean getlocation = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_register);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        submit = (Button) findViewById(R.id.button);
        description = (EditText) findViewById(R.id.description);
        tyextmain = (TextView) findViewById(R.id.tyextmain);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LocationTrack locationTrack = new LocationTrack(ComplainRegister.this);

                if (locationTrack.canGetLocation()) {

                    double longitude = locationTrack.getLongitude();
                    double latitude = locationTrack.getLatitude();

                    Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                } else {

                    locationTrack.showSettingsAlert();
                }


                int result = ComplainRegister.this.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                if(result == PackageManager.PERMISSION_GRANTED){

                    if(getlocation){
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        writeNewUser(currentUser.getEmail().replace("@", "").replace(".", ""), "new", "address", currentUser.getDisplayName());
                    }else{
                        Toast.makeText(ComplainRegister.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, ComplainRegister.this);
                    }


                }else{
                    Toast.makeText(ComplainRegister.this, "First Enable Location Permission", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(ComplainRegister.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.ACCESS_FINE_LOCATION},
                            9000);
                }

            }
        });

        int result = this.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if(result == PackageManager.PERMISSION_GRANTED){
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, this);

        }else{

            ActivityCompat.requestPermissions(this,
            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.ACCESS_FINE_LOCATION},
                    9000);
        }

        statusCheck();

    }


    private void writeNewUser(String emainid,String des, String adress,String username)  {
        Complain user = new Complain(des, adress,username,false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("quiz").push().getKey();
        mDatabase.child("complains").child(emainid).child(key).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("sucess","suess");
                Toast.makeText(ComplainRegister.this, "Add Complain Success Fully", Toast.LENGTH_SHORT).show();
                description.setText("");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("sucess",e.toString());
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.e("getLatitude" ,Double.toString(location.getLatitude()));
        Log.e("getLongitude" , Double.toString(location.getLongitude()));

        getlocation = true;
        tyextmain.setText("getLatitude:  - "+Double.toString(location.getLatitude())+"\n\n"+"getLongitude"+Double.toString(location.getLongitude()));

        Toast.makeText(ComplainRegister.this, "Get Current Location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

        Toast.makeText(ComplainRegister.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        boolean canUseExternalStorage = false;

        switch (requestCode) {
            case 9000: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canUseExternalStorage = true;
                }

                if (!canUseExternalStorage) {
                    Toast.makeText(ComplainRegister.this, "Cannot use this feature without requested permission", Toast.LENGTH_SHORT).show();
                } else {
                    // user now provided permission
                    // perform function for what you want to achieve
                }
            }
        }
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(ComplainRegister.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
            startActivity( new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
