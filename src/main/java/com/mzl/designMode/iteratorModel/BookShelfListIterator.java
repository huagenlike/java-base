package com.mzl.designMode.iteratorModel;

/**
 * @author lihuagen
 * @version 1.0.0
 * @description  - Iterator模式
 * @createTime 2022/11/04
 * @copyright Copyright ©️ 2022 北京魔马科技
 */
public class BookShelfListIterator implements Iterator{
    private BookShelfList bookShelf;

    private int index;

    public BookShelfListIterator(BookShelfList bookShelf) {
        this.bookShelf = bookShelf;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        if (index < bookShelf.getLength()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object next() {
        Book book = bookShelf.getBookAt(index);
        index++;
        return book;
    }
}
