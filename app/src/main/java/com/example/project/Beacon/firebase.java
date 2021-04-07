package com.example.project.Beacon;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.project.Beacon.BeaconDetect.TAG;

public class firebase {
    private  FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener mAuth;
    private List<Userdata>userdataList=new ArrayList<>();
    private RecyclerView recyclerView;

    public firebase(){
        databaseReference= FirebaseDatabase.getInstance().getReference("fda50693-a4e2-4fb1-afcf-c6eb07647825");
    }

    public interface Datastatus{
        void dataload(List<Userdata> userdataList, List<String>keys);
    }

    public void basicReadWrite(final Datastatus datastatus){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userdataList.clear();
                List<String>keys= new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    keys.add(ds.getKey());
                    Userdata userdata = ds.getValue(Userdata.class);
                    userdataList.add(userdata);
                }
                datastatus.dataload(userdataList,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}