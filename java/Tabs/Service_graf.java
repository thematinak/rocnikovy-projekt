package Tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.martin.rocnikovyprojekt.MainActivity;
import com.example.martin.rocnikovyprojekt.R;

import classes.ptransfer.GameParcelable;
import classes.ptransfer.MatchParcelable;
import classes.ptransfer.TennisSetParcelable;
import classes.ptransfer.VymenaParcelable;

/**
 * Created by Martin on 15.5.2016.
 */
public class Service_graf extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private MatchParcelable m;

    public Service_graf() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.graph, container, false);

        //TextView textView = (TextView) rootView.findViewById(R.id.stats1);
        Bundle b = this.getArguments();
        m = b.getParcelable("data");
        int hrac = b.getInt("hrac");
        Intent intent = getActivity().getIntent();
        int x =(int) intent.getFloatExtra("sirka", -1);
        int y =(int) intent.getFloatExtra("vyska", -1);
        RelativeLayout rel = (RelativeLayout) rootView.findViewById(R.id.Graf_Rel_Lout);
        rel.getLayoutParams().height= y + 20;
        ImageView kurt = (ImageView) rootView.findViewById(R.id.Graph_img);
        kurt.getLayoutParams().width= x;
        kurt.getLayoutParams().height= y;


        TextView name = (TextView) rootView.findViewById(R.id.Graph_name);
        name.setText(m.p1);
        Bitmap bmp = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(bmp);
        for (TennisSetParcelable item : m) {
            for (GameParcelable g : item) {
                if (g.serve == MainActivity.SERVE_P2)
                    continue;
                for (VymenaParcelable v : g) {
                    Paint p = new Paint(Color.BLACK);
                    if (v.list.get(0).out || v.list.get(0).type == "net")
                        p.setColor(Color.RED);
                    if (v.list.get(0).winner)
                        p.setColor(Color.GREEN);
                    cv.drawCircle(v.list.get(0).x , v.list.get(0).y , 10, p);
                    if (v.secServ) {
                        Paint p2 = new Paint(Color.BLACK);
                        if (v.list.get(1).out || v.list.get(1).type == "net")
                            p2.setColor(Color.RED);
                        if (v.list.get(1).winner)
                            p2.setColor(Color.GREEN);
                        cv.drawCircle(v.list.get(1).x , v.list.get(1).y , 10, p2);
                    }

                }
            }
        }
        kurt.setImageBitmap(bmp);
        return rootView;
    }
}
