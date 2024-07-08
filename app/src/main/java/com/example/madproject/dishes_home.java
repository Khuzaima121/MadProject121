package com.example.madproject;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class dishes_home extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference reference;
    dishAdapter adapter;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dishes_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        key = getIntent().getStringExtra("key");
        init();

        loadCategories();
    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();

        recyclerView = findViewById(R.id.rvDishes);
        recyclerView.setLayoutManager(new WrapContentGridLayoutManager(this,2));
        reference = FirebaseDatabase.getInstance().getReference().child("categories").child(key).child("Dish");

        FirebaseRecyclerOptions<model_dishes> options =
                new FirebaseRecyclerOptions.Builder<model_dishes>()
                        .setQuery(reference, model_dishes.class)
                        .build();
        adapter = new dishAdapter(options );
        recyclerView.setAdapter(adapter);
    }

    private void loadCategories() {
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
