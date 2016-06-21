package tected.pet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tected.pet.R;
import tected.pet.RecyclerViewOnClickListenerHack;
import tected.pet.model.TimelineItem;

/**
 * Created by erick on 20/06/16.
 */
public class TimelineItemAdapter extends RecyclerView.Adapter<TimelineItemAdapter.TimelineItemViewHolder>{
    private static final int TIPO_GRANDE = 1;
    private static final int TIPO_PEQUENO = 2;
    private List<TimelineItem> timelineItemList;
    private Context context;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    public TimelineItemAdapter(List<TimelineItem> timelineItemList, Context context){
        this.timelineItemList = timelineItemList;
        Log.d("mapa", timelineItemList.toString());
        this.context = context;
    }


    @Override
    public TimelineItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.i("LOG", "onCreateViewHolder()");
        //View v = mLayoutInflater.inflate(R.layout.timeline_item, parent, false);

        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_item, parent, false);
        //NoticiaViewHolder nvh = new NoticiaViewHolder(v);

        View v;
        final TimelineItemViewHolder vh;
        /*
        if (viewType == TIPO_GRANDE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.noticias_item_grande, parent, false);
        }
        else{ // TIPO_PEQUENO:
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_item, parent, false);
        }
        */
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_item, parent, false);
        vh = new TimelineItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TimelineItemViewHolder holder, int position) {
        TimelineItem tli= timelineItemList.get(position);
        /*
        if ( noticia.getUrlImagem(Noticia.LOW_RES) != null){
            holder.imagemNoticia.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(noticia.getUrlImagem(Noticia.LOW_RES))
                    .placeholder(R.mipmap.noticia_padrao)
                    .error(R.mipmap.noticia_padrao)
                    .into(holder.imagemNoticia);
            holder.parteDaNoticia.setText("");
        }
        else{
            if( position != 0 ){
                holder.imagemNoticia.setVisibility(View.GONE);
            }
            if(noticia.hasImg()){
                holder.parteDaNoticia.setText("");
                holder.parteDaNoticia.setVisibility(View.GONE);
            }
            else{
                holder.parteDaNoticia.setText(noticia.getParte());
                //Log.d("Noticia", noticia.getParte() + "\n");
            }

        }


        holder.tituloNoticia.setText(noticia.getTitulo());

        */
        holder.tituloItem.setText("Titulo Position " + position);
        holder.parteDoItem.setText("Parte do item Position"  + position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
        //return position == 0 ? TIPO_GRANDE : TIPO_PEQUENO;
    }


    @Override
    public int getItemCount() {
        return timelineItemList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }

    public class TimelineItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tituloItem;
        private TextView parteDoItem;

        public TimelineItemViewHolder(View itemView) {
            super(itemView);

            tituloItem = (TextView) itemView.findViewById(R.id.tituloItem);
            parteDoItem = (TextView) itemView.findViewById(R.id.parteDoItem);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
            }
        }
    }


}

