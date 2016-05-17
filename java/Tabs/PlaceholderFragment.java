package Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.martin.rocnikovyprojekt.MainActivity;
import com.example.martin.rocnikovyprojekt.R;

/**
 * Created by Martin on 14.5.2016.
 */
public class PlaceholderFragment extends Fragment {


    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment newInstance(int sectionNumber) {
        Fragment f = null;
        Bundle b = new Bundle();
        switch (sectionNumber) {
            case 1:
                f = new ServeTab();
                break;
            case 2:

                b.putString("uder", MainActivity.TYPE_SERVE);
                f = new HitMap();
                break;
            case 3:
                b.putString("uder", MainActivity.TYPE_FORHAND);
                f = new HitTab();
                break;
            case 4:
                b.putString("uder", MainActivity.TYPE_FORHAND);
                f = new HitMap();
                break;
            case 5:
                b.putString("uder", MainActivity.TYPE_BACKHAND);
                f = new HitTab();
                break;
            case 6:
                b.putString("uder", MainActivity.TYPE_BACKHAND);
                f = new HitMap();
                break;
            default:
                f = new PlaceholderFragment();
        }
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistika, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.stats1);
        return rootView;
    }
}