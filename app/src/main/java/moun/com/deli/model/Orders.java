package moun.com.deli.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


public class Orders implements Parcelable {
    private int id;
    private boolean ordered;
    private long date_created;

    public Orders() {
        super();
    }

    public Orders(int id, long date_created) {
        super();
        this.id = id;
        this.date_created = date_created;
    }


    private Orders(Parcel in) {
        super();
        this.id = in.readInt();
        this.ordered = in.readByte() != 0x00;
        this.date_created = in.readLong();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public long getDate_created() {
        return date_created;
    }

    public void setDate_created(long date_created) {
        this.date_created = date_created;
    }

    public boolean getOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getId());
        parcel.writeByte((byte) (ordered ? 0x01 : 0x00));
        parcel.writeLong(getDate_created());
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", ordered=" + ordered +
                ", date_created=" + date_created +
                '}';
    }

    public static final Creator<Orders> CREATOR = new Creator<Orders>() {
        public Orders createFromParcel(Parcel in) {
            return new Orders(in);
        }

        public Orders[] newArray(int size) {
            return new Orders[size];
        }
    };

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Orders other = (Orders) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
