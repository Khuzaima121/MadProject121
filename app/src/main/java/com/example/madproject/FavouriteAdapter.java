package com.example.madproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FavouriteAdapter extends FirebaseRecyclerAdapter<model_favourites, FavouriteAdapter.myviewHolder> {

    FirebaseUser user;
    FirebaseAuth mAuth;
    Context context;

    DatabaseReference cartReference;
    DatabaseReference ordersReference;

    public FavouriteAdapter(@NonNull FirebaseRecyclerOptions<model_favourites> options, Context context) {
        super(options);
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        cartReference = FirebaseDatabase.getInstance().getReference().child("Carts").child(user.getUid());
        ordersReference = FirebaseDatabase.getInstance().getReference().child("Orders");
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewHolder holder, int position, @NonNull model_favourites model) {

        String key = getRef(position).getKey();
        holder.tvName.setText(model.getName());
        holder.tvprice.setText(model.getPrice());
        holder.tvDesc.setText(model.getDescription());

        Glide.with(holder.ivlogo.getContext())
                .load(model.getImageUrl())
                .into(holder.ivlogo);




        holder.ivlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder order = new AlertDialog.Builder(v.getContext());
                order.setTitle("Choose an action");

                order.setPositiveButton("Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showOrderDialog(model);
                    }
                });
                order.setNegativeButton("Add to cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showAddToCartDialog(model);
                    }
                });

                order.show();
            }
        });

        holder.ivfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveFromFavoritesDialog(key);
            }

        });
    }

    private void showOrderDialog(model_favourites model) {
        AlertDialog.Builder buy = new AlertDialog.Builder(context);
        buy.setTitle("Buy now");

        View view = LayoutInflater.from(context).inflate(R.layout.order_layout, null, false);
        buy.setView(view);
        EditText etQuantity = view.findViewById(R.id.etQuantity);
        EditText etAddress = view.findViewById(R.id.etAddress);
        EditText etName = view.findViewById(R.id.etName);
        EditText etPhone = view.findViewById(R.id.etPhone);

        buy.setPositiveButton("Purchase now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleOrderAction(view, model);
            }
        });

        buy.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        buy.show();
    }
    private void showAddToCartDialog(model_favourites model) {
        AlertDialog.Builder add = new AlertDialog.Builder(context);
        add.setTitle("Add to cart");
        View view = LayoutInflater.from(context).inflate(R.layout.quantity_layout, null, false);
        add.setView(view);
        EditText etQuantity = view.findViewById(R.id.etQuantity);
        add.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleAddToCartAction(view, model);
            }
        });

        add.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        add.show();
    }

    private void handleOrderAction(View view, model_favourites model) {
        EditText etQuantity = view.findViewById(R.id.etQuantity);
        EditText etAddress = view.findViewById(R.id.etAddress);
        EditText etName = view.findViewById(R.id.etName);
        EditText etPhone = view.findViewById(R.id.etPhone);

        String Name = etName.getText().toString().trim();
        String Quantity = etQuantity.getText().toString().trim();
        String Address = etAddress.getText().toString().trim();
        String Phone = etPhone.getText().toString().trim();

        if (validateOrderForm(Name, Quantity, Address, Phone)) {
            int quantity = Integer.parseInt(Quantity);
            int total = quantity * Integer.parseInt(model.getPrice());
            String DishName = model.getName();
            String Total = Integer.toString(total);

            HashMap<String, Object> data = new HashMap<>();
            data.put("UserName", Name);
            data.put("UserPhone", Phone);
            data.put("TotalBill", Total);
            data.put("DishName", DishName);
            data.put("Address", Address);

            ordersReference.push()
                    .setValue(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Order placed successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Order placement failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void handleAddToCartAction(View view, model_favourites model) {
        EditText etQuantity = view.findViewById(R.id.etQuantity);
        String Quantity = etQuantity.getText().toString().trim();

        if (!Quantity.isEmpty()) {
            String userId = user.getUid();
            HashMap<String, Object> cartItem = new HashMap<>();
            cartItem.put("DishName", model.getName());
            cartItem.put("Price", model.getPrice());
            cartItem.put("Quantity", Quantity);

            cartReference.push()
                    .setValue(cartItem)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(context, "Please enter quantity", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateOrderForm(String Name, String Quantity, String Address, String Phone) {
        if (Name.isEmpty()) {
            Toast.makeText(context, "Enter your Name please", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Quantity.isEmpty() || Quantity.equals("0")) {
            Toast.makeText(context, "Quantity taken as 1", Toast.LENGTH_SHORT).show();
            Quantity = "1";
        }
        if (Phone.isEmpty()) {
            Toast.makeText(context, "Enter Phone Number please", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Address.isEmpty()) {
            Toast.makeText(context, "Enter your Address please", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showRemoveFromFavoritesDialog(String key) {
        AlertDialog.Builder rem = new AlertDialog.Builder(context);
        rem.setTitle("Confirmation");
        rem.setMessage("Are you sure you want to delete it from Favourites?");
        rem.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance().getReference().child("Favourites")
                        .child(user.getUid())
                        .child(key)
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        rem.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        rem.show();
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favouriteadapter, parent, false);
        return new myviewHolder(v);
    }

    public static class myviewHolder extends RecyclerView.ViewHolder {

        ImageView ivlogo, ivfav;
        TextView tvprice, tvName, tvDesc;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);
            ivlogo = itemView.findViewById(R.id.dish_image);
            tvDesc = itemView.findViewById(R.id.dish_description);
            tvprice = itemView.findViewById(R.id.dish_price);
            tvName = itemView.findViewById(R.id.dish_name);
            ivfav = itemView.findViewById(R.id.ivfav);
        }
    }
}
