package classes.body;

import classes.zapas.TennisSet;

/**
 * Created by Martin on 30.12.2015.
 */
public class BodyVsete {
    private int p1;
    private int p2;
    private TennisSet ts;

    public BodyVsete(TennisSet ts) {
        p1 = 0;
        p2 = 0;
        this.ts = ts;
    }

    /**
     * p1 vyhral game
     * @return
     */
    public boolean incP1() {
        p1++;
        if (p1 < 6) {
            return false;
        }

        if (p1 == 6 && p2 == 5) {
            return false;
        }
        if (p1 == 6 && p2 == 6) {
            ts.setTimebreak();

        }
        return true;
    }
    /**
     * p1 vyhral game
     * @return
     */
    public boolean incP2(){
        p2++;
        if (p2 < 6) {
            return false;
        }

        if (p2 == 6 && p1 == 5) {
            return false;
        }
        if (p2 == 6 && p1 == 6) {
            ts.setTimebreak();

        }
        return true;
    }



    @Override
    public String toString() {
        return p1 + " : " + p2;
    }
}
