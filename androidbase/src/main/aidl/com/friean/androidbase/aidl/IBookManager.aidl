// IBookManager.aidl
package com.friean.androidbase.aidl;

// Declare any non-default types here with import statements
import com.friean.androidbase.aidl.Book;

interface IBookManager {
    void addBook(in Book book);
    List<Book> getBookList();
}
