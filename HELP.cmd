# java8
    partitioningBy分隔/groupingBy分组
    maxBy取最大/minBy取最小
    averagingInt /averagingLong/averagingDouble取平均值
    collectingAndThen 此方法是在进行归纳动作结束之后，对归纳的结果进行二次处理。
    reduce 操作可以实现从Stream中生成一个值，其生成的值不是随意的，而是根据指定的计算模型。比如，之前提到count、min和max方法，因为常用而被纳入标准库中。事实上，这些方法都是reduce操作。