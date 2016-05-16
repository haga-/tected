package tected.pet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by hut8 on 5/11/16.
 */
public class PetInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_info);
        Intent i = getIntent();
        Double latitude = i.getDoubleExtra("latitude", 0.0f);
        Double longitude = i.getDoubleExtra("longitude", 0.0f);

        Button buttonPublicar = (Button) findViewById(R.id.ButtonPublicar);


    }


}
