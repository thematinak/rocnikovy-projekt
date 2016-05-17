package classes.ptransfer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Iterator;

import classes.zapas.Vymena;

/**
 * Created by Martin on 13.5.2016.
 */
public class GameParcelable implements Parcelable, Iterable<VymenaParcelable> {
    public ArrayList<VymenaParcelable> list;
    public int scoreP1, scoreP2, serve;

    public GameParcelable(ArrayList<VymenaParcelable> list, int scoreP1, int scoreP2, int serve) {
        this.list = list;
        this.scoreP1 = scoreP1;
        this.scoreP2 = scoreP2;
        this.serve = serve;
    }

    public GameParcelable(Parcel source) {
        source.readList(this.list, null);
        this.serve = source.readInt();
        this.scoreP1 = source.readInt();
        this.scoreP2 = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(list);
        dest.writeInt(serve);
        dest.writeInt(scoreP1);
        dest.writeInt(scoreP2);

    }


    public static final Parcelable.Creator<GameParcelable> CREATOR = new Parcelable.Creator<GameParcelable>() {
        @Override
        public GameParcelable createFromParcel(Parcel source) {
            return new GameParcelable(source);
        }

        @Override
        public GameParcelable[] newArray(int size) {
            return new GameParcelable[size];
        }
    };

    @Override
    public Iterator<VymenaParcelable> iterator() {
        return list.iterator();
    }
}
