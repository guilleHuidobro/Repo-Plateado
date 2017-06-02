package com.plateado.pregol.ghuidobro.pregol;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.util.Attributes;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ghuidobro on 4/12/17.
 */
public class PGPrediccionesFragment extends Fragment {

    private ArrayList<PGFixture> mDataSet;
    private RecyclerView mRecyclerView;
    private PagoViewAdapter mAdapter;
    private ArrayList<ItemPrediction> itemPredictions = new ArrayList<>();
    private FABToolbarLayout morph;
    private FloatingActionButton fab;
    private ProgressDialog loading;
    private Handler handler;
    private View fragmentView;
    View uno, dos, sendIcon, cuatro;
    private boolean isPrediction = false;
    private ArrayList<MGPrediccionUsuario> mDataSetPrediccion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStat) {
        fragmentView = inflater.inflate(R.layout.pagorecyclerview, container, false);

        mRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.my_recycler_view);
        morph = (FABToolbarLayout) fragmentView.findViewById(R.id.fabtoolbar);
        sendIcon = fragmentView.findViewById(R.id.send_icon);
        fab = (FloatingActionButton) fragmentView.findViewById(R.id.fab);
        uno = fragmentView.findViewById(R.id.uno);
        dos = fragmentView.findViewById(R.id.dos);
        cuatro = fragmentView.findViewById(R.id.cuatro);
        handler = new Handler();

        // Layout Managers:
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDataSet = new ArrayList<PGFixture>();
        mDataSetPrediccion = new ArrayList<MGPrediccionUsuario>();

        loadPrediccionData();
        loadData();

        // Creating Adapter object
        mAdapter = new PagoViewAdapter(getActivity(), itemPredictions);


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

        return fragmentView;
    }

    public void callInfoDialog() {
        DialogFragment newFragment = new InfoDialogFragment();
        newFragment.show(getActivity().getFragmentManager(),"");
    }

    public void callPagoDialog() {
        DialogFragment newFragment = new PagoDialogFragment();
        newFragment.show(getActivity().getFragmentManager(),"");
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

    // load initial data
    public void loadPrediccionData() {
        String urlString = getString(R.string.web_url_predicciones_usuario);
        isPrediction = true;
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

            if(!isPrediction) {
                convertFixtureJSONtoArrayList(jsonObject);
                setItemFixture();
            }else{
                convertPredictionJSONtoArrayList(jsonObject);
            }

            mAdapter.notifyDataSetChanged();

            if (mDataSet.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                morph.setVisibility(View.GONE);

            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                morph.setVisibility(View.VISIBLE);
            }

        }//end of onPostExecute
    }//end of

    private void setItemFixture() {

        for (PGFixture item:mDataSet) {
            ItemPrediction itemPrediction = new ItemPrediction();
            itemPrediction.setPgFixture(item);
            itemPrediction.setEstado(1);
            itemPredictions.add(itemPrediction);
        }
    }


    private void convertFixtureJSONtoArrayList(JSONObject states){

        mDataSet.clear();

        try{
            JSONArray list = states.getJSONArray("fixture");

            for(int i=0; i<list.length(); i++) {
                JSONObject stateobj = list.getJSONObject(i);
                mDataSet.add(new PGFixture(stateobj.getString("equipo_local"),stateobj.getString("equipo_visitante"),stateobj.getString("escudo_local"),stateobj.getString("escudo_visita"),stateobj.getString("id_partido")));
            }//end of for loop
            /*
            if (mDataSet.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                morph.setVisibility(View.GONE);

            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                morph.setVisibility(View.VISIBLE);
            }
*/
        }catch(JSONException e){
            e.printStackTrace();

        }//end of catch

    }//end of convertJSONArrayList

    private void convertPredictionJSONtoArrayList(JSONObject states){

        try{
            JSONArray list = states.getJSONArray("prediccionusuario");

            for(int i=0; i<list.length(); i++) {
                JSONObject stateobj = list.getJSONObject(i);
                mDataSetPrediccion.add(new MGPrediccionUsuario(stateobj.getString("fecha_fixture"),stateobj.getString("usuario"),stateobj.getString("id_partido"),stateobj.getString("resultado")));
            }//end of for loop
            isPrediction = false;

        }catch(JSONException e){
            e.printStackTrace();

        }//end of catch

    }//end of convertJSONArrayList
/*
    private void sendData(){

        if(mAdapter.prediccion.size() == 15){
            prediccion = (HashMap<String, String>) mAdapter.prediccion;
            prediccion.put("fecha","19");
            prediccion.put("usuario",usuario);

            class AddFixture extends AsyncTask<Void,Void,String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    new Thread(new PGPrediccionesFragment.Task()).start();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if(loading != null){
                        loading.dismiss();
                    }
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

        }

    }//end send data
*/
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
                    loading = ProgressDialog.show(getActivity(), "Enviando...", "Un momento...", false, false);
                }
            });
        }
    }

}
