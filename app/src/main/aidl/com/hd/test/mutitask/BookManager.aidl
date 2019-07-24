// BookManager.aidl
package com.hd.test.mutitask;

import com.hd.test.mutitask.Book;

interface BookManager {
    void addBook(in Book book);
    List<Book>getBookList();
}
