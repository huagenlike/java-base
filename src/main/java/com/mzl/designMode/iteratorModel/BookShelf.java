package com.mzl.designMode.iteratorModel;


/**
 * @author lihuagen
 * @version 1.0.0
 * @description 书架的类 - Iterator模式
 * @createTime 2022/11/03
 * @copyright Copyright ©️ 2022 北京魔马科技
 */
public class BookShelf implements Aggregate {

    private Book[] books;

    private int last = 0;

    public BookShelf (int maxSize) {
        this.books = new Book[maxSize];
    }

    public Book getBookAt(int index) {
        return books[index];
    }

    public void appendBook(Book book) {
        this.books[last] = book;
        last++;
    }

    public int getLength() {
        return last;
    }

    @Override
    public Iterator iterator() {
        return new BookShelfIterator(this);
    }

}
