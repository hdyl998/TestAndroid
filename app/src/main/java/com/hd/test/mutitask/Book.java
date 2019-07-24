package com.hd.test.mutitask;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Note：None
 * Created by Liuguodong on 2019/7/24 11:38
 * E-Mail Address：986850427@qq.com
 */
public class Book implements Parcelable {


    public String name;
    public String content;


    public Book(String name, String content) {
        this.name = name;
        this.content = content;
    }

    protected Book(Parcel in) {
        name = in.readString();
        content = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(content);
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
