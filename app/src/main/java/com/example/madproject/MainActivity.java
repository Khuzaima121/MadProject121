package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout llvanim;

    Button btnsignin,btnlogin;

    TextView tvSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();
        btnsignin.setOnClickListener(v -> MoveToSigin());
        btnlogin.setOnClickListener(v -> MoveToLogin());
        tvSkip.setOnClickListener(v-> MoveTOHome());

    }
    private  void MoveTOHome()
    {
        startActivity(new Intent(MainActivity.this,Home.class));
    }

    private void MoveToLogin()
    {
        startActivity(new Intent(MainActivity.this,Login.class));

    }
    private void MoveToSigin()
    {
        startActivity(new Intent(MainActivity.this,Signup.class));

    }
    private void init()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        llvanim=findViewById(R.id.llAnimMain);
        Animation anim= AnimationUtils.loadAnimation(this,R.anim.logo_animation);
        llvanim.setAnimation(anim);
        btnlogin=findViewById(R.id.btnLogin);
        btnsignin=findViewById(R.id.btnsignin);
        tvSkip=findViewById(R.id.tvskip);

    }
}