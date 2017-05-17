package com.plateado.pregol.ghuidobro.pregol;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.daimajia.swipe.util.Attributes;

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

/**
 * Created by ghuidobro on 4/12/17.
 */
public class PGPagoActivity extends AppCompatActivity {

    private ArrayList<PGFixture> mDataSet;
    private RecyclerView mRecyclerView;
    HashMap<String, String> prediccion ;
    PagoViewAdapter mAdapter;
    String usuario;
    ArrayList<ItemPrediction> itemPredictions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagorecyclerview);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        usuario = getIntent().getStringExtra("mail");

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
    }


    public void onResume(){
        super.onResume();

        if (mDataSet.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
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

            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
            }

        }catch(JSONException e){
            e.printStackTrace();

        }//end of catch

    }//end of convertJSONArrayList

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PGPagoActivity.this, PGMainFixtureActivity.class);
        startActivity(intent);
    }
}
