package com.growin.silveryogaapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.growin.silveryogaapp.adapter.ContentAdapter;
import com.growin.silveryogaapp.data.Content;
import com.growin.silveryogaapp.databinding.ActivityRankingYogaBinding;

import java.util.ArrayList;

public class RankingYoga extends BaseActivity {

    ActivityRankingYogaBinding act;

    private FirebaseDatabase pDatabase;
    private DatabaseReference pDatabaseRef;

    private FirebaseStorage pStorage;
    private StorageReference pStorageRef;

    private ArrayList<Content> contentList;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bindViews();
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

        act.tabLayout.addTab(act.tabLayout.newTab().setText("월간"));
        act.tabLayout.addTab(act.tabLayout.newTab().setText("주간"));
        act.tabLayout.addTab(act.tabLayout.newTab().setText("일간"));

    }

    @Override
    public void setValues() {
        InitDatabase();
    }

    private void InitDatabase() {

        Log.d("DB 연결 : ", "시작!!!!");
        pDatabase = FirebaseDatabase.getInstance();
        pDatabaseRef = pDatabase.getReference("SilverYoga").child("Body").child("Shoulder");

        pStorage = FirebaseStorage.getInstance("gs://growinyoga-4f680.appspot.com");
        layoutManager = new LinearLayoutManager(this.mContext);
        act.rankingList.setLayoutManager(layoutManager);
        contentList = new ArrayList<>();

        pDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                contentList.clear();

                for (DataSnapshot ss : snapshot.getChildren()) {
                    String strPoseName = ss.child("name").getValue().toString();
                    String strImgPath = ss.child("img").getValue().toString();

                    Log.d("요가 동작 : ", strPoseName);
                    Log.d("이미지 URI : ", strImgPath);

                    pStorageRef = pStorage.getReference(strImgPath);

                    pStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("이미지 다운로드 : ", "성공!!!!!!");

                            Content content = new Content();
                            content.setImgUri(uri);
                            content.setTitle(strPoseName);

                            contentList.add(content);

                            adapter = new ContentAdapter(contentList, mContext);
                            act.rankingList.setAdapter(adapter);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Fail : ", "실패!!!");
                        }
                    });
                }

//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB 연결 Fail : ", "실패!!!");
            }
        });

    }

    @Override
    public void bindViews() {

        act = DataBindingUtil.setContentView(this, R.layout.activity_ranking_yoga);

    }
}