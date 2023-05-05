package com.mzl.designMode.iteratorModel;

/**
 * @author lihuagen
 * @version 1.0.0
 * @description 遍历书架的类 - Iterator模式
 * @createTime 2022/11/03
 * @copyright Copyright ©️ 2022 北京魔马科技
 */
public class BookShelfIterator implements Iterator{

    private BookShelf bookShelf;

    private int index;

    public BookShelfIterator(BookShelf bookShelf) {
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
