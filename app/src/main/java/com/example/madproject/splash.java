package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class splash extends AppCompatActivity {


    ImageView ivlogo;
    TextView tvslogan;
    FirebaseUser user;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        init();

        // Check if user is authenticated and email is verified
        if (user != null && user.isEmailVerified()) {
            moveToHome();
        } else {
            moveToMain();
        }
    }

    private void moveToHome() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splash.this, Home.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }

    private void moveToMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splash.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }

    private void init() {
        tvslogan = findViewById(R.id.tvslogan);
        ivlogo = findViewById(R.id.ivlogo);
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Set animations
        Animation sloganAnim = AnimationUtils.loadAnimation(this, R.anim.slogan_animation);
        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        ivlogo.setAnimation(logoAnim);
        tvslogan.setAnimation(sloganAnim);

        // Full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Hide action bar


        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }







}