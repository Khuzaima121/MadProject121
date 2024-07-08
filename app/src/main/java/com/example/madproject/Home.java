package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Home extends AppCompatActivity {

    RecyclerView rvCategories, rvTopSellings, rvDeals;
    DatabaseReference reference;
    Button btnSignout;
    CatagoriesAdapter catAdapter;
    FloatingActionButton fabCart;
    FirebaseUser user;
    FirebaseAuth mAuth;
    BarMenuFragment fragment;
    Button btnMenu;
    topsellingadapter tadapter;
    private static final String TAG = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .hide(fragment)
                .commit();

        btnMenu.setOnClickListener(
                v -> {
                    getSupportFragmentManager().beginTransaction().show(fragment).commit();
                    btnMenu.setVisibility(Button.GONE);
                    fabCart.setVisibility(Button.GONE);
                });

        if (btnSignout != null) {
            btnSignout.setOnClickListener(v -> {
                mAuth.signOut();
                Intent i=new Intent(Home.this, MainActivity.class);
                i.putExtra("user",user);
                startActivity(i);
                finish();
            });
        }

        fabCart.setOnClickListener(v -> {
            if (user == null) {
                Toast.makeText(Home.this, "Please Login First", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(Home.this, CartItems.class));
            }
        });

        loadCategories();
    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();
        rvCategories = findViewById(R.id.rvCatagories);
        rvTopSellings = findViewById(R.id.rvTopSellings);
        rvDeals = findViewById(R.id.rvdeals);
        rvDeals.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTopSellings.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
       rvCategories.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        btnMenu = findViewById(R.id.btnmenu);
        btnSignout = findViewById(R.id.btnsignout);
        fragment = new BarMenuFragment();
        fabCart = findViewById(R.id.fabCart);


        reference = FirebaseDatabase.getInstance().getReference().child("categories");

        FirebaseRecyclerOptions<model_catagories> options =
                new FirebaseRecyclerOptions.Builder<model_catagories>()
                        .setQuery(reference, model_catagories.class)
                        .build();
        FirebaseRecyclerOptions<model_topselling> toption=new FirebaseRecyclerOptions.Builder<model_topselling>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Topsellers"),model_topselling.class)
                .build();
        catAdapter = new CatagoriesAdapter(options, this);
        rvCategories.setAdapter(catAdapter);
        tadapter =new topsellingadapter(toption,this);
        rvTopSellings.setAdapter(tadapter);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "Child added: " + snapshot.getKey());
                runOnUiThread(() -> catAdapter.notifyItemInserted((int) snapshot.getChildrenCount() - 1));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "Child changed: " + snapshot.getKey());
                runOnUiThread(catAdapter::notifyDataSetChanged);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Child removed: " + snapshot.getKey());
                runOnUiThread(catAdapter::notifyDataSetChanged);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "Child moved: " + snapshot.getKey());
                runOnUiThread(catAdapter::notifyDataSetChanged);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Child event listener cancelled: " + error.getMessage());
            }
        });
    }

    private void loadCategories() {
        catAdapter.startListening();
        tadapter.startListening();
        Log.d(TAG, "Adapter started listening");
    }

    @Override
    public void onBackPressed() {
        if (fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            showMenuButton();
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        catAdapter.startListening();
        tadapter.startListening();
        Log.d(TAG, "Activity started, adapter listening");
    }

    @Override
    protected void onStop() {
        super.onStop();
        catAdapter.stopListening();
        tadapter.stopListening();
        Log.d(TAG, "Activity stopped, adapter stopped listening");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        catAdapter.stopListening();
        Log.d(TAG, "Activity destroyed, adapter stopped listening");
    }

    public void showMenuButton() {
        btnMenu.setVisibility(Button.VISIBLE);
        fabCart.setVisibility(Button.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable listState = Objects.requireNonNull(rvCategories.getLayoutManager()).onSaveInstanceState();
        outState.putParcelable("recycler_state", listState);
        Log.d(TAG, "Saved instance state");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Parcelable listState = savedInstanceState.getParcelable("recycler_state");
        Objects.requireNonNull(rvCategories.getLayoutManager()).onRestoreInstanceState(listState);
        Log.d(TAG, "Restored instance state");
    }
}
