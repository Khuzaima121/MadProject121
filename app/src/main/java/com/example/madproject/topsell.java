package com.example.madproject;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class topsell extends AppCompatActivity {

    topsellingadapter adapter;
    RecyclerView rvtopsell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_topsell); // Inflate the layout first

        rvtopsell = findViewById(R.id.rvTopSellings); // Now find the RecyclerView
        init();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();

        rvtopsell.setLayoutManager(new WrapContentGridLayoutManager(this,2));
        FirebaseRecyclerOptions<model_topselling> toption=new FirebaseRecyclerOptions.Builder<model_topselling>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Topsellers"),model_topselling.class)
                .build();
        adapter=new topsellingadapter(toption,this);
        rvtopsell.setAdapter(adapter);
        adapter.startListening();

    }
    protected void onStart() {
        super.onStart();

        adapter.startListening();
        Log.d(TAG, "Activity started, adapter listening");
    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.stopListening();
        Log.d(TAG, "Activity stopped, adapter stopped listening");
    }
}