package com.growin.silveryogaapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.growin.silveryogaapp.adapter.ContentAdapter;
import com.growin.silveryogaapp.data.Content;
import com.growin.silveryogaapp.databinding.ActivityFavoriteYogaBinding;

import java.util.ArrayList;

public class FavoriteYoga extends BaseActivity {

    ActivityFavoriteYogaBinding act;

    private FirebaseDatabase pDatabase;
    private DatabaseReference pDatabaseRef;
    private Query pQuery;

    private FirebaseStorage pStorage;
    private StorageReference pStorageRef;

    private ArrayList<Content> contentList;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String pMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        pMail = signInAccount.getEmail();
        bindViews();
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

        act.allPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 리스트 영상 전체재생 기능 추가
            }
        });
    }

    @Override
    public void setValues() {
        InitDatabase();
    }

    private void InitDatabase() {
        Log.d("DB 연결 : ", "시작!!!!");
        pDatabase = FirebaseDatabase.getInstance();

        //수정=============★
        pDatabaseRef = pDatabase.getReference("SilverYoga");
        //pQuery = pDatabaseRef.child("Contents").orderByChild("group").equalTo("어깨"); //어깨관련동작쿼리
        pQuery = pDatabaseRef.child("Favorit").orderByChild("mail").equalTo(pMail); //로그인 메일이 growinapps@gmail.com 일때 즐겨찾기 쿼리☆


        pStorage = FirebaseStorage.getInstance("gs://growinyoga-4f680.appspot.com");
        layoutManager = new LinearLayoutManager(this.mContext);
        act.favoriteList.setLayoutManager(layoutManager);
        contentList = new ArrayList<>();


        //수정  pDatabaseRef -> pQuery =============★
        pQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                contentList.clear();

                for (DataSnapshot ss : snapshot.getChildren()) {

                    String strPoseName = ss.child("name").getValue().toString();
                    String strImgPath = ss.child("img").getValue().toString();

                    //추가=======================★
                    //int nIdx =  Integer.parseInt(ss.getKey());  //Key
                    //int nCnt = Integer.parseInt(ss.child("count").getValue().toString()); //Count  Content에서만 쓰임☆
                    //String strGroup = ss.child("group").getValue().toString(); // ex) 허리, 어깨, 팔, 다리  Content에서만 쓰임☆
                    String strVideo = ss.child("video").getValue().toString(); //요가영상 VideoId


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

                            //추가=======================★
                            //content.setIdx(nIdx);
                            //content.setCnt(nCnt);  Content에서만 쓰임☆
                            //content.setGroup(strGroup);  Content에서만 쓰임☆
                            content.setVideoId(strVideo);
                            contentList.add(content);

                            adapter = new ContentAdapter(contentList, mContext);
                            act.favoriteList.setAdapter(adapter);
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

        act = DataBindingUtil.setContentView(this, R.layout.activity_favorite_yoga);

    }
}