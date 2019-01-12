package com.example.emon.myapplication2;

import android.content.Intent;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class OptionPage extends AppCompatActivity {
    private Button logout;
    private ImageButton backToOption;
    private Button profile,addPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_page);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(OptionPage.this,MainActivity.class));
            }
        });
        backToOption=findViewById(R.id.back_from_option);
        backToOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        profile=findViewById(R.id.profile_show);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OptionPage.this,UserProfile.class));
            }
        });
        addPost=findViewById(R.id.add_post_option_Id);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OptionPage.this,ImagePostActivity.class));
            }
        });
    }
}
