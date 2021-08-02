# Redis实战
## 第1章　初识Redis
    Redis拥有两种不同形式的持久化方法，它们都可以用小而紧凑的格式将存储在内存中的数据写入硬盘：
        第一种持久化方法为时间点转储（point-in-time dump），转储操作既可以在“指定时间段内有指定数量的写操作执行”这一条件被满足时执行，又可以通过调用两条转储到硬盘（dump-to-disk）命令中的任何一条来执行；
        第二种持久化方法将所有修改了数据库的命令都写入一个只追加（append-only）文件里面，用户可以根据数据的重要程度，将只追加写入设置为从不同步（sync）、每秒同步一次或者每写入一个命令就同步一次。我们将在第4章中更加深入地讨论这些持久化选项。
### 1.2　Redis数据结构简介
    5种数据结构类型分别为STRING（字符串）、LIST（列表）、SET（集合）、HASH（散列）和ZSET（有序集合）。
    下面是一些常用命令
    String类型
        新增 set hello world
        查询 get hello
        删除 del hello
    list类型[linked-list]（值可以重复）
        将给定的值推入列表的右端 rpush list-key item
        将给定的值推入列表的左端 lpush list-key item1
        返回列表当前长度 lrange list-key 0 -1
        获取列表在给定位置上的单个元素 lindex list-key 1
        列表左边弹出一个值，并返回弹出的值 lpop list-key
        列表右边弹出一个值，并返回弹出的值 rpop list-key
    set集合（值不可重复）
        将给定元素添加到集合 sadd set-key item
        检查给定元素是否存在于集合中 sismember set-key item2
        返回集合包含的所有元素 smembers set-key
        如果给定的元素存在于集合，那么移除这个元素 srem set-key item
        集合除了基本的添加操作和移除操作之外，还支持很多其他操作，比如SINTER、SUNION、SDIFF这3个命令就可以分别执行常见的交集计算、并集计算和差集计算。
    HASH（散列，key不可重复）
        往散列里面关联起给定的键值对 hset hash-key sub-key1 value1
        获取制定散列的键的值 hget hash-key sub-key1
        获取散列包含的所有键值对 hgetall hash-key
        如果给定键存在散列里面，那么移除这个键 hdel hash-key sub-key2
    ZSET（有序集合）
        有序集合的键被称为成员（member），每个成员都是独一无二的；而有序集合的值则被称为分值（score），分值必须为浮点数。
        将一个带分值的成员添加到有序集合里面 zadd zset-key 728 member1
        根据元素在有序排列中所处的位置，从有序集合里面获取多个元素 zrange zset-key 0 -1 withscores
        获取有序集合在给定分值范围内的所有元素 zrangebyscore zset-key 0 800 withscores
        如果给定成员存在于有序集合，那么移除这个成员 zrem zset-key member1




