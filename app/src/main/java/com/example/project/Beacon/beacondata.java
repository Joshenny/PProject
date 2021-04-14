package com.example.project.Beacon;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.project.Beacon.BeaconDetect.TAG;

public class beacondata extends AppCompatActivity {
    private static final String TAG ="beacondata" ;
    private RecyclerView recyclerView;
    private Button vb;
    BluetoothAdapter mBlueAdapter;
    private static final int REQUEST_ENABLE_BT = 0;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener mAuth;
    private List<Userdata>userdataList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_data);

        recyclerView=(RecyclerView) findViewById(R.id.revview);
        new firebase().basicReadWrite(new firebase.Datastatus() {
            @Override
            public void dataload(List<Userdata> userdataList, List<String> keys) {
                new Recycler().set(recyclerView, beacondata.this, userdataList, keys);
            }
        });



    }


}
