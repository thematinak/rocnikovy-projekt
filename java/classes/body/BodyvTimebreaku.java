package classes.body;

/**
 * Created by Martin on 24.1.2016.
 */
public class BodyvTimebreaku extends BodyVgeme{
    int p1,p2;
    public BodyvTimebreaku(){
        p1=p2=0;
    }

    @Override
    public boolean incP1() {
        if(p1>=7 && p1-p2>=2){
            return true;
        }
        return false;
    }

    @Override
    public boolean incP2() {
        if(p2>=7 && p2-p1>=2){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return p1 +" : "+ p2;
    }
}
