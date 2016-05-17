package classes.ptransfer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Martin on 13.5.2016.
 */
public class TennisSetParcelable implements Parcelable, Iterable<GameParcelable> {
    public ArrayList<GameParcelable> list;

    public TennisSetParcelable(ArrayList<GameParcelable> list) {
        this.list = list;
    }

    public TennisSetParcelable(Parcel source) {
        source.readTypedList(this.list, GameParcelable.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list);
    }


    public static final Parcelable.Creator<TennisSetParcelable> CREATOR = new Parcelable.Creator<TennisSetParcelable>() {
        @Override
        public TennisSetParcelable createFromParcel(Parcel source) {
            return new TennisSetParcelable(source);
        }

        @Override
        public TennisSetParcelable[] newArray(int size) {
            return new TennisSetParcelable[size];
        }
    };

    @Override
    public Iterator<GameParcelable> iterator() {
        return list.iterator();
    }
}
