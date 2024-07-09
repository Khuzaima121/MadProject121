package com.example.madproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class CartItems extends AppCompatActivity {

    RecyclerView rvCartItem;
    Button btnBuyNow;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    CartAdapter adapter;
    int totalPrice = 0;
    StringBuilder dishesName = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        btnBuyNow.setOnClickListener(v -> {
            checkCartAndProceed();
        });
    }

    private void checkCartAndProceed() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(CartItems.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                buynow();
                showOrderDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartItems.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showOrderDialog() {
        AlertDialog.Builder add = new AlertDialog.Builder(CartItems.this);
        add.setTitle("Confirm Order");
        View view = LayoutInflater.from(CartItems.this)
                .inflate(R.layout.buy_now_layout, null, false);
        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPhone = view.findViewById(R.id.etPhone);
        EditText etAddress = view.findViewById(R.id.etAddress);
        add.setView(view);
        add.setPositiveButton("Purchase now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etUsername.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    Toast.makeText(CartItems.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                placeOrder(name, phone, address);
            }
        });
        add.show();
    }

    private void placeOrder(String name, String phone, String address) {
        String price = Integer.toString(totalPrice);
        HashMap<String, Object> data = new HashMap<>();
        data.put("Address", address);
        data.put("DishName", dishesName.toString());
        data.put("TotalBill", price);
        data.put("UserName", name);
        data.put("UserPhone", phone);

        FirebaseDatabase.getInstance().getReference().child("Orders").push()
                .setValue(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CartItems.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                        reference.removeValue();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CartItems.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void buynow() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalPrice = 0;
                dishesName.setLength(0);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    model_cart cartItems = ds.getValue(model_cart.class);
                    if (cartItems != null) {
                        totalPrice += Integer.parseInt(cartItems.getPrice()) * Integer.parseInt(cartItems.getQuantity());
                        dishesName.append(cartItems.getDishName()).append(",");
                    }
                }

                if (dishesName.length() > 0) {
                    dishesName.setLength(dishesName.length() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartItems.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Carts").child(user.getUid());
        rvCartItem = findViewById(R.id.rvCartItems);
        rvCartItem.setLayoutManager(new WrapContentLinearLayoutManager(this));
        btnBuyNow = findViewById(R.id.btnBuyNow);
        FirebaseRecyclerOptions<model_cart> options =
                new FirebaseRecyclerOptions.Builder<model_cart>()
                        .setQuery(reference, model_cart.class)
                        .build();
        adapter = new CartAdapter(options);
        rvCartItem.setAdapter(adapter);
    }

    @Override
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