package classes.ptransfer;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import classes.zapas.Hit;

/**
 * Created by Martin on 13.5.2016.
 */
public class VymenaParcelable implements Parcelable, Serializable, Iterable<HitParcelable> {
    public ArrayList<HitParcelable> list;
    public boolean secServ;
    public String score;

    public VymenaParcelable(ArrayList<HitParcelable> list,boolean secServ, String score){
        this.list=list;
        this.secServ=secServ;
        this.score=score;
    }

    public VymenaParcelable(Parcel source) {

        source.readTypedList(this.list, HitParcelable.CREATOR);
        this.score=source.readString();
        this.secServ=(source.readInt() == 1);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(list);
        dest.writeString(score);
        if(secServ)
            dest.writeInt(1);
        else
            dest.writeInt(0);
    }

    public static final Parcelable.Creator<VymenaParcelable> CREATOR =new Parcelable.Creator<VymenaParcelable>(){
        @Override
        public VymenaParcelable createFromParcel(Parcel source) {
            return new VymenaParcelable(source);
        }

        @Override
        public VymenaParcelable[] newArray(int size) {
            return new VymenaParcelable[size];
        }
    };

    @Override
    public Iterator<HitParcelable> iterator() {
        return list.iterator();
    }
}
