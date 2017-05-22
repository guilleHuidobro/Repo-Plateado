package com.plateado.pregol.ghuidobro.pregol;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.mercadopago.core.MercadoPago;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.model.Payment;
import com.mercadopago.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ghuidobro on 4/12/17.
 */
public class PGPagoActivity extends AppCompatActivity {

    private ArrayList<PGFixture> mDataSet;
    private RecyclerView mRecyclerView;
    private HashMap<String, String> prediccion ;
    private PagoViewAdapter mAdapter;
    private String usuario;
    private ArrayList<ItemPrediction> itemPredictions = new ArrayList<>();
    private FABToolbarLayout morph;
    private FloatingActionButton fab;
    private String sendMessage = "La prediccion no esta completa !";
    private boolean isOKToSend = false;
    private ProgressDialog loading;
    private Handler handler;
    private View infoPagoLL;

    final Context context = this;
    private Button mbutton;

    View uno, dos, sendIcon, cuatro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagorecyclerview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        morph = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        sendIcon = findViewById(R.id.send_icon);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        infoPagoLL = findViewById(R.id.infoPagoLL);

        uno = findViewById(R.id.uno);
        dos = findViewById(R.id.dos);
        cuatro = findViewById(R.id.cuatro);

        usuario = getIntent().getStringExtra("mail");

        handler = new Handler();

        // Layout Managers:
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDataSet = new ArrayList<PGFixture>();

        loadData();
        prediccion = new HashMap<>();

        // Creating Adapter object
        mAdapter = new PagoViewAdapter(this, itemPredictions,prediccion);


        // Setting Mode to Single to reveal bottom View for one item in List
        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
        ((PagoViewAdapter) mAdapter).setMode(Attributes.Mode.Single);

        mRecyclerView.setAdapter(mAdapter);

        /* Scroll Listeners */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i("RecyclerView", "onScrollStateChanged");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.fab) {
                    morph.show();
                }
                morph.hide();
            }
        });
        uno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callInfoDialog();
                morph.hide();

            }
        });




        dos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morph.hide();
            }
        });
        sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPagoDialog();
                morph.hide();
            }
        });
        cuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morph.hide();
            }
        });
    }

    public void callInfoDialog() {
        DialogFragment newFragment = new InfoDialogFragment();
        newFragment.show(getFragmentManager(),"");
    }

    public void callPagoDialog() {
        DialogFragment newFragment = new PagoDialogFragment();
        newFragment.show(getFragmentManager(),"");
    }



    public void onResume(){
        super.onResume();
        if(loading != null){
            loading.dismiss();
        }
        if (mDataSet.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            morph.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            morph.setVisibility(View.VISIBLE);
        }
    }

    // load initial data
    public void loadData() {
        String urlString = getString(R.string.web_url_fixture);

        try{
            URL url = new URL(urlString);
            StateTask stateTask = new StateTask();
            stateTask.execute(url);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private class StateTask extends AsyncTask<URL, String,JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... params) {

            HttpURLConnection connection = null;
            try{

                connection = (HttpURLConnection)params[0].openConnection();
                int response = connection.getResponseCode();

                if(response == HttpURLConnection.HTTP_OK){

                    StringBuilder builder = new StringBuilder();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        while ((line = reader.readLine())!= null ) {
                            builder.append(line);
                        }
                    }catch (IOException e){
                        publishProgress("Read Error");
                        e.printStackTrace();
                    }//inner try

                    return new JSONObject(builder.toString());

                }else{
                    publishProgress("Connection Error");
                }


            }//end outer try
            catch (Exception e){

                publishProgress("Connection Error 2");
                e.printStackTrace();

            }finally {
                connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //Toast.makeText(MainActivity.this, values[0], Toast.LENGTH_SHORT).show();
        }//end of onProgressUpdate

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            convertFixtureJSONtoArrayList(jsonObject);



            for (PGFixture item:mDataSet) {
                ItemPrediction itemPrediction = new ItemPrediction();
                itemPrediction.setPgFixture(item);
                itemPrediction.setEstado(ItemPrediction.SIN_ESTADO);
                itemPredictions.add(itemPrediction);
            }

            mAdapter.notifyDataSetChanged();

        }//end of onPostExecute
    }//end of




    private void convertFixtureJSONtoArrayList(JSONObject states){

        //fixtureList.clear();

        mDataSet.clear();

        try{
            JSONArray list = states.getJSONArray("fixture");

            for(int i=0; i<list.length(); i++) {
                JSONObject stateobj = list.getJSONObject(i);
                mDataSet.add(new PGFixture(stateobj.getString("equipo_local"),stateobj.getString("equipo_visitante"),stateobj.getString("escudo_local"),stateobj.getString("escudo_visita"),stateobj.getInt("id_partido")));
            }//end of for loop
            if (mDataSet.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                morph.setVisibility(View.GONE);

            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                morph.setVisibility(View.VISIBLE);
            }

        }catch(JSONException e){
            e.printStackTrace();

        }//end of catch

    }//end of convertJSONArrayList

    private void sendData(){

        if(mAdapter.prediccion.size() == 15){
            prediccion = (HashMap<String, String>) mAdapter.prediccion;
            prediccion.put("fecha","19");
            prediccion.put("usuario",usuario);
            isOKToSend = true;

            for (Map.Entry<String, String> entry : prediccion.entrySet()) {
                System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            }


            class AddFixture extends AsyncTask<Void,Void,String> {



                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    new Thread(new PGPagoActivity.Task()).start();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if(loading != null){
                        loading.dismiss();
                    }
                    Intent intent = new Intent(PGPagoActivity.this, PGPagoActivity.class);
                    startActivity(intent);
                }

                @Override
                protected String doInBackground(Void... v) {
                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Config.URL_ADD, prediccion);
                    return res;
                }
            }

            AddFixture addFixture = new AddFixture();
            addFixture.execute();

        }else{
            isOKToSend = false;
        }

    }//end send data

    private class Task implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loading = ProgressDialog.show(PGPagoActivity.this, "Enviando...", "Un momento...", false, false);
                }
            });
        }
    }

    // Espera los resultados del checkout
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        if (requestCode == MercadoPago.CHECKOUT_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {

                // Listo! El pago ya fue procesado por MP.
                Payment payment = JsonUtil.getInstance()
                        .fromJson(data.getStringExtra("payment"), Payment.class);

                TextView results = (TextView) findViewById(R.id.infoPagoText);

                if (payment != null) {

                    infoPagoLL.setBackgroundColor(Color.parseColor("#008b08"));

                    results.setText("Tu pago ha sido registrado estas participando !!!");
                } else {
                    infoPagoLL.setBackgroundColor(Color.parseColor("#a92b00"));
                    results.setText("No se ha confirmado el pago.");
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
