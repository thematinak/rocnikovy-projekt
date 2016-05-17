package classes.zapas;

import android.graphics.Paint;
import android.view.View;

import classes.ptransfer.HitParcelable;

/**
 * Created by Martin on 28.12.2015.
 */
public class Hit {
    private float x, y;
    private Paint paint;
    private String type;
    private boolean out, winner;

    public Hit(float x, float y, Paint paint) {
        this.x = x;
        this.y = y;
        this.paint = paint;
        winner = out = false;

    }

    public float getX() {
        return x;
    }

    /**
     * nastavi uder ci bol winner true/false
     */
    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    /**
     * vracia ci bol winner true/false
     */
    public boolean getWinner() {
        return winner;
    }

    public float getY() {
        return y;
    }

    /**
     * vracia typ uderu
     * f-forhand
     * b-backhand
     * fv-forhandvolly
     * bv-backhandvolly
     * s-slizy
     * sm-smach
     */
    public String getType() {
        return type;
    }

    /**
     * nastavi typ uderu
     * f-forhand
     * b-backhand
     * fv-forhandvolly
     * bv-backhandvolly
     * s-slizy
     * sm-smach
     */
    public void setType(String type) {
        this.type = type;
    }

    public Paint getPaint() {
        return paint;
    }

    /**
     * nastavy ze uder bol out
     */
    public void setOut() {
        this.out = true;
    }

    public boolean Out() {
        return this.out;
    }

    /**
     * povie ci bol out
     * view potrebuje na zistenie max velkosti ihriska
     */
    public boolean isOut(View v, Vymena vymena) {
        float maxX = v.getWidth(), maxY = v.getHeight();
        if (zlaPolka(maxX, maxY, vymena))
            return out = true;
        if (type.equals("servis")) {
            return out = isServeOut(maxX, maxY, vymena);
        } else {
            return out = isOuti(maxX, maxY, vymena);
        }
    }

    public boolean isOut(float maxX,float maxY, Vymena vymena) {
        //float maxX = v.getWidth(), maxY = v.getHeight();
        if (zlaPolka(maxX, maxY, vymena))
            return out = true;
        if (type.equals("servis")) {
            return out = isServeOut(maxX, maxY, vymena);
        } else {
            return out = isOuti(maxX, maxY, vymena);
        }
    }

    /**
     * povie ci zahral uder na zlu polku ihrisa true ak ano
     * false ak nie
     */
    private boolean zlaPolka(float maxX, float maxY, Vymena vymena) {
        float relativeX = maxX / 382, relativeY = maxY / 171;
        int hrac;
        int servuje = vymena.getServuje();
        if (vymena.isDruhyServis()) {
            hrac = (vymena.size() + servuje) % 2;
        } else {
            hrac = (vymena.size() + servuje + 1) % 2;
        }

        if (hrac == 0) {
            if (x > (188 * relativeX) + 10)
                return true;
        } else {
            if (x < (188 * relativeX) - 10)
                return true;
        }
        return false;
    }

    /**
     * povie ano ak zahral na zly horny stvorec
     */
    private boolean zlyservisStvorecHore(float maxX, float maxY) {
        float relativeY = maxY / 171;
        if (y > (85 * relativeY) + 15)
            return true;
        return false;
    }

    /**
     * povie ano ak zahral na zly dolny stvorec
     */
    private boolean zlyservisStvorecDole(float maxX, float maxY) {
        float relativeY = maxY / 171;
        if (y < (85 * relativeY) - 15)
            return true;
        return false;
    }

    /**
     * povie true ak uder nebol v kurte
     * inak false
     */
    private boolean isOuti(float maxX, float maxY, Vymena vymena) {
        float relativeX = maxX / 382, relativeY = maxY / 171;

        if (x < (18 * relativeX) - 10 || x > (354 * relativeX) + 10)
            return true;

        if (y < (25 * relativeY) - 10 || y > (146 * relativeY) + 10)
            return true;

        return false;
    }

    /**
     * povie ano ak bol uder mimo korektneho miesta na servis
     */
    private boolean isServeOut(float maxX, float maxY, Vymena vymena) {
        float relativeX = maxX / 382, relativeY = maxY / 171;
        if (x < (105 * relativeX) - 10 || x > (265 * relativeX) + 10)
            return true;
        if (y < (25 * relativeY) - 10 || y > (146 * relativeY) + 10)
            return true;
        int servuje = vymena.getServuje();
        int strana = (vymena.getGameScore().p1 + vymena.getGameScore().p2) % 2;

        if (strana == 1) {
            if (servuje == 1) {
                return zlyservisStvorecHore(maxX, maxY);
            } else {
                return zlyservisStvorecDole(maxX, maxY);
            }
        } else {
            if (servuje == 1) {
                return zlyservisStvorecDole(maxX, maxY);
            } else {
                return zlyservisStvorecHore(maxX, maxY);
            }
        }
    }

    public HitParcelable getParcelable(){
        return new HitParcelable(x,y,type,out,winner);
    }
}
