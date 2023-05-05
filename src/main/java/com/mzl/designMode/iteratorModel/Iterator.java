package com.mzl.designMode.iteratorModel;

/**
 * @author '李华根'
 * @version 1.0.0
 * @description 遍历集合的接口 - Iterator模式
 * @createTime 2022/11/03
 * @copyright Copyright ©️ 2022 北京魔马科技
 */
public interface Iterator {

    public abstract boolean hasNext();

    public abstract Object next();

}
