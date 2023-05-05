package com.mzl.designMode.iteratorModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lihuagen
 * @version 1.0.0
 * @description  - Iterator模式
 * @createTime 2022/11/04
 * @copyright Copyright ©️ 2022 北京魔马科技
 */
public class BookShelfList implements Aggregate {

    private List<Book> books;

    public BookShelfList (int maxSize) {
        this.books = new ArrayList<>(maxSize);
    }

    public Book getBookAt(int index) {
        return books.get(index);
    }

    public void appendBook(Book book) {
        this.books.add(book);
    }

    public int getLength() {
        return books.size();
    }

    @Override
    public Iterator iterator() {
        return new BookShelfListIterator(this);
    }
}
