package com.growin.silveryogaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    TextView tvName, tvMail;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnLogout = findViewById(R.id.btnLogout);
        tvName = findViewById(R.id.tvName);
        tvMail = findViewById(R.id.tvMail);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount!=null){
            tvName.setText(signInAccount.getDisplayName());
            tvMail.setText(signInAccount.getEmail());
        }


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });


    }
}