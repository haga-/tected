package tected.pet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.annotations.Ignore;
import tected.pet.model.Status;

/**
 * Created by hut8 on 5/11/16.
 */
public class PetInfo extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String especie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_info);
        Intent i = getIntent();
        final Double latitude = i.getDoubleExtra("latitude", 0.0f);
        final Double longitude = i.getDoubleExtra("longitude", 0.0f);
        final int tipo = i.getIntExtra("Tipo", -1);
        //final Status tipo = (Status) i.getSerializableExtra("Tipo");

        Button buttonPublicar = (Button) findViewById(R.id.ButtonPublicar);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.especies, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        final TextInputEditText nomePet = (TextInputEditText) findViewById(R.id.nome);
        final TextInputEditText racaPet = (TextInputEditText) findViewById(R.id.raca);
        final TextInputEditText generoPet = (TextInputEditText) findViewById(R.id.genero);
        final TextInputEditText caracteristicasPet = (TextInputEditText) findViewById(R.id.carateristicas);
        final TextInputEditText nomeDono = (TextInputEditText) findViewById(R.id.nomeDono);
        final TextInputEditText enderecoDono = (TextInputEditText) findViewById(R.id.enderecoDono);
        final TextInputEditText ultimoEndereco = (TextInputEditText) findViewById(R.id.ultimoEndereco);
        final TextInputEditText telefone = (TextInputEditText) findViewById(R.id.telefone);


        assert buttonPublicar != null;
        buttonPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
                RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).deleteRealmIfMigrationNeeded().build();
                // Get a Realm instance for this thread
                Realm realm = Realm.getInstance(realmConfig);

                if(nomePet.getText().length() > 0 && caracteristicasPet.getText().length() > 0
                        && nomeDono.getText().length() > 0 && ultimoEndereco.getText().length() > 0){

                    Cadastro c = new Cadastro(
                            nomePet.getText().toString(),
                            especie,
                            racaPet.getText().toString(),
                            generoPet.getText().toString(),
                            caracteristicasPet.getText().toString(),
                            nomeDono.getText().toString(),
                            enderecoDono.getText().toString(),
                            ultimoEndereco.getText().toString(),
                            telefone.getText().toString(),
                            latitude,
                            longitude,
                            tipo
                    );

                    realm.beginTransaction();
                    realm.copyToRealm(c);
                    realm.commitTransaction();
                    realm.close();

                    Toast.makeText(getApplicationContext(), "Cadastro Inserido.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Snackbar.make(findViewById(R.id.ButtonPublicar), "Por favor, insira as informações obrigatórias.", Snackbar.LENGTH_SHORT);
                }



            }
        });



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i("log", parent.getItemAtPosition(position).toString());
        especie = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
