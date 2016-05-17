package classes.zapas;

import java.util.ArrayList;
import java.util.Iterator;

import classes.body.BodyVsete;
import classes.ptransfer.GameParcelable;
import classes.ptransfer.TennisSetParcelable;

/**
 * Created by Martin on 30.12.2015.
 */
public class TennisSet implements Iterable<Game> {

    private ArrayList<Game> list;
    private Game current;
    private boolean isEnd, timebreak;
    private Match match;
    private int p1score, p2score;
    private BodyVsete score;


    public TennisSet(Match match) {
        timebreak = false;
        score = new BodyVsete(this);
        p1score = p2score = 0;
        this.match = match;
        list = new ArrayList<>();
        current = new Game(this, p1score, p2score);
    }

    /**
     * vracia skore gemou v sete
     *
     * @return
     */
    public BodyVsete getScore() {
        return score;
    }

    /**
     * vracia pocet vyhratych gemou hraca p2 v sete
     *
     * @return
     */
    public int getP2score() {
        return p2score;
    }

    /**
     * vracia pocet vyhratych gemou hraca p1 v sete
     *
     * @return
     */
    public int getP1score() {
        return p1score;
    }

    /**
     * vracia match
     *
     * @return
     */
    public Match getMatch() {
        return match;
    }

    /**
     * vracia list gemou v sete
     *
     * @return
     */
    public ArrayList<Game> getList() {
        return list;
    }

    /**
     * metoda add prida vymenu do setu.
     * ak v daka teto vymene skonci set
     * upozornu na to match
     *
     * @param vymena
     */
    public void add(Vymena vymena) {
        current.add(vymena);
        if (isEnd) {
            list.add(current);
            if (current.getVyhral() == 0) {
                if (score.incP1()) {
                    match.setIsEnd();
                    match.p1scoreinc();
                }
            }

            if (current.getVyhral() == 1) {
                if (score.incP2()) {
                    match.setIsEnd();
                    match.p2scoreinc();
                }
            }
            match.nextServe();
            match.redoGameScoce(p1score, p2score);
            isEnd = false;

            if (timebreak)
                current = new Timebreak(this, match.getServe());
            else
                current = new Game(this, p1score, p2score);
        }
    }

    public void setTimebreak() {
        this.timebreak = true;
    }

    public void setIsEnd() {
        this.isEnd = true;
    }

    /**
     * inkrementne pocet gemou vyhratych hracom p1 v sete
     */
    protected void p1scoreinc() {
        p1score++;
    }

    /**
     * inkrementne pocet gemou vyhratych hracom p2 v sete
     */
    protected void p2scoreinc() {
        p2score++;
    }

    @Override
    public Iterator<Game> iterator() {
        ArrayList<Game> l = new ArrayList<>(list);
        l.add(current);
        return l.iterator();
    }

    public TennisSetParcelable getParcelable() {
        ArrayList<GameParcelable> l = new ArrayList<>();
        for (Game g : list) {
            l.add(g.getParcelable());
        }
        l.add(current.getParcelable());
        return new TennisSetParcelable(l);
    }


}
