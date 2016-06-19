package tected.pet;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by erick on 18/06/16.
 */
public class Timeline_fragment extends Fragment {
    private static View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.timeline_fragment, container, false);
        super.onCreate(savedInstanceState);

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
        //tv.setText("mentira");

        return v;
    }
}
