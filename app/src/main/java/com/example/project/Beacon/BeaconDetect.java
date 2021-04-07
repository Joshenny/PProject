package com.example.project.Beacon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.register.LoginActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import static java.lang.Math.round;

public class BeaconDetect extends AppCompatActivity implements BeaconConsumer{
        protected static final String TAG = "BeaconDetect";
        private TextView beacontype;
        private TextView distancee;
        private BeaconManager beaconManager;
        private TextView datetimes;
        public BeaconLocationData beaconLocationData;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;
        public static final String FILTER_UUID ="FDA50693-A4E2-4FB1-AFCF-C6EB07647825";
        private static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
        DatabaseReference databaseReference;
        private Button logoutt;
        private Button vdata;
        private ImageView iv;
    BluetoothAdapter mBlueAdapter;
    private static final int REQUEST_ENABLE_BT = 0;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_beacon_detect);

            mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBlueAdapter.isEnabled()){
                showToast("please turn on Bluetooth");
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,REQUEST_ENABLE_BT);
             }

            permission();
            logoutact();
            initview();
            initbeacon();
            initdata();
            viewdata();
        }

        private void permission() {
         /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("This app needs background location access");
                            builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @TargetApi(23)
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                            PERMISSION_REQUEST_BACKGROUND_LOCATION);
                                }
                            });
                            builder.show();
                        } else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Functionality limited");
                            builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons in the background.  Please go to Settings -> Applications -> Permissions and grant background location access to this app.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }

                            });
                            builder.show();
                        }
                    }
                } else {
                    if (this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                PERMISSION_REQUEST_FINE_LOCATION);
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Functionality limited");
                        builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location access to this app.");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {
                            }
                        });
                        builder.show();
                    }

                }
            }
        }

            @Override
            public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
                switch (requestCode) {
                    case PERMISSION_REQUEST_FINE_LOCATION: {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "fine location permission granted");
                        } else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Functionality limited");
                            builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }

                            });
                            builder.show();
                        }
                        return;
                    }
                    case PERMISSION_REQUEST_BACKGROUND_LOCATION: {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "background location permission granted");
                        } else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Functionality limited");
                            builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons when in the background.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }

                            });
                            builder.show();
                        }
                        return;
                    }
                }*/


            if (ContextCompat.checkSelfPermission(BeaconDetect.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if (
                        ActivityCompat.shouldShowRequestPermissionRationale(BeaconDetect.this, Manifest.permission.ACCESS_FINE_LOCATION)){
                        ActivityCompat.requestPermissions(BeaconDetect.this,  new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                else{
                    ActivityCompat.requestPermissions(BeaconDetect.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(BeaconDetect.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
        private void logoutact(){
            logoutt=(Button) findViewById(R.id.butonlogout);
            Intent in = getIntent();
            String string = in.getStringExtra("message");
            logoutt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BeaconDetect.this);
                    builder.setTitle("Confirmation!").
                            setMessage("Sure to logout?");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(getApplicationContext(),
                                            MainActivity.class);
                                    startActivity(i);
                                }
                            });
                    builder.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder.create();
                    alert11.show();
                }
            });
        }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    //bluetooth is on

                    showToast("Bluetooth is on");
                }
                else {
                    showToast("Bluetooth is not open");
                    return;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

        private void viewdata(){
            vdata=(Button) findViewById(R.id.buttonview);
            vdata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mBlueAdapter.isEnabled()){
                        showToast("please turn on Bluetooth");
                        Intent x = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(x,REQUEST_ENABLE_BT);
                    }

                    else{
                        Intent intent=new Intent(BeaconDetect.this, beacondata.class);
                        startActivity(intent);
                    }
                }
            });
        }

       private void initview(){
            beacontype=(TextView) findViewById(R.id.beacon_type);
            distancee = (TextView) findViewById(R.id.distance);
            datetimes=(TextView) findViewById(R.id.last_seen);
        }

        private void initdata(){
            beaconLocationData = new BeaconLocationData();
        }

        private void initbeacon(){
            beaconManager = BeaconManager.getInstanceForApplication(this);
            beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));
            beaconManager.bind(this);
        }

        @Override
        public void onBeaconServiceConnect() {
            beaconManager.addRangeNotifier(new RangeNotifier() {
                @Override
                public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                    if (beacons.size() > 0) {
                        for(Beacon b:beacons) {
                            String uuid= String.valueOf(b.getId1());
                            String major = String.valueOf(b.getId2());
                            String minor = String.valueOf(b.getId3());

                            String distance=String.format("%.2f", b.getDistance());
                            updateTextviewx(distance);

                           String location = beaconLocationData.getLocation(major, minor);
                           updateTextview(location);

                            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            String formattedDate=simpleDateFormat.format(new Date());
                            Log.d("myLog", simpleDateFormat.toString());
                            updateTextviewxx(formattedDate);

                            FirebaseDatabase.getInstance();
                            databaseReference=FirebaseDatabase.getInstance().getReference(uuid).child(minor).child("minors");
                            databaseReference.setValue(location);
                            databaseReference=FirebaseDatabase.getInstance().getReference(uuid).child(minor).child("date");
                            databaseReference.setValue(formattedDate);

                            Log.i(TAG, "DidRangeBeaconsInRegion : " + beacons.toString());

                            iv=(ImageView) findViewById(R.id.imageView);
                            BitmapFactory.Options myOptions = null;
                            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.map,myOptions);
                            final Bitmap w=Bitmap.createBitmap(bitmap);
                            final Bitmap y=w.copy(Bitmap.Config.ARGB_8888,true);
                            int height= bitmap.getHeight();
                            int width= bitmap.getWidth();
                            Log.e("圖片大小 ","width: "+width+"height "+height);

                            final Canvas canvas=new Canvas(y);
                            final Paint paint=new Paint();
                            paint.setStrokeWidth(10);
                            paint.setStyle(Paint.Style.FILL);

                            if(minor.equals("77"))
                            {
                                paint.setColor(Color.RED);
                                canvas.drawCircle(1800,818,50, paint);
                                iv.setImageBitmap(y);}

                            else if(minor.equals("23366"))
                            {
                                paint.setColor(Color.RED);
                                canvas.drawCircle(1725,775,25, paint);
                                iv.setImageBitmap(y);}
                        }
                    }
                }
            });

            try {
                beaconManager.startRangingBeaconsInRegion(new Region(FILTER_UUID, null, null, null));
            }
            catch (RemoteException e){
                e.printStackTrace();
            }
        }


      private void updateTextview(final String location){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    beacontype.setText(location);
                }
            });
        }

        private void updateTextviewx(final String distance) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    distancee.setText(distance);
                }
            });
        }

    private void updateTextviewxx(final String formattedDate){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                datetimes.setText(formattedDate);
            }
        });
    }

    protected void onDestroy() {
            super.onDestroy();
            beaconManager.unbind(this);
        }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}