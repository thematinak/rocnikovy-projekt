package Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.martin.rocnikovyprojekt.MainActivity;
import com.example.martin.rocnikovyprojekt.R;

import java.util.ArrayList;

import classes.ptransfer.GameParcelable;
import classes.ptransfer.HitParcelable;
import classes.ptransfer.MatchParcelable;
import classes.ptransfer.TennisSetParcelable;
import classes.ptransfer.VymenaParcelable;

/**
 * Created by Martin on 16.5.2016.
 */
public class HitTab extends Fragment {

    private MatchParcelable m;
    private ArrayList<String> stats;

    public HitTab() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistika, container, false);

        Bundle b = this.getArguments();
        m = b.getParcelable("data");
        stats = new ArrayList<>();
        Intent intent = getActivity().getIntent();
        float x = intent.getFloatExtra("sirka", -1);
        float y = intent.getFloatExtra("vyska", -1);
        ScrollView s = (ScrollView) rootView.findViewById(R.id.scroll_bar);
        s.getLayoutParams().height = (int) y + 20;
        String uder = b.getString("uder");
        //right, left, mid-nahravky
        int right = 0, rightp2 = 0;
        int mid = 0, midp2 = 0;
        int left = 0, leftp2 = 0; //mam
        //longy
        int lon = 0, lonp2 = 0;         //mam
        //crossi
        int cros = 0, crosp2 = 0;
        //insideout
        int inside = 0, insidep2 = 0;   //mam
        //winneri
        int win = 0, winp2 = 0;         //mam
        //net
        int net = 0, netp2 = 0;         //mam
        //out dlzka
        int dlzka = 0, dlzkap2 = 0;     //mam
        //out sirka
        int sirka = 0, sirkap2 = 0;     //mam

        for (TennisSetParcelable item : m) {
            for (GameParcelable g : item) {
                for (VymenaParcelable v : g) {
                    int count = 0;
                    for (HitParcelable h : v) {
                        if (h.type.equals(uder)) {
                            if (count % 2 == 0) {
                                if (isOut(h)) {
                                    if (h.type == "net") {
                                        ++net;
                                    } else {
                                        if (isTooLong(x, h)) {
                                            ++dlzka;
                                        }
                                        if (badWidth(y, h)) {
                                            ++sirka;
                                        }
                                    }
                                }
                                if (h.winner)
                                    ++win;
                                boolean jeDalsi = false;
                                if (v.list.size() >= count + 2) {//count je idenx teda ho zvacsit o 2
                                    jeDalsi = true;
                                }
                                if (wasLeft(h, (int) y, 0)) {
                                    ++left;
                                    if (jeDalsi && wasRight(v.list.get(count + 1), (int) y, 0)) {
                                        ++inside;
                                    }
                                }
                                if (wasMid(h, (int) y)) {
                                    ++mid;
                                    if (jeDalsi && wasRight(v.list.get(count + 1), (int) y, 0)) {
                                        ++inside;
                                    }
                                }
                                if (wasRight(h, (int) y, 0)) {
                                    ++right;
                                    if (jeDalsi && wasRight(v.list.get(count + 1), (int) y, 0)) {
                                        ++lon;
                                    }
                                    if (jeDalsi && wasLeft(v.list.get(count + 1), (int) y, 0)) {
                                        ++cros;
                                    }
                                }
                            } else {
                                if (isOut(h)) {
                                    if (h.type == "net") {
                                        ++netp2;
                                    } else {
                                        if (isTooLong(x, h)) {
                                            ++dlzkap2;
                                        }
                                        if (badWidth(y, h)) {
                                            ++sirkap2;
                                        }
                                    }
                                }
                                if (h.winner)
                                    ++winp2;
                                boolean jeDalsi = false;
                                if (v.list.size() >= count + 2) {//count je idenx teda ho zvacsit o 2
                                    jeDalsi = true;
                                }
                                if (wasLeft(h, (int) y, 0)) {
                                    ++leftp2;
                                    if (jeDalsi && wasRight(v.list.get(count + 1), (int) y, 0)) {
                                        ++insidep2;
                                    }
                                }
                                if (wasMid(h, (int) y)) {
                                    ++midp2;
                                    if (jeDalsi && wasRight(v.list.get(count + 1), (int) y, 0)) {
                                        ++insidep2;
                                    }
                                }
                                if (wasRight(h, (int) y, 0)) {
                                    ++rightp2;
                                    if (jeDalsi && wasRight(v.list.get(count + 1), (int) y, 0)) {
                                        ++lonp2;
                                    }
                                    if (jeDalsi && wasLeft(v.list.get(count + 1), (int) y, 0)) {
                                        ++crosp2;
                                    }
                                }
                            }
                        }
                        ++count;
                    }
                }
            }
        }

        stats.add(String.format("%-14s    :    %14s", m.p1, m.p2));
        stats.add(String.format("%-14s Winner  %14s", Integer.toString(win), Integer.toString(winp2)));
        stats.add(String.format("%-16s   Net   %16s", Integer.toString(net), Integer.toString(netp2)));
        stats.add(String.format("%-14s Out-long %13s", Integer.toString(dlzka), Integer.toString(dlzkap2)));
        stats.add(String.format("%-14s Out-side %13s", Integer.toString(sirka), Integer.toString(sirkap2)));
        stats.add(String.format("%-14s Long-line %12s", Integer.toString(lon), Integer.toString(lonp2)));
        stats.add(String.format("%-15s  Cross  %15s", Integer.toString(cros), Integer.toString(crosp2)));
        stats.add(String.format("%-14s Inside-out %11s", Integer.toString(inside), Integer.toString(insidep2)));
        stats.add(String.format("%-14s Area: Right %10s", Integer.toString(right), Integer.toString(rightp2)));
        stats.add(String.format("%-11s Area: Middle %9s", Integer.toString(mid), Integer.toString(midp2)));
        stats.add(String.format("%-14s Area: Left %12s", Integer.toString(left), Integer.toString(leftp2)));

        nastavText(rootView);
        return rootView;
    }

    private boolean wasLeft(HitParcelable h, int vyska, int hrac) {
        if (isOut(h))
            return false;
        float offset = vyska / 3;
        float dole = offset * 2;
        float hore = offset * 1;

        if (hrac == MainActivity.SERVE_P1) {
            if (hore >= h.y)
                return true;
        } else {
            if (dole <= h.y)
                return true;
        }
        return false;
    }

    private boolean wasMid(HitParcelable h, int vyska) {
        if (isOut(h))
            return false;
        float offset = vyska / 3;
        float dole = offset * 3;
        float hore = offset * 1;

        if (hore <= h.y && dole >= h.y)
            return true;
        return false;
    }

    private boolean wasRight(HitParcelable h, int vyska, int hrac) {
        if (isOut(h))
            return false;
        float offset = vyska / 3;
        float dole = offset * 2;
        float hore = offset * 1;

        if (hrac == MainActivity.SERVE_P1) {
            if (dole <= h.y)
                return true;
        } else {
            if (hore >= h.y)
                return true;
        }
        return false;
    }

    private boolean badWidth(float maxY, HitParcelable h) {
        float relativeY = maxY / 171;
        if (h.y < (25 * relativeY) - 10 || h.y > (146 * relativeY) + 10)
            return true;
        return false;
    }

    private boolean isTooLong(float maxX, HitParcelable h) {
        float relativeX = maxX / 382;
        if (h.x < (18 * relativeX) - 10 || h.x > (354 * relativeX) + 10)
            return true;
        return false;
    }

    private boolean isOut(HitParcelable h) {

        return (h.out || h.type == "net");
    }

    private void nastavText(View rootView) {
        TextView t1 = (TextView) rootView.findViewById(R.id.stats1);
        t1.setText(stats.get(0));
        TextView t2 = (TextView) rootView.findViewById(R.id.stats2);
        t2.setText(stats.get(1));
        TextView t3 = (TextView) rootView.findViewById(R.id.stats3);
        t3.setText(stats.get(2));
        TextView t4 = (TextView) rootView.findViewById(R.id.stats4);
        t4.setText(stats.get(3));
        TextView t5 = (TextView) rootView.findViewById(R.id.stats5);
        t5.setText(stats.get(4));
        TextView t6 = (TextView) rootView.findViewById(R.id.stats6);
        t6.setText(stats.get(5));
        TextView t7 = (TextView) rootView.findViewById(R.id.stats7);
        t7.setText(stats.get(6));
        TextView t8 = (TextView) rootView.findViewById(R.id.stats8);
        t8.setText(stats.get(7));
        TextView t9 = (TextView) rootView.findViewById(R.id.stats9);
        t9.setText(stats.get(8));
        TextView t10 = (TextView) rootView.findViewById(R.id.stats10);
        t10.setText(stats.get(9));
        TextView t11 = (TextView) rootView.findViewById(R.id.stats11);
        t11.setText(stats.get(10));
    }
}
