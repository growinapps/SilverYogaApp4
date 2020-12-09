package com.growin.silveryogaapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.growin.silveryogaapp.data.Content;
import com.growin.silveryogaapp.data.Favorit;

import java.util.List;

public class YogaVideo extends YouTubeBaseActivity {

    String videoId = "NmkYHmiNArc";

    YouTubePlayerView playerView;
    Button btn;
    YouTubePlayer.OnInitializedListener listener;
    Content pItem; //☆
    Button btnInsert;
    Button btnDelete;
    String pMail;
    TextView txtFavorit;
    private final FirebaseDatabase pDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference pDatabaseRef;
    private Query pQuery;

    //intent로 넘어온 값
    private String pVideo;
    private String pImg;
    private String pName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga_video);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        pMail = signInAccount.getEmail();
        pVideo =  getIntent().getStringExtra("videoId");
        pImg = getIntent().getStringExtra("imgPath");
        pName =  getIntent().getStringExtra("poseName");
        btnInsert = findViewById(R.id.btnInsert);
        btnDelete = findViewById(R.id.btnDelete);
        txtFavorit = findViewById(R.id.txtFavorit);


        CheckFavoritVideo(pMail, pVideo, new IYogaVideo() {
            @Override
            public void onCallbak(Boolean bFavorit) {
                if (bFavorit) {txtFavorit.setText("찜한상태");}
                else{txtFavorit.setText("찜안했음");}
            }
        });


        btn = findViewById(R.id.youtubeBtn);
        playerView = findViewById(R.id.youtubeView);

        listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerView.initialize("아무키", listener);
            }
        });


        //여기서부터☆
        //Insert 클릭하면 Favorit에 있는지 없는지 확인 후 Insert
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Favorit에 들어있지 않을때 Insert
                CheckFavoritVideo(pMail, pVideo, new IYogaVideo() {
                    @Override
                    public void onCallbak(Boolean bFavorit) {
                        if (!bFavorit){
                            Favorit pFavor = new Favorit();
                            pFavor.setImg(pImg);
                            pFavor.setMail(pMail);
                            pFavor.setMail_video(pMail+"_"+pVideo);
                            pFavor.setName(pName);
                            pFavor.setVideo(pVideo);
                            pDatabaseRef = pDatabase.getReference("SilverYoga").child("Favorit");
                            pDatabaseRef.push().setValue(pFavor);
                            txtFavorit.setText("찜한상태");
                        }
                    }
                });
            }
        });

        //Delete 클릭하면 Favorit에 있는지 없는지 확인 후 Delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckFavoritVideo(pMail, pVideo, new IYogaVideo() {
                    @Override
                    public void onCallbak(Boolean bFavorit) {
                        //Favorit에 들어있을때 Delete
                        if (bFavorit){
                            Favorit pFavor = new Favorit();
                            pFavor.setImg(pImg);
                            pFavor.setMail(pMail);
                            pFavor.setMail_video(pMail+"_"+pVideo);
                            pFavor.setName(pName);
                            pFavor.setVideo(pVideo);
                            pQuery = pDatabase.getReference("SilverYoga").child("Favorit").orderByChild("mail_video").equalTo(pMail+"_"+pVideo);
                            pQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ss: snapshot.getChildren()) {
                                        ss.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            //pDatabaseRef.push().setValue(pFavor);
                            txtFavorit.setText("찜안했음");
                        }
                    }
                });

            }
        });

    }//End the OnCreate

    public void CheckFavoritVideo(String strMail, String strVideo, IYogaVideo iYogaVideo){

        pDatabaseRef = pDatabase.getReference("SilverYoga");
        pQuery = pDatabaseRef.child("Favorit").orderByChild("mail_video").equalTo(strMail+"_"+strVideo);
        pQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getChildrenCount()==1){
                    iYogaVideo.onCallbak(true);
                }
                else{
                    iYogaVideo.onCallbak(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });

    }

}