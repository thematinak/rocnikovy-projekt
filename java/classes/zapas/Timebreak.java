package classes.zapas;

import java.util.ArrayList;
import java.util.List;

import classes.body.BodyVgeme;
import classes.body.BodyvTimebreaku;

/**
 * Created by Martin on 30.12.2015.
 */
public class Timebreak extends Game {
    List<Vymena> list;
    private BodyVgeme body;

    public Timebreak(TennisSet tennisSet, int serve) {
        super(tennisSet, 6, 6);
        list = new ArrayList<>();
        tennisSet.getMatch().setInGameScore(new BodyvTimebreaku());
        body = tennisSet.getMatch().getInGameScore();
        tennisSet.getMatch().redoScore();
    }

    /**
     * prida vymenu do timebreaku
     * @param vymena
     */
    @Override
    public void add(Vymena vymena) {
        list.add(vymena);

        if (vymena.getVyhral() == 0) {
            if (body.incP1()) {
                setVyhral(0);
                getTennisSet().setIsEnd();
                getTennisSet().p1scoreinc();
            }
        } else {
            if (body.incP2()) {
                setVyhral(1);
                getTennisSet().setIsEnd();
                getTennisSet().p1scoreinc();
            }
        }
        if (list.size() % 2 == 1) {
            getTennisSet().getMatch().nextServe();
        }
    }
}
