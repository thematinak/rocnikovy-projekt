package Tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.martin.rocnikovyprojekt.MainActivity;
import com.example.martin.rocnikovyprojekt.R;

import classes.ptransfer.GameParcelable;
import classes.ptransfer.HitParcelable;
import classes.ptransfer.MatchParcelable;
import classes.ptransfer.TennisSetParcelable;
import classes.ptransfer.VymenaParcelable;

/**
 * Created by Martin on 17.5.2016.
 */
public class HitMap extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hit_map_fragment, container, false);

        Bundle b = this.getArguments();
        MatchParcelable m = b.getParcelable("data");
        Intent intent = getActivity().getIntent();
        int x = (int) intent.getFloatExtra("sirka", -1);
        int y = (int) intent.getFloatExtra("vyska", -1);
        ImageView mapa = (ImageView) rootView.findViewById(R.id.mapaP1);
        mapa.getLayoutParams().width = x;
        mapa.getLayoutParams().height = y;
        TextView name = (TextView) rootView.findViewById(R.id.hit_map_namep1);
        name.setText(m.p1);
        ImageView mapa2 = (ImageView) rootView.findViewById(R.id.mapaP2);
        mapa2.getLayoutParams().width = x;
        mapa2.getLayoutParams().height = y;
        name = (TextView) rootView.findViewById(R.id.hit_map_namep2);
        name.setText(m.p2);
        ScrollView s = (ScrollView) rootView.findViewById(R.id.scroll_bar);
        s.getLayoutParams().height = y + 20;


        String uder = b.getString("uder");
        Bitmap bmp = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Bitmap bmp2 = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(bmp);
        Canvas cv2 = new Canvas(bmp2);
        for (TennisSetParcelable item : m) {
            for (GameParcelable g : item) {
                for (VymenaParcelable v : g) {
                    int count = 0;
                    for (HitParcelable h : v) {
                        if (h.type.equals(uder)) {
                            Paint p = new Paint(Color.BLACK);
                            if (h.out || h.type == "net")
                                p.setColor(Color.RED);
                            if (h.winner)
                                p.setColor(Color.GREEN);
                            if(count %2 == 0) {
                                cv.drawCircle(h.x, h.y, 10, p);
                            }else{
                                cv2.drawCircle(h.x, h.y, 10, p);
                            }
                        }
                        ++count;
                    }
                }
            }
        }
        ImageView i = (ImageView) rootView.findViewById(R.id.mapaP1);
        i.setImageBitmap(bmp);
        ImageView im = (ImageView) rootView.findViewById(R.id.mapaP2);
        im.setImageBitmap(bmp2);


        return rootView;
    }
}
