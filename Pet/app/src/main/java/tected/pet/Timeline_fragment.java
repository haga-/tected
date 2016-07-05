package tected.pet;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import tected.pet.adapters.TimelineItemAdapter;
import tected.pet.model.TimelineItem;

/**
 * Created by erick on 18/06/16.
 */
public class Timeline_fragment extends Fragment implements RecyclerViewOnClickListenerHack{
    private static View rootview;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<TimelineItem> tlItems;
    TimelineItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.timeline_fragment, container, false);
        super.onCreate(savedInstanceState);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewTimeline);

        tlItems = new ArrayList<>();

//        tlItems.add(new TimelineItem("titulo0","descricao0", "data0", "tipo0", "link0"));
//        tlItems.add(new TimelineItem("titulo1","descricao1", "data1", "tipo1", "link1"));
//        tlItems.add(new TimelineItem("titulo2","descricao2", "data2", "tipo2", "link2"));
//        tlItems.add(new TimelineItem("titulo3","descricao3", "data3", "tipo3", "link3"));
//        tlItems.add(new TimelineItem("titulo4","descricao4", "data4", "tipo4", "link4"));
//        tlItems.add(new TimelineItem("titulo5","descricao5", "data5", "tipo5", "link5"));
//        tlItems.add(new TimelineItem("titulo6","descricao6", "data6", "tipo6", "link6"));
//        tlItems.add(new TimelineItem("titulo7","descricao7", "data7", "tipo7", "link7"));
//        tlItems.add(new TimelineItem("titulo8","descricao8", "data8", "tipo8", "link8"));
//        tlItems.add(new TimelineItem("titulo9","descricao9", "data9", "tipo9", "link9"));

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getActivity().getApplicationContext()).deleteRealmIfMigrationNeeded().build();
        // Get a Realm instance for this thread
        Realm realm = Realm.getInstance(realmConfig);
        final RealmResults<Cadastro> cadastros = realm.where(Cadastro.class).findAll();

        for (Cadastro c : cadastros) {
            tlItems.add(new TimelineItem(c.getNomePet(), c.getCaracteristicasPet(), c.getDataDeCriacao().toString(), c.getTipo() + "", ""));
            Log.i("Log",tlItems.size()+"");
        }

        realm.close();

        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setScrollbarFadingEnabled(true);
        recyclerView.setHasFixedSize(true);

        adapter = new TimelineItemAdapter(tlItems, getActivity().getApplicationContext());
        //adapter.setRecyclerViewOnClickListenerHack(this); //"Pq o proprio fragment esta implementando"
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), recyclerView, this));
        recyclerView.setAdapter(adapter);


        /*
        TextView tv = (TextView) v.findViewById(R.id.texto);
        Button button = (Button) v.findViewById(R.id.buttonDelete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
                RealmConfiguration realmConfig = new RealmConfiguration.Builder(getActivity().getApplicationContext()).deleteRealmIfMigrationNeeded().build();
                // Get a Realm instance for this thread
                Realm realm = Realm.getInstance(realmConfig);
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
                Toast.makeText(getActivity().getApplicationContext(), "deletado?", Toast.LENGTH_SHORT).show();
            }
        });
        */
        //tv.setText("mentira");

        return v;
    }

    @Override
    public void onClickListener(View v, int position) {
        Intent intent = new Intent(getActivity(), Timeline_FullScreen_activity.class);
        //intent.putExtra("Timeline", tlItems.get(position));
        intent.putExtra("Timeline", position);
        startActivity(intent);
    }

//    @Override
//    public void onViewStateRestored(Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getActivity().getApplicationContext()).deleteRealmIfMigrationNeeded().build();
//        // Get a Realm instance for this thread
//        Realm realm = Realm.getInstance(realmConfig);
//        final RealmResults<Cadastro> cadastros = realm.where(Cadastro.class).findAll();
//
//        for (Cadastro c : cadastros) {
//            tlItems.add(new TimelineItem(c.getNomePet(), c.getCaracteristicasPet(), c.getDataDeCriacao().toString(), c.getTipo() + "", ""));
//            Log.i("Log",tlItems.size()+"");
//        }
//
//        realm.close();
//    }

    @Override
    public void onResume() {
        super.onResume();
        tlItems.clear();
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getActivity().getApplicationContext()).deleteRealmIfMigrationNeeded().build();
        // Get a Realm instance for this thread
        Realm realm = Realm.getInstance(realmConfig);
        final RealmResults<Cadastro> cadastros = realm.where(Cadastro.class).findAll();

        for (Cadastro c : cadastros) {
            tlItems.add(new TimelineItem(c.getNomePet(), c.getCaracteristicasPet(), c.getDataDeCriacao().toString(), c.getTipo() + "", ""));
            Log.i("Log",tlItems.size()+"");
        }

        realm.close();
    }

    private static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecyclerViewTouchListener(Context mContext, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh) {
            this.mContext = mContext;
            mRecyclerViewOnClickListenerHack = rvoclh;
            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if (cv != null && mRecyclerViewOnClickListenerHack != null) {
                        mRecyclerViewOnClickListenerHack.onClickListener(cv, rv.getChildAdapterPosition(cv));
                    }
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
