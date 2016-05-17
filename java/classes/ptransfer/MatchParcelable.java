package classes.ptransfer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by Martin on 13.5.2016.
 */
public class MatchParcelable implements Parcelable, Iterable<TennisSetParcelable> {
    public ArrayList<TennisSetParcelable> list;
    public String p1, p2;

    public MatchParcelable(ArrayList<TennisSetParcelable> list, String p1, String p2) {
        this.list = list;
        this.p1 = p1;
        this.p2 = p2;
    }

    public MatchParcelable(Parcel source) {
        source.readTypedList(this.list, TennisSetParcelable.CREATOR);
        this.p1=source.readString();
        this.p2=source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.list);
        dest.writeString(p1);
        dest.writeString(p2);
    }

    public static final Parcelable.Creator<MatchParcelable> CREATOR =new Parcelable.Creator<MatchParcelable>(){
        @Override
        public MatchParcelable createFromParcel(Parcel source) {
            return new MatchParcelable(source);
        }

        @Override
        public MatchParcelable[] newArray(int size) {
            return new MatchParcelable[size];
        }
    };

    @Override
    public Iterator<TennisSetParcelable> iterator() {
        return list.iterator();
    }
}
