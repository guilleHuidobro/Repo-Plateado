package com.plateado.pregol.ghuidobro.pregol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;

/**
 * Created by ghuidobro on 5/22/17.
 */

public class EstadisticasFragment extends Fragment implements View.OnClickListener{

    private SessionManager session;
    private Button estadisticasBotonJugar;
    private SignInButton botonSignIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.estadisticas_view, container, false);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) fragmentView.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        estadisticasBotonJugar = (Button) fragmentView.findViewById(R.id.btn_fixture);
        botonSignIn = (SignInButton) fragmentView.findViewById(R.id.sign_in_button);

         session = new SessionManager(getActivity().getApplicationContext());

        if(session.isLoggedIn()){
            fragmentView.findViewById(R.id.fecha_actual_LL).setVisibility(View.VISIBLE);
            fragmentView.findViewById(R.id.historial_ganados_LL).setVisibility(View.VISIBLE);
            estadisticasBotonJugar.setVisibility(View.VISIBLE);
        }else{
            botonSignIn.setVisibility(View.VISIBLE);
        }

        estadisticasBotonJugar.setOnClickListener(this);
        botonSignIn.setOnClickListener(this);

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
        int id = v.getId();
        Fragment fragment = null;
        switch (id) {
            case R.id.sign_in_button:
                fragment = new AuthFragment();
                break;

            case R.id.btn_fixture:
                fragment = new PGMainFixtureFragment();
                break;

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}