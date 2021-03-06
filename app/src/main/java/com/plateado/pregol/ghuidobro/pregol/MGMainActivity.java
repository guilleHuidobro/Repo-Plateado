package com.plateado.pregol.ghuidobro.pregol;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mercadopago.core.MercadoPago;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.model.Payment;
import com.mercadopago.util.JsonUtil;

public class MGMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Session Manager Class
    private SessionManager session;
    private AlertDialogManager alert = new AlertDialogManager();
    private String usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_inicio);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment = null;
        switch (id) {
            case R.id.action_resultados:
                //replaceFragment(fragment);
                break;
            case R.id.action_creditos:
                if(session.isLoggedIn()){
                    fragment = new CreditosFragment();
                    callPagoDialog();
                }else{
                    fragment = new AuthFragment();
                }

                break;
            case R.id.action_ganadores:
                //replaceFragment(fragment);
                break;

        }
        replaceFragment(fragment);
        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_inicio:
                fragment = new AuthFragment();
                break;
            case R.id.nav_jugar:
                if(session.isLoggedIn()){
                fragment = new PGMainFixtureFragment();
                }else{
                    fragment = new AuthFragment();
                }

                break;
            case R.id.nav_predicciones:
                if(session.isLoggedIn()){
                    fragment = new PGPrediccionesFragment();
                }else{
                    fragment = new AuthFragment();
                }
                break;
            case R.id.nav_creditos:
                if(session.isLoggedIn()){
                    fragment = new CreditosFragment();
                }else{
                    fragment = new AuthFragment();
                }
                break;
            case R.id.nav_estadistica:
                fragment = new EstadisticasFragment();
                break;

            case R.id.nav_reglas:
                fragment = new ReglasFragment();
                break;
        }

        replaceFragment(fragment);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void replaceFragment(Fragment fragment) {
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    public void callPagoDialog() {
        DialogFragment newFragment = new PagoDialogFragment();
        newFragment.show(getFragmentManager(),"");
    }
    // Espera los resultados del checkout
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {

        super.onActivityResult(requestCode,resultCode, data);

        if (requestCode == MercadoPago.CHECKOUT_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {

                // Listo! El pago ya fue procesado por MP.
                Payment payment = JsonUtil.getInstance()
                        .fromJson(data.getStringExtra("payment"), Payment.class);

                if (payment != null) {

                    payment.getDescription();
                    payment.getStatus();
                    payment.getStatusDetail();
                    payment.getTransactionAmount();

                    if(session.getUserDetails() != null){
                        usuario = session.getUserDetails().get("email");
                    }

                    MGPagoCreditoAsyncTask pagoCreditoAsyncTask = new MGPagoCreditoAsyncTask(String.valueOf(payment.getTransactionAmount()),usuario);
                    pagoCreditoAsyncTask.execute();

                    Fragment fragment = null;
                    if(fragment != null){
                        fragment = new CreditosFragment();
                    }
                    replaceFragment(fragment);
                } else {
                    System.out.println("ERROR");
                }

            } else {
                if ((data != null) && (data.hasExtra("mpException"))) {
                    MPException mpException = JsonUtil.getInstance()
                            .fromJson(data.getStringExtra("mpException"), MPException.class);
                    // Manejá tus errores.
                }
            }
        }
    }

}
