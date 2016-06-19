package tected.pet;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by erick on 18/06/16.
 */
public class Mapa_fragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private static View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.mapa_fragment, container, false);

        MapFragment mapFragment = new MapFragment().newInstance();


        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)
            mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.busMap);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.busMap);

        mapFragment.getMapAsync(this);


        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        CameraPosition cameraPosition = new CameraPosition.Builder().
                target(new LatLng(-31.749632, -52.336349)).
                zoom((float)12.5).
                build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(final LatLng latLng) {
                /*
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity().getApplicationContext(), R.style.AppTheme).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Inserir Pet Perdido");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                /*
                                Intent i = new Intent(getActivity().getApplicationContext(), PetInfo.class);
                                i.putExtra("latitude",latLng.latitude);
                                i.putExtra("longitude",latLng.longitude);
                                startActivity(i);
                                *
                            }
                        });
                alertDialog.show();
                */

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Deseja avisar que um pet foi perdido?")
                        .setTitle("Inserir Pet Perdido");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getActivity().getApplicationContext(), PetInfo.class);
                        i.putExtra("latitude",latLng.latitude);
                        i.putExtra("longitude",latLng.longitude);
                        startActivity(i);
                        //Toast.makeText(getActivity().getApplicationContext(),"User clicked OK button", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(getActivity().getApplicationContext(),"User cancelled the dialog", Toast.LENGTH_SHORT).show();
                    }
                });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getActivity().getApplicationContext()).deleteRealmIfMigrationNeeded().build();
        // Get a Realm instance for this thread
        Realm realm = Realm.getInstance(realmConfig);


        final RealmResults<Cadastro> cadastros = realm.where(Cadastro.class).findAll();
        for(Cadastro c: cadastros){
            Log.i("Main", c.getNomeDono() + " " + c.getTelefone());
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(c.getLatitude(), c.getLongitude())).title(c.getNomePet());
            if (mMap != null) {
                mMap.addMarker(markerOptions);
            }
        }
        realm.close();
    }
}


