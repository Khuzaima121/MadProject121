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

public class orderAdapter extends FirebaseRecyclerAdapter<model_orders, orderAdapter.MyviewHolder> {

FirebaseUser user;
FirebaseAuth mAuth;
    public orderAdapter(@NonNull FirebaseRecyclerOptions options) {
        super(options);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

    }

    @Override
    protected void onBindViewHolder(@NonNull MyviewHolder Holder, int i, @NonNull model_orders modelOrders) {
        String key=getRef(i).getKey();
        Holder.tvUsername.setText(modelOrders.getUserName());
        Holder.tvDish.setText(modelOrders.getDishName());
        Holder.tvBill.setText(modelOrders.getTotalBill());
        Holder.tvPhone.setText(modelOrders.getUserPhone());
        Holder.tvAddress.setText(modelOrders.getAddress());
        Holder.itemView.setOnLongClickListener(v->{

            AlertDialog.Builder orderCom=new AlertDialog.Builder(v.getContext());
            orderCom.setTitle("Confirm Action");
            orderCom.setMessage("Are you sure you want to cancel the order");
            orderCom.setPositiveButton("yes" , new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseDatabase.getInstance().getReference().child("Orders").child(key)
                            .removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(v.getContext(), "Order cancelled", Toast.LENGTH_SHORT).show();
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
            orderCom.show();
            return true;
        });


    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_design_adapter,parent,false);
        return new MyviewHolder(v);
    }


    public class MyviewHolder extends RecyclerView.ViewHolder{

        TextView tvUsername,tvPhone,tvDish,tvBill,tvAddress;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername=itemView.findViewById(R.id.tvUserNameValue);
            tvDish=itemView.findViewById(R.id.tvDishNameValue);
            tvPhone=itemView.findViewById(R.id.tvUserPhoneValue);
            tvBill=itemView.findViewById(R.id.tvTotalBillValue);
            tvAddress=itemView.findViewById(R.id.tvAddressValue);
        }
    }
}
