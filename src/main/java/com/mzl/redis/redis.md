# Redis实战
## 第1章　初识Redis
    Redis拥有两种不同形式的持久化方法，它们都可以用小而紧凑的格式将存储在内存中的数据写入硬盘：
        第一种持久化方法为时间点转储（point-in-time dump），转储操作既可以在“指定时间段内有指定数量的写操作执行”这一条件被满足时执行，又可以通过调用两条转储到硬盘（dump-to-disk）命令中的任何一条来执行；
        第二种持久化方法将所有修改了数据库的命令都写入一个只追加（append-only）文件里面，用户可以根据数据的重要程度，将只追加写入设置为从不同步（sync）、每秒同步一次或者每写入一个命令就同步一次。我们将在第4章中更加深入地讨论这些持久化选项。
### 1.2　Redis数据结构简介
    5种数据结构类型分别为STRING（字符串）、LIST（列表）、SET（集合）、HASH（散列）和ZSET（有序集合）。
    下面是一些常用命令
    String类型
        set key1 world
            新增key1，值为world
        incr key1
            将键存储的值加一，如果key1不存在，则当做0，如果值不能被解释为十进制数或浮点数，则会报错
        decr key1
            将键存储的值减一，如果key1不存在，则当做0，如果值不能被解释为十进制数或浮点数，则会报错
        incrby key1 10
            将键存储的值加上整数10，如果值不能被解释为十进制数或浮点数，则会报错
        decrby key1 20
            将键存储的值减去整数10，如果值不能被解释为十进制数或浮点数，则会报错
        incrbyfloat key1 10.10
            将键存储的值加上浮点数10.10，如果值不能被解释为十进制数或浮点数，则会报错
        get key1
            查询key
        del key1
            删除key
        append key1 world
            将值 world 追加到给定键key1当前存储的值的末尾
        getrange key1 2 4
            获取key1由偏移量start至偏移量end范围内的字符串组成的子串，包括start 和 end在内
        setrange key1 5 w
            命令用指定的字符串覆盖给定 key 所储存的字符串值，覆盖的位置从偏移量 offset（这里对应 5） 开始。
        STRLEN key
            STRLEN命令返回键值的长度，如果键不存在则返回0。UTF-8编码的长度都是3。
        MSET key1 v1 key2 v2 key3 v3
        MGET key1 key3
            MGET/MSET 可以同时获得/设置多个键的键值
        位操作
            GETBIT key offset
            SETBIT key offset value
            BITCOUNT key [start] [end] 命令可以获得字符串类型键中值是1的二进制位个数
            BITOP operation destkey key [key …] 可以对多个字符串类型键进行位运算，并将结果存储在destkey参数指定的键中
            一个字节由8个二进制位组成，Redis提供了4个命令可以直接对二进制位进行操作。
    list 列表类型[linked-list]（值可以重复）
        列表类型内部是使用双向链表（double linked list）实现的，所以向列表两端添加元素的时间复杂度为O(1)，获取越接近两端的元素速度就越快。通过索引访问元素比较慢。
        向列表两端增加元素
            LPUSH key value [value …] 将给定的值推入列表的左端
            RPUSH key value [value …] 将给定的值推入列表的右端
            LPUSH命令用来向列表左边增加元素，返回值表示增加元素后列表的长度。
        从列表两端弹出元素
            LPOP key 列表左边弹出一个值，并返回弹出的值
            RPOP key 列表右边弹出一个值，并返回弹出的值
            有进有出，LPOP命令可以从列表左边弹出一个元素。LPOP命令执行两步操作：第一步是将列表左边的元素从列表中移除，第二步是返回被移除的元素值。
        返回列表当前长度 lrange list-key 0 -1
        获取列表在给定位置上的单个元素 lindex list-key 1
    set集合（值不可重复）
        将给定元素添加到集合 sadd set-key item
        检查给定元素是否存在于集合中 sismember set-key item2
        返回集合包含的所有元素 smembers set-key
        如果给定的元素存在于集合，那么移除这个元素 srem set-key item
        集合除了基本的添加操作和移除操作之外，还支持很多其他操作，比如SINTER、SUNION、SDIFF这3个命令就可以分别执行常见的交集计算、并集计算和差集计算。
    HASH（散列，key不可重复）
        赋值与取值 文章发布
            HSET key field value  往散列里面关联起给定的键值对，新增时返回1，更新时返回0。
            HGET key field  获取制定散列的键的值
            HMSET key field value [field value …]
            HMGET key field [field …]
            HGETALL key  获取散列包含的所有键值对
            HSET命令用来给字段赋值，而HGET命令用来获得字段的值。
        判断字段是否存在
            HEXISTS key field
            HEXISTS命令用来判断一个字段是否存在。如果存在则返回1，否则返回0（如果键不存在也会返回0）。
        当字段不存在时赋值
            HSETNX key field value
            HSETNX 命令与HSET命令类似，区别在于如果字段已经存在，HSETNX命令将不执行任何操作。HSETNX命令是原子操作，不用担心竞态条件。
        增加数字
            HINCRBY key field increment
            上一节的命令拾遗部分介绍了字符串类型的命令INCRBY，HINCRBY命令与之类似，可以使字段值增加指定的整数。
            HINCRBY命令的示例如下：
                redis> HINCRBY person score 60
                (integer) 60
                之前person键不存在，HINCRBY命令会自动建立该键并默认score字段在执行命令前的值为“0”。命令的返回值是增值后的字段值。
        删除字段
            HDEL key field [field …]
            HDEL命令可以删除一个或多个字段，返回值是被删除的字段个数
        只获取字段名或字段值
            HKEYS key
            HVALS key
        获得字段数量
            HLEN key
    ZSET（有序集合）
        有序集合的键被称为成员（member），每个成员都是独一无二的；而有序集合的值则被称为分值（score），分值必须为浮点数。
        将一个带分值的成员添加到有序集合里面 zadd zset-key 728 member1
        根据元素在有序排列中所处的位置，从有序集合里面获取多个元素 zrange zset-key 0 -1 withscores
        获取有序集合在给定分值范围内的所有元素 zrangebyscore zset-key 0 800 withscores
        如果给定成员存在于有序集合，那么移除这个成员 zrem zset-key member1
## 第2章　使用Redis构建Web应用



