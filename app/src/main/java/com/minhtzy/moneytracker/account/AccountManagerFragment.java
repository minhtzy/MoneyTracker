package com.minhtzy.moneytracker.account;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.WelcomeActivity;

public class AccountManagerFragment extends Fragment {

    private TextView mFirstName;

    private TextView mNameEmail;

    private TextView mEmail;

    private Button mLogout;

    private FirebaseAuth mAuth;

    private final View.OnClickListener mLogoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logOutAccount();
        }
    };

    public AccountManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_account_manager, container, false);
        mFirstName = view.findViewById(R.id.txtFirstName);
        mNameEmail = view.findViewById(R.id.txtNameEmail);
        mEmail = view.findViewById(R.id.txtEmail);
        mLogout = view.findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();

        onBindView();

        initEvents();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.information_account));
    }

    private void initEvents() {
        mLogout.setOnClickListener(mLogoutListener);
    }

    private void onBindView() {

        if (mAuth != null) {

            String email = mAuth.getCurrentUser().getEmail();

            String name = mAuth.getCurrentUser().getDisplayName();

            if (email != null) {
                mEmail.setText(email);
            }

            if(name != null)
            {
                mNameEmail.setText(name);
                String[] names = name.split(" ");
                StringBuilder shortName = new StringBuilder();
                for(String n : names)
                {
                    shortName.append(String.valueOf(n.charAt(0)).toUpperCase());
                }
                mFirstName.setText(shortName.toString());
            }

        }
    }

    private void logOutAccount() {

        if (mAuth != null) {

            mAuth.signOut();
            Intent intent = new Intent(getContext(), WelcomeActivity.class);
            startActivity(intent);
        }
    }

}
