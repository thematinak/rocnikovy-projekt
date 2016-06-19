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
import java.util.List;

import classes.ptransfer.GameParcelable;
import classes.ptransfer.HitParcelable;
import classes.ptransfer.MatchParcelable;
import classes.ptransfer.TennisSetParcelable;
import classes.ptransfer.VymenaParcelable;

/**
 * Created by Martin on 14.5.2016.
 */
public class ServeTab extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private MatchParcelable m;
    private ArrayList<String> stats;


    public ServeTab() {
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
        //1 first serve
        float in = 0, inp2 = 0;
        int max = 0, maxp2 = 0;         //mam
        //2 aces
        int aces = 0, acesp2 = 0;       //mam
        //3 Double folts
        int Dfolts = 0, Dfoltsp2 = 0;   //mam
        //4 out
        int out = 0, outp2 = 0;              //nem
        //5 net
        int net = 0, netp2 = 0;
        //6 Winn After 1st serve
        int Wafs = 0, Wafsp2 = 0;       //mam
        //7 right corner
        int right = 0, rightp2 = 0;         //mam
        //8 left corner
        int left = 0, leftp2 = 0;       //mam
        //9 middle
        int middle = 0, middlep2 = 0;   //mam
        //10 Point from serve - aces
        int pntfs = 0, pntfsp2 = 0;     //mam
        for (TennisSetParcelable item : m) {
            for (GameParcelable g : item) {
                int count = 0;
                for (VymenaParcelable v : g) {
                    List<HitParcelable> l = v.list;
                    if (g.serve == MainActivity.SERVE_P1) {
                        if (!v.secServ)
                            ++in;
                        ++max;
                        if (l.get(0).winner) {
                            ++aces;
                        }
                        if (v.secServ) {
                            if (l.get(1).winner)
                                ++aces;
                        }
                        if (v.secServ && isOut(l.get(1))) {
                            ++Dfolts;
                        }
                        if (!v.secServ && ((l.size() % 2 == 0 && isOut(l.get(l.size() - 1))) || (l.size() % 2 == 1 && l.get(l.size() - 1).winner))) {
                            ++Wafs;
                        }
                        if ((!v.secServ && l.size() == 2) || (v.secServ && l.size() == 3)) {
                            ++pntfs;
                        }
                        if ((!v.secServ && wasRight(l.get(0), y, g.serve, count)) || (v.secServ && wasRight(l.get(1), y, g.serve, count))) {
                            ++right;
                        }
                        if ((!v.secServ && wasMiddle(l.get(0), y, g.serve, count)) || (v.secServ && wasMiddle(l.get(1), y, g.serve, count))) {
                            ++middle;
                        }
                        if ((!v.secServ && wasLeft(l.get(0), y, g.serve, count)) || (v.secServ && wasLeft(l.get(1), y, g.serve, count))) {
                            ++left;
                        }
                        if (l.get(0).type == "net") {
                            ++net;
                        }
                        if (v.secServ) {
                            ++out;
                            if (l.get(1).out) {
                                ++out;
                            }
                            if (l.get(1).type == "net") {
                                ++net;
                            }
                        }
                    } else {
                        if (!v.secServ)
                            ++inp2;
                        ++maxp2;
                        if (l.get(0).winner) {
                            ++acesp2;
                        }
                        if (v.secServ) {
                            if (l.get(1).winner)
                                ++acesp2;
                        }
                        if (v.secServ && isOut(l.get(1))) {
                            ++Dfoltsp2;
                        }
                        if (!v.secServ && ((l.size() % 2 == 0 && isOut(l.get(l.size() - 1))) || (l.size() % 2 == 1 && l.get(l.size() - 1).winner))) {
                            ++Wafsp2;
                        }
                        if ((!v.secServ && l.size() == 2) || (v.secServ && l.size() == 3)) {
                            ++pntfsp2;
                        }
                        if ((!v.secServ && wasRight(l.get(0), y, g.serve, count)) || (v.secServ && wasRight(l.get(1), y, g.serve, count))) {
                            ++rightp2;
                        }
                        if ((!v.secServ && wasMiddle(l.get(0), y, g.serve, count)) || (v.secServ && wasMiddle(l.get(1), y, g.serve, count))) {
                            ++middlep2;
                        }
                        if ((!v.secServ && wasLeft(l.get(0), y, g.serve, count)) || (v.secServ && wasLeft(l.get(1), y, g.serve, count))) {
                            ++leftp2;
                        }
                        if (l.get(0).type == "net") {
                            ++netp2;
                        }
                        if (v.secServ) {
                            ++outp2;
                            if (l.get(1).out) {
                                ++outp2;
                            }
                            if (l.get(1).type == "net") {
                                ++netp2;
                            }
                        }
                    }
                    ++count;
                }
            }
        }
        if (maxp2 == 0)
            maxp2 = 1;
        if (max == 0)
            max = 1;
        stats.add(String.format("%-14s    :    %14s", m.p1, m.p2));
        stats.add(String.format("%2.2f %-5s   1st Serve   %5s %2.2f ", (in * 100 / max), " ", " ", (inp2 * 100 / maxp2)));
        stats.add(String.format("%-14s   aces  %14s", Integer.toString(aces), Integer.toString(acesp2)));
        stats.add(String.format("%-9s Double folts %9s", Integer.toString(Dfolts), Integer.toString(Dfoltsp2)));
        stats.add(String.format("%-16s   out   %16s", Integer.toString(out), Integer.toString(outp2)));
        stats.add(String.format("%-16s   net   %16s", Integer.toString(net), Integer.toString(netp2)));
        stats.add(String.format("%-5s Win After 1st serve %5s", Integer.toString(Wafs), Integer.toString(Wafsp2)));
        stats.add(String.format("%-11s serve points %11s", Integer.toString(pntfs), Integer.toString(pntfsp2)));
        stats.add(String.format("%-11s serve to right %10s", Integer.toString(right), Integer.toString(rightp2)));
        stats.add(String.format("%-8s serve to middle %9s", Integer.toString(middle), Integer.toString(middlep2)));
        stats.add(String.format("%-12s serve to left %12s", Integer.toString(left), Integer.toString(leftp2)));

        nastavText(rootView);

        return rootView;
    }

    //prerob
    private boolean wasLeft(HitParcelable h, float vyska, int servuje, int vymVporadi) {
        if (isOut(h))
            return false;

        float offset = (vyska / 2) / 4;

        float horevl = offset * 2;
        //float midvl = offset * 3;
        //float dolevl = offset*4;

        float horevp = offset * 5;
        //float midvp = offset * 6;
        //float dolevp =0;
        if (MainActivity.SERVE_P1 == servuje) {
            if (vymVporadi % 2 == 0) {
                if (horevl >= h.y)
                    return true;
            } else {
                if (horevp >= h.y)
                    return true;
            }
        } else {
            if (vymVporadi % 2 == 0) {
                if (horevp >= h.y)
                    return true;
            } else {
                if (horevl >= h.y)
                    return true;
            }
        }
        return false;
    }

    private boolean wasRight(HitParcelable h, float vyska, int servuje, int vymVporadi) {
        if (isOut(h))
            return false;

        float offset = (vyska / 2) / 4;

        //float horevl = offset * 2;
        float midvl = offset * 3;
        //float dolevl = offset*4;

        //float horevp = offset * 5;
        float midvp = offset * 6;
        //float dolevp =0;

        if (MainActivity.SERVE_P1 == servuje) {
            if (vymVporadi % 2 == 0) {
                if (midvl <= h.y)
                    return true;
            } else {
                if (midvp <= h.y)
                    return true;
            }
        } else {
            if (vymVporadi % 2 == 0) {
                if (midvp <= h.y)
                    return true;
            } else {
                if (midvl <= h.y)
                    return true;
            }

        }
        return false;
    }

    private boolean wasMiddle(HitParcelable h, float vyska, int servuje, int vymVporadi) {
        if (isOut(h))
            return false;
        if (wasLeft(h, vyska, servuje, vymVporadi))
            return false;

        float offset = (vyska / 2) / 4;

        //float horevl = offset * 2;
        float midvl = offset * 3;
        //float dolevl = offset*4;

        //float horevp = offset * 5;
        float midvp = offset * 6;
        //float dolevp =0;

        if (MainActivity.SERVE_P1 == servuje) {
            if (vymVporadi % 2 == 0) {
                if (midvl >= h.y)
                    return true;
            } else {
                if (midvp >= h.y)
                    return true;
            }
        } else {
            if (vymVporadi % 2 == 0) {
                if (midvp >= h.y)
                    return true;
            } else {
                if (midvl >= h.y)
                    return true;
            }

        }

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
