package tected.pet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tected.pet.model.TimelineItem;

/**
 * Created by erick on 20/06/16.
 */
public class Timeline_FullScreen_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_fullscreen_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        assert toolbar != null;
        toolbar.setTitle("Informações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        final TimelineItem item = intent.getParcelableExtra("Timeline");


        TextView descricao = (TextView) findViewById(R.id.descricaoItem_fullscreen);
        assert descricao != null;
        descricao.setText(item.getDescricao());

        Button b = (Button) findViewById(R.id.botaoLocalizei);
        assert b != null;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

// 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Informações para contato:\n" + "Nome: " + item.getTipo() + "\nTelefone: " + item.getLink())
                        .setTitle("Contate o dono");

// 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
