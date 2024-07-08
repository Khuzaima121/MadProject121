package com.example.madproject;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class CartAdapter extends FirebaseRecyclerAdapter<model_cart,CartAdapter.MyViewHolder> {

    FirebaseAuth mAuth;
    FirebaseUser user;
    public CartAdapter(@NonNull FirebaseRecyclerOptions<model_cart> options) {
        super(options);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull model_cart modelCart) {
        holder.tvDishName.setText(modelCart.getDishName());
        holder.tvPrice.setText(modelCart.getPrice());
        holder.tvQuantity.setText(modelCart.getQuantity());
        String key=getRef(i).getKey();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder DelcartItem=new AlertDialog.Builder(v.getContext());
                DelcartItem.setTitle("Confirm action");
                DelcartItem.setMessage("Are you sure you wan to delete it");
                DelcartItem.setPositiveButton("clear Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Carts").child(user.getUid()).child(key)
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(v.getContext(), "Order Completed", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });
                DelcartItem.show();
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_design,parent,false);

        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvDishName,tvPrice,tvQuantity;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDishName=itemView.findViewById(R.id.tvDishNameValue);
            tvPrice=itemView.findViewById(R.id.tvPriceValue);
            tvQuantity=itemView.findViewById(R.id.tvQuantityValue);
        }
    }
}
