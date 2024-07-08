package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    TextInputEditText tietemail, tietpass;
    Button btnlogin;
    TextView tvforgot;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        init();



        LoginFunction();

        ForgotPassFunction();
    }

    private void ForgotPassFunction() {
        tvforgot.setOnClickListener(v -> {
            AlertDialog.Builder fpDialog = new AlertDialog.Builder(Login.this);
            EditText etRegEmail = new EditText(Login.this);
            etRegEmail.setHint("Enter Email ");
            fpDialog.setView(etRegEmail);
            fpDialog.setPositiveButton("Send", (dialog, which) -> {
                String email = etRegEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    etRegEmail.setError("Email can't be empty");
                    return;
                }
                mAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(unused -> Toast.makeText(Login.this, "Email Sent", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            });
            fpDialog.show();
        });
    }

    private void LoginFunction() {
        btnlogin.setOnClickListener(v -> {
            String email = tietemail.getText().toString().trim();
            String pass = tietpass.getText().toString();
            if (email.isEmpty()) {
                tietemail.setError("Email can't be empty");
                return;
            }
            if (pass.isEmpty()) {
                tietpass.setError("Password can't be empty");
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(authResult -> {
                        user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            progressBar.setVisibility(View.GONE);
                            MovetoHome();
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void MovetoHome() {
        startActivity(new Intent(Login.this, Home.class));
        finish();
    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        tietemail = findViewById(R.id.tietEmail);
        tietpass = findViewById(R.id.tietPass);
        btnlogin = findViewById(R.id.btnLogin);
        tvforgot = findViewById(R.id.tvForgot);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }
}
