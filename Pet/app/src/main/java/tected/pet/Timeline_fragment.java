package tected.pet;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        tv.setText("mentira");

        return v;
    }
}
