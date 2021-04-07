package com.example.project.Beacon;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.List;

public class beacondata extends AppCompatActivity {
    private static final String TAG ="beacondata" ;
    private RecyclerView recyclerView;
    private Button vb;
    BluetoothAdapter mBlueAdapter;
    private static final int REQUEST_ENABLE_BT = 0;

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
