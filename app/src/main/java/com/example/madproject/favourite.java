package com.example.madproject;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class favourite extends AppCompatActivity {

    FavouriteAdapter adapter;
    RecyclerView rvfav;
    DatabaseReference reference;
    FirebaseAuth mAth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourite);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

    }
    private void init()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();
        rvfav=findViewById(R.id.rvfav);
        mAth=FirebaseAuth.getInstance();
        user=mAth.getCurrentUser();
        rvfav.setLayoutManager(new WrapContentLinearLayoutManager(this));
        reference = FirebaseDatabase.getInstance().getReference().child("Favourites").child(user.getUid());
        FirebaseRecyclerOptions<model_favourites> options =
                new FirebaseRecyclerOptions.Builder<model_favourites>()
                        .setQuery(reference, model_favourites.class)
                        .build();
        adapter=new FavouriteAdapter(options,this);
        rvfav.setAdapter(adapter);

    }
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}