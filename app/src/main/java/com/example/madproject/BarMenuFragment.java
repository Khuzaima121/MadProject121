package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class BarMenuFragment extends Fragment {

    Button btnBack, btnSignOut, btnSettings,btnts,fbtn,obtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bar_menu, container, false);
        btnBack = view.findViewById(R.id.btnback);
        btnSignOut = view.findViewById(R.id.btnsignout);
        btnSettings = view.findViewById(R.id.setbtn);
        btnts=view.findViewById(R.id.tsbtn);
        fbtn=view.findViewById(R.id.fbtn);
        obtn=view.findViewById(R.id.obtn);


        obtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RecentOrders.class));
            }
        });
        fbtn.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), favourite.class));
        });
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            btnSignOut.setText("Sign Up");
        }


        btnts.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), topsell.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> {
            // Handle back button click (hide the fragment)
            getParentFragmentManager().beginTransaction().hide(this).commit();
            if (getActivity() instanceof Home) {
                ((Home) getActivity()).showMenuButton();
            }
        });

        btnSignOut.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                // Navigate to Sign Up activity
                Intent intent = new Intent(getActivity(), Signup.class);
                startActivity(intent);
            } else {
                // Perform sign out
                FirebaseAuth.getInstance().signOut();
                // Handle UI updates and navigation after sign-out
                // For example, navigate to the login screen:
                Intent intent = new Intent(getActivity(), MainActivity.class); // Replace MainActivity with your login activity
                startActivity(intent);
                requireActivity().finish(); // Optional: Finish the current activity
            }
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Settings.class);
            startActivity(intent);
        });

        return view;
    }
}
