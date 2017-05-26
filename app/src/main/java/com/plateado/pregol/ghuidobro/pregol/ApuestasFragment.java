package com.plateado.pregol.ghuidobro.pregol;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by ghuidobro on 5/22/17.
 */

public class ApuestasFragment extends Fragment implements View.OnClickListener{

    private TextView txtName, txtEmail,welcomeLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.apuestas_view, container, false);

        txtName = (TextView) fragmentView.findViewById(R.id.txtName);
        txtEmail = (TextView) fragmentView.findViewById(R.id.txtEmail);
        welcomeLabel = (TextView) fragmentView.findViewById(R.id.welcome_label);

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}