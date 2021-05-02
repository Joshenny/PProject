package com.example.project.Beacon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.register.LoginActivity;
import com.example.project.register.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.SimpleDateFormat;
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
        public static final String FILTER_UUID ="FDA50693-A4E2-4FB1-AFCF-C6EB07647825";
        private static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
        DatabaseReference databaseReference;
        private Button logoutt;
        private Button vdata;
        private ImageView iv;
        BluetoothAdapter mBlueAdapter;
        private static final int REQUEST_ENABLE_BT = 0;
        NotificationManager notificationManager;
        Context context=this;
        private TextView tv;
        private ImageView image;
        SignUpActivity signUpActivity;
        private float scale;
        private float beforescale=1.0f;
        NotificationChannel channelId;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_beacon_detect);

             iv=(ImageView) findViewById(R.id.imageView);
             iv.setImageResource(R.drawable.map);

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
                    showToast("Bluetooth is on");
                }
                else {
                    showToast("Bluetooth is not on");
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
                        showToast("Please Turn on Bluetooth");
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

    private void sendNotification(String message){
        notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        channelId=new NotificationChannel("ID","test", NotificationManager.IMPORTANCE_MIN);
        notificationManager.createNotificationChannel(channelId);

        Intent intent=new Intent(context, BeaconDetect.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder= new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setChannelId("ID")
                .setContentTitle("現在位置")
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        Notification notification=builder.build();
        notificationManager.notify(0,notification);
    }

    private void initview(){
            beacontype=(TextView) findViewById(R.id.beacon_type);
            distancee = (TextView) findViewById(R.id.distance);
            datetimes=(TextView) findViewById(R.id.last_seen);
    }

    private void initdata(){
            beaconLocationData = new BeaconLocationData();
            signUpActivity=new SignUpActivity();
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
                            Log.i(TAG, "DidRangeBeaconsInRegion : " + beacons.toString());

                            String location = beaconLocationData.getLocation(major, minor);
                           updateTextview(location);

                            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            String formattedDate=simpleDateFormat.format(new Date());
                            Log.d("myLog", simpleDateFormat.toString());
                            updateTextviewxx(formattedDate);

                            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                            databaseReference=FirebaseDatabase.getInstance().getReference(user.getUid()).child(minor).child("minors");
                            databaseReference.setValue(location);
                            databaseReference=FirebaseDatabase.getInstance().getReference(user.getUid()).child(minor).child("date");
                            databaseReference.setValue(formattedDate);
                            databaseReference=FirebaseDatabase.getInstance().getReference(user.getUid()).child(minor).child("emails");
                            databaseReference.setValue(user.getEmail());

                            iv=(ImageView) findViewById(R.id.imageView);
                            BitmapFactory.Options myOptions = null;
                            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.map,myOptions);
                            final Bitmap w=Bitmap.createBitmap(bitmap);
                            final Bitmap y=w.copy(Bitmap.Config.ARGB_8888,true);


                           // int height= bitmap.getHeight();
                           // int width= bitmap.getWidth();
                           // Log.e("圖片大小 ","width: "+width+"height "+height);

                            final Canvas canvas=new Canvas(y);
                            final Paint paint=new Paint();
                            paint.setStrokeWidth(10);
                            paint.setStyle(Paint.Style.FILL);

                            tv=(TextView)findViewById(R.id.infos_rv);
                            Typeface type = Typeface.createFromAsset(tv.getContext().getAssets(), "HanyiSentyChalk2018.ttf");
                            tv.setTypeface(type);
                            image=(ImageView)findViewById(R.id.photo);

                            if(minor.equals("77"))
                            {
                                sendNotification("理工二館");
                                paint.setColor(Color.RED);
                                canvas.drawCircle(1404,895,50, paint);
                                iv.setImageBitmap(y);

                                databaseReference = FirebaseDatabase.getInstance().getReference(uuid).child(minor).child("introduction");
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String data = snapshot.getValue().toString();
                                            data = data.replace("_b", "\n");
                                            tv.setText(data);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                                databaseReference = FirebaseDatabase.getInstance().getReference(uuid).child(minor).child("image");
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String data = snapshot.getValue().toString();
                                            Picasso.get().load(data).into(image);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }

                            else if(minor.equals("23366"))
                            {
                                sendNotification("集賢館");
                                paint.setColor(Color.RED);
                                canvas.drawCircle(1782,767,50, paint);
                                iv.setImageBitmap(y);

                                databaseReference = FirebaseDatabase.getInstance().getReference(uuid).child(minor).child("introduction");
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String data = snapshot.getValue().toString();
                                            data = data.replace("_b", "\n");
                                            tv.setText(data);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                                databaseReference = FirebaseDatabase.getInstance().getReference(uuid).child(minor).child("image");
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String data = snapshot.getValue().toString();
                                            Picasso.get().load(data).into(image);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }

                            else if(minor.equals("2626"))
                            {
                                sendNotification("管理學院");
                                paint.setColor(Color.RED);
                                canvas.drawCircle(926,895,50, paint);
                                iv.setImageBitmap(y);

                                databaseReference = FirebaseDatabase.getInstance().getReference(uuid).child(minor).child("introduction");
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String data = snapshot.getValue().toString();
                                            data = data.replace("_b", "\n");
                                            tv.setText(data);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                                databaseReference = FirebaseDatabase.getInstance().getReference(uuid).child(minor).child("image");
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String data = snapshot.getValue().toString();
                                            Picasso.get().load(data).into(image);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
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