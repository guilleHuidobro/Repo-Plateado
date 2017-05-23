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

public class AuthFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;
    private Button btnSignOut,btnFixture;
    private TextView txtName, txtEmail,welcomeLabel;
    private static final int RC_SIGN_IN = 007;
    private ImageView bienvenidoLogo,iconApp,appLogo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.auth_view, container, false);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) fragmentView.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        btnSignIn = (SignInButton) fragmentView.findViewById(R.id.sign_in_button);
        btnSignOut = (Button) fragmentView.findViewById(R.id.btn_sign_out);
        btnFixture = (Button) fragmentView.findViewById(R.id.btn_fixture);

        txtName = (TextView) fragmentView.findViewById(R.id.txtName);
        txtEmail = (TextView) fragmentView.findViewById(R.id.txtEmail);
        welcomeLabel = (TextView) fragmentView.findViewById(R.id.welcome_label);
        bienvenidoLogo = (ImageView) fragmentView.findViewById(R.id.bienvenido_logo);
        iconApp = (ImageView) fragmentView.findViewById(R.id.iconApp);
        appLogo = (ImageView) fragmentView.findViewById(R.id.app_logo);

        welcomeLabel.setText(R.string.welcome_label);

        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnFixture.setOnClickListener(this);


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity() /* FragmentActivitSy */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.sign_in_button:
                signIn();
                break;

            case R.id.btn_sign_out:
                signOut();
                break;

            case R.id.btn_fixture:
                goFixture();
                break;
        }
    }

    private void goFixture() {
        Fragment fragment = null;
        fragment = new PGMainFixtureFragment();
        if (fragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String personName = acct.getDisplayName();
            String email = acct.getEmail();

            txtName.setText(personName);
            txtEmail.setText(email);

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            txtName.setVisibility(View.VISIBLE);
            txtEmail.setVisibility(View.VISIBLE);
            btnFixture.setVisibility(View.VISIBLE);
            welcomeLabel.setVisibility(View.GONE);
            bienvenidoLogo.setVisibility(View.VISIBLE);
            iconApp.setVisibility(View.GONE);
            appLogo.setVisibility(View.GONE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            txtName.setVisibility(View.GONE);
            txtEmail.setVisibility(View.GONE);
            btnFixture.setVisibility(View.GONE);
            welcomeLabel.setVisibility(View.VISIBLE);
            bienvenidoLogo.setVisibility(View.GONE);
            iconApp.setVisibility(View.VISIBLE);
            appLogo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
}