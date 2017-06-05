package com.plateado.pregol.ghuidobro.pregol;

import android.os.AsyncTask;

import java.util.HashMap;

/**
 * Created by ghuidobro on 6/5/17.
 */

public class MGPagoCreditoAsyncTask extends AsyncTask<Void,Void,String> {

    private String paymentAmount,usuario;
    HashMap<String, String> creditousuario = new HashMap<>(); ;


    public MGPagoCreditoAsyncTask(String paymentAmount,String usuario) {
        this.paymentAmount = paymentAmount;
        this.usuario = usuario;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(Void... v) {
        creditousuario.put("pago",paymentAmount);
        creditousuario.put("usuario",usuario);
        RequestHandler rh = new RequestHandler();
        String res = rh.sendPostRequest(Config.URL_ADD,creditousuario);
        return res;
    }
}
