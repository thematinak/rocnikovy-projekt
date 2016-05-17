package classes.zapas;

import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import classes.body.BodyVgeme;
import classes.ptransfer.MatchParcelable;
import classes.ptransfer.TennisSetParcelable;

/**
 * Created by Martin on 30.12.2015.
 */
public class Match implements Iterable<TennisSet> {
    private ArrayList<TennisSet> list;
    private TennisSet current;
    private String p1, p2, gamescore1, gamescore2;
    private TextView twp1, twp2, p1gamescore, p2gamescore;
    private int NumberOfSets;
    private boolean isEnd, endOfMath;
    private TextView scoreText;
    private int p1score, p2score, serve, zacalservovat;
    private BodyVgeme currentGameScore;

    public Match(String p1, String p2, int NumberOfSets, int serve, TextView p1text, TextView p2text, TextView scoreText, TextView p1gamescore, TextView p2gamescore) {
        this.serve = zacalservovat = serve;
        this.NumberOfSets = NumberOfSets;
        this.p1 = p1;
        this.p2 = p2;
        this.twp1 = p1text;
        this.twp2 = p2text;
        this.p1gamescore = p1gamescore;
        this.p2gamescore = p2gamescore;
        isEnd = endOfMath = false;
        p1score = p2score = 0;
        this.scoreText = scoreText;
        gamescore1 = gamescore2 = "";
        list = new ArrayList<>();
        current = new TennisSet(this);
        redoScore();
        redoServeStar();
        redoGameScoce(0, 0);

    }

    /**
     * vracia true ak zapas skoncil
     * v inom pripade vracia false
     *
     * @return
     */
    public boolean isEndOfMath() {

        return endOfMath;
    }

    /**
     * vracia string s menom hraca p2
     *
     * @return
     */
    public String getP2name() {
        return p2;
    }

    /**
     * vracia string s menom hraca p1
     *
     * @return
     */
    public String getP1name() {

        return p1;
    }

    /**
     * touto metodou sa pridavaju uz submitovane vymeny
     *
     * @param vymena
     */
    public void add(Vymena vymena) {
        current.add(vymena);
        if (isEnd) {
            list.add(current);
            if (NumberOfSets > p1score && NumberOfSets > p2score) {
                isEnd = false;
                current = new TennisSet(this);
            } else {
                endOfMath = true;
                current = null;
                redoGameScoce();
            }
        }
    }

    /**
     * touto metodou nastavis pocet vyhratych setou hracovi p1
     *
     * @param p1score
     */
    public void setP1score(int p1score) {

        this.p1score = p1score;
    }

    /**
     * touto metodou nastavis pocet vyhratych setou hracovi p2
     *
     * @param p2score
     */
    public void setP2score(int p2score) {

        this.p2score = p2score;
    }

    /**
     * tato metoda sa pouziva na upozornenie matchu ze skoncil set
     */
    protected void setIsEnd() {

        this.isEnd = true;
    }

    /**
     * vracia int, kto servuje 0-p1, 1-p2
     *
     * @return
     */
    public int getServe() {

        return serve;
    }

    /**
     * mastavis kto bude servovat v nasledujucom geme
     * vloz len 0 alebo 1.
     *
     * @param serve
     */
    public void setServe(int serve) {
        this.serve = serve;
        redoServeStar();
    }

    /**
     * funkcia ktora prepisuje hviezdicku
     * ktora hovori kto servuje
     */
    public void redoServeStar() {
        if (twp1 == null || twp2 == null)
            return;
        if (serve == 0) {
            twp2.setText(p2);
            twp1.setText(p1 + "*");
        } else {
            twp1.setText(p1);
            twp2.setText("*" + p2);
        }
    }

    /**
     * funkcia ktora prepisuje stav v geme
     */
    public void redoScore() {
        if (scoreText == null)
            return;
        scoreText.setText(currentGameScore.toString());
    }

    /**
     * funkcia ktora prepise stav podla hodnot v match
     */
    public void redoGameScoce() {
        if (p1gamescore == null || p2gamescore == null)
            return;
        p1gamescore.setText(gamescore1);
        p2gamescore.setText(gamescore2);
    }

    /**
     * funkcia ktora prepisuje stav v sete
     * dale ak je koniec setu dak modifikuje match
     * p1,p2 su pocty vyhratych gemov v danom sete
     */
    public void redoGameScoce(int p1, int p2) {
        if (isEnd) {
            gamescore1 = gamescore1 + " " + current.getP1score();
            gamescore2 = current.getP2score() + " " + gamescore2;
            p1 = p2 = 0;

        }
        if (p1gamescore != null && p2gamescore != null) {
            p1gamescore.setText(gamescore1 + " " + Integer.toString(p1));
            p2gamescore.setText(Integer.toString(p2) + " " + gamescore2);
        }
    }

    /**
     * nastavy skore v geme
     */
    public void setInGameScore(BodyVgeme currentGameScore) {
        this.currentGameScore = currentGameScore;
    }

    /**
     * vracia bodyVgeme
     *
     * @return
     */
    public BodyVgeme getInGameScore() {

        return currentGameScore;
    }

    /**
     * funkcia ulozi match a vymena do suboru f
     * vracia true ak sa jej to podarilo
     *
     * @param f
     * @param vymena
     * @return
     */
    public boolean saveMath(File f, Vymena vymena) {
        try {
            PrintWriter writer = new PrintWriter(f, "UTF-8");
            writer.print(p1 + " ");
            writer.print(p2 + " ");
            writer.print(NumberOfSets + " ");
            writer.print(zacalservovat + " ");
            for (TennisSet ts : this) {
                for (Game g : ts) {
                    for (Vymena v : g) {
                        for (Hit h : v) {
                            int x = (int) h.getX() * 10000;
                            int y = (int) h.getY() * 10000;
                            writer.print(x + " " + y + " " + h.getType() + " " + h.getWinner() + " ");
                        }
                    }
                }
            }
            for (Hit h : vymena) {
                int x = (int) h.getX() * 10000;
                int y = (int) h.getY() * 10000;
                writer.print(x + " " + y + " " + h.getType() + " " + h.getWinner() + " ");
            }

            System.out.println();
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Iterator<TennisSet> iterator() {
        ArrayList<TennisSet> l = new ArrayList<>(list);
        l.add(current);
        return l.iterator();
    }

    /**
     * inkrementuje pocet vyhratych setov hraca1
     */
    protected void p1scoreinc() {
        p1score++;
    }

    /**
     * inkrementuje pocet vyhratych setov hraca2
     */
    protected void p2scoreinc() {
        p2score++;
    }

    /**
     * nastavy druheho hraca na servovanie
     */
    protected void nextServe() {
        serve++;
        serve = serve % 2;
        redoServeStar();
    }

    public MatchParcelable getParcelable() {
        ArrayList<TennisSetParcelable> l = new ArrayList<>();
        for (TennisSet ts : list) {
            l.add(ts.getParcelable());
        }
        if (current != null) {
            l.add(current.getParcelable());
        }
        //MatchParcelable m= new MatchParcelable(l, p1, p2);
        return new MatchParcelable(l, p1, p2);

    }
}
