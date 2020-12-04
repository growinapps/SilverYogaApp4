package com.growin.silveryogaapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.growin.silveryogaapp.R;
import com.growin.silveryogaapp.YogaVideo;
import com.growin.silveryogaapp.data.Content;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ItemViewHolder> {

    private ArrayList<Content> contentsList;
    private Context context;

    public ContentAdapter(ArrayList<Content> data, Context context) {
        this.contentsList = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ContentAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yoga_content, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Glide.with(holder.itemView).load(contentsList.get(position).getImgUri()).into(holder.imageView);
        holder.textView.setText(contentsList.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return (contentsList != null ? contentsList.size() : 0);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ItemViewHolder(@NonNull View contentView) {
            super(contentView);

            this.imageView = contentView.findViewById(R.id.yogaContentImg);
            this.textView = contentView.findViewById(R.id.yogaContentTxt);

            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //추가=======================★
                    int pos = getAdapterPosition();
                    int updateCnt;
                    Content pItem = contentsList.get(pos);
                    updateCnt = UpdateCount(pItem);
                    contentsList.get(pos).setCnt(updateCnt);

                    Intent intent = new Intent(v.getContext(), YogaVideo.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }


    //추가=======================★
    private int UpdateCount(Content content){
        int cnt =0;
        cnt = content.getCnt()+1;
        FirebaseDatabase pDatabase = FirebaseDatabase.getInstance();
        DatabaseReference pDatabaseRef = pDatabase.getReference("SilverYoga");
        pDatabaseRef.child("Contents").child(String.valueOf(content.getIdx())).child("count").setValue(String.valueOf(cnt));

        return cnt;
    }



}
