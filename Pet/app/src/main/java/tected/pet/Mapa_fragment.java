package tected.pet;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import tected.pet.model.Evento;
import tected.pet.model.Status;

/**
 * Created by erick on 18/06/16.
 */
public class Mapa_fragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private static View rootView;
    private Location location = null;
    //Map<Marker, Cadastro> markerCadastroMap;
    private FloatingActionMenu menu ;
    private FloatingActionButton cat;
    private FloatingActionButton dog;
    private FloatingActionButton clear;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.mapa_fragment, container, false);
        //markerCadastroMap = new HashMap<>();

        MapFragment mapFragment = new MapFragment().newInstance();


        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)
            mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.busMap);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.busMap);

        mapFragment.getMapAsync(this);

        Log.d("mapa","noOnCreate");
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
                setLocation(location);

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("mapa", "dentro do if das permissoes");
            return rootView;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        Log.d("mapa", "passou pelo locationManager");


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        menu = (FloatingActionMenu) rootView.findViewById(R.id.menu);
        cat = (FloatingActionButton) rootView.findViewById(R.id.cat);
        dog = (FloatingActionButton) rootView.findViewById(R.id.dog);
        clear = (FloatingActionButton) rootView.findViewById(R.id.clear);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);

        cat.setOnClickListener(clickListener);
        dog.setOnClickListener(clickListener);
        clear.setOnClickListener(clickListener);

        menu.hideMenu(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                menu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(rootView.getContext(), R.anim.fab_scale_up));
                menu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(rootView.getContext(), R.anim.fab_scale_down));
            }
        }, 150);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    if (positionOffsetPixels > 730)
                        menu.showMenu(true);
                    else
                        menu.hideMenu(true);
                }
                else if  (position == 1) {
                    if (positionOffsetPixels > 100)
                        menu.hideMenu(true);
                    else
                        menu.showMenu(true);
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private  View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(getActivity().getApplicationContext()).deleteRealmIfMigrationNeeded().build();
            // Get a Realm instance for this thread
            Realm realm = Realm.getInstance(realmConfig);
            final RealmResults<Cadastro> cadastros = realm.where(Cadastro.class).findAll();
            switch(v.getId()) {
                case R.id.cat:
                    mMap.clear();
                    for(Cadastro c: cadastros){
                        MarkerOptions markerOptions = null;
                        if (c.getEspecie().equals("Gato")){
                            switch (c.getTipo()) {
                                case Status.PERDIDO: {
                                    markerOptions = new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                            .position(new LatLng(c.getLatitude(), c.getLongitude())).title(c.getNomePet());
                                    break;
                                }
                                case Status.VISTO:{
                                    markerOptions = new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                            .position(new LatLng(c.getLatitude(), c.getLongitude())).title(c.getNomePet());
                                    break;
                                }
                                case Status.ENCONTRADO: {
                                    markerOptions = new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                            .position(new LatLng(c.getLatitude(), c.getLongitude())).title(c.getNomePet());
                                    break;
                                }
                            }
                        }
                        if (mMap != null && markerOptions != null) {
                            mMap.addMarker(markerOptions);
                        }
                    }
                    realm.close();
                    break;
                case R.id.dog:
                    mMap.clear();
                    for(Cadastro c: cadastros){
                        MarkerOptions markerOptions = null;
                        if (c.getEspecie().equals("Cachorro")){
                            switch (c.getTipo()) {
                                case Status.PERDIDO: {
                                    markerOptions = new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                            .position(new LatLng(c.getLatitude(), c.getLongitude())).title(c.getNomePet());
                                    break;
                                }
                                case Status.VISTO:{
                                    markerOptions = new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                            .position(new LatLng(c.getLatitude(), c.getLongitude())).title(c.getNomePet());
                                    break;
                                }
                                case Status.ENCONTRADO: {
                                    markerOptions = new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                            .position(new LatLng(c.getLatitude(), c.getLongitude())).title(c.getNomePet());
                                    break;
                                }
                            }
                        }
                        if (mMap != null && markerOptions != null) {
                            mMap.addMarker(markerOptions);
                        }
                    }
                    realm.close();
                    break;
                case R.id.clear:
                    mMap.clear();
                    for(Cadastro c: cadastros){
                        MarkerOptions markerOptions = null;
                        switch (c.getTipo()) {
                            case Status.PERDIDO: {
                                markerOptions = new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                        .position(new LatLng(c.getLatitude(), c.getLongitude())).title(c.getNomePet());
                                break;
                            }
                            case Status.VISTO:{
                                markerOptions = new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                        .position(new LatLng(c.getLatitude(), c.getLongitude())).title(c.getNomePet());
                                break;
                            }
                            case Status.ENCONTRADO: {
                                markerOptions = new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                        .position(new LatLng(c.getLatitude(), c.getLongitude())).title(c.getNomePet());
                                break;
                            }
                        }

                        if (mMap != null && markerOptions != null) {
                            mMap.addMarker(markerOptions);
                        }
                    }
                    realm.close();
                    break;
            }

        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        CameraPosition cameraPosition;
        Location location = getLocation();

        if(location == null) {
            //nao tem localização, vai pro padrão
            cameraPosition = new CameraPosition.Builder().
                    target(new LatLng(-31.749632, -52.336349)).
                    zoom((float) 12.5).
                    build();
            Log.d("mapa", "location null");
        }
        else{
            cameraPosition = CameraPosition.builder().
                    target(new LatLng(location.getLatitude(),location.getLongitude())).
                    zoom((float)13.0).
                    build();
            Log.d("mapa", "location not null");
        }


        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(final LatLng latLng) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Deseja avisar que um pet foi perdido?");

                final CharSequence[] opcoes = {"Perdi meu pet!", "Avistei um pet perdido!", "Não"};

                builder.setItems(opcoes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which != 2) {
                            Intent i = new Intent(getActivity().getApplicationContext(), PetInfo.class);
                            i.putExtra("latitude", latLng.latitude);
                            i.putExtra("longitude", latLng.longitude);
                            if (which == 0) { //perdi meu pet
                                i.putExtra("Tipo", Status.PERDIDO);
                            } else { //foi somente avistado
                                i.putExtra("Tipo", Status.VISTO);
                            }
                            startActivity(i);
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        /*
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
                RealmConfiguration realmConfig = new RealmConfiguration.Builder(getActivity().getApplicationContext()).deleteRealmIfMigrationNeeded().build();
                // Get a Realm instance for this thread
                Realm realm = Realm.getInstance(realmConfig);
                realm.beginTransaction();
                //realm.where(Cadastro.class).findFirst().
                Cadastro c = markerCadastroMap.get(marker);
                final RealmResults<Cadastro> found = realm.where(Cadastro.class).equalTo("nomeDono",);

                Log.d("mapa", c.getNomeDono() + " " + c.getTipo());
                realm.close();

                return true;
            }
        });*/



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
            MarkerOptions markerOptions = null;

            switch (c.getTipo()) {
                case Status.PERDIDO: {
                    markerOptions = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .position(new LatLng(c.getLatitude(), c.getLongitude())).title(c.getNomePet());
                    break;
                }
                case Status.VISTO:{
                    markerOptions = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .position(new LatLng(c.getLatitude(), c.getLongitude())).title(c.getNomePet());
                    break;
                }
                case Status.ENCONTRADO: {
                    markerOptions = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .position(new LatLng(c.getLatitude(), c.getLongitude())).title(c.getNomePet());
                    break;
                }
            }
            if (mMap != null && markerOptions != null) {
                mMap.addMarker(markerOptions);
                //Marker marker = mMap.addMarker(markerOptions);
                //markerCadastroMap.put(marker, c);
            }
        }
        realm.close();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}


