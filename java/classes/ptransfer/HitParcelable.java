package classes.ptransfer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Martin on 13.5.2016.
 */
public class HitParcelable implements Parcelable {
    public float x, y;
    public String type;
    public boolean out, winner;

    public HitParcelable(float x, float y, String type, boolean out, boolean winner) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.out = out;
        this.winner = winner;
    }

    public HitParcelable(Parcel source) {
        this.x = source.readFloat();
        this.y = source.readFloat();
        this.type = source.readString();
        this.out = (source.readInt() == 1);
        this.winner = (source.readInt() == 1);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(x);
        dest.writeFloat(y);
        dest.writeString(type);
        if (out)
            dest.writeInt(1);
        else
            dest.writeInt(0);
        if (winner)
            dest.writeInt(1);
        else
            dest.writeInt(0);


    }

    public static final Parcelable.Creator<HitParcelable> CREATOR = new Parcelable.Creator<HitParcelable>() {
        @Override
        public HitParcelable createFromParcel(Parcel source) {
            return new HitParcelable(source);
        }

        @Override
        public HitParcelable[] newArray(int size) {
            return new HitParcelable[size];
        }
    };
}
