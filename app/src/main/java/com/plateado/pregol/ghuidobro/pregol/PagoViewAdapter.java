package com.plateado.pregol.ghuidobro.pregol;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PagoViewAdapter extends RecyclerSwipeAdapter<PagoViewAdapter.SimpleViewHolder> {


    private Context mContext;
    private ArrayList<ItemPrediction> fixtureList;
    private String resultado;


    public PagoViewAdapter(Context context, ArrayList<ItemPrediction> objects) {
        this.mContext = context;
        this.fixtureList = objects;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pago_row_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final ItemPrediction item = fixtureList.get(position);

        viewHolder.equipoLocal.setText((item.getPgFixture().getEquipoLocal()));
        viewHolder.equipoVisita.setText((item.getPgFixture().getEquipoVisita()));
        viewHolder.opcionEmpate.setText(("Vs"));

        int id = item.getEstado();
        switch(id) {
            case 0:
                resultado="E";
                break;
            case 1:
                resultado="L";
                break;
            case 2:
                resultado="V";
                break;
        }

        viewHolder.resultadoBoton.setText(resultado);

        Picasso.with(mContext)
                .load(item.getPgFixture().getImagenEquipoLocal().toString())
                .into(viewHolder.escudoLocal);

        Picasso.with(mContext)
                .load(item.getPgFixture().getImagenEquipoVisita().toString())
                .into(viewHolder.escudoVisita);


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position);

    }


    @Override
    public int getItemCount() {
        return fixtureList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    //  ViewHolder Class

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView equipoLocal;
        TextView equipoVisita;
        TextView opcionEmpate;
        ImageView escudoLocal;
        ImageView escudoVisita;
        Button resultadoBoton;


        public SimpleViewHolder(View itemView) {
            super(itemView);
            equipoLocal = (TextView) itemView.findViewById(R.id.equipoLocal);
            equipoVisita = (TextView) itemView.findViewById(R.id.equipoVisita);
            opcionEmpate = (TextView) itemView.findViewById(R.id.opcionEmpate);
            escudoLocal = (ImageView) itemView.findViewById(R.id.escudoLocal);
            escudoVisita = (ImageView) itemView.findViewById(R.id.escudoVisita);
            resultadoBoton = (Button) itemView.findViewById(R.id.resultadoBoton);
        }
    }

}
