package com.growin.silveryogaapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.growin.silveryogaapp.data.Content;

public class YogaVideo extends YouTubeBaseActivity {

    String videoId = "NmkYHmiNArc";

    YouTubePlayerView playerView;
    Button btn;
    YouTubePlayer.OnInitializedListener listener;
    Content pItem; //☆
    Button btnInsert;
    Button btnDelete;
    String pMail;
    private FirebaseDatabase pDatabase;
    private DatabaseReference pDatabaseRef;
    private Query pQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga_video);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        pMail = signInAccount.getEmail();


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



        //아직 작업중......☆
        btnInsert = findViewById(R.id.btnInsert);
        btnDelete = findViewById(R.id.btnDelete);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDatabase = FirebaseDatabase.getInstance();

                pDatabaseRef = pDatabase.getReference("SilverYoga");

                //pQuery = pDatabaseRef.child("Favorit").orderByChild("mail").equalTo(pMail);
                pQuery = pDatabaseRef.child("Favorit").orderByKey().limitToLast(1);
            }
        });

    }
}