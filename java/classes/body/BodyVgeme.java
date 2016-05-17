package classes.body;

import classes.zapas.Game;

/**
 * Created by Martin on 30.12.2015.
 */
public class BodyVgeme {
    public int p1, p2;
    public BodyVgeme() {
        p1 = 0;
        p2 = 0;
    }

    /**
     * p1 vyhral vymenu
     * @return
     */
    public boolean incP1() {
        p1++;
        if (p1 < 4) {
            return false;
        }
        if (p1 == 4 && p2 == 3) {
            return false;
        }
        if (p1 == 4 && p2 == 4) {
            p2 = 3;
            p1 = 3;
            return false;
        }
        p1++;
        return true;

    }

    /**
     * p2 vyhral vymenu
     * @return
     */
    public boolean incP2() {

        p2++;
        if (p2 < 4) {
            return false;
        }
        if (p2 == 4 && p1 == 3) {
            return false;
        }
        if (p2 == 4 && p1 == 4) {
            p2 = 3;
            p1 = 3;
            return false;
        }
        p2++;
        return true;


    }

    private String ohodnotP(int x) {
        switch (x){
            case 0:
                return "0";
            case 1:
                return "15";
            case 2:
                return "30";
            case 3:
                return "40";
            case 4:
                return "AD";
            case 5:
                return "W";
            case 6:
                return "W";
            default: return "error";
        }
    }

    @Override
    public String toString() {
        return ohodnotP(p1)+" : "+ ohodnotP(p2) ;
    }
}
