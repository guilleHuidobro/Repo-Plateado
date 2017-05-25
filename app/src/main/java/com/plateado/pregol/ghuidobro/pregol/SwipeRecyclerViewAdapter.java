package com.plateado.pregol.ghuidobro.pregol;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> {


    private Context mContext;
    private ArrayList<ItemPrediction> fixtureList;
    Map<String, String> prediccion;
    private ArrayList<HashMap> aList = new ArrayList<>();

    public SwipeRecyclerViewAdapter(Context context, ArrayList<ItemPrediction> objects, HashMap prediccion,ArrayList aList) {
        this.mContext = context;
        this.fixtureList = objects;
        this.prediccion = prediccion;
        this.aList = aList;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_row_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final ItemPrediction item = fixtureList.get(position);

        viewHolder.equipoLocal.setText((item.getPgFixture().getEquipoLocal()));
        viewHolder.equipoVisita.setText((item.getPgFixture().getEquipoVisita()));
        viewHolder.opcionEmpate.setText(("E"));

        if(item.getEstado() == ItemPrediction.EMPATE ){
        viewHolder.opcionEmpate.setBackgroundColor(ContextCompat.getColor(viewHolder.equipoLocal.getContext(),R.color.opcion_elegida));
        viewHolder.equipoLocal.setBackgroundColor(ContextCompat.getColor(viewHolder.equipoLocal.getContext(),R.color.opcion_apagada));
        viewHolder.equipoVisita.setBackgroundColor(ContextCompat.getColor(viewHolder.equipoLocal.getContext(),R.color.opcion_apagada));

        }else if(item.getEstado() == ItemPrediction.LOCAL){
            viewHolder.opcionEmpate.setBackgroundColor(ContextCompat.getColor(viewHolder.equipoLocal.getContext(),R.color.opcion_apagada));
            viewHolder.equipoLocal.setBackgroundColor(ContextCompat.getColor(viewHolder.equipoLocal.getContext(),R.color.opcion_elegida));
            viewHolder.equipoVisita.setBackgroundColor(ContextCompat.getColor(viewHolder.equipoLocal.getContext(),R.color.opcion_apagada));

        }else if(item.getEstado() == ItemPrediction.VISITA){
            viewHolder.opcionEmpate.setBackgroundColor(ContextCompat.getColor(viewHolder.equipoLocal.getContext(),R.color.opcion_apagada));
            viewHolder.equipoLocal.setBackgroundColor(ContextCompat.getColor(viewHolder.equipoLocal.getContext(),R.color.opcion_apagada));
            viewHolder.equipoVisita.setBackgroundColor(ContextCompat.getColor(viewHolder.equipoLocal.getContext(),R.color.opcion_elegida));

        }else{
            viewHolder.opcionEmpate.setBackgroundColor(ContextCompat.getColor(viewHolder.equipoLocal.getContext(),R.color.primer_estado_fixture));
            viewHolder.equipoLocal.setBackgroundColor(ContextCompat.getColor(viewHolder.equipoLocal.getContext(),R.color.app_));
            viewHolder.equipoVisita.setBackgroundColor(ContextCompat.getColor(viewHolder.equipoLocal.getContext(),R.color.app_));
        }



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

        /*viewHolder.swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((((SwipeLayout) v).getOpenStatus() == SwipeLayout.Status.Close)) {
                    //Start your activity

                    Toast.makeText(mContext, " onClick : " + item.getName() + " \n" + item.getEmailId(), Toast.LENGTH_SHORT).show();
                }

            }
        });*/

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(mContext, " onClick : " + item.getName() + " \n" + item.getEmailId(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.btnInfoBet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "Todo lo necesario para apostar", Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.opcionEmpateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Todo lo necesario para apostar", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.opcionLocalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Todo lo necesario para apostar", Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.opcionVisitaInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Todo lo necesario para apostar", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.opcionEmpate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prediccion.put( String.valueOf(item.getPgFixture().getIdPartido()),"0");
                checkPredictionSize();
                item.setEstado(ItemPrediction.EMPATE);
                notifyDataSetChanged();
            }
        });

        viewHolder.equipoLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prediccion.put( String.valueOf(item.getPgFixture().getIdPartido()),"1");
                checkPredictionSize();
                item.setEstado(ItemPrediction.LOCAL);
                notifyDataSetChanged();
            }
        });


        viewHolder.equipoVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prediccion.put( String.valueOf(item.getPgFixture().getIdPartido()),"2");
                checkPredictionSize();
                item.setEstado(ItemPrediction.VISITA);
                notifyDataSetChanged();
            }
        });


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position);

    }

    private boolean checkPredictionSize(){

        boolean isFill = false;

        if(prediccion.size() >= fixtureList.size()){
            isFill = true;
        }

        if (isFill){
            for (Map.Entry<String, String> entry : prediccion.entrySet()) {
                System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            }
        }else{
            System.out.println("NO ESTA LLENO");
        }

        return isFill;
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
        TextView opcionVisitaInfo;
        TextView opcionLocalInfo;
        TextView opcionEmpateInfo;
        ImageButton btnInfoBet;
        Button equipoLocal;
        Button equipoVisita;
        Button opcionEmpate;
        ImageView escudoLocal;
        ImageView escudoVisita;


        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            horario = (TextView) itemView.findViewById(R.id.horario);
            opcionVisitaInfo = (TextView) itemView.findViewById(R.id.opcionVisitaInfo);
            opcionLocalInfo = (TextView) itemView.findViewById(R.id.opcionLocalInfo);
            opcionEmpateInfo = (TextView) itemView.findViewById(R.id.opcionEmpateInfo);
            btnInfoBet = (ImageButton) itemView.findViewById(R.id.btnInfoBet);
            equipoLocal = (Button) itemView.findViewById(R.id.equipoLocal);
            equipoVisita = (Button) itemView.findViewById(R.id.equipoVisita);
            opcionEmpate = (Button) itemView.findViewById(R.id.opcionEmpate);
            escudoLocal = (ImageView) itemView.findViewById(R.id.escudoLocal);
            escudoVisita = (ImageView) itemView.findViewById(R.id.escudoVisita);

        }
    }

}
