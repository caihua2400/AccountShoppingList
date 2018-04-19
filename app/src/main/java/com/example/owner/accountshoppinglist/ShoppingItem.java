package com.example.owner.accountshoppinglist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Owner on 16/04/2018.
 */

public class ShoppingItem implements Parcelable{
    private int Id;
    private int Quantity;
    private String Name;
    private int Price;
    private String Tag;
    private String Path;

    public ShoppingItem(){}

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(Id);
            parcel.writeInt(Quantity);
            parcel.writeString(Name);
            parcel.writeInt(Price);
            parcel.writeString(Tag);
            parcel.writeString(Path);
    }
    public static final Parcelable.Creator<ShoppingItem> CREATOR=new Creator<ShoppingItem>() {
        @Override
        public ShoppingItem createFromParcel(Parcel parcel) {
            ShoppingItem s=new ShoppingItem();
            s.setId(parcel.readInt());
            s.setQuantity(parcel.readInt());
            s.setName(parcel.readString());
            s.setPrice(parcel.readInt());
            s.setTag(parcel.readString());
            s.setPath(parcel.readString());
            return s;
        }

        @Override
        public ShoppingItem[] newArray(int i) {
            return new ShoppingItem[i];
        }
    };
}
