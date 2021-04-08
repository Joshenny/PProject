package com.example.project.Beacon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.List;

public class Recycler {
    private Context mcontext;
    private adapter madapter;

    public void set(RecyclerView recyclerView, Context context, List<Userdata> userdataList, List<String>keys){
        mcontext=context;
        madapter=new adapter(userdataList,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(madapter);
    }

    class item extends RecyclerView.ViewHolder{
        private TextView mtitle;
        private TextView mdate;
        private String key;

        public item(ViewGroup parent){
            super(LayoutInflater.from(mcontext).inflate(R.layout.item_list, parent, false));
            mtitle=(TextView) itemView.findViewById(R.id.item_title);
            mdate=(TextView) itemView.findViewById(R.id.item_content);
        }

        public void bind(Userdata userdata, String key){
            mtitle.setText(userdata.getMinors());
            mdate.setText(userdata.getDate());
            this.key=key;
        }

    }

    class adapter extends RecyclerView.Adapter<item>{
        private List<Userdata> muserdata;
        private List<String> mkeys;

        public adapter(List<Userdata> muserdata, List<String>mkeys){
            this.muserdata=muserdata;
            this.mkeys=mkeys;
        }
        @Override
        public item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new item(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull item holder, int position) {
                holder.bind(muserdata.get(position), mkeys.get(position));
        }

        @Override
        public int getItemCount() {
            return muserdata.size();
        }
    }
}
