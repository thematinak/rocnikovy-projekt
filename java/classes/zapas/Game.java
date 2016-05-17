package classes.zapas;


import java.util.ArrayList;
import java.util.Iterator;

import classes.body.BodyVgeme;
import classes.ptransfer.GameParcelable;
import classes.ptransfer.VymenaParcelable;

/**
 * Created by Martin on 30.12.2015.
 */
public class Game implements Iterable<Vymena> {
    private ArrayList<Vymena> list;
    private boolean end;
    private int vyhral, serve, p1, p2;
    private TennisSet tennisSet;
    private BodyVgeme score;

    public Game(TennisSet ts, int p1score, int p2score) {
        list = new ArrayList<>();

        p1 = p1score;
        p2 = p2score;
        tennisSet = ts;
        end = false;
        vyhral = -1;
        serve = ts.getMatch().getServe();
        ts.getMatch().setInGameScore(new BodyVgeme());
        score = ts.getMatch().getInGameScore();
        tennisSet.getMatch().redoScore();
    }

    /**
     * prida vymenu do gemu
     *
     * @param vymena
     */
    public void add(Vymena vymena) {
        list.add(vymena);

        if (vymena.getVyhral() == 0) {
            if (score.incP1()) {
                p1++;
                vyhral = 0;
                tennisSet.setIsEnd();
                tennisSet.p1scoreinc();
            }
        }

        if (vymena.getVyhral() == 1) {
            if (score.incP2()) {
                p2++;
                vyhral = 1;
                tennisSet.setIsEnd();
                tennisSet.p2scoreinc();
            }
        }

        tennisSet.getMatch().redoScore();
    }

    /**
     * vracia 0 ak vyhral p1 gem vracia p2 ak vyhral p2 gem a
     * vracia -1 ak esce nikto nevyhral
     *
     * @return
     */
    public int getVyhral() {
        return vyhral;
    }

    public void setVyhral(int vyhral) {
        this.vyhral = vyhral;
    }

    public TennisSet getTennisSet() {
        return tennisSet;
    }

    @Override
    public Iterator<Vymena> iterator() {
        ArrayList<Vymena> l = new ArrayList<>(list);
        return l.iterator();
    }

    public GameParcelable getParcelable() {
        ArrayList<VymenaParcelable> l = new ArrayList<>();
        for (Vymena v : list) {
            l.add(v.getParcelable());
        }
        return new GameParcelable(l, p1, p2, serve);


    }
}
