package com.plateado.pregol.ghuidobro.pregol;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PagoViewAdapter extends RecyclerSwipeAdapter<PagoViewAdapter.SimpleViewHolder> {


    private Context mContext;
    private ArrayList<ItemPrediction> fixtureList;
    Map<String, String> prediccion;

    public PagoViewAdapter(Context context, ArrayList<ItemPrediction> objects, HashMap prediccion) {
        this.mContext = context;
        this.fixtureList = objects;
        this.prediccion = prediccion;

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

        Picasso.with(mContext)
                .load(item.getPgFixture().getImagenEquipoLocal().toString())
                .into(viewHolder.escudoLocal);

        Picasso.with(mContext)
                .load(item.getPgFixture().getImagenEquipoVisita().toString())
                .into(viewHolder.escudoVisita);


        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        // Drag From Left
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        // Drag From Right
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));


        // Handling different events when swiping
        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });


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
        SwipeLayout swipeLayout;
        TextView horario;
        TextView equipoLocal;
        TextView equipoVisita;
        TextView opcionEmpate;
        ImageView escudoLocal;
        ImageView escudoVisita;


        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            horario = (TextView) itemView.findViewById(R.id.horario);
            equipoLocal = (TextView) itemView.findViewById(R.id.equipoLocal);
            equipoVisita = (TextView) itemView.findViewById(R.id.equipoVisita);
            opcionEmpate = (TextView) itemView.findViewById(R.id.opcionEmpate);
            escudoLocal = (ImageView) itemView.findViewById(R.id.escudoLocal);
            escudoVisita = (ImageView) itemView.findViewById(R.id.escudoVisita);

        }
    }

}
