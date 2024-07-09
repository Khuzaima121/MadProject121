package com.example.madproject;

import android.annotation.SuppressLint;
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

public class topsellingadapter extends FirebaseRecyclerAdapter<model_topselling, topsellingadapter.MyViewHolder> {

    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Context context;

    public topsellingadapter(@NonNull FirebaseRecyclerOptions<model_topselling> options, Context context) {
        super(options);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull model_topselling model) {
        holder.tvDishprice.setText(model.getName());
        holder.tvdishname.setText(model.getPrice());
        holder.tvDesc.setText(model.getDescription());
        Glide.with(holder.ivDishImage.getContext())
                .load(model.getImageUrl())
                .into(holder.ivDishImage);
        holder.ivfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name= model.getName();
                String Price=model.getPrice();
                String Desc= model.getDescription();
                String image= model.getImageUrl();
                HashMap<String,Object> data=new HashMap<>();
                data.put("name",name);
                data.put("price",Price);
                data.put("description",Desc);
                data.put("imageUrl",image);

                FirebaseDatabase.getInstance().getReference().child("Favourites").child(user.getUid())
                        .push()
                        .setValue(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Added to favourite", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        holder.ivDishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder order = new AlertDialog.Builder(v.getContext());
                order.setTitle("Choose one");

                order.setPositiveButton("Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder buy = new AlertDialog.Builder(v.getContext());
                        buy.setTitle("Buy now");

                        View view = LayoutInflater.from(v.getContext())
                                .inflate(R.layout.order_layout, null, false);
                        buy.setView(view);
                        EditText etQuantity = view.findViewById(R.id.etQuantity);
                        EditText etAddress = view.findViewById(R.id.etAddress);
                        EditText etName = view.findViewById(R.id.etName);
                        EditText etPhone = view.findViewById(R.id.etPhone);

                        buy.setPositiveButton("Purchase now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (user == null) {
                                    Toast.makeText(view.getContext(), "Please login first", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String Name = etName.getText().toString().trim();
                                String Quantity = etQuantity.getText().toString().trim();
                                String Address = etAddress.getText().toString().trim();
                                String Phone = etPhone.getText().toString().trim();

                                if (Name.isEmpty()) {
                                    Toast.makeText(view.getContext(), "Enter your Name please", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (Quantity.isEmpty() || Quantity.equals("0")) {
                                    Toast.makeText(view.getContext(), "Quantity taken as 1", Toast.LENGTH_SHORT).show();
                                    Quantity = "1";
                                }
                                if (Phone.isEmpty()) {
                                    Toast.makeText(view.getContext(), "Enter Phone Number please", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (Address.isEmpty()) {
                                    Toast.makeText(view.getContext(), "Enter your Address please", Toast.LENGTH_SHORT).show();
                                    return;
                                }

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

                                FirebaseDatabase.getInstance().getReference().child("Orders")
                                        .push()
                                        .setValue(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(view.getContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(view.getContext(), "Order placement failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                        buy.show();
                    }
                });

                order.setNegativeButton("Add to cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String DishName = model.getName();
                        String Price = model.getPrice();

                        AlertDialog.Builder add = new AlertDialog.Builder(v.getContext());
                        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.quantity_layout, null, false);
                        add.setView(view);
                        add.setTitle("Add to cart");
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText etQuantity =view.findViewById(R.id.etQuantity);
                        add.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String Quantity = etQuantity.getText().toString().trim();

                                if (Quantity.isEmpty() || Quantity.equals("0")) {
                                    Toast.makeText(view.getContext(), "Quantity taken as 1 ", Toast.LENGTH_SHORT).show();
                                    Quantity = "1";
                                }

                                if (user == null) {
                                    Toast.makeText(view.getContext(), "Please login first", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String userId = user.getUid();
                                HashMap<String, Object> cartItem = new HashMap<>();
                                cartItem.put("DishName", DishName);
                                cartItem.put("Price", Price);
                                cartItem.put("Quantity", Quantity);

                                reference.child("Carts").child(userId)
                                        .push()
                                        .setValue(cartItem)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(view.getContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(view.getContext(), "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                        add.show();
                    }
                });

                order.show();
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dishes_adapter_design, parent, false);
        return new MyViewHolder(v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivDishImage;
        TextView tvdishname;
        TextView tvDishprice;
        TextView tvDesc;
        ImageView ivfav;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDishImage = itemView.findViewById(R.id.dish_image);
            tvdishname = itemView.findViewById(R.id.dish_name);
            tvDishprice = itemView.findViewById(R.id.dish_price);
            tvDesc=itemView.findViewById(R.id.dish_description);
            ivfav=itemView.findViewById(R.id.ivfav);
        }
    }
}
