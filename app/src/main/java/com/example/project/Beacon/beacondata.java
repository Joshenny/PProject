package com.example.project.Beacon;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private ImageView ivs1,ivs2,ivs3,ivp1,ivp2,ivp3,ivlibrary,ivgym,ivgii,ivlake,ivd,ive,ivteacher,ivhotel,ivm,ivtpeople;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener mAuth;
    private TextView ss2,u;
    private int ints1,ints2,ints3,ints4,ints5,ints6,ints7,ints8,ints9,ints10,ints11,ints12;
    BluetoothAdapter mBlueAdapter;
    private static final int REQUEST_ENABLE_BT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_data);

        ivs1=findViewById(R.id.iv_s1);
        ivs1.setImageResource(R.drawable.s1a);
        ivs2=findViewById(R.id.iv_s2);
        ivs2.setImageResource(R.drawable.s2a);
        ivs3=findViewById(R.id.iv_s3);
        ivs3.setImageResource(R.drawable.s3a);
        ivp1=findViewById(R.id.iv_p1);
        ivp1.setImageResource(R.drawable.p1a);
        ivp2=findViewById(R.id.iv_p2);
        ivp2.setImageResource(R.drawable.p2a);
        ivp3=findViewById(R.id.iv_p3);
        ivp3.setImageResource(R.drawable.p3a);
        ivlibrary=findViewById(R.id.iv_library);
        ivlibrary.setImageResource(R.drawable.s2a);
        ivgym=findViewById(R.id.iv_gym);
        ivgym.setImageResource(R.drawable.s3a);

        ivgii=findViewById(R.id.iv_ggi);
        ivgii.setImageResource(R.drawable.giia);
        ive=findViewById(R.id.iv_e);
        ive.setImageResource(R.drawable.ea);
        ivd=findViewById(R.id.iv_d);
        ivd.setImageResource(R.drawable.da);
        ivlake=findViewById(R.id.iv_lake);
        ivlake.setImageResource(R.drawable.lakea);
        ivteacher=findViewById(R.id.iv_teacher);
        ivteacher.setImageResource(R.drawable.teachera);
        ivtpeople=findViewById(R.id.iv_tpeople);
        ivtpeople.setImageResource(R.drawable.tpeoplea);
        ivm=findViewById(R.id.iv_m);
        ivm.setImageResource(R.drawable.ma);
        ivhotel=findViewById(R.id.iv_hotel);
        ivhotel.setImageResource(R.drawable.hotela);
        /*public void onDataChange(DataSnapshot dataSnapshot) {
            userdataList.clear();
            List<String>keys= new ArrayList<>();
            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                keys.add(ds.getKey());
                Userdata userdata = ds.getValue(Userdata.class);
                userdataList.add(userdata);
            }
            datastatus.dataload(userdataList,keys);
        }*/
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Userdata userdata = dataSnapshot.getValue(Userdata.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        ss2=(TextView) findViewById(R.id.s2);
        u=(TextView) findViewById(R.id.user);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference(user.getUid()).child("77").child("minors");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor= preferences.edit();
                if(snapshot.exists()){
                    String data=snapshot.getValue().toString();
                    ss2.setText(data);
                    ivs2.setImageResource(R.drawable.show_s2);
                    editor.putString("ints2","1");
                    editor.apply();
                }
                else editor.putString("ints2","0");
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference(user.getUid()).child("23366").child("minors");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor= preferences.edit();
                if(snapshot.exists()){
                    String data=snapshot.getValue().toString();

                    ivgii.setImageResource(R.drawable.show_gii);
                    editor.putString("ints1","1");
                    editor.apply();
                }
                else editor.putString("ints1","0");
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
       /* recyclerView=(RecyclerView) findViewById(R.id.revview);
        new firebase().basicReadWrite(new firebase.Datastatus() {
            @Override
            public void dataload(List<Userdata> userdataList, List<String> keys) {
                new Recycler().set(recyclerView, beacondata.this, userdataList, keys);

            }
        });*/
        //ints1+ints2+ints3+ints4+ints5+ints6+ints7+ints8+ints9+ints10+ints11+ints12
        //String s =ints+"/12";

        SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
        String ints2=preferences.getString("ints2","0");
        String ints1=preferences.getString("ints1","0");
        int a=Integer.parseInt(ints1);
        int b=Integer.parseInt(ints2);

        String ints=Integer.toString(a+b);
        if(a+b<=4)
        {
            u.setText("歡迎回來青銅冒險家  目前已收集:"+ints+"/16");
        }
        else if(a+b<=8&&a+b>4)
        {
            u.setText("歡迎回來白銀冒險家  目前已收集:"+ints+"/16");
        }
        else if(a+b<=12&&a+b>8)
        {
            u.setText("歡迎回來黃金冒險家  目前已收集:"+ints+"/16");
        }
        else if(a+b<=16&&a+b>12)
        {
            u.setText("歡迎回來鑽石冒險家  目前已收集:"+ints+"/16");
        }
    }
}