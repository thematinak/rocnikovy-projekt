package classes.zapas;

import java.util.ArrayList;
import java.util.Iterator;

import classes.body.BodyVgeme;
import classes.ptransfer.HitParcelable;
import classes.ptransfer.VymenaParcelable;

/**
 * Created by Martin on 30.12.2015.
 */
public class Vymena implements Iterable<Hit> {
    private ArrayList<Hit> list;
    private int vyhral, servuje;
    private boolean druhyServis, isEnd;
    private BodyVgeme currentGameScore;

    public Vymena(int servuje, BodyVgeme currentGameScore) {
        list = new ArrayList<>();
        vyhral = -1;
        this.servuje = servuje;
        druhyServis = isEnd = false;
        this.currentGameScore = currentGameScore;

    }

    public BodyVgeme getGameScore() {
        return currentGameScore;
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

    /**
     * prida uder do vymeny
     * vracia true ak sa tym to uderom skoncila vymena
     * inak vracia false
     */
    public boolean add(Hit h) {
        list.add(h);
        if ((h.Out() || h.getType().equals("net")) && list.size() == 1) {
            druhyServis = true;
            return false;
        }

        if ((h.Out() || h.getType().equals("net"))) {
            nastavVytaza();
            isEnd = true;
            return true;
        }

        if (list.get(list.size() - 1).getWinner()) {
            if (druhyServis) {
                vyhral = (servuje + list.size()) % 2;
            } else {
                vyhral = (servuje + 1 + list.size()) % 2;
            }
            isEnd = true;
            return true;
        }

        return false;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public Iterator<Hit> iterator() {
        return list.iterator();
    }

    /**
     * vracia list uderov
     *
     * @return
     */
    public ArrayList<Hit> getList() {
        return list;
    }

    public int getServuje() {
        return servuje;
    }

    /**
     * vrati true ak vo vymene servujuci pokazil prvy servis
     * inak false
     *
     * @return
     */
    public boolean isDruhyServis() {
        return druhyServis;
    }

    /**
     * vymaze posledny uder z vymeny
     */
    public void undo() {
        if (!list.isEmpty()) {
            list.remove(list.size() - 1);
        }
        if (list.isEmpty()) {
            druhyServis = false;

        }
        isEnd = false;

    }

    private void nastavVytaza() {
        if (druhyServis) {
            vyhral = (servuje + list.size() - 1) % 2;
        } else {
            vyhral = (servuje + list.size()) % 2;
        }
    }

    public boolean isEnd() {
        return isEnd;
    }

    /**
     * vracia pocet uderov vo vymene
     */
    public int size() {
        return list.size();
    }

    /**
     * vrati uder na pozicii index
     * pocita sa od nuli
     */
    public Hit get(int index) {
        return list.get(index);
    }

    public VymenaParcelable getParcelable() {
        ArrayList<HitParcelable> l = new ArrayList<>();
        for (Hit h : list) {
            l.add(h.getParcelable());
        }
        return new VymenaParcelable(l, druhyServis, currentGameScore.toString());
    }
}
