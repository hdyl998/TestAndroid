// BookManager.aidl
package com.hd.test.mutitask;

import com.hd.test.mutitask.Book;
import com.hd.test.mutitask.DataCallBack;

interface BookManager {
    void addBook(in Book book);
    List<Book>getBookList();

    void register(DataCallBack callBack);
}
